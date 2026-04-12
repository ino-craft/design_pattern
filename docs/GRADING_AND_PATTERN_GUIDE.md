# 채점 방식과 패턴별 주요 확인점

이 문서는 GitHub Classroom 자동 채점이 실제로 확인하는 항목과, 수업자가 디자인 패턴 이해도를 볼 때 확인하면 좋은 주요 포인트를 정리한다.

## 현재 자동 채점 방식

현재 구성은 공개 smoke test 방식이다. 학생이 push하면 GitHub Actions가 각 실습 폴더를 독립 job으로 실행한다.

1. `actions/setup-java`로 Java 21을 준비한다.
2. `scripts/grade_java_exercise.py`가 `grader/exercises.json`을 읽는다.
3. 각 실습 폴더 아래의 `*.java` 파일을 `javac -encoding UTF-8`로 컴파일한다.
4. `runs`가 설정된 실습은 지정된 `main` class를 실행한다.
5. 출력에 `expectedOutputContains` 문자열이 모두 포함되면 통과로 본다.
6. 컴파일 실패, 실행 실패, timeout, 기대 출력 누락은 해당 실습 실패로 처리한다.

기본 점수는 실습별 10점, 총 120점이다. 현재 스크립트는 실습 단위 pass/fail 방식이라 한 실습 안에서 부분 점수를 계산하지 않는다.

## 자동 채점의 한계

공개 테스트는 학생이 볼 수 있으므로 정답 하드코딩을 강하게 막는 용도는 아니다. 이 구성은 다음을 빠르게 확인하기 위한 것이다.

- 제출물이 Linux GitHub Actions 환경에서 컴파일되는가
- 패키지명, 클래스명, public API가 깨지지 않았는가
- 콘솔 실행이 가능한 예제는 최소 동작을 유지하는가
- GUI 또는 무한 루프 성격의 예제는 최소한 컴파일 가능한가

패턴을 제대로 적용했는지는 자동 채점만으로 완전히 판정하기 어렵다. 필요한 경우 아래의 패턴별 확인점을 기준으로 수동 리뷰나 추가 테스트를 붙인다.

## 권장 평가 구조

Classroom 점수는 자동 채점으로 빠르게 부여하고, 중요한 과제는 코드 리뷰 점수를 별도로 보완하는 방식을 권장한다.

| 항목 | 권장 비중 | 확인 내용 |
| --- | ---: | --- |
| 컴파일 및 API 호환 | 30% | 기존 패키지명, 클래스명, 메서드 시그니처 유지 |
| 패턴 구조 | 35% | 역할 분리, 인터페이스/추상 클래스 사용, 의존 방향 |
| 동작 결과 | 20% | 예제 실행 결과, 상태 변화, 출력 결과 |
| 확장성 | 10% | 새 기능을 기존 코드 수정 최소화로 추가할 수 있는가 |
| 가독성 | 5% | 중복, 불필요한 조건문, 이름, 단순성 |

## 실습별 자동 채점 기준

