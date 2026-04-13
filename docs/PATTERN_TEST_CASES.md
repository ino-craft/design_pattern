# Pattern Learning Test Cases

This repository now has two public test-case layers in addition to the original
compile and smoke-run checks. The exercise Java source folders remain untouched;
all learning checks live under `grader/` and `scripts/`.

## Layers

1. Compile every `*.java` file in the selected exercise.
2. Run `structureChecks` from `grader/exercises.json`.
3. Run the original `runs` smoke checks.
4. Compile and run `behaviorTests` Java drivers from `grader/java-tests/behavior`.

`--skip-run` still keeps the old compile-only behavior.

## Structure Checks

`structureChecks` are static pattern checks. They read source files, strip Java
comments, and then apply literal `contains`, `notContains`, `regex`, and
`notRegex` rules. A `hint` explains the design-pattern lesson when a rule fails.
Prefer checks about roles, relationships, and contracts over exact class names.
For example, check that a strategy context composes replaceable behavior objects;
do not fail a submission just because a helper class uses a different concrete
name. Exact names should be limited to the starter's public entry points or
framework/API contracts that the grader must invoke.

Example:

```json
{
  "name": "CondimentDecorator keeps the Beverage type",
  "path": "headfirst/decorator/starbuzz/CondimentDecorator.java",
  "regex": "class\\s+CondimentDecorator\\s+extends\\s+Beverage",
  "hint": "Decorators must be substitutable wherever a Beverage is expected."
}
```

Paths are relative to the exercise folder. Test-driver paths are relative to the
repository root.

## Behavior Tests

`behaviorTests` are small Java programs that use the submitted exercise classes
without modifying them. They should assert one learning behavior and print a
`PASS ...` marker when successful.
Because Java tests must call compiled code, these tests may rely on the starter's
public API or configured main class. Avoid using them to police incidental helper
names; use them to prove observable pattern behavior.

Example:

```json
{
  "name": "strategy-runtime-swap",
  "source": "grader/java-tests/behavior/strategy/StrategyBehaviorTest.java",
  "class": "grader.behavior.strategy.StrategyBehaviorTest",
  "timeoutSeconds": 5,
  "expectedOutputContains": [
    "PASS strategy runtime swap"
  ]
}
```

## Intentional Starter Failures

Some structure checks are meant to guide incomplete practice skeletons. The
checks now follow the PPT practice guide
`1.1 Design Patterns 실습교안 v.1.25.5－20260413.pptx`, so the starter is expected
to fail until the exercise-specific extension is implemented. Examples include
`DoubleQuack`, `IceCreamStore`, `UrgentState`, `ShiftInputStream`,
`getFullName()`, `CarElementDestroyVisitor`, `Trunk`, and `FileFindVisitor`.
The SnowWhite factory-method, abstract-factory, and prototype paths are also
expected to fail until they inherit the right factory base class and return
SnowWhite products instead of `null`.

## Local Commands

```bash
python scripts/grade_java_exercise.py --list
python scripts/grade_java_exercise.py 02-strategy
python scripts/grade_java_exercise.py
```

Java 21 JDK and Python 3 are required for full local grading.
