# SQL

(postgres 기준)

등급이 (A, B, C, S)가 있고, 우선순위가 S, A, B, C일 때 어떻게 조회를 해야하는가?

```SQL
SELECT tn.importance FROM table_name tn
order by
	CASE 
		WHEN tn.importance = 'S' THEN tn.importance END ASC,
	tn.importance ASC
```

order by 에 CASE WHEN [조건] THEN [RESULT] END를 하면 된다.

S를 제일 높은 우선 순위로 주고, 그다음 A,B,C를 오름차순 정렬 하면  S, A, B, C가 된다.
