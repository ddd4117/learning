# 상속과 코드 재사용

**합성** : 새로운 클래스의 인스턴스 안에 기존 클래스의 인스턴스를 포함시키는 방법

## 1. 상속과 중복 코드 

### DRY (Don't Repeat Yourself)원칙

**한 번, 단 한번(Once and Only Once) 원칙** 또는 **단일 지점 제어(Single Point Control) 원칙**이라고 부른다.



```java
public class Call {
	private LocalDateTime from;
	private LocalDateTime to;
	
	public Call(LocalDateTime from, LocalDateTime to) {
		this.from = from;
		this.to = to;
	}

	public Duration getDuration() {
		return Duration.between(from, to);
	}
	
	public LocalDateTime getFrom() {
		return from;
	}

}
```

```java
public class Phone {
    private Money amount;
    private Duration seconds;
    private List<Call> calls = new ArrayList<>();

    public Phone(Money amount, Duration seconds) {
        this.amount = amount;
        this.seconds = seconds;
    }

    public void call(Call call) {
        calls.add(call);
    }

    public List<Call> getCalls() {
        return calls;
    }

    public Money getAmount() {
        return amount;
    }

    public Duration getSeconds() {
        return seconds;
    }

    public Money calculateFee() {
        Money result = Money.ZERO;

        for(Call call : calls) {
            result = result.plus(amount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
        }

        return result;
    }
}
```

Phone 인스턴스는

1. 단위요금을 젖아하는 amount
2. 단위시간을 저장하는 seconds
3. calls는 전체 통화 목록을 저장하고 있는 Call의 리스트

요구사항은 항상 변한다.

요구사항 추가 -> 밤 10시 이후의 통화에 요금에 할인을 해주는 방식

가장 쉬운 해결방법)  Phone의 코드를 복사해서 NightlyDiscountPhone이라는 새로운 클래스를 만든 후 수정

```java
public class NightlyDiscountPhone {
    private static final int LATE_NIGHT_HOUR = 22;

    private Money nightlyAmount;
    private Money regularAmount;
    private Duration seconds;
    private List<Call> calls = new ArrayList<>();

    public NightlyDiscountPhone(Money nightlyAmount, Money regularAmount, Duration seconds) {
        this.nightlyAmount = nightlyAmount;
        this.regularAmount = regularAmount;
        this.seconds = seconds;
    }

    public Money calculateFee() {
        Money result = Money.ZERO;

        for(Call call : calls) {
            if (call.getFrom().getHour() >= LATE_NIGHT_HOUR) {
                result = result.plus(nightlyAmount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
            } else {
                result = result.plus(regularAmount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
            }
        }

        return result;
    }
}
```

하지만 이렇게 하면 중복코드가 발생한다. 책에서는 시한폭탄이라고 한다.



요구사항에 세금을 붙힌다고하면, Phone과 NIghtlyDiscountPhone 둘 다 수정해야한다.

중복 코드는 같이 수정되어야 하기 때문에, 수정할 때 하나라도 빠트린다면 버그로 이어진다.



**타입 코드 사용하기**

하나의 클래스로 합치는 것, 요금제를 구분하는 타입 코드를 추가하고 타입 코드의 값에 따라 로직을 분기하는 방법

-> 낮은 응집도와 높은 결합도

### 

### 상속을 이용해서 중복 코드 제거하기

NightlyDiscountPhone 클래스가 Phone클래스를 상속하자

하지만 상속을 염두에 두고 설계되지 않은 클래스를 상속을 이용해 재사용 하는 것은 생걱처럼 쉽지 않다

자식 클래스의 작성자가 부모 클래스의 구현 방법에 대한 정확한 지식을 가져야 한다는 것을 의미한다.

상속은 결합도를 높인다. 그리고 상속이 초래하는 부모 클래스와 자식 클래스 사이의 강한 결합이 코드를 수정하기 어렵게 만든다.

> **상속을 위한 경고 1**
>
> 자식 클래스의 메서드 안에서 super 참조를 이용해 부모 클래스의 메서드를 직접 호출할 경우 두 클래스는 강하게 결합된다. super 호출을 제거할 수 있는 방법을 찾아 결합도를 제거하라.

## 2. 취약한 기반 클래스 문제

부모 클래스의 변경에 의해 자식 클래스가 영향을 받는 현상을 **취약한 기반 클래스 문제(Fragile Base Class Problem, Brittle Base Class Proble)**이라고 부른다.

취약한 기반 클래스 문제는 캡슐화를 약화시키고 결합도를 높인다.

### 불필요한 인터페이스 상속 문제

인터페이스 설계는 제대로 쓰기엔 쉽게, 엉터리로 쓰기엔 어렵게 만들어야 한다.

Stack이 Vector를 상속하는 문제 참고

> **상속을 위한 경고 2**
>
> 상속받은 부모 클래스의 메서드가 자식 클래스의 내부 구조에 대한 규칙을 깨트릴 수 있다.

### 

### 메서드 오버라이딩의 오작용 문제

