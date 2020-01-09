# 유연한 설계

## 1. 개방-폐쇄 원칙

소프트웨어 개체(클래스, 모듈, 함수 등등)는 **<u>확장</u>**에 대해 열려 있어야 하고, <u>**수정**</u>에 대해서는 닫혀 있어야 한다

- 확장에 대해 열려 있다 : 애플리케이션의 요구사항이 변경될 때 이 변경에 맞게 새로운 '동작'을 추가해서 애플리케이션의 기능을 확장할 수 있다.
- 수정에 대해 닫혀 있다 : 기존의 '코드'를 수정하지 않고도 애플리케이션의 동작을 추가하거나 변경할 수 있다.

### 컴파일타임 의존성을 고정시키고 런타임 의존성을 변경하라 

컴파일 타임의존성은 코드에서 드러나는 클래스들 사이의 관계

런타임 의존성은 실행시에 협력에 참여하는 객체들 사이의 관계



### 추상화가 핵심이다

개방-폐쇄 우너칙의 핵심은 추상화에 의존하는 것이다.

**추상화**란 핵심적인 부분만 남기고 불필요한 부분은 생략함으로써 복잡성을 극복하는 기법, 추상화 과정을 거치면 문맥이 바뀌더라도 변하지 않는 부분만 남게 되고 문맥에 따라 변하는 부분은 생략

```java
public abstract class DiscountPolicy {
    private List<DiscountCondition> conditions = new ArrayList<>();

    public DiscountPolicy(DiscountCondition ... conditions) {
        this.conditions = Arrays.asList(conditions);
    }

    public Money calculateDiscountAmount(Screening screening) {
        for(DiscountCondition each : conditions) {
            if (each.isSatisfiedBy(screening)) {
                return getDiscountAmount(screening);
            }
        }

        return screening.getMovieFee();
    }

    abstract protected Money getDiscountAmount(Screening Screening);
}
```

여기서 변하지 않는 부분은 할인 여부를 판단하는 로직, 변하는 부분은 할인된 요금을 계산하는 방법

변하는 부분을 고정하고, 변하지 않는 부분을 생략하는 추상화 메커니즘



단순히 어떤 개념을 추상화했다고 해서 수정에 대해 닫혀 있는 설계를 만드는 것은 아니다.

개방-폐쇄 원칙에서 폐쇄를 가능하게 하는 것은 의존성 방향이다. 수정에 대한 영향을 최소화하기 위해서는 모든 요소가 추상화에 의존해야 한다.

```java
public class Movie {
    private String title;
    private Duration runningTime;
    private Money fee;
    private DiscountPolicy discountPolicy;

    public Movie(String title, Duration runningTime, Money fee, DiscountPolicy discountPolicy) {
        this.title = title;
        this.runningTime = runningTime;
        this.fee = fee;
        this.discountPolicy = discountPolicy;
    }

    public Money getFee() {
        return fee;
    }

    public Money calculateMovieFee(Screening screening) {
        return fee.minus(discountPolicy.calculateDiscountAmount(screening));
    }
}
```

Movie는 안정된 추상화인 DIscountPolicy에 의존하기 때문에 할인 정책을 추가하기 위헤 DIscountPolicy의 자식을 추가하더라도 영향이 가지 않는다. 따라서 Moive와 DiscountPolicy는 수정에 닫혀있다.

변경에 의한 파급효과를 최대한 피하기 위해서는 변하는 것과 변하지 않는 것이 무엇인지를 이해하고 이를 추사화의 목적으로 삼아야만 한다. 

## 2. 생성 사용 분리

Movie의 할인 정책을 변경할 수 있는 방법은 한 가지 뿐이다. AmountDiscountPolicy의 생성 부분을 PercentDiscountPolicy의 인스턴스를 생성하도록 직접 코드를 수정하는 것 뿐이다. 이것은 개방-폐쇄 원칙을 벗어난다.

객체 생성을 피할 수는 없다. 어딘가에서는 해야한다. 문제는 객체 생성이 아니라, 부적절한 곳에서 한다는 것

**생성과 사용을 분리(Seperation user from creation)**

**소프트웨어 시스템은(응용 프로그램 객체를 제작하고 의존성을 서로 "연결"하는) 시작 단게와 (시작 단계 이후에 이어지는) 실행 단계를 분리해야 한다.**

그래서, 생성 주체를 협력하는 클라이언트에게 권한을 준다.



### Factory 추가하기

