# Filebeat docker image로 띄우기

![filebeat](C:\Users\ddd41\Desktop\filebeat.png)



1. Dockerfile 작성

```dockerfile
FROM docker.elastic.co/beats/filebeat:7.4.2
# Copy our custom configuration file
COPY filebeat.yml /usr/share/filebeat/filebeat.yml
USER root
# Create a directory to map volume with all docker log files
RUN mkdir /usr/share/filebeat/dockerlogs
RUN chown -R root /usr/share/filebeat/
RUN chmod -R go-w /usr/share/filebeat/
```

filebeat의 공식 이미지를 가져와서 내가 작성한 filebeat.yml 설정으로 덮어쓰고 디렉토리에 대한 권한을 줍니다

2. filebeat.yml 작성

```yaml
filebeat.inputs:
  - type: log
  # Change to true to enable this input configuration.
    enabled: true

    # Paths that should be crawled and fetched. Glob based paths.
    paths:
      - /usr/share/filebeat/dockerlogs/*.log

output.elasticsearch:
   hosts: [elasticsearch ip : port]
   template:
   
#output.logstash:
#   host: [logstash ip:port]

```

- filebeat의 type은 여러 종류가 존재한다. 필요한 내용은 아래 링크를 통해 확인하자
  https://www.elastic.co/guide/en/beats/filebeat/master/configuration-filebeat-options.html 

- 우리는 elasticsearch로 바로 보낼꺼기 때문에 hosts에 본인이 설치된 elastic search ip를 작성한다.
  logstash나 다른 output에 관련된 정보는 https://www.elastic.co/guide/en/beats/filebeat/master/configuring-output.html 에서 확인하자

- paths는 어떤 파일을 읽을지에 대한 설정이다.

  우리는 `/usr/share/filebeat/dockerlogs`로 마운트 한다
  이 때 주의할 점은 `/var/log/*/*.log` 형식으로 작성해야 한다. `/var/log`는 폴더 자체를 의미하한다.
  `/var/**/*.log`으로 활용할 수도 있다. 세부 설정은 공식 홈페이지를 확인하자.

3. Build

```shell
$ docker build --tag filebeat .

Sending build context to Docker daemon 4.096 kB
Step 1/6 : FROM docker.elastic.co/beats/filebeat:7.4.2
 ---> bdd5a76d3891
Step 2/6 : COPY filebeat.yml /usr/share/filebeat/filebeat.yml
 ---> Using cache
 ---> 8bf52b1341d6
Step 3/6 : USER root
 ---> Using cache
 ---> 82bbbd426de9
Step 4/6 : RUN mkdir /usr/share/filebeat/dockerlogs
 ---> Using cache
 ---> e61c7df67b3d
Step 5/6 : RUN chown -R root /usr/share/filebeat/
 ---> Using cache
 ---> 213fdb329e77
Step 6/6 : RUN chmod -R go-w /usr/share/filebeat/
 ---> Using cache
 ---> 1add570514be
Successfully built 1add570514be

$ docker image ls 

REPOSITORY          TAG           IMAGE ID            CREATED             SIZE
filebeat            latest        1add570514be        1 minutes ago      510 MB
```

build를 하면 image가 생성된 것을 확인할 수 있다.



4. docker-compose.yml 작성

```yaml
version: "3.7"
services:
  filebeat:
    image: filebeat
    command: filebeat -e -strict.perms=false
    volumes:
      - ./filebeat.yml:/usr/share/filebeat/filebeat.yml
      - /a/logs/a.log:/usr/share/filebeat/dockerlogs/a.log
      - /b/logs/b.log:/usr/share/filebeat/dockerlogs/b.log
      - /c/logs/c.log:/usr/share/filebeat/dockerlogs/c.log
```

volumes에서 host의 log와 마운트 해준다.



5. 실행

```shell
$ docker-compose up
또는
$ docker-compose up -d


filebeat_1  | 2020-06-08T06:53:23.025Z  INFO    log/harvester.go:251    Harvester started for file: /usr/share/filebeat/dockerlogs/a.log
filebeat_1  | 2020-06-08T06:53:23.025Z  INFO    log/harvester.go:251    Harvester started for file: /usr/share/filebeat/dockerlogs/b.log
filebeat_1  | 2020-06-08T06:53:23.027Z  INFO    log/harvester.go:251    Harvester started for file: /usr/share/filebeat/dockerlogs/c.log
```

-d 옵션은 background 실행이다.

해당하는 파일을 읽는 지 확인할 수 있다.



## 파일을 읽지 못하는 경우

실행을 했는데 파일을 읽지 못하는 경우나 permission denied가 발생하는 경우 SELinux 문제일 수 있다.

- 첫번째 방법, Permissive 모드로 바꿔주고 재실행

```shell
$ getenforce
Enforcing
$ setenforce 0
$ getenforce
Permissive
```

- 두번째 방법, z option을 붙혀준다.

```yaml
version: "3.7"
services:
  filebeat:
    image: filebeat
    command: filebeat -e -strict.perms=false
    volumes:
      - ./filebeat.yml:/usr/share/filebeat/filebeat.yml
      - /a/logs/a.log:/usr/share/filebeat/dockerlogs/a.log:z
      - /b/logs/b.log:/usr/share/filebeat/dockerlogs/b.log:z
      - /c/logs/c.log:/usr/share/filebeat/dockerlogs/c.log:z
```

