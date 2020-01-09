# Annotation

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Override {
}
```

1. Retention

   - 자바 컴파일러가 Annotation을 다루는 방법을 기술하며, 특정 시점까지 여향을 미치는지를 결정

   - RetentionPolicy.SOURCE

     컴파일 전까지만 유효(컴파일 이후에는 사라짐)

   - RetentionPolicy.CLASS

     컴파일러가 클래스를 참조할 때까지 유효

   - RetentionPolicy.RUNTIME

     컴파일 이후에도 JVM에 의해 계속 참조가 가능(리플렉션 사용)

2. Target

   - Annotation이 적용할 위치를 선택

     ```java
     public enum ElementType {
         /** Class, interface (including annotation type), or enum declaration */
         TYPE,
     
         /** Field declaration (includes enum constants) */
         FIELD,
     
         /** Method declaration */
         METHOD,
     
         /** Formal parameter declaration */
         PARAMETER,
     
         /** Constructor declaration */
         CONSTRUCTOR,
     
         /** Local variable declaration */
         LOCAL_VARIABLE,
     
         /** Annotation type declaration */
         ANNOTATION_TYPE,
     
         /** Package declaration */
         PACKAGE,
     
         /**
          * Type parameter declaration
          *
          * @since 1.8
          */
         TYPE_PARAMETER,
     
         /**
          * Use of a type
          *
          * @since 1.8
          */
         TYPE_USE
     }
     ```

   - TYPE : 타입 선언

   - FIELD : 필드 선언

   - METHOD : 메소드 선언

   - PARAMETER : 파라미터 선언

   - CONSTRUCTOR : 생성자 선언

   - LOCAL_VARIABLE : 지역 변수 선언

   - ANNOTATION_TYPE  : 애노테이션 타입 선언

   - PACKAGE : 패키지 선언

   - TYPE_PARAMETER : 전달인자 타입 선언

   - TYPE_USE : 타입 선언

3. Documented

   - 해당 Annotation을 JavaDoc에 포함시킨다

4. Inhereited

   - 어노테이션의 상속을 가능하게 합니다.

5. Repeatable

   - 연속적인 어노테이션을 선언할 수 있게 해줌
   
   



```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface UserResolver {
}
```

