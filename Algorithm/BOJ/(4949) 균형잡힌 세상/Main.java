import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Stack;

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