#!/usr/bin/env python3
from __future__ import annotations

import argparse
import json
import os
import shutil
import subprocess
import sys
from pathlib import Path
from typing import Any


EXCLUDED_DIRS = {
    ".git",
    ".github",
    ".gradle",
    ".idea",
    "build",
    "out",
    "target",
}


def repo_root() -> Path:
    return Path(__file__).resolve().parents[1]


def load_config(path: Path) -> dict[str, Any]:
    with path.open("r", encoding="utf-8") as file:
        data = json.load(file)

    exercises = data.get("exercises")
    if not isinstance(exercises, list) or not exercises:
        raise ValueError(f"{path} must contain a non-empty exercises array")

    seen_ids: set[str] = set()
    for exercise in exercises:
        exercise_id = exercise.get("id")
        exercise_path = exercise.get("path")
        if not exercise_id or not exercise_path:
            raise ValueError("Each exercise must define id and path")
        if exercise_id in seen_ids:
            raise ValueError(f"Duplicate exercise id: {exercise_id}")
        seen_ids.add(exercise_id)

    return data


def java_sources(exercise_dir: Path) -> list[Path]:
    sources: list[Path] = []
    for path in exercise_dir.rglob("*.java"):
        if any(part in EXCLUDED_DIRS for part in path.parts):
            continue
        sources.append(path)
    return sorted(sources)


def run_command(
    command: list[str],
    cwd: Path,
    *,
    stdin: str | None = None,
    timeout_seconds: int = 30,
) -> tuple[int, str]:
    try:
        completed = subprocess.run(
            command,
            cwd=str(cwd),
            input=stdin,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            text=True,
            timeout=timeout_seconds,
        )
        return completed.returncode, completed.stdout
    except FileNotFoundError:
        return 127, f"Command not found: {command[0]}"
    except subprocess.TimeoutExpired as error:
        output = error.stdout or ""
        if isinstance(output, bytes):
            output = output.decode("utf-8", errors="replace")
        return 124, output + f"\nTimed out after {timeout_seconds} seconds."


def github_escape(value: str) -> str:
    return value.replace("%", "%25").replace("\r", "%0D").replace("\n", "%0A")


def github_error(title: str, message: str) -> None:
    if os.environ.get("GITHUB_ACTIONS") == "true":
        print(f"::error title={github_escape(title)}::{github_escape(message)}")


def output_excerpt(output: str, max_chars: int = 4000) -> str:
    if len(output) <= max_chars:
        return output
    return output[-max_chars:]


def compile_exercise(
    root: Path,
    exercise: dict[str, Any],
    build_root: Path,
) -> tuple[bool, Path, str]:
    exercise_id = exercise["id"]
    exercise_dir = root / exercise["path"]
    classes_dir = build_root / exercise_id / "classes"

    if not exercise_dir.exists():
        return False, classes_dir, f"Exercise directory does not exist: {exercise['path']}"

    sources = java_sources(exercise_dir)
    if not sources:
        return False, classes_dir, f"No Java source files found under {exercise['path']}"

    if classes_dir.exists():
        shutil.rmtree(classes_dir)
    classes_dir.mkdir(parents=True, exist_ok=True)

    command = ["javac", "-encoding", "UTF-8", "-d", str(classes_dir), *map(str, sources)]
    exit_code, output = run_command(command, root, timeout_seconds=60)
    if exit_code != 0:
        return False, classes_dir, output

    return True, classes_dir, output


def run_main_class(
    root: Path,
    classes_dir: Path,
    run_config: dict[str, Any],
) -> tuple[bool, str]:
    main_class = run_config["class"]
    timeout_seconds = int(run_config.get("timeoutSeconds", 5))
    stdin = run_config.get("stdin")
    arguments = run_config.get("args", [])
    expected_parts = run_config.get("expectedOutputContains", [])

    command = [
        "java",
        "-Djava.awt.headless=true",
        "-cp",
        str(classes_dir),
        main_class,
        *arguments,
    ]
    exit_code, output = run_command(
        command,
        root,
        stdin=stdin,
        timeout_seconds=timeout_seconds,
    )

    if exit_code != 0:
        return False, f"{main_class} exited with code {exit_code}\n{output_excerpt(output)}"

    missing = [part for part in expected_parts if part not in output]
    if missing:
        return (
            False,
            f"{main_class} output did not contain {missing}\n{output_excerpt(output)}",
        )

    return True, output


