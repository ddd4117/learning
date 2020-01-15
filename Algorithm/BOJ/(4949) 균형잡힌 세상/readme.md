# (4949) 균형잡힌 세상

## 문제 설명

문자열에 포함되는 괄호는 소괄호("()")와 대괄호("[]")

- 모든 왼쪽 소괄호("(")는 오른쪽 소괄호(")")와만 짝을 이룰 수 있다.
- 모든 왼쪽 대괄호("[")는 오른쪽 대괄호("]")와만 짝을 이룰 수 있다.
- 모든 오른쪽 괄호들은 자신과 짝을 이룰 수 있는 왼쪽 괄호가 존재한다.
- 모든 괄호들의 짝은 1:1 매칭만 가능하다. 즉, 괄호 하나가 둘 이상의 괄호와 짝지어지지 않는다.
- 짝을 이루는 두 괄호가 있을 때, 그 사이에 있는 문자열도 균형이 잡혀야 한다.
  

**Input**

```
So when I die (the [first] I will see in (heaven) is a score list).
[ first in ] ( first out ).
Half Moon tonight (At least it is better than no Moon at all].
A rope may form )( a trail in a maze.
Help( I[m being held prisoner in a fortune cookie factory)].
([ (([( [ ] ) ( ) (( ))] )) ]).
 .
.
```

### output

```
yes
yes
no
no
no
yes
yes
```



## 풀이 방법

- 괄호와 .을 제외한 문자는 제외한다.

- '(' 이나 '['이 들어오면 스택에 Push

- ')' 이나 ']'이 들어오면 스택에 Pop

  - Pop 할때 size가 <= 0인 경우 "no" 출력
  - ')'인 경우 pop이 '['면 "no" 출력
  - ']'인 경우 pop이 ')'면 "no 출력"

- '.'을 만나면 한줄 종료

  - Stack size == 0 이면 "yes"
  - Stack size > 0 이면 "no"

  

~~오랜만에 풀었더니 Stack을 초기화 안해서 계속 틀림 ㅎ~~

```java
public class Main {
    private static char LEFT_SQUARE_BRACKET = '[';
    private static char RIGHT_SQUARE_BRACKET = ']';
    private static char LEFT_ROUND_BRACKET = '(';
    private static char RIGHT_ROUND_BRACKET = ')';
    private static String POINT = ".";

    public static void main(String... args) throws Exception {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

        while(true){
            Stack<Character> stack = new Stack<>();
            boolean answer = true;

            String line = bf.readLine();
            if(line.equals(POINT)){
                break;
            }

            int lineLength = line.length();
            for(int idx = 0; idx < lineLength; ++idx){
                char character = line.charAt(idx);
                if(character == LEFT_ROUND_BRACKET || character == LEFT_SQUARE_BRACKET){
                    stack.push(character);
                } else if(character == RIGHT_ROUND_BRACKET || character == RIGHT_SQUARE_BRACKET){
                    if(stack.empty()){
                        answer = false;
                        break;
                    }
                    char pop = stack.pop();
                    if((character == RIGHT_SQUARE_BRACKET && pop == LEFT_ROUND_BRACKET) ||
                            (character == RIGHT_ROUND_BRACKET && pop == LEFT_SQUARE_BRACKET)){
                        answer = false;
                        break;
                    }
                }
            }
            if(!stack.empty() || !answer){
                System.out.println("no");
                continue;
            }
            System.out.println("yes");
        }
        bf.close();
    }
}
```