| ID | 폴더 | 자동 채점 | 실행 기준 |
| --- | --- | --- | --- |
| `01-delegation` | `1_delegationProblem` | 컴파일 + 실행 | `delegationProblem.Main` 출력에 `Working like a dog`, `Meow`, `Beep` 포함 |
| `02-strategy` | `2_strategy` | 컴파일 + 실행 | `MiniDuckSimulator` 출력에 `Quack`, `Squeak`, rocket fly 포함 |
| `03-observer` | `3_observerPractice` | 컴파일 + 실행 | 날씨 상태, 우산 판매, 의류 판매 출력 포함 |
| `04-state` | `4_hiroshi.State.Problem` | 컴파일 | GUI와 무한 루프가 있어 실행 테스트 제외 |
| `05-iterator` | `5_headfirst.iterator.dinemerger_pro` | 컴파일 + 실행 | 메뉴 출력에 `MENU`, `BREAKFAST`, `LUNCH` 포함 |
| `06-mediator` | `6_hiroshi.mediatorProblem` | 컴파일 | GUI 예제라 실행 테스트 제외 |
| `07-factory-method-maze` | `7_factorymethod_mazeProblem` | 컴파일 | Swing maze 예제라 실행 테스트 제외 |
| `08-1-decorator-starbuzz` | `8-1_headfirst.decorator.starbuzzProblem` | 컴파일 + 실행 | 음료 설명에 `Espresso`, `Dark Roast`, `House Blend` 포함 |
| `08-2-decorator-io` | `8-2_headfirst.decorator.io.skeleton` | 컴파일 | 반복 입력 루프가 있어 실행 테스트 제외 |
| `09-composite` | `9_hiroshi.directoryCompositeProblem` | 컴파일 + 실행 | root/user directory 출력 포함 |
| `11-1-interface-visitor` | `11_1_interfaceVisitor` | 컴파일 + 실행 | visitor 출력에 `Visiting engine`, `Starting my car` 포함 |
| `11-3-visitor` | `11_3_hiroshi.VisitorProblem` | 컴파일 + 실행 | root/user directory와 `diary.html` 출력 포함 |

## 패턴별 주요 확인점

### 01 Delegation

핵심은 객체가 직접 모든 일을 처리하지 않고, 공통 역할 또는 협력 객체에 일을 위임하는 구조다.

- `Worker`, `Sayable` 같은 역할이 명확하게 분리되어 있는가
- `Dog`, `Cat`, `Robot`이 자신의 동작을 책임지고, 호출자는 공통 타입으로 다룰 수 있는가
- 특정 하위 타입을 계속 검사하는 `if`/`instanceof` 중심 코드로 퇴행하지 않았는가
- 새 worker 유형을 추가할 때 기존 반복 처리 코드를 크게 바꾸지 않아도 되는가

### 02 Strategy

핵심은 변하는 알고리즘을 객체로 분리하고 실행 시점에 교체할 수 있게 하는 것이다.

- `FlyBehavior`, `QuackBehavior`가 행동 인터페이스로 분리되어 있는가
- `Duck`이 구체 행동 클래스를 직접 구현하지 않고 위임하는가
- `setFlyBehavior`, `setQuackBehavior`로 런타임 교체가 가능한가
- 새 행동을 추가할 때 기존 duck 클래스 수정이 최소화되는가

### 03 Observer

핵심은 subject의 상태 변화를 observer들에게 통지하고, subject와 observer의 결합을 줄이는 것이다.

- 날씨 데이터 변경과 observer 갱신 책임이 분리되어 있는가
- observer가 필요한 데이터만 받아 자신의 상태를 갱신하는가
- 확장 과제라면 observer 추가/삭제가 고정 필드가 아니라 목록 기반으로 처리되는가
- 새 store를 추가할 때 subject의 변경이 최소화되는가

### 04 State

핵심은 상태별 행동을 조건문 덩어리가 아니라 상태 객체로 분리하는 것이다.

- `State` 인터페이스가 상태별 행동 계약을 표현하는가
- `DayState`, `NightState`가 시간, 사용, 알람, 전화 동작을 각각 책임지는가
- `Context`가 현재 상태를 보관하고 상태 전환을 위임받는가
- 시간 조건이 여러 UI 이벤트 처리부에 중복되지 않는가

### 05 Iterator

핵심은 컬렉션 내부 구조를 숨기고 동일한 순회 인터페이스로 접근하는 것이다.

- `Iterator`가 `hasNext`, `next` 계약을 제공하는가
- 배열, `ArrayList`, `Hashtable` 등 내부 저장 방식 차이가 client에 새지 않는가
- `Waitress`가 구체 menu 자료구조에 직접 의존하지 않는가
- 새 menu를 추가할 때 출력 로직 중복이 늘어나지 않는가

### 06 Mediator

핵심은 UI 컴포넌트들이 서로 직접 제어하지 않고 mediator를 통해 협력하는 것이다.

