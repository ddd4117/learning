# 다형성

상속의 목적은 코드 재사용이 아니다. 상속은 타입 계층을 구조화하기 위해 사용해야 한다.

상속에 대한 질문

- 상속을 사용하려는 목적이 단순히 코드를 재사용하기 위해서인가?
- 클라이언트 관점에서 인스턴스들을 동일하게 행동하는 그룹으로 묶기 위해서인가?



## 1. 다형성

'**poly(많은)**'와 '**morph(형태)'**의 합성어로 **'많은 형태를 가질 수 있는 능력'**

다형성

- **유니버설(Universal)**
  - **매개변수(Parametric)**
    클래스의 인스턴스 변수나 메서드의 매개변수 타입을 임의의 타입으로 선언한 후 사용하는 시점에 구체적인 타입으로 지정하는 방식
  - **포함(Inclusion)/서브타입(Subtype)**
    메시지가 동일하더라도 수신한 객체의 타입에 따라 실제로 수행되는 행동이 달라지는 능력
    - 포함 다형성을 위한 전제조건은 자식 클래스가 부모 클래스의 서브타입
- **임시(Ad Hoc)**
  - **오버로딩(Overloading)**
    하나의 클래스 안에 동일한 이름의 메서드가 존재하는 경우
  - **강제(Coercion)**
    언어가 지원하는 자동적인 타입 변환이나 사용자가 직접 구현한 타입 변환을 이용해 동일한 연산자를 다양한 타입에 사용할 수 있는 방식

## 2. 상속의 양면성

데이터와 행동을 객체라고 불리는 하나의 실행 단위안으로 통합하는 것이다.

**상속 메커니즘 이해**

- 업캐스팅
- 동적 메서드 탐색
- 동적 바인딩
- 동적 바인딩
- self 참조
- super 참조

### 

### 상속을 사용한 강의 평가

수강생들의 성적을 계산하는 간단한 예제 프로그램 구현

```java
public class Lecture {
    private int pass;
    private String title;
    private List<Integer> scores = new ArrayList<>();

    public Lecture(String title, int pass, List<Integer> scores) {
        this.title = title;
        this.pass = pass;
        this.scores = scores;
    }

    public double average() {
        return scores.stream().mapToInt(Integer::intValue).average().orElse(0);
    }

    public List<Integer> getScores() {
        return Collections.unmodifiableList(scores);
    }

    public String evaluate() {
        return String.format("Pass:%d Fail:%d", passCount(), failCount());
    }

    private long passCount() {
        return scores.stream().filter(score -> score >= pass).count();
    }

    private long failCount() {
        return scores.size() - passCount();
    }
}
```

- pass는 이수 여부를 판단할 기준 점수를 저장한다
- average 메서드는 평균을 계산한다
- evaluate 메서드는 강의를 이수한 학생의 수와 낙제한 학생의 수를 형식에 맞게 구성 후 반환한다.
- getScores 메서드는 전체 학생의 성적을 반환한다.

#### 상속을 이용해 Lecture 클래스 재사용하기

```java
public class Grade {
    private String name;
    private int upper,lower;

    private Grade(String name, int upper, int lower) {
        this.name = name;
        this.upper = upper;
        this.lower = lower;
    }

    public String getName() {
        return name;
    }

    public boolean isName(String name) {
        return this.name.equals(name);
    }

    public boolean include(int score) {
        return score >= lower && score <= upper;
    }
}
```

```java
public class GradeLecture extends Lecture {
    private List<Grade> grades;

    public GradeLecture(String name, int pass, List<Grade> grades, List<Integer> scores) {
        super(name, pass, scores);
        this.grades = grades;
    }

    @Override
    public String evaluate() {
        return super.evaluate() + ", " + gradesStatistics();
    }

    private String gradesStatistics() {
        return grades.stream().map(grade -> format(grade)).collect(joining(" "));
    }

    private String format(Grade grade) {
        return String.format("%s:%d", grade.getName(), gradeCount(grade));
    }

    private long gradeCount(Grade grade) {
        return getScores().stream().filter(grade::include).count();
    }

    public double average(String gradeName) {
        return grades.stream()
                .filter(each -> each.isName(gradeName))
                .findFirst()
                .map(this::gradeAverage)
                .orElse(0d);
    }

    private double gradeAverage(Grade grade) {
        return getScores().stream()
                .filter(grade::include)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
    }
}
```



**메서드 오버라이딩** : 부모 클래스의 구현을 새로운 구현으로 대체하는 것



### 데이터 관점의 상속

GradeLecture 클래스의 인스턴스는 직접 정의한 인스턴스 변수뿐만 아니라 부모 클래스인 Lecture가 정의한 인스턴스 변수도 포함한다.



