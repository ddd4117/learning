# HTTP1.1, HTTP2

## HTTP(HyperText Transper Protocol)

HTTP란 www(world wide web)에서 정보를 주고받을 수 있는 프로토콜, TCP/UDP를 사용하며, 80번 포트를 사용한다.

Client와 Server 사이에 이루어지는 요청/응답(Request/Response) 프로토콜이다.

## 

## HTTP1.1

- 1997년도에 발표된 Spec
- 기본적으로 Connection당 하나의 요청과 응답을 처리
- 동시전송 문제와 다수 리소스 처리함에 있어 속도와 성능 이슈를 가짐
- 예를 들어) a.png, b.png, c.png를 순서대로 요청할 때 a.png의 응답이 길어지면 b.png c.png는 a.png의 응답이 종료될 때까지 기다려야 한다. 이를 **Head Of Line Blocking(HOLB)**라고 한다

> **Domain Sharding**
>
> HTTP1.1의 단점을 극복하기 위해 다수의 Connection을 생성해서 병렬요청으로 보내기도 한다. 하지만 Domain당 Connection 의 개수의 제한이 존재하기 때문에 근본적 해결이 될 수 없었다.



## HTTP2

HTTP1.2가 아니라 HTTP2인 이유는 HTTP1.x 서버 및 클라이언트와 호환되지 않기 때문이다.



### HTTP의 구성요소

- Frame : HTTP2 통신에서 데이터를 주고받을 수 있는 가장작은 단위, 헤더 프레임과 데이터 프레임으로 구성
- Message : HTTP1.1처럼 요청과 응답의 단위, 메시지는 다수의 Frame으로 구성
- Stream : Connection 내에서 전달되는 바이트의 양방향 흐름이며, 하나 이상의 메시지가 전달
- 모든 통신은 단일 TCP 연결을 통해 수행되며 전달될 수 있는 양방향 스트림의 수는 제한이 없음
- 각 Stream에는 양방향 메시지 전달에 사용되는 고유 식별자와 우선순위 정보(선택 사항)가 있음
- 각 Message는 하나의 논리적 HTTP Meesage이며, 하나 이상의 Frame으로 구성
- ![HTTP/2 스트림, 메시지 및 프레임](https://developers.google.com/web/fundamentals/performance/http2/images/streams_messages_frames01.svg?hl=ko)

### HTTP2의 목표

- 전체 요청을 통해 지연 시간을 줄이는 것
- Multiplexed Streams
  - 한 커넥션으로 동시에 여러 개의 메시지를 주고 받을 수 있음
  - 응답은 순서에 상관없이 stream으로 주고 받음
- Header Compression
  - Header 정보를 압축하기 위해 Header Table과 Huffman Encoding 기법을 사용해서 압축(HPACK 압축방식)
  - HTTP1.1의 경우 Header에 중복값이 있어도 중복전송
  - HTTP2의 경우 중복값은 Static/Dynamic Header Table 개념을 사용하여 중복 Header를 검출, 중복헤더는 index값만 전송, 중복되지 않은 값은 Huffman Encoding 기법으로 인코딩하여 처리
- Stream Prioritization
- Server Push
  - 서버는 클라이언트의 요청에 대해 요청하지도 않은 리소스를 보내줄 수 있음
  - PUSH_PROMISE를 통해서 서버가 전송한 리소스에 대해서는 요청을 하지 않음



 ![HTTP/1.1 vs. HTTP/2 Protocol](https://www.imperva.com/learn/wp-content/uploads/sites/13/2019/01/http2.jpg) 

## 참고 자료

- https://developers.google.com/web/fundamentals/performance/http2?hl=ko 
-  https://ijbgo.tistory.com/26 

