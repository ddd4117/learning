import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    private static int length;
    private static int ALPHABET_SIZE = 26;
    private static int ALPHABET[] = new int[ALPHABET_SIZE];

    public static void main(String... args) throws Exception {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String s = bf.readLine();
        length = s.length();
        for (int i = 0; i < length; i++) {
            ALPHABET[s.charAt(i) - 'a']++;
        }

        System.out.println(bfs('.',0));
    }

    private static int bfs(char before, int count) {
        int answer = 0;
        if (count == length) {
            return 1;
        }

        for (int i = 0; i < ALPHABET_SIZE; i++) {
            if(ALPHABET[i] < 1 || (i +'a') == before){
                continue;
            }
            ALPHABET[i]--;
            answer += bfs((char) ('a' + i), count + 1);
            ALPHABET[i]++;
        }
        return answer;
    }
}
