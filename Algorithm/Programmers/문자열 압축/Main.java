public class Main {
    public static void main(String... args) throws Exception {
        Solution solution = new Solution();
        System.out.println(solution.solution("aabbaccc"));
        System.out.println(solution.solution("ababcdcdababcdcd"));
        System.out.println(solution.solution("abcabcdede"));
        System.out.println(solution.solution("abcabcabcabcdededededede"));
        System.out.println(solution.solution("xababcdcdababcdcd"));
    }
}

class Solution {
    public int solution(String s) {
        int answer = s.length();
        int length = s.length();

        for (int subLength = 1; subLength <= length / 2; ++subLength) {
            int count = 0;
            StringBuilder compression = new StringBuilder();
            StringBuilder prevString = new StringBuilder(s.substring(0, subLength));
            for (int idx = 0; idx < length; idx += subLength) {
                if (idx + subLength < length) {
                    String subString = s.substring(idx, idx + subLength);
                    if (prevString.toString().equals(subString)) {
                        count++;
                        continue;
                    }
                    if (count > 1) {
                        compression.append(count);
                    }
                    compression.append(prevString);
                    prevString.setLength(0);
                    prevString.append(subString);
                    count = 1;
                } else {
                    String subString = s.substring(idx);
                    if (prevString.toString().equals(subString)) {
                        compression.append(++count);
                        compression.append(subString);
                        break;
                    }
                    if (count > 1) {
                        compression.append(count);
                    }
                    compression.append(prevString);
                    compression.append(subString);
                }
            }
            answer = Math.min(answer, compression.length());
        }
        return answer;
    }
}