> **상속을 위한 경고 3**
>
> 자식 클래스가 부모 클래스의 메서드를 오버라딩할 경우 부모 클래스가 자신의 메서드를 사용하는 방법에 자식 클래스가 결합될 수 있다.

### 

### 부모 클래스와 자식 클래스의 동시 수정 문제

결합도란 다른 대상에 알고 있는 지식의 양이다.

서브클래스는 올바른 기능을 위해 슈퍼클래스의 세부적인 구현에 의존한다. 

> **상속을 위한 경고 4**
>
> 클래스를 상속하면 결합도로 인해 자식 클래스와 부모 클래스의 구현을 영원히 변경하지 않거나, 자식 클래스와 부모 클래스를 동시에 변경하거나 둘 중 하나를 선택할 수 밖에 없다.



## 3. Phone 다시 살펴보기

### 추상화에 의존하자

자식 클래스가 부모 클래스의 구현이 아닌 추상화에 의존하도록 만드는 것이다.

코드 중복을 제거하기 위해 상속을 도입할 때 따르는 두가지 원칙

1. 두 메서드가 유사하게 보인다면 차이점을 메서드로 추출하라. 메서드 추출을 통해 두 메서드를 동일한 형태로 보이도록 만들 수 있다.
2. 부모 클래스의 코드를 하위로 내리지 말고 자식 클래스의 코드를 상위로 올려라. 부모 클래스의 구체적인 메서드를 자식 클래스로 내리는 것보다 자식 클래스의 추상적인 메서드를 부모 클래스로 올리는 것이 재사용과 응집도 측면에서 더 뛰어난 결과를 얻을 수 있다.



### 차이를 메서드로 추출하라

​	**변하는 것으로부터 변하지 않는 것을 분리하라**, **변하는 부분을 찾고 이를 캡슐화 하라**

### 

### 중복 코드를 부모 클래스로 올려라

Phone과 NightlyDiscountPhone이 AbstractPhone을 상속받도록 하자

```java
public abstract class AbstractPhone {
    private List<Call> calls = new ArrayList<>();

    public Money calculateFee() {
        Money result = Money.ZERO;

        for(Call call : calls) {
            result = result.plus(calculateCallFee(call));
        }

        return result;
    }

    abstract protected Money calculateCallFee(Call call);
}
```



이제 우리의 설계는 추상화에 의존하게 된다.

"위로 올리기" 전략은 실패했더라도 수정하기 쉬운 문제를 발생시킨다.



### 추상화가 핵심이다

상속 계층의 코드를 진화시키는 데 걸림돌이 된다면 추상화를 찾아내고 상속 계층 안의 클래스들이 그 추상화에 의존하도록 코드를 리팩토링하라. 차이점을 메서드로 추출하고 공통적인 부분은 부모 클래스로 이동하라



### 의도를 드러내는 이름 선택하기

NightlyDiscountPhone은 심야할인요금제와 관련된 사실을 명확하게 전달하지만, Phone과 AbstractPhone은 그렇지 않다.

그래서 Phone은 RegularPhone으로, AbstractPhone은 Phone으로 변경한다.



### 세금 추가하기

세금은 모든 요금제에 공통으로 적용되어야 한다. 따라서 추상 클래스인 Phone을 수정하면 모든 자식 클래스 간에 수정사항을 공유할 수 있다.

```java
public abstract class Phone {
    private double taxRate;
    private List<Call> calls = new ArrayList<>();

    public Phone(double taxRate) {
        this.taxRate = taxRate;
    }

    public Money calculateFee() {
        Money result = Money.ZERO;

        for(Call call : calls) {
            result = result.plus(calculateCallFee(call));
        }

        return result.plus(result.times(taxRate));
    }

    protected abstract Money calculateCallFee(Call call);
}
```

Phone에 taxRate 필드를 추가했고, 생성자를 추가했다. 그로인해 자식 클래스도 역시 초기화를 해야한다.

이렇듯, 객체 행동만 변경된다면 독립적으로 진화시킬 수 있다. 하지만 인스턴스 변수가 추가되는 경우에는 자식 전반에 걸쳐 수정이 일어난다.

하지만 초기화 로직을 변경하는 것이 세금 계산 코드를 중복시키는 것보다 현명하다.

우리가 원하는 것은 행동을 변경하기 위해 인스턴스 변수를 추가하더라도 상속 계층 전체에 걸쳐 부작용이 퍼지지 않게 하는 것이다.

## 

## 4. 차이에 의한 프로그래밍

기존 코드와 다른 부분만을 추가함으로써 애플리케이션의 기능을 확장하는 방법을 **차이에 의한 프로그래밍(programming by difference)**

목표는 중복 코드를 제거하고 코드를 재사용하는 것이다.



상속은 매력적인 도구이지만, 너무 취해서 모든 설계에 상속을 적용하는 것은 지양해야한다.

**<u>정말로 필요한 경우에만 상속을 사용해라</u>**

**상속보다 좋은 방법이 합성이다. 잊지마라!**