- `Mediator`가 colleague 생성과 상태 조정의 중심 역할을 하는가
- `Colleague` 컴포넌트가 mediator 참조만 알고 다른 컴포넌트를 직접 조작하지 않는가
- 로그인 모드, 게스트 모드, 입력값 변경에 따른 enable/disable 규칙이 mediator에 모여 있는가
- UI 컴포넌트가 늘어날 때 상호 참조가 급격히 늘어나지 않는가

### 07 Factory Method / Abstract Factory

이 폴더에는 factory method와 abstract factory 성격의 maze 예제가 함께 있다.

- `MazeGameCreator`가 `makeMaze`, `makeRoom`, `makeDoor`, `makeWall` factory method를 통해 객체 생성을 위임하는가
- 하위 creator가 생성할 제품군을 바꾸고, maze 생성 알고리즘은 재사용하는가
- `MazeFactory` 계열이 관련 제품 객체를 한 family로 생성하는가
- Harry/Snow 같은 변형을 추가할 때 maze 조립 코드의 중복이 늘어나지 않는가

### 08-1 Decorator: Starbuzz

핵심은 객체를 감싸면서 기능과 비용 계산을 누적하는 것이다.

- `CondimentDecorator`가 `Beverage`와 같은 타입으로 취급되는가
- `Mocha`, `Soy`, `Whip`, `Milk`가 감싼 beverage에 설명과 가격을 더하는가
- 조합 순서와 개수에 따라 결과가 자연스럽게 누적되는가
- 음료/첨가물을 추가할 때 기존 클래스 수정이 최소화되는가

### 08-2 Decorator: IO

핵심은 Java IO stream처럼 입력 stream을 감싸며 읽기 동작을 확장하는 것이다.

- `LowerCaseInputStream`이 `FilterInputStream`을 상속하고 대상 stream을 감싸는가
- `read()`와 `read(byte[], int, int)` 모두에서 변환 규칙이 일관적인가
- EOF인 `-1`을 정상 처리하는가
- 추가 decorator를 겹쳐도 stream contract가 깨지지 않는가

### 09 Composite

핵심은 leaf와 composite을 같은 타입으로 다뤄 트리 구조를 재귀적으로 처리하는 것이다.

- `Entry`가 `File`과 `Directory`의 공통 인터페이스 역할을 하는가
- `Directory`가 `Entry` 목록을 보관하고 크기/출력을 재귀적으로 계산하는가
- `File`에 `add`를 호출하면 예외가 발생해 leaf 제약이 유지되는가
- client가 file과 directory를 같은 방식으로 다룰 수 있는가

### 11-1 Interface Visitor

핵심은 element 구조와 element에 수행할 operation을 분리하는 것이다.

- 각 element가 `accept(visitor)`를 구현하는가
- visitor가 `Wheel`, `Engine`, `Body`, `Car` 타입별 `visit` 메서드를 제공하는가
- `Car`가 하위 element를 순회한 뒤 자기 자신도 visitor에 전달하는가
- 새 operation은 visitor 추가로 해결되고 element 수정이 최소화되는가

### 11-3 Visitor

핵심은 composite 구조를 visitor로 순회하며 작업을 분리하는 것이다.

- `Element`와 `Visitor` 계약이 분리되어 있는가
- `File`, `Directory`가 visitor를 받아 double dispatch를 수행하는가
- `ListVisitor`가 traversal 상태인 현재 directory path를 안전하게 관리하는가
- 파일 검색 같은 새 작업을 visitor 추가로 구현할 수 있는가

## 채점 기준을 강화할 때

현재 smoke test보다 더 엄격하게 보려면 다음 순서로 확장한다.

1. `grader/exercises.json`의 `expectedOutputContains`를 보강한다.
2. 실행 가능한 실습은 별도 test driver class를 추가하고 해당 class를 `runs`에 넣는다.
3. GUI 또는 무한 루프 실습은 headless test가 가능한 작은 model class를 분리한 뒤 그 class를 테스트한다.
4. 패턴 구조 검사는 공개 테스트만으로 한계가 있으므로 수동 리뷰 체크리스트나 별도 비공개 평가를 사용한다.
