# Log와 Metrics의 차이



## 모니터링이란

모니터링이란 시스템 내부에서 무슨일이 일어나고 있는지, 얼마나 많은 트래픽을 받고 있는지, 어떻게 실행되고 있는지, 얼마나 많은 오류가 있는지 등을 의미한다.

그러나 이것들은 수단일 뿐이지 최종 목표가 아니다. 목표는 발생하는 문제를 탐지, 디버그 및 해결할 수 있는 것이며, 모니터링은 그 과정의 필수적인 부분이다.

## 

## Logs

Log는 다양한 형태로 나온다.  Text logs, Binary logs 등이 있지만, 여기서 말하는 것은 Application logs이다.

Log Message는 이벤트가 발생했을 때 시스템이 생성한 이벤트를 설명하는 데이터 집합이다.

각 요청에 대한 logs, end point에 도달했는지, 얼마나 걸렸는지, 무엇을 return 했는지, source ip는 무엇인지 등등이 있다.

Log는 해결하고 있는 문제를 푸는 단서이다.



## **Metrics**

Metrics도 다양하지만, 여기서 말하는 것은 Counter라고 형태의 Metric 이다.

Log는 특정 이벤트에 관한 것이지만, Metric은 시스템의 한 시점에 측정이 된다. 측정 단위는 해당 값이 적용되는 값, 타임스탬프 및 식별자를 가질 수 있다. Log는 이벤트가 발생할 때마다 수집될 수 있지만, Metrics는 일반적으로 고정 시간 간격으로 수집된다.



## 차이점

가장 큰 차이는 Log는 발생한 사건이고 Metrics는 시스템의 상태를 측정한 것이다.

예를 들어, 내가 아픈 것(혈압, 온도, 체중) 은 Metrics이고, 아프기 까지의 과정(뭘 먹고, 어딜 가고 등)은 Log라고 할 수 있다.



## 참고자료

-  https://www.sumologic.com/blog/logs-metrics-overview/ 
-  https://grafana.com/blog/2016/01/05/logs-and-metrics-and-graphs-oh-my/ 