Client로 옮기면 Movie는 DiscountPolicy만 사용하게 된다. 하지만 Client는 Movie를 생성하고 사용하게 되는데 이를 분리해 보자.

Movie에서 해결했던 방법을 하면 좋겠지만, 객체 생성과 관련된 지식이 Client와 협력하는 클라이언트에게까지 새어나가기를 원하지 않는다고 가정하자.

이 경우 <u>객체 생성과 관련된 책임만 전담하는 별도의 객체</u>를 추가하고 Client는 이 객체를 사용하도록 만들 수 있다.

이 처럼 생성과 사용을 분리하기 위해 <u>객체 생성에 특화된 객체</u>를 **FACTORY**라고 한다.

```java
public class Factory {
    public Movie createAvatarMovie() {
        return new Movie("아바타",
                Duration.ofMinutes(120),
                Money.wons(10000),
                new AmountDiscountPolicy(
                    Money.wons(800),
                    new SequenceCondition(1),
                    new SequenceCondition(10)));
    }
}
```

```java
public class Client {
    private Factory factory;

    public Client(Factory factory) {
        this.factory = factory;
    }

    public Money getAvatarFee() {
        Movie avatar = factory.createAvatarMovie();
        return avatar.getFee();
    }
}
```

이렇게 하면 Client는 사용과 관련된 책임만 지게 된다.



### 순수한 가공물에게 책임 할당하기

5장의 GRSAP 패턴에 나오듯, 책임 할당의 가장 기본 원칙은 책임을 수행하는 데 필요한 정보를 가장 많이 알고 있는 **INFORMATION EXPERT**에게 책임을 할당하는 것이다.

<u>방금 전에 추가한 FACTORY는 도메인 모델에 속하지 않는다!</u>



**표현적 분해(representational decomposition)**란

- 도메인에 존재하는 사물 또는 개념을 표현하는 객체들을 이용해 시스템을 분해하는 것
- 도메인 모델에 담겨 있는 개념과 관계를 따르며 도메인과 소프트웨어 사이의 표현적 차이를 최소화하는 것을 목적
- 객체지향 설계를 위한 가장 기본적인 접근법

도메인 모델은 설계를 위한 중요한 출발점이지만 단지 출발점일 뿐이다. 도메인 개념을 표현하는 객체에게 책임을 할당하는 것만으로는 부족한 경우가 발생한다. 

실제 동작하는 애플리케이션은 데이터베이스 접근을 위한 객체와 같이 도메인 개념들을 초월하는 기계적인 개념들을 필요로 할 수 있다.



**PURE FABRICATION(순수한 가공물)**

- 책임을 할당하기 위해 창조되는 도메인과 무관한 인공적인 객체

- 어떤 행위를 하는데 책임질 마땅한 도메인 개념이 존재하지 않으면 이를 추가하고 책임을 할당한다.

- 특정한 행동을 표현하는 것이 일반적

- 행위적 분해에 의해 생성되는 것이 일반적

  

**<u>객체지향이 실세계를 모방해야 한다는 헛된 주장에 현혹될 필요가 없다.</u>**

## 3. 의존성 주입

사용하는 객체가 아닌 외부의 독립적인 객체가 인스턴스를 생성한 후 이를 전달해서 의존성을 해결하는 방법

- 생성자 주입(constructor injection) : 객체를 생성하는 시점에 생성자를 통해 의존성 해결

```java
    public Money getAvatarFee() {
        Movie avatar = new Movie("아바타",
                Duration.ofMinutes(120),
                Money.wons(10000),
                new AmountDiscountPolicy(...));
    }
```

- setter 주입(setter injection) : 객체 생성 후 setter 메서드를 통한 의존성 해결	

```java
avatar.setDiscountPolicy(new AmountDiscountPolicy(...));   
```

- method 주입(method injection) : 메서드 실행 시 인자를 이용한 의존성 해결

```java
avatar.calculateMovieFee(screening, new AmountDiscountPolicy(...));
```



### 숨겨진 의존성은 나쁘다

의존성 주입 외에도 의존성을 해결할 수 있는 다양한 방법이 존재한다.

**SERVICE LOCATOR** 패턴

- 의존성을 해결할 객체들을 보관하는 의존성 저장소
- 객체가 직접 **SERVICE LOCATOR**에게 의존성을 해결해줄 것을 요청
- 서비스를 사용하는 코드로부터 서비스가 누구인지(서비스를 구현한 구체 클래스의 타입이 무엇인지), 어디에 있는지(클래스 인스턴스를 어떻게 얻을지)를 몰라도 되게 해준다.

