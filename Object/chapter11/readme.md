# 합성과 유연한 설계



상속 관계 : is-a 관계, 정적인 관계

합성 관계 : has-a 관계, 동적인 관계

> [코드 재사용을 위해서는] 객체 합성이 클래스 상속보다 더 좋은방법이다.



## 1. 상속을 합성으로 변경하기

### 불필요한 인터페이스 상속 문제

Properties와 Stack 문제(Chapter10 참고)

상속관계를 정리하고 Hashtable을 Properties의 인스턴스 변수로 포함시키면 합성관계로 변경할 수 있다.

```java
public class Properties {
    private Hashtable<String, String> properties = new Hashtable <>();

    public String setProperty(String key, String value) {
        return properties.put(key, value);
    }

    public String getProperty(String key) {
        return properties.get(key);
    }
}
```

Verctor의 인스턴스 변수를 Stack의 인스턴스 변수로 선언함으로써 합성 관계로 변경할 수 있다.

```Java
public class Stack<E> {
    private Vector<E> elements = new Vector<>();

    public E push(E item) {
        elements.addElement(item);
        return item;
    }

    public E pop() {
        if (elements.isEmpty()) {
            throw new EmptyStackException();
        }
        return elements.remove(elements.size() - 1);
    }
}
```

이제 Stack이나 Properties의 부모 클래스의 불필요한 오퍼레이션에 영향을 받지 않는다.

### 메서드 오버라이딩의 오작용 문제

InstrumentedHashSet도 같은 방법을 통해 해결 가능

하지만, InstrumentedHashSet의 경우 HashSet이 제공하는 퍼블릭 인터페이스를 그대로 제공해야 한다.

=> 자바의 인터페이스를 활용하여 해결 가능

```java
public class InstrumentedHashSet<E> implements Set<E> {
    private int addCount = 0;
    private Set<E> set;

    public InstrumentedHashSet(Set<E> set) {
        this.set = set;
    }

    @Override
    public boolean add(E e) {
        addCount++;
        return set.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return set.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }

    @Override public boolean remove(Object o) {
        return set.remove(o);
    }

    @Override public void clear() {
        set.clear();
    }

    @Override public boolean equals(Object o) {
        return set.equals(o);
    }

    @Override public int hashCode() {
        return set.hashCode();
    }

    @Override public Spliterator<E> spliterator() {
        return set.spliterator();
    }

    @Override public int size() {
        return set.size();
    }

    @Override public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override public Iterator<E> iterator() {
        return set.iterator();
    }

    @Override public Object[] toArray() {
        return set.toArray();
    }

    @Override public <T> T[] toArray(T[] a) {
        return set.toArray(a);
    }

    @Override public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }

    @Override public boolean retainAll(Collection<?> c) {
        return set.retainAll(c);
    }

    @Override public boolean removeAll(Collection<?> c) {
        return set.removeAll(c);
    }
}
```

오버라이딩한 인스턴스 메서드에서 내부의 HashSet 인스턴스에게 동일한 메서드 호출을 그대로 전달한다는 것을 알 수 있다. 이를 **포워딩(forwarding)**이라고 부르고, 메서드를 호출하기 위해 추가된 메서드를 포워딩 메서드(forwarding method)라고 부른다.



### 부모 클래스와 자식 클래스의 동시 수정 문제: PersonalPlaylist

합성으로 변경하더라도 함께 수정해야하는 문제는 해결되지 않는다. (그래도 합성이 더 좋음)

PlayList의 내부 구현을 변경하더라도 파급효과를 최대한 PersonalPlayList 내부로 캡슐화할 수 있기 때문이다.

> **몽키 패치**
>
> 현재 실행 중인 환경에만 영향을 미치도록 지역적으로 코드를 수정하거나 확장하는 것을 가리킨다. PlayList의 코드를 수정할 권한이 없거나 소스코드가 존재하지 않는다고 하더라도 몽키 패치가 지원되는 환경이라면 PlayList에서 직접 remove 메서드를 추가하는 것이 가능하다.



