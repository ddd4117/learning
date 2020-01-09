프로그램 패러다임이란 개발자 공동체가 동일한 프로그래밍 스타일과 모델을 공유할 수 있게 함으로써 불필요한 부분에 대한 의견충돌을 방지한다. 

글래스는 실무보다 이론이 먼저, 실무가 발전하고 정립된 뒤에야 이론이 발전한다고 주장. 즉, 실무를 먼저 생각하자

# 1. 티켓 판매 애플리케이션

초대장이 있으면 연극을 무료로 볼 수 있게하는 이벤트를 진행한다.

초대장이 있으면 티켓으로 교환 후 입장하고, 초대장이 없으면 티켓을 구입 후 입장한다.

```java
import java.time.LocalDateTime;

public class Invitation {
    private LocalDateTime when;
}

```

```java
public class Ticket {
    private Long fee;

    public Long getFee() {
        return fee;
    }
}
```

이벤트 당첨자는 초대장을 가지고 있을 것이고, 당첨되지 않은 사람은 티켓을 구매할 수 있는 현금을 가지고 있을 것이다.

즉, 관람객이 가지고 있을 물건은 초대장, 현금, 티켓 3가지다.

추가적으로, 관람객이 가방도 들고 올 수 있다고 하자.  가방에는 현금을 인스턴스 변수(amount)로 한다.

```java
public class Bag {
    private Long amount;
    private Invitation invitation;
    private Ticket ticket;

    public boolean hasInvitaion(){
        return invitation != null;
    }

    public boolean hasTicket(){
        return ticket != null;
    }

    public void setTicket(Ticket ticket){
        this.ticket = ticket;
    }

    public void minusAmount(Long amount){
        this.amount -= amount;
    }

    public void plusAmount(Long amount){
        this.amount += amount;
    }
}
```

입장객은 초대장없이 현금만 보관하거나, 초대장과 현금을 동시에 보유하고 있을 것이다. 따라서 생성자를 다음과 같이 만들어 보자.

```java
public Bag(Long amount) {
	this(null, amount);
}

public Bag(Invitation invitation, Long amount) {
	this.amount = amount;
    this.invitation = invitation;
}
```

관람객은 소지품 보관을 위해 가방을 가지고 있을 수 있다.

```java
public class Audience {
    private Bag bag;

    public Audience(Bag bag) {
        this.bag = bag;
    }

    public Bag getBag() {
        return bag;
    }
}
```

극장에 입장하기 위해서는 매표소에서 초대장을 티켓으로 교환하거나, 현금으로 구매해야 한다. 매표소에는 관람객에 판매할 티켓과 티켓의 판매 금액이 보관돼야 한다.

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TicketOffice {
    private Long amount;
    private List<Ticket> tickets = new ArrayList<>();

    public TicketOffice(Long amount, Ticket ... tickets) {
        this.amount = amount;
        this.tickets.addAll(Arrays.asList(tickets));
    }

    public Ticket getTicket() {
        return this.tickets.remove(0);
    }

    public void minusAmount(Long amount){
        this.amount -= amount;
    }

    public void plusAmount(Long amount){
        this.amount += amount;
    }
}
```

```java
public class TicketSeller {
    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }

    public TicketOffice getTicketOffice() {
        return ticketOffice;
    }
}
```

```java
public class Theater {
    private TicketSeller ticketSeller;

    public Theater(TicketSeller ticketSeller) {
        this.ticketSeller = ticketSeller;
    }

    public void enter(Audience audience){
        if(audience.getBag().hasInvitaion()){
            Ticket ticket = ticketSeller.getTicketOffice().getTicket();
            audience.getBag().setTicket(ticket);
        }else{
            Ticket ticket = ticketSeller.getTicketOffice().getTicket();
            audience.getBag().minusAmount(ticket.getFee());
            ticketSeller.getTicketOffice().plusAmount(ticket.getFee());
            audience.getBag().setTicket(ticket);
        }
    }
}
```



1. Theater(소극장)은 관람객의 가방 안에 초대장이 들어있는지 확인한다.
2. 초대장이 들어있다면 판매원에게서 받은 티켓을 관람객의 가방 안에 넣어준다.
3. 가방안에 초대장이 없다면 티켓을 판매한다.
4. 소극장은 관람객의 가방에서 티켓 금액만큼을 차감한 후 매표소에 금액을 증가시킨다.
5. 소극장은 관람객의 가방 안에 티켓을 넣어줌으로써 입장 절차를 끝낸다.

---

# 2. 무엇이 문제인가



로버트 마틴(Robert C. Martin)은 소프트웨어 모듈이 가져야 하는 세 가지 기능에 관해 설명 한다.

1. 실행 중에 제대로 동작하는 것
2. 변경을 위해 존재하는 것
3. 코드를 읽는 사람과 의사소통 하는 것

즉, 모듈은 제대로 실행돼야 하고, 변경이 용이하며, 이해하기 쉬워야 한다.

이 시스템은, 기능은 제대로 동작 하지만 변경 용이성과 의사소통을 만족시키지 못한다.

문제는 관람객과 판매원이 소극장의 통제를 받는 수동적 존재라는 것. 즉, 제 3자인 소극장이 내 물건을 건드린다. 

> "이해 가능한 코드"
>
> 그 동작이 우리의 예상을 크게 벗어나지 않는 코드

또 다른 이유는 enter 메소드를 이해 할 때, Audience의 Bag을 가지고 있어야하고, Bag안에 현금과 티켓이 있고, TicketSeller가 TicketOffice에서 티켓을 팔고, TicketOffice에는 돈과 티켓이 모두 보관된다는 모든 것을 이해해야 한다.

가장 심각한 것은. Audience와 TicketSeller를 변경하면 Theater도 변경된다는 점이다.

1. 가방을 가지고 있지 않다면?
2. 현금이 아니라 신용카드를 이용한다면?
3. 매표소 밖에서 티켓을 판매해야 한다면?

등등과 같이 변경에 취약하다.

이것은 객체 사이의 **Dependency(의존성)** 문제다.

의존성이란 어떤 객체가 변경 될때 그 객체에게 의존하는 다른 객체도 변경될 수 있다는 사실을 내포한다.

의존성을 아예 없애자는게 아니라, 최소한의 의존성만 두고 불필요한 의존성을 없애자는 거다.

또 다른 말로는 **Coupling(결합도)**

## 자율성을 높이자

```java
public class Theater {
    private TicketSeller ticketSeller;

