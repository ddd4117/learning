# 이상적인 ThreadPool 설정방법

## ThreadPool을 왜 쓰는걸까?

스레드를 요청마다 생성하면 좋겠지만, 스레드 생성은 시간이 걸리고, 오버헤드가 발생한다.

요청 처리에 대해 지연시간이 발생되며, JVM과 OS에 의해 처리되어야하는 부분도 발생한다.

그래서 생각해낸 것이 ThreadPool인데, ThreadPool은 말그래도 Thread가 모여있는 자료구조? 집합? 뭐그런거다.

ThreadPool은 이전에 생성한 스레드를 재사용해서 오버헤드와 리소스 문제를 줄일 수 있다.



## ThreadPool의 제한

Worker Thread가 데이터베이스에 의존하는 경우, Database Connection Pool size에 따라 제한된다. 100개의 DB Connection Pool에 1000개의 worker thread는 말이 되지 않는다. 

또한, Worker Thread가 몇개의 요청만 동시에 처리할 수 있는 외부 서비스를 호출하는 경우, 외부 서비스 처리량에 의해 스레드 풀이 제한된다.

## 

## ThreadPool의 고려 요소

ThreadPool의 가장 중요한 요소 중 하나는 CPU이다. 

```java
int numOfCores = Runtime.getRuntime().availableProcessors();
```

다른 제약으로는 memory, file handles, socket handles 등등이 있다.



## ThreadPool 계산

브라이언 괴츠(Brian Goetz)는 다음과 같은 공식을 추천한다.

```java
 Number of threads = Number of Available Cores * (1 + Wait time / Service time)
```

- **Waiting time** - I/O 바인딩 작업이 완료될 때 까지 대기하는 시간, 즉 원격 서비스에서 HTTP 응답을 기다리는 시간

- **Service time** - HTTP 응답 처리, 마샬링/언마샬링, 기타 변환 등의 작업을 수행하는 데 소요되는 시간

> 예를 들어
>
> Worker Thread가 Microservice를 호출한다. MicroService 응답시간이 50ms, 처리 시간이 5ms이다. 그리고 우리 서버는 듀얼코어일 때
>
> Number of Available = 2, Waittime = 50, Service time = 5
>
> 2 * (1 + 50 / 5) = 22 threads

그러나, 이런 방식은 굉장히 단순화되었다. HTTP Connection Pool 외에도, JDBC 연결 풀이 있을 수 있다.

만약, 여러 종류의 Worker Thread가 필요하다면, 각 ThreadPool은 작업량에 따라 조정될 수 있도록 여러 개의 ThreadPool을 사용하는 것이 가장 좋다.

## 참고 자료

- https://jobs.zalando.com/en/tech/blog/how-to-set-an-ideal-thread-pool-size/?gh_src=4n3gxh1
- [https://en.wikipedia.org/wiki/Little%27s_law](https://en.wikipedia.org/wiki/Little's_law)
