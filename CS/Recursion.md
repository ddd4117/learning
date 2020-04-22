# 재귀함수(Recursion)

메소드를 정의할 때 자기 자신을 재참조하는 방법

- Base Case : 적어도 하나의 Recursion에 빠지지 않는 경우가 존재해야 한다.
- Recursive Case : Recursion을 하다보면 Base Case로 수렴해야 한다.

기본적으로 Recursion <> Iteration은 서로 변경 가능하다.



## 왜 쓸까?

1. 가독성
2. 적은 변수 사용



### 가독성

알고리즘 자체가 재귀적인 표현이 자연스러운 경우



### 적은 변수 사용

변수가 잡는 메모리에 관한게 아니다. mutable state(변경가능 상턔)에 관한 내용인데, 바뀔 수 있는 상태를 줄여 프로그램 오류가 발생할 수 있는 가능성을 없앤다는 이야기다.

mutable state를 가능한 피하는 것은 변수의 수를 줄이는 것과 변수가 가질 수 있는 값의 종류 또는 범위를 줄이는 것



## 재귀함수의 문제점

1. 재귀호출을 하게되면 함수 호출 횟수가 커질 수 있다.
2. Stack이 너무 커질 경우 StackOverFlow가 발생한다.



## 해결방안

1. Iteration으로 바꾸면 되지 않을까?
2. Tail Recursion

1번의 경우를 제일 많이 생각할 것 같다. 나도 그럤고, 그러다가 Tail Recursion을 알게되었다.

### Tail Recursion

Tail Recursion은 함수를 호출하게 되면 Stack에 쌓이고 그 함수가 종료될 경우 반환 지점을 가지고 있어야 한다. 그렇다면, 반환 지점이 필요없게 만들면 되지 않을까? Stack을 누적하는게 아니라 있는 걸 재사용하면 되지 않을까?

반환 지점을 가져야하는 이유 :question:   돌아가면 다시할 일이 있기 때문에

원래 자리에서 해야할 일을 남겨두지 않는 방법이 Tail Call이다.

> **Tail Call**
>
> 함수를 호출해서 값을 반환 받은 후 아무 일도 하지 않고 바로 반환하게 하기 위해 논리적으로 가장 마지막 위치에서 함수를 호출하는 방식



하지만 이렇게 짰다고 해도, 컴파일러가 처리해주지 않으면 소용이 없는데

 JAVA8은 지원해주지 않는다. (Kotlin은 지원해주는데) GCC, 스칼라 같은 언어도 지원해준다.

JAVA8을 지원해주지 않는 이유는 

> jdk 클래스들에는 몇몇 보안에 민감한 메소드들이 있는데, 이 메소드들은 메소드 호출을 누가 했는지를 알아내기 위해 jdk 라이브러리 코드와 호출 코드간의 스택 프레임 갯수에 의존한다. 스택 프레임의 수의 변경을 유발하게 되면 이 의존관계를 망가뜨리게 되고 에러가 발생할 수 있다. 이게 멍청한 이유라는 것을 인정하며, JDK 개발자들은 이 메커니즘을 교체해 오고 있다.
>
> 그리고 추가적으로, tail recursion이 최상위 우선순위는 아니지만,
>
> 결국에는 지원될 것이다.
>
> 

## 참고자료

-  [https://medium.com/sjk5766/%EC%9E%AC%EA%B7%80%ED%95%A8%EC%88%98%EB%A5%BC-%EC%93%B0%EB%8A%94-%EC%9D%B4%EC%9C%A0-ed7c37d01ee0](https://medium.com/sjk5766/재귀함수를-쓰는-이유-ed7c37d01ee0) 
-  [https://homoefficio.github.io/2015/07/27/%EC%9E%AC%EA%B7%80-%EB%B0%98%EB%B3%B5-Tail-Recursion/](https://homoefficio.github.io/2015/07/27/재귀-반복-Tail-Recursion/) 
-  http://wiki.sys4u.co.kr/display/SOWIKI/Tail+call+Optimization 