    public Theater(TicketSeller ticketSeller) {
        this.ticketSeller = ticketSeller;
    }

    public void enter(Audience audience){
        ticketSeller.sellTo(audience);
    }
}
```

```java
public class TicketSeller {
    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }

    public void sellTo(Audience audience){
        if(audience.getBag().hasInvitaion()){
            Ticket ticket = ticketOffice.getTicket();
            audience.getBag().setTicket(ticket);
        }else{
            Ticket ticket = ticketOffice.getTicket();
            audience.getBag().minusAmount(ticket.getFee());
            ticketOffice.plusAmount(ticket.getFee());
            audience.getBag().setTicket(ticket);
        }
    }
}
```

다음과 같이 바꾸었더니, TicketSeller의 getTicketOffice()메소드가 사라지고 외부에서는 ticketOffice에 대한 접근이 사라졌다.

**캡슐화**는 개념적이나 물리적으로 객체 내부의 세부적인 사항을 감추는 것, 캡슐화를 통해 객체 내부로의 접근을 제한하면 객체와 객체 사이의 결합도를 낮출 수 있다.

Theater입장에서는 TicketOffice가 어디있는지 몰라도 된다.

Theater는 TicketSeller의 **인터페이스(interface)**에만 의존한다. TicketSeller가 TicketOffice를 포함하고 있다는 사실은 **구현(implementation)**이다.

```java
public class Audience {
    private Bag bag;

    public Audience(Bag bag) {
        this.bag = bag;
    }

    public Long buy(Ticket ticket){
        if(bag.hasInvitaion()){
            bag.setTicket(ticket);
            return 0L;
        }else{
            bag.setTicket(ticket);
            bag.minusAmount(ticket.getFee());
            return ticket.getFee();
        }
    }
}
```

```java
public class TicketSeller {
    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }

    public void sellTo(Audience audience){
        ticketOffice.plusAmount(audience.buy(ticketOffice.getTicket()));
    }
}

```

변경된 코드에서는 Audience가 스스로 가방에서 초대장이 있는지 없는지 확인한다. Audience가 가방을 처리하기 때문에, Bag을 캡슐화할 수 있다.

## 무엇이 개선되었는가?

Audience나 TicketSeller의 내부구현을 변경하더라도 Theater를 함께 변경할 필요가 없어졌다는 것이다.

Audience에서 가방이 지갑으로 바뀌어도, TicketSeller에서 매표소가 은행으로 바뀌어도 수정되지 않아도 된다.

### 절차지향 프로그래밍

프로세스와 데이터를 별도의 모듈에 위치시키는 방식

위에서는 Theater가 TicketSeller, TicketOffice, Audience, Bag 모두에 의존하고 있다. 이것은 모든 처리가 하나의 클래스 안에 위치하고 나머지 클래스는 데이터의 역할만 수행하기 때문이다.

> **변경하기 쉬운 설계는 한번에 하나의 클래스만 변경할 수 있는 설계다.**

### 객체지향  프로그래밍

데이터와 프로세스가 동일한 모듈 내부에 위치 하도록 프로그래밍을 하는 방식

훌륭한 객체지향 설계의 핵심은 캡슐화를 이용해 의존성을 적절히 관리함으로써 객체 사이의 결합도를 낮추는 것이다.

---



절차지향과 객체지향의 근본적 차이는 책임의 이동(shift of responsibility)이다.

설계를 만드는 것은 의존성이다.

불필요한 의존성을 제거함으로써 객체 사이의 결합도를 낮추는 것이다.

결합도를 낮추기 위해 Theater가 몰라도 되는 세부사항을 캡슐화하는 것이다.

결과적으로 객체의 자율성을 높이고 응집도 높은 객체들의 공동체를 창조할 수 있게 된다.

---

### 조금 더 개선해 보자

Bag의 자율성을 주고 TicketOffice의 TicketSeller의 메소드를 옮겨보자. 이랬더니 자율성을 추가했지만, TicketOffice의 Audience 문제가 생겼다. 이는 뭘 의미하는가?

Trade Off가 필요하다

1. 어떤 기능을 설계하는 방법은 한가지 이상일 수 있다.
2. 동일한 기능을 한 가지 이상의 방법으로 설계할 수 있기 때문에 결국 설계는 트레이오프다.

> 의인화(anthropomorphism) - 레베카 워프스브록(Rebecca Wirfs-Brock)
>
> 능동적이고 자율적인 존재로 소프트웨어 객체를 설계하는 원칙

훌륭한 객체지향 설계란 소프트웨어를 구성하는 모든 객체들이 자율적으로 행동하는 설계를 카리킨다. 그 대상이 비록 실세계에서는 생명이 없는 수동적인 존재라 할지라도.

### 설계가 왜 필요한가?

> 설계란 코드를 배치하는 것이다

우리가 짜는 프로그램은 오늘 완성해야 하는 기능을 구현하는 코드를 짜야 하는 동시에 내일 쉽게 변경할 수 있어야한다.

변경을 수용할 수 있어야 하는 이유는? 요구사항은 항상 변경된다.