# JAVA Garbage Collection

> **stop-the-world**
>
> GC를 실행하기 위해 JVM이 애플리케이션 실행을 멈추는 것
>
> stop-the-world가 발생하면 GC를 실행하는 쓰레드를 제외한 나머지 쓰레드는 모두 작업을 멈춘다. GC작업을 완료 후 중단했던 작업을 다시 시작한다.
>
> 대게 GC 튜닝이란 stop-the-world 시간을 줄이는 것이다.

JAVA는 메모리를 명시적으로 해제하지 않기 때문에 가비지 컬렉터(Garbage Collector)가 더 이상 필요 없는(쓰레기) 객체를 찾아 지우는 작업을 함

![Java Memory Model, JVM Memory Model, Memory Management in Java, Java Memory Management](https://cdn.journaldev.com/wp-content/uploads/2014/05/Java-Memory-Model-450x186.png)



### Weak generational hypothesis 

- 대부분의 객체는 금방 접근 불가능한 상태(unreachable)가 된다.
- 오래된 객체에서 젊은 객체로의 참조는 아주 적게 존재한다.

이에 따라 HotSpot VM(가상 머신)에서는 크게 2개의 물리적 공간으로 나누었다.

- Young Generation

  - 새롭게 생성한 대부분의 객체가 여기에 위치
  - 대부분의 객체가 금방 접근 불가능 상태(unreachable) 상태가 되기 때문에 매우 많은 객체가 여기서 생성되었다가 사라진다.
  - 이 영역에서 객체가 사라질 때 **Minor GC**가 발생한다고 말한다.

- Old Generation

  - 접근 불가능 상태로 되지 않아 Young 영역에서 살아남은 객체가 복사되는 곳
  - 대부분 Young 영역보다 크게 할당, 크기가 큰 만큼 Young 영역보다 GC는 적게 발생
  - 이 영역에서 객체가 사라질 때 **Major GC(Full GC)**가 발생한다고말한다.

- Permanent Generation

  - Method Aread라고 불리며, 객체나 억류(intern)된 문자열 정보를 저장하는 곳
  - Old에서 넘어온 객체가 영원히 남아있는 곳은 아니며, 이 영역도 GC가 발생한다

  

### Old 영역에 있는 객체가 Young 영역의 객체를 참조하는 경우

- Old 영역에는 512바이트의 덩어리(chunk)로 되어 있는 카드 테이블이 존재
- 카드 테이블에는 Old 영역에 있는 객체가 Young 영역의 객체를 참조할 때마다 정보가 표시
- Young 영역의 GC를 실행할 때에는 OLD 영역에 있는 모든 객체의 참조를 확인하지 않고, 카드 테이블만 뒤져서 GC 대상인지 식별한다.
- 카드 테이블은 write barrier를 사용하여 관리한다.

> **write barrier**
>
> Minor GC를 빠르게 할 수 있도록 하는 장치, 약간의 오버헤드는 발생하지만 전반적인 GC시간은 줄어든다.

## Young Generation

- Eden 영역
- Survivor 영역(2개)

Survivor 영역이 2개이기 때문에 총 3개의 영역으로 나뉜다.

### 각 영역의 처리 절차 순서

- 새로 생성된 대부분의 객체는 Eden 영역에 위치
- Eden 영역에서 GC가 한번 발생한 후 살아남은 객체는 Survivor 영역 중 하나로 이동
- Eden 영역에서 GC가 발생하면 이미 살아남은 객체가 존재하는 Survivor 영여긍로 객체가 계속 쌓인다.
- 하나의 Survivor 영역이 가득 차게 되면 그 중에서 살아남은 객체를 다른 Survivor 영역으로 이동한다.
- 가득 찬 Survivor 영역은 아무 데이터도 없는 상태로 된다.
- 이 과정을 반복하다가 계속해서 살아남아 있는 객체는 Old 영역으로 이동하게 된다.

이런 절차를 확인해 보면 Survivor 영역 중 하나는 반드시 비어있는 상태가 된다.

### 메모리 할당을 위한 두 가지 기술

- bump-the-pointer
  - Eden 영역에 할당된 마지막 객체를 추적, 마지막 객체는 Eden 영역의 맨 위(top)에 잇다.
  - 다음에 생성되는 객체가 있으면 Eden 영역에 넣기 적당한지만 확인한다.
  - 적당하다고 판정되면 Eden 영역에 넣고, 새로 생성된 객체가 맨 위에 있게 된다.
  - 새로운 객체를 생성할 때 마지막에 추가된 객체만 점검하면 되므로 메모리 할당이 빠르게 이루어진다.
- TLABs(Thread-Local Allocation Buffers)
  - Thread-Safe하기 위해서 여러 스레드에서 사용하는 객체를 Eden 영역에 저장하려면 Lock이 발생할 수 밖에 없다.
  - 각각의 스레드가 각각의 몫에 해당하는 Eden 영역의 작은 덩어리를 가질 수 있도록 하는 것이다.
  - 각 쓰레드에는 자기가 갖고 있는 TALB에만 접근할 수 있다.
  - 따라서, bump-the-pointer를 사용하더라도 문제가 되지 않는다.



## Old Generation

- Serial GC
- Parallel GC
- Parallel Old GC(Parallel Compacting GC)
- Concurrent Mark & Sweep GC(CMS)
- G1(Garbage First) GC

*절대 운영 서버에서 사용하면 안되는 GC - Serial GC  : CPU 코어가 하나만 있을 때 사용하기 위해 만들어 진 것

### Serial GC(-XX:+UserSerialGC)

Young 영역에서는 앞에서 기술한 방식을 사용, Old 영역에서는 mark-sweep-compact 알고리즘 사용한다.

1. Old 영역에서 살아 있는 객체를 식별(Mark)
2. 힙(Heap)의 앞 부분부터 확인하여 살아 있는것만 남긴다(Sweep)
3. 각 객체들이 연속되게 쌓이도록 힙의 가장 앞 부분부터 채워서 객체가 존재하는 부분과 객체가 없는 부분으로 나눈다(Compaction)

적은 메모리와 CPU 코어 개수가 적을 때 적합한 방식



### Parallel GC(-XX:+UseParallelGC)

Serial GC와 기본적인 알고리즘은 같다. 그러나 Serial GC는 처리하는 스레드가 하나지만, Parallel GC는 쓰레드가 여러개다. 

Parallel GC는 메모리가 충분하고 코어의 개수가 많을 때 유리하다.

![JavaGarbage4](https://d2.naver.com/content/images/2015/06/helloworld-1329-4.png)



### Parallel Old GC(-XX:+UseParallelOldGC)

Parallel GC와 비교하여 Old영역 GC 알고리즘만 다르다. Mark-Summary-Compaction

Summary 단계에서는 GC를 수행한 영역에 대해서 별도로 살아 있는 객체를 식별한다는 점에서 Sweep 단계와 다르다.



### CMS GC(--X:+UseConcMarkSweepGC)

![JavaGarbage5](https://d2.naver.com/content/images/2015/06/helloworld-1329-5.png)

1. 초기 Initial Mark 단계에서 클래스 로더에서 가장 가까운 객체 중 살아 있는 객체만 찾는 것으로 끝낸다.
2. 그리고 Concurrent Mark 단계에서는 방금 살아있다고 확인한 객체에서 참조하고 있는 객체들을 따라가면서 확인한다.(다른 스레드가 실행 중인 상태에서 동시에 진행된다)
3. Remark 단계에서는 Concurrent Mark 단계에서 새로 추가되거나 참조가 끊긴 객체를 확인한다.
4. Concurrent Sweep 단계에서는 쓰레기를 정리하는 작업을 실행한다. ((다른 스레드가 실행 중인 상태에서 동시에 진행된다)

이러한 방식으로 진행되기 때문에 stop-the-world 시간이 매우 짧다.

모든 애플리케이션의 응답 속도가 매우 중요할 때 CMS GC를 사용하며, Low Latency GC라고도 부른다.

#### 단점

- 다른 GC방식보다 메모리와 CPU를 더 많이 사용한다.
- Compaction단계가 기본적으로 제공되지 않는다.

조각난 메모리가 많아 Compaction 작업을 실행하면 다른 GC 방식의 Stop-the-world 보다 더 길기 때문에 Compaction 작업이 얼마나 자주 오랫동안 수행되는지 확인해야 한다..

CMS 콜렉터 방식은 2개 이상의 프로세서를 사용하는 서버에 적당하다. (= Webserver)

Parallel GC 보다 약 10~20% Heap Memory를 사용한다.



### G1 GC

![JavaGarbage6](https://d2.naver.com/content/images/2015/06/helloworld-1329-6.png)

1. 바둑판의 각 영역에 객체를 할당하고 GC를 실행한다. 
2. 그러다가, 해당 영역이 꽉 차면 다른 영역에서 객체를 할당하고 GC를 실행한다.

Young GC : Parallel GC, Old GC : Old 영역의 일부 Region만 GC가 일어난다.

Young, Old 영역으로 이동하는 단계가 사라진 GC방식



CMS와의 차이는 CMS는 전체를 하나로 본다면, G1은 N개의 영역으로 나누어 GC를 처리한다는 것이다.

즉, 큰 영역을 한번에 처리할 거냐?, 여러개로 나누어 자주처리할 거냐의 문제

## Default Garbage Collectors

- Java7 : Parallel GC
- Java8 : Parallel GC
- Java9 : G1 GC
- Java10 : G1 GC

## 참고 자료

- https://12bme.tistory.com/57
- https://d2.naver.com/helloworld/1329

