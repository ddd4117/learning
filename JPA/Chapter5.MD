# Chapter6. 다양한 연관관계 매핑

연관관계 매핑시 고려해야할 3가지

1. 다중성
   - 다대일(ManyToOne)
   - 일대다(OneToMany)
   - 일대일(OneToOne)
   - 다대다(ManyToMany)
2. 단방향, 양방향
   - 테이블은 외래키 하나로 조인을 사용해서 양방향으로 쿼리(방향성X)
   - 객체는 참조용 필드를 가조있는 객체만 연관된 객체를 조회 가능
3. 연관관계 주인
   - 데이터베이스는 외래키 하나로 연관관계를 관리
   - 엔티티를 양방향으로 매핑하면 A->B, B->A 관리 포인트가 2곳이 되며 이 중 하나를 골라 연관관계 주인으로 설정
   - 주인이 아닌 방향은 외래 키를 변경할 수 없고 읽기만 가능하다
   - 연관관계 주인은 `mappedBy` 속성을 이용하지 않는다.
   - 연관관계 주인이 아니면 `mappedBy` 속성을 사용하고 주인 필드 이름을 값으로 입력



## 6.1 다대일

### 6.1.1 다대일 단방향

다대일 <> 일대다 관계에서 연관관계 주인은 항상 다(N) 쪽이다.

![img1](./img/img1.png)

- 회원은 Member.team으로 팀 엔티티를 참조할 수 있다
- 팀에는 회원을 참조하는 필드가 없다.
- 회원과 팀은 다대일 단방향 연관관계다.



### 6.1.2 다대일 양방향 [N:1, 1:N]

![img2](./img/img2.png)

- 양방향 객체 연관관계에서 실선이 연관관계의 주인(Member.team)
- 점선(Team.members)은 연관관계의 주인이 아님

```java
@Entity
public class Member {
	@Id @GeneratedValue
	@Column(name = "MEMBER_ID") 
	private Long id;

    private String username;

    @ManyToOne 
    @JoinColumn(name = "TEAM_ID")
    private Team team;
    
	public void setTeam(Team team) {
        this.team = team;
		//무한루프에 빠지지 않도록 체크 
        if(!team.getMembers().contains(this)){
            team.getMembers().add(this);
        }
    }
}
```

```java
@Entity 
public class Team {
	@Id @GeneratedValue @Column(name = "TEAM—ID")
    private Long id;
	
    private String name;
	
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<Member>();

    public void addMember(Member member) { 
        this.members.add(member); 
        if (member.getTeam() != this) { 
            //무한루프에 빠지지 않도록 체크 
            member.setTeam(this);
        }
    }
}
```



- 양방향은 외래 키가 있는 쪽이 연관관계의 주인
  - 일대다, 다대일의 경우 항상 다(N)쪽에 외래키가 있다
  - JPA는 외래키를 관리할 때 연관관계의 주인만 사용
  - 주인이 아닌 Team.members는 조회를 위한 JPQL이나 객체 그래프를 탐색할 때 사용
- 양방향 연관관계는 항상 서로를 참조해야 함
  - 한 쪽만 참조할 경우 연관관계가 성립하지 않음
  - 위의 setTeam, addMember의 경우처럼 항상 서로를 참조하게 하는 메소드를 작성하는 것이 편함
  - 양쪽다 작성할 경우 무한루프에 빠질 수 있음



## 6.2 일대다

엔티티를 하나 이상 참조할 수 있으므로 `Collection, List, Set, Map` 중에 하나를 사용

### 6.2.1 일대다 단방향[1:N]

- 일대다 단뱡항 관계는 JPA2.0부터 지원

![img3](./img/img3.png)

- 그림을 보면 Team.members가 Member 테이블의 TEAM_ID 외래키를 관리
- 이 매핑은 반대쪽 테이블에 있는 외래 키를 관리
- 외래키는 항상 다(N)쪽에 있음



```java
@Entity
public class Team {
	@Id @GeneratedValue 
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    @OneToMany @JoinColumn(name = "TEAM_ID") //MEMBER 테이블의 TEAM_ID(FK)
    private List<Member> members = new ArrayList<Member>();
	
    //Getter, Setter ...
}
```

```java
@Entity 
public class Member {
	@Id @GeneratedValue 
    @Column(name = "MEMBER_ID") 
	private Long id;
    
    private String username;
	//Getter, Setter ... 
}
```

- 일대다 단방향 관계를 매핑할 때는 `@JoinColumn` 을 명시
- 명시하지 않을 경우 JPA는 연결 테이블을 중간에 두고 연관관계를 관리하는 조인테이블(JoinTable) 전략을 기본으로 사용해서 매핑(7. 4절에서 설명)

#### 단점

- 매핑한 객체가 관리하는 외래 키가 다른 테이블에 있음
- 본인 테이블에 있으면 `INSERT SQL`로 한번에 처리할 수 있지만 `UPDATE SQL`을 추가로 실행
- 일대다 단뱡항 매핑보다는 다대일 양방향 매핑을 사용하자
  - 성능 문제도 있지만 관리가 부담스러우므로, 다대일 양방향 매핑을 사용하는 것



### 6.2.2 일대다 양방향[1:N, N:1]

- 일대다 양방향 매핑은 존재하지 않음, 더 정확히 말하면 양방향 매핑에서 `OneToMany`는 연관관계의 주인이 될 수 없음
- 다대일 관계는 항상 다(N) 쪽에 외래키가 있기 때문
- 이런 이유로 `ManyToOne`에는 `mappedBy`속성이 없음

![img4](./img/img4.png)



```java
@Entity
public class Team {
	@Id @GeneratedValue 
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    @OneToMany @JoinColumn(name = "TEAM_ID")
    private List<Member> members = new ArrayList<Member>();
	
    //Getter, Setter ...
}
```

```java
@Entity 
public class Member {
	@Id @GeneratedValue 
    @Column(name = "MEMBER_ID") 
	private Long id;
    
    private String username;
    
    @ManyToOne
    @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
    private Team team;
	//Getter, Setter ... 
}
```

- 둘다 관리하면 문제가 생기므로 Team에 insertable, updatable을 false로 설정해 읽기만 가능하게 한다
- 이러지 말고 그냥 다대일 양방향을 사용하자



## 6.3 일대일[1:1]

### 특징

- 일대일 관계는 그 반대도 일대일 관계
- 테이블 관계에서 일대다, 다대일은 항상 다(N) 쪽이 외래키를 가지지만, 일대일 관계는 주 테이블이나 대상 테이블 둘 중 어느 곳이나 외래키를 가질 수 있음

:star: **주 테이블에 외래 키**

- 주 객체가 대상 객체를 참조하는 것처럼 주 테이블에 외래 키를 두고 대상 테이블을 참조
- 객체지향 개발자들이 선호
- 주 테이블만 확인해도 대상 테이블과 연관관계 파악

:star: **대상 테이블에 외래 키**

- 전통적인 데이터베이스 개발자들 선호
- 테이블 관계를 일대일에서 일대다로 변경할 때 테이블 구조를 그대로 유지



### 6.3.1 주 테이블에 외래 키

#### 단방향

![img5](./img/img5.png)

### 

```java
@Entity
public class Member{
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    
    private String username;
    
    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;
    ...
}

@Entity
public class Locker{
    @Id @GeneratedValue
    @Column(name = "LOCKER_ID")
    private Long id;
    
    private String name;
    ...
}
```

- 일대일 관계이므로 `@OneToOne` 사용
- 데이터베이스에는 LOCKER_ID에 대해 UNIQUE 제약 추가
- 다대일 단방향과 비슷



#### 
