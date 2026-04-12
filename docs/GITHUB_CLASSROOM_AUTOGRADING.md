# GitHub Classroom Autograding

이 저장소는 모든 디자인 패턴 실습 폴더를 하나의 GitHub Classroom 과제로 배포하고, 공개 테스트로 자동 채점하도록 구성되어 있다.

## 방식

- 학생 저장소에는 공개 채점 스크립트와 workflow가 포함된다.
- secret, token, private test repository는 사용하지 않는다.
- 각 하위 폴더는 독립적으로 컴파일된다.
- 콘솔 실행이 안전한 실습은 main class를 실행하고 핵심 출력 문자열을 확인한다.
- GUI가 뜨거나 무한 입력 루프가 있는 실습은 기본적으로 컴파일만 확인한다.

GitHub Classroom 공식 문서의 Custom GitHub Actions workflow 방식을 사용한다.

https://docs.github.com/en/education/manage-coursework-with-github-classroom/teach-with-github-classroom/use-autograding

## 파일

- `.github/workflows/classroom.yml`: 학생이 push할 때 실행되는 GitHub Actions workflow
- `grader/exercises.json`: 실습 폴더, 점수, 실행할 main class 설정
- `scripts/grade_java_exercise.py`: Java 컴파일 및 smoke test 실행기

## Classroom 설정

가장 간단한 방식은 starter repository에 현재 파일들을 포함한 뒤 Classroom assignment에서 Custom YAML을 사용하는 것이다.

1. 이 폴더를 GitHub repository로 만들고 push한다.
2. GitHub Classroom에서 assignment를 만든다.
3. Starter code repository로 이 repository를 선택한다.
4. Grading and feedback에서 Custom YAML을 선택한다.
5. `.github/workflows/classroom.yml`을 사용하도록 설정한다.

이 방식은 Actions check 결과로 각 실습의 pass/fail을 볼 수 있다.

## 점수 bubble이 꼭 필요한 경우

Classroom UI의 Run command test를 실습별로 추가한다. 각 테스트의 Run command는 다음 형식이다.

```bash
python scripts/grade_java_exercise.py 01-delegation
```

Setup command는 비워도 된다. Timeout은 5분, Points는 `grader/exercises.json`의 points와 맞춘다.

실습별 명령:

```bash
python scripts/grade_java_exercise.py 01-delegation
python scripts/grade_java_exercise.py 02-strategy
python scripts/grade_java_exercise.py 03-observer
python scripts/grade_java_exercise.py 04-state
python scripts/grade_java_exercise.py 05-iterator
python scripts/grade_java_exercise.py 06-mediator
python scripts/grade_java_exercise.py 07-factory-method-maze
python scripts/grade_java_exercise.py 08-1-decorator-starbuzz
python scripts/grade_java_exercise.py 08-2-decorator-io
python scripts/grade_java_exercise.py 09-composite
python scripts/grade_java_exercise.py 11-1-interface-visitor
python scripts/grade_java_exercise.py 11-3-visitor
```

## 로컬 실행

Java 21 JDK와 Python 3가 필요하다.

전체 채점:

```bash
python scripts/grade_java_exercise.py
```

특정 실습만 채점:

```bash
python scripts/grade_java_exercise.py 02-strategy
```

컴파일만 확인:

```bash
python scripts/grade_java_exercise.py --skip-run
```

실습 목록 확인:

```bash
python scripts/grade_java_exercise.py --list
```

## 채점 기준 수정

`grader/exercises.json`에서 실습별 설정을 수정한다.

- `points`: 점수
- `runs`: 실행할 main class 목록
- `expectedOutputContains`: 출력에 포함되어야 하는 문자열
- `timeoutSeconds`: 실행 제한 시간

예시:

```json
{
  "id": "02-strategy",
  "path": "2_strategy",
  "points": 10,
  "runs": [
    {
      "class": "headfirst.strategy.MiniDuckSimulator",
      "timeoutSeconds": 5,
      "expectedOutputContains": [
        "Quack",
        "Squeak",
        "I'm flying with a rocket"
      ]
    }
  ]
}
```

주의: 공개 테스트는 학생이 볼 수 있으므로, 최종 평가에서 정답 하드코딩을 강하게 막지는 못한다. 지금 구성은 "제출물이 컴파일되고 기본 동작을 만족하는지"를 자동 확인하는 용도다.
