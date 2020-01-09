# ElasticSearch

## 	1. Elasticsearch 개념정리

검색에 자주 쓰이는 Apache Lucene Library 기반으로 만들어진 검색 엔진 어플리케이션

검색 엔진, 데이터 저장소, 분석 엔진으로 활용할 수 있다.

Json 형식으로 저장되고 사용되며, Schemless&Document 지원하고, Data를 원본과 복사본으로 구분하여 분산 저장하며 REST API를 지원한다.

## 	2. Elasticsearch와 RDB 비교

| Relational Database |    ElasticSearch     |
| :-----------------: | :------------------: |
|      Database       |        Index         |
|        Table        |         Type         |
|         Row         |       Document       |
|       Column        |        Field         |
|        Index        |       Analyze        |
|     Primary key     |         _id          |
|       Schema        |       Mapping        |
| Physical partition  |        Shard         |
|  Logical partition  |        Route         |
|     Relational      | Parent/Child, Nested |
|         SQL         |      Query DSL       |

## 	3. ElasticSearch 용어정리

### 		1. Cluster

​	가장 큰 시스템 단위, <u>최소 하나 이상의 노드로 이루어진 노드들의 집합</u>, 각 cluster는 자동으로 선정 된 Master Node를 가지고 있으며, Master Node가 실패할 경우 다른 Node로 대체될 수 있다.

### 		2. Master Node

​	Node를 관리하는 Node이며, 고사양 컴퓨터가 아니어도 된다.

  - Index 생성, 삭제

  - Cluster Node들의 추적 및 관리

  - 데이터 입력시 할당할 샤드 선택

    ### 3.  Data Node

​	데이터와 관련된 CRUD 작업노드, CPU, 메모리 자원을 많이 소모하므로 모니터링 요구, Master노드와 분리되는 것이 좋음

### 	4. Shard

​	검색의 기본 데이터베이스가 되는 Index 중 큰 인덱스를 여러개의 작은 Index로 나누어 저장하는 것(인덱스의 조각, 데이터 분산)

​	ElasticSearch에서 Scale-out을 위해 index를 여러 Shard로 쪼갠 것

### 	5. Replica

​	다른 노드에 Shard와 같은 데이터를 복제하여 안정성 제공(검색할 때 이용하기도 하며, Replica Shard라고도 불림)

​	노드를 손실했을 경우 데이터의 신뢰성을 위해 샤드를 복제하는 것

### 6. Id

​		Document를 구분하기 위함, Document의 Index/id는 유일해야한다.

### 7. Mapping

​	Mapping은 RDB의 Schema와 비슷하다. 각각의 Index는 Mapping을 갖는다. 

![image-20191217174413963](C:\Users\ddd41\AppData\Roaming\Typora\typora-user-images\image-20191217174413963.png)

![image-20191217172312107](C:\Users\ddd41\AppData\Roaming\Typora\typora-user-images\image-20191217172312107.png)



참고자료

 https://blog.naver.com/PostView.nhn?blogId=hihello0426&logNo=221494255164&parentCategoryNo=50&categoryNo=52&viewDate=&isShowPopularPosts=false&from=postView 

 https://victorydntmd.tistory.com/308 