def grade_exercise(
    root: Path,
    exercise: dict[str, Any],
    build_root: Path,
    *,
    skip_run: bool,
    verbose: bool,
) -> dict[str, Any]:
    exercise_id = exercise["id"]
    print(f"\n== {exercise_id}: {exercise['path']} ==")

    passed, classes_dir, compile_output = compile_exercise(root, exercise, build_root)
    if not passed:
        message = output_excerpt(compile_output)
        print(message)
        github_error(f"{exercise_id} compile failed", message)
        return {
            "id": exercise_id,
            "path": exercise["path"],
            "points": exercise.get("points", 0),
            "passed": False,
            "reason": "compile failed",
        }

    if verbose and compile_output.strip():
        print(compile_output)
    print("compile: pass")

    if skip_run:
        print("run: skipped")
        return {
            "id": exercise_id,
            "path": exercise["path"],
            "points": exercise.get("points", 0),
            "passed": True,
            "reason": "compile passed",
        }

    runs = exercise.get("runs", [])
    if not runs:
        print("run: none configured")
        return {
            "id": exercise_id,
            "path": exercise["path"],
            "points": exercise.get("points", 0),
            "passed": True,
            "reason": "compile passed",
        }

    for run_config in runs:
        passed, output = run_main_class(root, classes_dir, run_config)
        if not passed:
            print(output)
            github_error(f"{exercise_id} run failed", output_excerpt(output))
            return {
                "id": exercise_id,
                "path": exercise["path"],
                "points": exercise.get("points", 0),
                "passed": False,
                "reason": f"{run_config['class']} failed",
            }
        if verbose and output.strip():
            print(output)
        print(f"run {run_config['class']}: pass")

    return {
        "id": exercise_id,
        "path": exercise["path"],
        "points": exercise.get("points", 0),
        "passed": True,
        "reason": "all checks passed",
    }


def selected_exercises(
    all_exercises: list[dict[str, Any]],
    selectors: list[str],
) -> list[dict[str, Any]]:
    if not selectors:
        return all_exercises

    selected: list[dict[str, Any]] = []
    by_id = {exercise["id"]: exercise for exercise in all_exercises}
    by_path = {exercise["path"]: exercise for exercise in all_exercises}

    for selector in selectors:
        exercise = by_id.get(selector) or by_path.get(selector)
        if exercise is None:
            valid = ", ".join(exercise["id"] for exercise in all_exercises)
            raise ValueError(f"Unknown exercise '{selector}'. Valid ids: {valid}")
        selected.append(exercise)

    return selected


def print_summary(results: list[dict[str, Any]]) -> str:
    possible = sum(int(result["points"]) for result in results)
    earned = sum(int(result["points"]) for result in results if result["passed"])

    lines = [
        "",
        "## Autograding summary",
        "",
        "| Exercise | Points | Result | Detail |",
        "| --- | ---: | --- | --- |",
    ]
    for result in results:
        points = int(result["points"]) if result["passed"] else 0
        status = "PASS" if result["passed"] else "FAIL"
        lines.append(
            f"| {result['id']} | {points}/{int(result['points'])} | {status} | {result['reason']} |"
        )
    lines.extend(["", f"Total: {earned}/{possible}", ""])
    summary = "\n".join(lines)
    print(summary)

    summary_path = os.environ.get("GITHUB_STEP_SUMMARY")
    if summary_path:
        with open(summary_path, "a", encoding="utf-8") as file:
            file.write(summary)

    return summary


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Compile and smoke-test Java design-pattern exercises."
    )
    parser.add_argument(
        "exercises",
        nargs="*",
        help="Exercise ids or paths. Omit to grade every configured exercise.",
    )
    parser.add_argument(
        "--config",
        default="grader/exercises.json",
        help="Path to the exercise configuration JSON.",
    )
    parser.add_argument(
        "--build-dir",
        default="build/autograding",
        help="Directory for compiled classes.",
    )
    parser.add_argument(
        "--skip-run",
        action="store_true",
        help="Only compile; do not run configured main classes.",
    )
    parser.add_argument(
        "--list",
        action="store_true",
        help="Print configured exercises and exit.",
    )
    parser.add_argument(
        "--verbose",
        action="store_true",
        help="Print command output even when a check passes.",
    )
    return parser.parse_args()


def main() -> int:
    args = parse_args()
    root = repo_root()
    config_path = root / args.config

    try:
        config = load_config(config_path)
        exercises = config["exercises"]
        if args.list:
            for exercise in exercises:
                print(f"{exercise['id']}\t{exercise['path']}\t{exercise.get('points', 0)} pts")
            return 0

        selected = selected_exercises(exercises, args.exercises)
        build_root = root / args.build_dir
        results = [
            grade_exercise(
                root,
                exercise,
                build_root,
                skip_run=args.skip_run,
                verbose=args.verbose,
            )
            for exercise in selected
        ]
        print_summary(results)
        return 0 if all(result["passed"] for result in results) else 1
    except ValueError as error:
        print(f"error: {error}", file=sys.stderr)
        return 2


if __name__ == "__main__":
    raise SystemExit(main())
