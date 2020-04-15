# REST API

## REST

- **RE**presentational **S**tate **T**ransfer
- 인터넷 상의 시스템 간의 상호 운용성(interoperability)을 제공하는 방법 중 하나
- 시스템 제 가각의 독립적인 진화를 보장하기 위한 방법
- REST API : REST 아키텍처 스타일을 따르는 API
- https://www.youtube.com/watch?v=RP_f5dMoHFc

### REST 아키텍처 스타일

- Client - Server

- Stateless

- Cache

- Uniform Interface

  - Identication of resources
  - manipulation of resources through representations
  - self-descrive messages
    - 메시지 스스로 메시지에 대한 설명이 가능해야 한다.
    - 서버가 변해서 메시지가 변해도 클라이언트는 그 메시지를 보고 해석이 가능하다.
    - **확장 가능한 커뮤니케이션**
  - hypermisa as the engine of application state(HATEOAS)
    - 하이퍼 미디어(링크)를 통해 애플리케이션 상태 변화가 가능해야 한다
    - 링크 정보를 동적으로 바꿀수 있다.(Versioning 할 필요 없다)

  

  예를 들어) 유튜브에 들어가서 원하는 것을 보는데 어떤 응답으로 오던 다음 상태(영상)으로의 전이를 하려면 서버의 응답의 하이퍼 링크 정보를 통해서 이동해야한다.

  하이퍼 링크 정보를 바꾸더라도 링크 의미를 파악해서 이동한다면 실제 URI가 바뀌더라도 아무 상관없다. -> URI에 version 정보를 안붙혀도 된다. 그리고 그렇게 해야 독립적인 진화가 가능하다.

  이러고 싶지 않으면 그냥 HTTP API라고 부르면 되는데, 대부분의 API가 REST API가 아니라 HTTP API라고 보면 된다. 정말 잘 되어 있는 REST API 예제는 Github API다.

  ```json
  {
    "total_count": 40,
    "incomplete_results": false,
    "items": [
      {
        "id": 3081286,
        "node_id": "MDEwOlJlcG9zaXRvcnkzMDgxMjg2",
        "name": "Tetris",
        "full_name": "dtrupenn/Tetris",
        "owner": {
          "login": "dtrupenn",
          "id": 872147,
          "node_id": "MDQ6VXNlcjg3MjE0Nw==",
          "avatar_url": "https://secure.gravatar.com/avatar/e7956084e75f239de85d3a31bc172ace?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
          "gravatar_id": "",
          "url": "https://api.github.com/users/dtrupenn",
          "received_events_url": "https://api.github.com/users/dtrupenn/received_events",
          "type": "User"
        },
        "private": false,
        "html_url": "https://github.com/dtrupenn/Tetris",
        "description": "A C implementation of Tetris using Pennsim through LC4",
        "fork": false,
        "url": "https://api.github.com/repos/dtrupenn/Tetris",
        "created_at": "2012-01-01T00:31:50Z",
        "updated_at": "2013-01-05T17:58:47Z",
        "pushed_at": "2012-01-01T00:37:02Z",
        "homepage": "",
        "size": 524,
        "stargazers_count": 1,
        "watchers_count": 1,
        "language": "Assembly",
        "forks_count": 0,
        "open_issues_count": 0,
        "master_branch": "master",
        "default_branch": "master",
        "score": 10.309712
      }
    ]
  }
  ```

  github의 repository 검색 API인데, 이를 보면 api에 대한 self-descrive가 이루어져 있고 전이할 수 있는 url 정보가 나와있다. 그래서 client는 정보를 보고 url로 이동하면 된다.

  > **RSS(Rich Stie Summary)**
  >
  > 이 정보를 받는 사람은 다른 형식으로 이용할 수 있다. 사이트 내 구성되어 있는 정보 단위를 디자인 없이 텍스트로만 간략하게 전달하는 기능

  



## 참고 자료

- 인프런 REST-API 강좌, 백기선
