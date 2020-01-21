import java.util.Stack;

/**
 *
 * 1. 입력이 빈 문자열인 경우, 빈 문자열을 반환합니다.
 * 2. 문자열 w를 두 "균형잡힌 괄호 문자열" u, v로 분리합니다. 단, u는 "균형잡힌 괄호 문자열"로 더 이상 분리할 수 없어야 하며, v는 빈 문자열이 될 수 있습니다.
 * 3. 문자열 u가 "올바른 괄호 문자열" 이라면 문자열 v에 대해 1단계부터 다시 수행합니다.
 *   3-1. 수행한 결과 문자열을 u에 이어 붙인 후 반환합니다.
 * 4. 문자열 u가 "올바른 괄호 문자열"이 아니라면 아래 과정을 수행합니다.
 *   4-1. 빈 문자열에 첫 번째 문자로 '('를 붙입니다.
 *   4-2. 문자열 v에 대해 1단계부터 재귀적으로 수행한 결과 문자열을 이어 붙입니다.
 *   4-3. ')'를 다시 붙입니다.
 *   4-4. u의 첫 번째와 마지막 문자를 제거하고, 나머지 문자열의 괄호 방향을 뒤집어서 뒤에 붙입니다.
 *   4-5. 생성된 문자열을 반환합니다.
 */

class Solution {
    public String solution(String s) {
        if(isRight(s)){
            return s;
        }
        return makeRightString(s);
    }

    private String makeRightString(String w){
        if(w.equals("")){
            return "";
        }

        int subIdx = getBalanceStringIndex(w);
        String u = w.substring(0, subIdx);
        String v = w.substring(subIdx);

        if(isRight(u)){
            u += makeRightString(v);
            return u;
        }
        String tempString = "("
                + makeRightString(v)
                + ")"
                + reverse(u);
        return tempString;
    }

    private String reverse(String s){
        String subStr = s.substring(1, s.length() - 1);
        StringBuilder reverseString = new StringBuilder();
        for(int idx = 0 ; idx < subStr.length(); ++idx){
            reverseString.append(subStr.charAt(idx) == ')' ? '(' : ')');
        }
        return reverseString.toString();
    }

    private boolean isRight(String str){
        if(str.length() == 0){
            return true;
        }
        int length = str.length();
        Stack<Character> stack = new Stack<>();
        stack.push(str.charAt(0));

        for(int idx = 1; idx < length ; ++idx){
            char ch = str.charAt(idx);
            if(ch == '('){
                stack.push(ch);
                continue;
            }
            if(stack.empty()){
                return false;
            }
            stack.pop();
        }
        return stack.empty();
    }

    private int getBalanceStringIndex(String str){
        int length = str.length();
        Stack<Character> stack = new Stack<>();
        stack.push(str.charAt(0));

        for(int idx = 1; idx < length ; ++idx){
            char ch = str.charAt(idx);
            if(ch == '(' && stack.peek() == '('){
                stack.push(ch);
            } else if(ch == ')' && stack.peek() == ')'){
                stack.push(ch);
            } else{
                stack.pop();
            }
            if(stack.empty()){
                return idx + 1;
            }
        }
        return -1;
    }
}

public class Main {
    public static void main(String... args) throws Exception {
        Solution solution = new Solution();
        System.out.println(solution.solution("(()())()"));
        System.out.println(solution.solution(")("));
        System.out.println(solution.solution("()))((()"));
    }

}
