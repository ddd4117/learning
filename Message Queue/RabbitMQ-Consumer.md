# RabbitMQ Consumer

"Consumer"는 보톡 message를 소비하는 application을 의미한다. 

RabbitMQ는 메시징 브로커다. Publiser로부터 메시지를 받고, 라우팅 한다. 라우팅 할 큐가 있다면 저장하거나, Consumer에게 즉시 전달한다.

Consumer는 queue에서 메시지를 소비하며, 메시지를 소비하려면 Queue가 있어야 한다. Consumer가 추가된다면, Queue에 메시지가 준비되어 있다면 바로 전달한다.

존재하지 않는 Queue에서 소비하려하면 `404 Not Found` 코드와 함께 `Channel-Level Exception`이 발생하며, 시도된 채널이 닫히도록 렌더링 된다.



## Consumer Lifecycle

Consumer는 오래 살도록 설계되어 있다. 즉, Consumer는 lifetime동안 여러 번 배달을 받는다. 단일 메시지를 소비하기 위해 Consumer를 등록하는 것은 좋은 방법이 아니다.

Consumer는 일반적으로 애플리케이션이 시작 중에 등록된다.

### Connection Recovery

클라이언트가 RabbitMQ와의 연결을 끊을 수 있다. Connection loss가 발견되면, 메시지 배달이 중지된다.

일부  Client Library는 자동 연결 복구 기능을 제공한다. (Java, .NET, Bunny와 같은 몇몇 Library)

라이브러리들은 100% 모든 시나리오를 처리할 수는 없지만 일반적으로는 잘 작동하며 사용하길 권장된다.

다음과 같은 Recovery 순서가 잘 작동한다,

- Connection
- Channels
- Queues
- Exchanges
- Bindings
- Consumers

## 

## Acknowledgement Modes

- Automatic
- Manual
