# Transactional

- @Transactional 애노테이션이 붙은 클래스에 프록시를 생성하고, 프록시는 트랜잭션 로직을 메서드 앞 뒤에 넣어 준다.

- @Transactional이 포함된 메소드가 호출 될 경우, PlaformTransactionManager를 사용하여 트랜잭션을 시작하고 정상 여부에 따라 Commit 또는 Rollback 한다.

- @Transactional에 아무 속성을 주지 않으면, UnCheckedException과 Error를 기본 롤백 정책으로 이해한다.

- 정상 여부는 RuntimeException(UnCheckedException)이 발생했는지 기준으로 결정
- 다른 Exception(SQLException)의 경우 @Transactional(rollbackFor = {Exception.class})를 활용한다.



## Propagation

트랜잭션 동작 도중 다른 트랜잭션을 호출하는 상황에서 트랜잭션 생성유무를 다룬다.

1. REQUIRED : 현재 진행 중인 트랜잭션이 있으면 그것을 사용하고, 없으면 생성한다(DEFAULT)
2. MANDANTORY : 현재 진행중인 트랜잭션이 없으면 Exception 발생, 없으면 생성
3. REQUIRES_NEW : 항상 새로운 트랜잭션을 만든다.(트랜잭션 분리)
4. SUPPORTS : 현재 진행중인 트랜잭션이 있으면 그것을 사용, 없으면 그냥 진행
5. NOT_SUPPORTED : 현재 진행중인 트랜잭션이 있으면 그것을 미사용, 없으면 그냥 진행
6. NEVER : 현재 진행중인 트랜잭션이 있으면 EXCEPTION. 없으면 그냥 진행

> **트랜잭션 주의사항**
>
> 트랜잭션은 꼭 필요한 최소한의 범위로 수행, DB Connection 갯수는 제한적이기 때문에 커넥션을 소유하는 시간이 길어지면, 그 후에 사용 가능한 커넥션의 갯수가 줄어든다. 그러다 다른 트랜잭션이 수행될 때, 커넥션이 부족하여 커넥션을 받기 위해 기다리는 상황이 발생한다.



## 여담

같은 Bean에 대해서는 Transaction propagation을 REQUIRES_NEW로 설정해도 먹지 않는다.

왜인고 찾아보니, Transactional의 경우 Spring AOP를 사용하는데 AOP는 Proxy기반으로 한다.  근데 같은 Bean에 있을 경우 인터셉트하지 못해 Transaction을 새로 만들 수 없다고 한다.

## 참고사항

-  http://jmlim.github.io/spring/2018/12/07/spring-transaction/ 