## 2. 상속으로 인한 조합의 폭발적인 증가

작은 기능들을 조합해서 더 큰 기능을 수행하는 객체를 만드는 경우 문제점

- 하나의 기능을 추가하거나 수정하기 위해 불필요하게 많은 수의 클래스를 추가하거나 수정해야 한다.
- 단일 상속만 지원하는 언어에서는 상속으로 인해 오히려 중복 코드의 양이 늘어날 수 있다.



### 기본 정책과 부가 정책 조합하기

부가 정책은 다음과 같은 특성을 가진다.

- **기본 정책의 계산 결과에 적용된다.**
  기본 정책의 계산이 끝난 결과에 적용된다.
- **선택적으로 적용할 수 있다.**
  적용하 수도 있고 적용하지 않을 수도 있다.
- **조합 가능하다.**
  여러 군데에서 적용 가능하다.
- **부가 정책은 임의의 순서로 적용 가능하다.**
  여러 부가정책을 적용할 수 있다.



**훅 메서드(Hook Method)** : 추상 메서드와 동일하게 자식 클래스에서 오버라이딩할 의도로 메서드를 추가했지만 편의를 위해 기본 구현을 제공하는 메서드

**클래스 폭발(class explosion) / 조합의 폭발(combination explosion)** : 상속의 남용으로 하나의 기능을 추가하기 위해 필요 이상으로 많은 수의 클래스를 추가해야 하는 경우



## 3. 합성 관계로 변경하기

상속 관께는 컴파일타임에 결정되고 고정되기 때문에 코드를 실해하는 도중에는 변경할 수 없다.

### 

### 기본 정책 합성하기

가장 먼저 할 일은 각 정책을 별도의 클래스로 구현하는 것이다.

핸드폰이라는 개념에서 요금계산이라는 개념을 분리한다.

1. 기본 정책을 구현하자
2. 일반 요금제를 구현하자
3. 심야 할인 요금제를 구현하자
4. 기본 정책을 이용해 요금을 계산할 수 있도록 Phone을 수정하자
   Phone 내부에 RatePolicy를 합성한다



### 부가 정책 적용하기

알아둬야 할 것) 부가 정책은 기본정책에 대한 계산이 끝난 후에 적용되어야 한다.

- 부가 정책은 기본 정책이나 다른 부가 정책의 인스턴스를 참조할 수 있어야 한다. 다시 말해서 부가 정책의 인스턴스는 어떤 종류의 정책과도 합성될 수 있어야 한다.
- Phone의 입장에서는 자신의 기본 정책의 인스턴스에게 메시지를 전송하고 있는지, 부가 정책의 인스턴스에게 메시지를 전송하고 있는지 몰라야 한다. 
- 기본 정책과 부가 정책은 협력 안에서 동일한 '역할'을 수행해야 한다. 이것은 부가 정책이 기본 정책과 동일한 RatePolicy 인터페이스를 구현해야 한다는 것을 의미한다.



### 새로운 정책 적용하기

필요한 요금제를 구현한 클래스 '하나'만을 추가한 후 원하는 방식으로 조합하면 된다.

요구사항을 변경할 때 오직 하나의 클래스만 수정해도 된다는 것이다.



### 객체 합성이 클래스 상속보다 더 좋은 방법이다

상속을 사용하면 안되는 것인가? 그렇지는 않다. 상속을 구현 상속과 인터페이스 상속 두 가지로 나눠야 한다. 이는 13장에서 확인하자



## 4. 믹스인

- 객체를 생성할 때 코드 일부를 클래스 안에 섞어 넣어 재사용하는 기법

- 합성이 실행 시점에 객체를 조합하는 재사용 방법이라면 믹스인은 컴파일 시점에 필요한 코드 조각을 조합하는 재사용 방법이다
- 상속과 다른 점은 상속의 목적은 같은 범주에 두는 것이다. 믹스인은 말 그대로 코드를 다른 코드 안에 섞어 넣기 위한 방법이다.

~~자바에서는 못쓰기 때문에 넘어가야지..~~