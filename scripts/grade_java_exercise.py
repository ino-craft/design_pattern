#!/usr/bin/env python3
from __future__ import annotations

import argparse
import json
import os
import re
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


def as_string_list(value: Any, field_name: str) -> list[str]:
    if value is None:
        return []
    if isinstance(value, str):
        return [value]
    if isinstance(value, list) and all(isinstance(item, str) for item in value):
        return value
    raise ValueError(f"{field_name} must be a string or an array of strings")


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
    return run_java_class(root, [classes_dir], run_config)


def run_java_class(
    root: Path,
    classpath_entries: list[Path],
    run_config: dict[str, Any],
) -> tuple[bool, str]:
    main_class = run_config["class"]
    timeout_seconds = int(run_config.get("timeoutSeconds", 5))
    stdin = run_config.get("stdin")
    arguments = run_config.get("args", [])
    expected_parts = run_config.get("expectedOutputContains", [])
    classpath = os.pathsep.join(str(path) for path in classpath_entries)

    command = [
        "java",
        "-Djava.awt.headless=true",
        "-cp",
        classpath,
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


def compile_behavior_tests(
    root: Path,
    exercise: dict[str, Any],
    classes_dir: Path,
    build_root: Path,
) -> tuple[bool, Path, str]:
    exercise_id = exercise["id"]
    test_classes_dir = build_root / exercise_id / "test-classes"
    tests = exercise.get("behaviorTests", [])

    sources: list[Path] = []
    for test in tests:
        source = test.get("source")
        if not source:
            return False, test_classes_dir, f"Behavior test for {exercise_id} is missing source"
        source_path = root / source
        if not source_path.exists():
            return False, test_classes_dir, f"Behavior test source does not exist: {source}"
        sources.append(source_path)

    if test_classes_dir.exists():
        shutil.rmtree(test_classes_dir)
    test_classes_dir.mkdir(parents=True, exist_ok=True)

    command = [
        "javac",
        "-encoding",
        "UTF-8",
        "-cp",
        str(classes_dir),
        "-d",
        str(test_classes_dir),
        *map(str, sorted(sources)),
    ]
    exit_code, output = run_command(command, root, timeout_seconds=60)
    if exit_code != 0:
        return False, test_classes_dir, output

    return True, test_classes_dir, output


def run_behavior_tests(
    root: Path,
    exercise: dict[str, Any],
    classes_dir: Path,
    build_root: Path,
    *,
    verbose: bool,
) -> tuple[bool, str]:
    tests = exercise.get("behaviorTests", [])
    if not tests:
        print("behavior: none configured")
        return True, ""

    passed, test_classes_dir, compile_output = compile_behavior_tests(
        root,
        exercise,
        classes_dir,
        build_root,
    )
    if not passed:
        return False, output_excerpt(compile_output)
    if verbose and compile_output.strip():
        print(compile_output)
    print("behavior compile: pass")

    for test_config in tests:
        label = test_config.get("name", test_config["class"])
        passed, output = run_java_class(
            root,
            [classes_dir, test_classes_dir],
            test_config,
        )
        if not passed:
            return False, f"{label} failed\n{output_excerpt(output)}"
        if verbose and output.strip():
            print(output)
        print(f"behavior {label}: pass")

    return True, ""


def structure_check_sources(
    exercise_dir: Path,
    check: dict[str, Any],
) -> tuple[bool, list[Path], str]:
    paths: list[Path] = []

    for item in as_string_list(check.get("path"), "path"):
        paths.append(exercise_dir / item)

    for item in as_string_list(check.get("paths"), "paths"):
        paths.append(exercise_dir / item)

    for pattern in as_string_list(check.get("glob"), "glob"):
        paths.extend(sorted(exercise_dir.glob(pattern)))

    if not paths:
        return False, [], "no path, paths, or glob configured"

    missing = [path for path in paths if not path.exists()]
    if missing:
        relative = ", ".join(str(path.relative_to(exercise_dir)) for path in missing)
        return False, [], f"missing source file(s): {relative}"

    files = [path for path in paths if path.is_file()]
    if not files:
        return False, [], "no source files matched"

    return True, sorted(set(files)), ""


def strip_java_comments(text: str) -> str:
    return re.sub(r"/\*.*?\*/|//[^\r\n]*", "", text, flags=re.DOTALL)


def run_structure_check(exercise_dir: Path, check: dict[str, Any]) -> tuple[bool, str]:
    ok, sources, message = structure_check_sources(exercise_dir, check)
    if not ok:
        return False, message

    source_texts = [
        strip_java_comments(path.read_text(encoding="utf-8", errors="replace"))
        for path in sources
    ]
    combined = "\n".join(source_texts)
    label = check.get("name", "structure check")

    for expected in as_string_list(check.get("contains"), "contains"):
        if expected not in combined:
            return False, f"{label}: expected source to contain {expected!r}"

    for forbidden in as_string_list(check.get("notContains"), "notContains"):
        if forbidden in combined:
            return False, f"{label}: source must not contain {forbidden!r}"

    flags = re.MULTILINE | re.DOTALL
    for pattern in as_string_list(check.get("regex"), "regex"):
        if re.search(pattern, combined, flags) is None:
            return False, f"{label}: expected regex {pattern!r} to match"

    for pattern in as_string_list(check.get("notRegex"), "notRegex"):
        if re.search(pattern, combined, flags) is not None:
            return False, f"{label}: forbidden regex {pattern!r} matched"

    if "minMatches" in check:
        patterns = as_string_list(check.get("regex"), "regex")
        if len(patterns) != 1:
            return False, f"{label}: minMatches requires exactly one regex"
        count = len(re.findall(patterns[0], combined, flags))
        minimum = int(check["minMatches"])
        if count < minimum:
            return False, (
                f"{label}: expected at least {minimum} matches for "
                f"{patterns[0]!r}, found {count}"
            )

    return True, ""


def run_structure_checks(
    root: Path,
    exercise: dict[str, Any],
) -> tuple[bool, str]:
    checks = exercise.get("structureChecks", [])
    if not checks:
        print("structure: none configured")
        return True, ""

    exercise_dir = root / exercise["path"]
    failures: list[str] = []
    for check in checks:
        label = check.get("name", "structure check")
        passed, message = run_structure_check(exercise_dir, check)
        if passed:
            print(f"structure {label}: pass")
            continue

        hint = check.get("hint")
        if hint:
            message = f"{message}\nHint: {hint}"
        failures.append(message)

    if failures:
        return False, "\n".join(failures)

    return True, ""


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

    passed, structure_output = run_structure_checks(root, exercise)
    if not passed:
        message = output_excerpt(structure_output)
        print(message)
        github_error(f"{exercise_id} structure failed", message)
        return {
            "id": exercise_id,
            "path": exercise["path"],
            "points": exercise.get("points", 0),
            "passed": False,
            "reason": "structure check failed",
        }

    runs = exercise.get("runs", [])
    if not runs:
        print("run: none configured")
    else:
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

    passed, behavior_output = run_behavior_tests(
        root,
        exercise,
        classes_dir,
        build_root,
        verbose=verbose,
    )
    if not passed:
        message = output_excerpt(behavior_output)
        print(message)
        github_error(f"{exercise_id} behavior failed", message)
        return {
            "id": exercise_id,
            "path": exercise["path"],
            "points": exercise.get("points", 0),
            "passed": False,
            "reason": "behavior test failed",
        }

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
