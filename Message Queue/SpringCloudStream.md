# Spring cloud Stream

추상 컴포넌트

- Exchange : 메시지 브로커에서 큐에 메시지를

## @EnableBinding

해당 Annotation은 Application과 Channel을 바인딩 시켜주는 것, 두 채널 모두 Concrete Message를 사용하도록 구성할 수 있는 바인딩이다.

- Bindings

  - input channel과 output channel을 명시적으로 식별하는 인터페이스 모음

- Binder

  - Kafka나 RabbitMQ와 같은 Messaging 미들웨어를 구현해 놓은 것

- Channel

  - 메시징 미들웨어와 Application간 파이프로 통신하는 것

- StreamListener

  - MessageConverter가 직렬화/역직렬화를 한 후 채널에서 자동으로 처리되는 빈의 메시지 처리방법

- Message Schemas

  - 메시지를 직렬화/역직렬화할 때 사용하는 것

  

- RabbitMQ Binder는 기본적으로 **TopicExchange**로 매핑되어 있다.

- 각각 나뉘어진 Producer와 Consumer의 경우, Queue는 Partition index로 suffiexed되어 있고, partition index로 routing key가 사용된다.

- Consumer의 경우 Group을 지정하지 않으면 Anonymous Consumer가 되며 이 경우 Queue는 랜덤하게 자동으로 만들어지며 종료될 경우 자동으로 삭제된다.

- **autoBinDlq** 옵션을 사용하면, DLQ(Dead-Letter-Queues)를 생성하고 설정할 수 있다. 

- 기본적으로. Dead letter queue는 destination에 .dlq를 붙히는 이름을 가진다



- Retry를 Enable(maxAttemps > 1) 하면, 실패된 Message들은 retry가 소비되면 DLQ로 옮겨진다. 
- Retry를 Disable(maxAttemps = 1)하면, 실패한 Message들이 Re-Queue 대신 DLQ로 라우팅되도록 requeue-rejected를 false(기본값)으로 설정해야 한다.



- 기본적으로, RabbitMQ Binder는 Spring boot의 ConnectionFactory를 사용한다.

## Consumer Option

- **acknowledgeMode**
- **anonymousGroupPrefix**
  Binding이 "group" property가 없을 때, anonymous는 auto-delete queue를 destination exchange로 바인딩된다.  anonymous.<base64 representation of a UUID> 
- **autoBindDlq**
  DLQ를 자동으로 선언하고 바인더 DLX에 바인딩할지 여부
  default: false
- **bindingRoutingKey**
  queue를 exchange에 바인딩 할 routingKey(if bindQueue is true)
  default: #
- **bindQueue**





## 이미 있는 Queues/Exchanges를 사용할 경우

<prefix><destination>.<group>(group이 써있을 경우) 큐는 자동적으로 프로비저닝된다.

큐는 바인딩 된다 exchage에 "match-all" wildcard routing key(#)

prefix는 기본값이 ""이다.

output binding이 써있으면, queue/binding은 각 그룹으로 바인딩된다.



- `spring.cloud.stream.binding..destination=myExhange`
- `spring.cloud.stream.binding..group=myQueue`
- `spring.cloud.stream.rabbit.bindings..consumer.bindQueue=false`
- `spring.cloud.stream.rabbit.bindings..consumer.declareExchange=false`
- `spring.cloud.stream.rabbit.bindings..consumer.queueNameGroupOnly=true`

- `spring.cloud.stream.rabbit.bindings..consumer.bindingRoutingKey=myRoutingKey`
- `spring.cloud.stream.rabbit.bindings..consumer.exchangeType=`
- `spring.cloud.stream.rabbit.bindings..producer.routingKeyExpression='myRoutingKey'`






## 참고자료

- https://github.com/spring-cloud/spring-cloud-stream-binder-rabbit 