### 행동 관점의 상속

데이터 관점의 상속이 자식 클래스의 인스턴스 안에 부모 클래스의 인스턴스를 포함하는 개념

해동 관점의 상속은 부모 클래스가 정의한 일부 메서드를 자식 클래스의 메서드로 포함하는 것

런타임에서 시스템이 자식 클래스에 정의되지 않은 메스드가 있을 경우 이 메서드를 부모 클래스 안에서 탐색



## 3. 업캐스팅과 동적 바인딩

### 같은 메시지, 다른 메서드

코드 안에서 참조 타입과 무관하게 실제로 메시지를 수신하는 객체의 타입에 따라 실행되는 메서드가 달라질 수 있는 것은 업캐스팅과 동적 바인딩이라는 메커니즘이 작용하기 때문이다.

**업 캐스팅** : 부모 클래스 타입으로 선언된 변수에 자식 클래스의 인스턴스를 할당하는 것

**동적 바인딩** : 선언된 변수의 타입이 아니라 메시지를 수신하는 객체의 타입에 따라 실행되는 메서드가 결정



> **개방-폐쇄 원칙과 의존성 역전 원칙**
>
> 유연하고 확장 가능한 코드를 만들기 위해 의존관계를 구조화하는 바업



### 업 캐스팅

모든 객체지향 언어는 명시적으로 타입을 변환하지 않고도 부모 클래스 타입의 참조 변수에 자식 클래스의 인스턴스를 대입할 수 있게 허용한다.

***다운캐스팅** : 부모 클래스의 인스턴스를 자식 클래스 타입으로 변환하는 것



### 동적 바인딩

컴파일타임 바인딩 = 정적 바인딩 = 초기 바인딩

동적 바인딩 = 지연 바인딩



## 4. 동적 메서드 탐색과 다형성

실행할 메서드 선택 방법

- 메시지를 수신한 객체는 먼저 자신을 생성한 클래스에 적합한 메서드가 존재하는지 검사한다. 존재하면 메서드를 실행하고 탐색을 종료한다.
- 메서드를 찾지 못했다면 부모 클래스에서 메서드 탐색을 계속한다. 찾을 때까지 상속 계층을 올라가며 반복한다.
- 최상위 클래스에서도 찾지 못한 경우 예외를 발생시키며 탐색을 중단한다.

:star: **객체가 메시지를 수신하면 컴파일러는 self 참조라는 임시 변수를 자동으로 생성한 후 메시지를 수신한 객체를 가리키도록 설정한다. 메서드 탐색이 종료되면 소멸된다.**

메서드 탐색은 자식 클래스에서 부모 클래스의 방향으로 진행된다. 자식 클래스가 부모 클래스보다 먼저 호출되기 때문에 더 높은 우선순위를 갖는다.

**자동적인 메시지 위임** : 자신이 이해할 수 없는 메시지를 전송받은 경우 상속 계층을 따라 부모 클래스에게 처리를 위ㅣㅁ

**동적인 문맥** : 메시지를 수신했을 때 실행할지 말지 결정하는 것은 컴파일 시점이 아닌 런타임 시점



### 동적인 문맥

동적인 문맥을 결정하는 것은 메시지를 수신한 객체를 가리키는 self 참조다

self 참조가 가리키는 객체의 타입을 변경함으로써 객체가 실행될 문맥을 동적으로 바꿀 수 있다.



### 이해할 수 없는 메시지

- 정적타입 언어에서는 컴파일할 때 메서드를 찾지 못했다면 <u>컴파일에 에러</u>를 발생시킨다.
- 동적 타입 언어에서는 실제 코드를 실행해야 컴파일 에러를 발생시킬 수 있다.



### self 대 suepr

self 참조의 가장 큰 특징은 동적

super 참조 : 부모 클래스의 인스턴스 변수나 메서드에 접근하기 위해 사용

super를 통해 호출 하는 것은 '메서드를 호출' 한다가 아니라 '메시지를 전송' 한다고 표현한다.



## 5. 상속 대 위임

### 위임과 self 참조

**위임** : 자신이 정의하지 않거나 처리할 수 없는 속성 또는 메서드의 탐색 과정을 다른 객체로 이동시키기 위해 사용

self 참조를 전달하지 않는 경우를 포워딩, 전달하는 경우 위임

상속은 동적으로 메서드를 탐색하기 위해 현재의 실행 문맥을 가지고 있는 self 참조를 전달한다.



### 프로토 타입 기반의 객체지향 언어

클래스가 존재하지 않고 오직 객체만 존재하는 프로토타입 기반의 객체지향 언어에서 상속을 구현하는 유일한 방법은 객체 사이의 위임을 이용하는 것

