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

