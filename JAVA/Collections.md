# Collectors를 잘 써보자



## groupingBy

```sql
SELECT
    company_subject_sn
    , COUNT(member_subject_sn)
FROM
    MEMBER
GROUP BY
    company_subject_sn
```

위와 같은 SQL에서 GROUP BY를 사용하듯 Java에서도 grouping을 할 수 있다.

Collection 객체에 대한 특정조건으로 Grouping을 할 수 있는데

```JAVA
Map<Dish.Type, List<Dish>> dishesByType =
    menu.stream().collect(groupingBy(Dish::getType));
```

또는 조건을 넣어서도 처리할 수 있다.

```java
public enum CaloricLevel { DIET, NORMAL, FAT }

Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream().collect(
  groupingBy(dish -> {
      if (dish.getCalories() <= 400) {
        return CaloricLevel.DIET;
      } else if (dish.getCalories() <= 700) {
        return CaloricLevel.NORMAL;
      } else {
        return CaloricLevel.FAT;
      }
  })
);
```







## 참고자료

-  https://yongho1037.tistory.com/704 

-  [https://medium.com/@SlackBeck/java-stream-api%EB%A1%9C-%EB%8D%B0%EC%9D%B4%ED%84%B0-grouping-%ED%95%98%EA%B8%B0-9a86d46f5123](https://medium.com/@SlackBeck/java-stream-api로-데이터-grouping-하기-9a86d46f5123) 
