# Query



## 1) match_all, match_none

- 지정된 index의 모든 document를 검색하는 방법
- 특별한 검색어 없이 모든 document를 가져오고 싶을 때 사용

```json
{  
   "query":{  
      "match_all":{}
   }
}
```

- match_none의 경우 match_all과 반대된다.

```json
{
    "query": {
        "match_none": {}
    }
}
```



## 2) match

- 기본 필드 검색 쿼리로써, 텍스트/숫자/날짜 허용

```json
{
    "query": {
        "match" : {
            "message" : {
                "query" : "this is a test"
            }
        }
    }
}
```



## 3) bool

- **must**
  bool must 절에 지정된 모든 쿼리가 일치하는 document 조회
- **should**
  지정된 모든 쿼리 중 하나라도 일치하는 document 조회
- **must_not**
  지정된 모든 쿼리가 모두 일치하지 않는 document 조회

```json
{
  "query": {
    "bool": {
      "must": {
        "match_all": {}
      },
      "filter": {
        "term": {
          "status": "active"
        }
      }
    }
  }
}
```



## 4) filter

- must와 같이 지정된 모든 쿼리가 일치하는 document를 조회하지만, Filter Context에서 실행되기 때문에 score를 무시



## 5) Range

범위를 지정하여 범위에 해당하는 값을 갖는 document를 조회

- **gte** : 크거나 같다(Greater than equals, >=)
- **gt** : 크다(Greater than, >)
- **lte** : 작거나 같다(Less than equals, <=)
- **le** : 작다(Less than, <)
- **boost** : 쿼리의 boost 값 셋팅(기본값 1.0), 검색에 가중치 부여

```json
{
    "query": {
        "range" : {
            "age" : {
                "gte" : 10,
                "lte" : 20,
                "boost" : 2.0
            }
        }
    }
}
```



## 6) term

- 역색인에 명시된 토큰 중 **정확한 키워드**가 포함된 document 조회

**text type** : 이메일 본문과 같은 전문(full text), 역색인 가능

**keyword type** : 전화번호, 우편번호 등등, 역색인 불가

- term 쿼리는 정확한 키워드를 찾기 땜누에 전문 검색에는 어울리지 않음
- 전문 검색을 할 경우 match 쿼리를 사용하는 것이 좋음

```json
{
    "query": {
        "term": {
            "user": {
                "value": "Kimchy",
                "boost": 1.0
            }
        }
    }
}
```



## 7) terms

- 배열에 나열된 키워드 중 하나와 일치하는 document를 조회

```java
{
    "query" : {
        "terms" : {
            "user" : ["kimchy", "elasticsearch"],
            "boost" : 1.0
        }
    }
}
```



## 8) regexp

- 정규 표현식 term 쿼리를 사용할 수 있음
- Term level  Query로 정확한 검색을 한다는 의미

```json
{
    "query": {
        "regexp": {
            "user": {
                "value": "k.*y",
                "flags" : "ALL",
                "max_determinized_states": 10000,
                "rewrite": "constant_score"
            }
        }
    }
}
```





## 참고자료

-  https://victorydntmd.tistory.com/314 
-  https://bakyeono.net/post/2016-08-20-elasticsearch-querydsl-basic.html 