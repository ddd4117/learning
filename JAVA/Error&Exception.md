# Error와 Exception

## 오류(Error)와 예외(Exception) 개념

오류(Error)란 시스템에 비정상적인 상황이 생겼을 경우를 뜻한다. 시스템 레벨에서 발생하기 때문에 심각한 수준의 오류이다. 개발자가 미리 예측하여 처리할 수 없기 때문에, 오류에 대한 처리를 신경 쓰지 않아도 된다.

예외(Exception)란 개발자가 구현한 로직에서 발생한 경우를 뜻한다. 즉, 예외는 발생할 상황을 미리 예측하여 처리할 수 있다.

 ![okky](https://lh3.googleusercontent.com/proxy/kQ9w23jk3uMhHKk3RaIUUHaCmUhdhGyDHzTVmA70_9Rt2iw0cQa_wksr5HJorMhGn7bb0zSA8VqQeogEmCQO4JnJNpWTqOpJm5Sut_nKuGDqVw)

자료 : https://okky.kr/article/362305 



## Checked Exception/Unchecked(Runtime) Exception

### Checked Exception

반드시 예외를 처리해야하는 Exception으로, 컴파일 단계에서 확인할 수 있다. Roll-back하지 않으며, 대표적으로 IOException, SQLException이 있다.

### UnChecked Exception

명시적인 처리를 강제하지 않는 Exception으로 실행단게에서 확인할 수 있다. Roll-back을 진행하며, RuntimeException 하위 예외가 해당한다(NullPointerException, IllegalArgumentException, IndexOutofBoundException 등)

Checked Exception과 UnChecked Exception의 가장 명확한 구분 기준은 '꼭 처리를 해야 하는가?'이다. Checked Exception이 발생할 가능성이 있는 메소드라면 반드시 로직을 처리해야 하고, UnChecked Exception은 명시적인 예외처리를 하지 않아도 된다.

또한, 예외처리 시점에서도 구분할 수 있다.

## 참고 자료
-  https://okky.kr/article/362305 
-  http://www.nextree.co.kr/p3239/ 