```java
public class ServiceLocator {
    private static ServiceLocator soleInstance = new ServiceLocator();
    private DiscountPolicy discountPolicy;

    public static DiscountPolicy discountPolicy() {
        return soleInstance.discountPolicy;
    }

    public static void provide(DiscountPolicy discountPolicy) {
        soleInstance.discountPolicy = discountPolicy;
    }

    private ServiceLocator() {
    }
}
```

```java

public class Movie {
    public Movie(String title, Duration runningTime, Money fee) {
        this.title = title;
        this.runningTime = runningTime;
        this.fee = fee;
        this.discountPolicy = ServiceLocator.discountPolicy();
    }
}
```



**SERVICE LOCATOR**패턴은 의존성을 가장 쉽고 간단하게 해결할 수 있지만, 의존성을 숨긴다는 문제점이 발생한다.

discountPolicy의 값이 null이라는 사실을 숨기고 있기 때문이다.

이는 컴파일타임이 아닌 런타임에 가서 알게되는데, 문제를 발견할 수 있는 시점이 코드 작성시점이 아니라 실행 시점으로 미루기 때문이다.

테스트 케이스 작성도 어렵다.모든 단위 테스트 케이스에 걸쳐 ServiceLocator의 상태를 공유하게 되는데 이는 단위 테스트의 기본 원칙(테스트는 서로 고립되어야 한다)을 위배한다.



<u>접근해야 할 객체가 있다면 전역 메커니즘 대신, 필요한 객체를 인수로 넘겨줄 수는 없는지부터 생각해보자. 이 방법이 굉장히 쉬운데다가 결합을 명확하게 보여줄 수 있다.</u>



## 4. 의존성 역전 원칙

하위 수준의 이슈로 인해 상위 수준에 위치하는 클래스들을 재사용하는 것이 어렵다면 문제다.

역시 해결 방법은 **<u>추상화</u>**다.

1. 상위 수준의 모듈은 하위 수준의 모듈에 의존해서는 안 된다. 둘 모두 추상화에 의존해야 한다.
2. 추상화는 구체적인 사항에 의존해서는 안 된다. 구체적인 사항은 추상화에 의존해야 한다.

### 의존성 역전 원칙과 패키지

역전은 의존성의 방향뿐만 아니라 인터페이스의 소유권에도 적용된다는 것이다.

Movie의 재사용을 위해 필요한 것이다 DiscountPolicy뿐이라면 DiscountPolicy를 Movie와 같은 패키지로 모으고 AmountDiscountPolicy와 PercentDiscountPolicy를 별도의 패키지에 위치시켜 의존성을 해결 한다. 

-> **SERPERATED INTERFACE PATTERN**

<u>훌륭한 객체지향 설계를 위해서는 의존성을 역전시켜야 한다.</u>



## 5. 유연성에 대한 조언

유연하고 재사용 가능한 설계가 항상 좋은 것은 아니다. 설계의 미덕은 단순함과 명확함으로부터 나온다. 단순하고 명확한 설계를 가진 코드는 읽기 쉽고 이해하기도 편하다.

불필요한 유연성은 불필요한 복잡성을 낳는다. 단순하고 명확한 해법이 그런대로 만족스럽다면 유연성을 제거하라. 

유연성은 코드를 읽는 사람들이 복잡함을 수용할 수 있을 때만 가치가 있다.

### 협력과 책임이 중요하다.

객체의 협력과 책임이 중요하다. 설계를 유연하게 만들기 위해서는 먼저 역할, 책임, 협력에 초점을 맞춰야 한다.

초보자가 자주 저지르는 실수 중 하나는 객체의 역할과 책임이 자리를 잡기 전에 너무 성급하게 객체 생성에 집중한다는 것이다.  

중요한 비즈니스 로직을 처리하기 위해 책임을 할당하고 협력의 균형을 맞추는 것이 객체 생성에 관한 책임을 할당하는 것보다 우선이다.

핵심은 객체를 생성하는 방법에 대한 결정은 모든 책임이 자리를 잡은 후 가장 마지막 시점에 내리는 것이 적절하다.

의존성을 관리해야 하는 이유는 역할, 책임, 협력의 관점에서 설계가 유연하고 재사용 가능해야 하기 떄문이다.