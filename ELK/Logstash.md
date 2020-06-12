# Logstash 설정



## Spring boot의 Logback Pattern

```xml
<pattern>[%date{ISO8601}] [PID:${PID:-}] [TID:%X{Transaction-ID}] [%thread] %-5level: %logger{36}\(%M:%line\) - %msg%n</pattern>
```



## logstash config

```tex
input {
  beats {
    port => "5000"
  }
}
filter {
  grok {
    match => {"message" => "\[%{TIMESTAMP_ISO8601:time}\] \[PID:%{NUMBER:pid}\] \[TID:%{DATA:TID}\] \[%{DATA:threadId}\] %{LOGLEVEL:logLevel}%{SPACE}: %{DATA:method} - %{GREEDYDATA:data}"}
  }

  #2020-06-09 13:51:12.510
  date {
    match => [ "time", "YYYY-MM-dd HH:mm:ss,SSS"]
    timezone => 'Asia/Seoul'
  }

  date {
    match => ["time", "YYYY-MM-dd HH:mm:ss,SSS"]
    target => 'kst_timezone'
  }

  ruby {
    code => "event.set('index_date',event.get('kst_timezone').time.strftime('%Y-%m-%d'))"
  }

  mutate{
     copy => {"[@metadata][ip_address]" => "[beat][ip]"}
     remove_field => ["message", "time"]
  }
}
output {
  elasticsearch {
    hosts => ["http://elasticsearch:9200"]
    index => "%{[@metadata][beat]}-%{index_date}"
  }
}
```

### Grok Pattern

```tex
\[%{TIMESTAMP_ISO8601:time}\] \[PID:%{NUMBER:pid}\] \[TID:%{DATA:TID}\] \[%{DATA:threadId}\] %{LOGLEVEL:logLevel}%{SPACE}: %{DATA:method} - %{GREEDYDATA:data}
```



### filebeat의 ip 구하기

```tex
  filter{
    mutate{
       copy => {"[@metadata][ip_address]" => "[beat][ip]"}
       ...
    }
  }
```

메타데이터의 값을 이용해서 logstash를 호출한 ip를 구할 수 있다.



### @Timestamp 값 변경

- Log Stash는 데이터를 받은 기준으로 `@timestamp`를 생성한다. 그래서 우리는 로그가 실제 발생한 시각으로 바꿔 줄 필요가 있다.

- `2020-06-09 13:51:12,510` 날짜 형식을 다음과 같이 `YYYY-MM-dd HH:mm:ss,SSS` 변환시켜 준다
  target을 지정하지 않으면 default는 `@timestamp`이다. 그리고 UTC 기준으로 저장이 될 수 있게 timezone을 'Asia/Seoul'로 지정해준다.
- 다음은 index 문제인데 `2020-06-09 13:51:12,510`를 변환하여 `@timestamp`를 `2020-06-09T4:51:12.510Z`로 변환 했는데, 09시 이전은 이전 날로 저장이 된다. 오늘 새벽에 만들어진게 어제의 index로 저장이 된다는 것이다. 그래서 로그가 발생한 `YYMMDD`를 `index_date`로 저장해 사용한다.

```tex
{
   date {
    match => ["time", "YYYY-MM-dd HH:mm:ss,SSS"]
    target => 'kst_timezone'
  }
  
  ruby {
    code => "event.set('index_date',event.get('kst_timezone').time.strftime('%Y-%m-%d'))"
  }
} 
  
output {
  elasticsearch {
    hosts => ["http://elasticsearch:9200"]
    index => "%{[@metadata][beat]}-%{index_date}"
  }
}
```
