import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
    public static void main(String args[]) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int count = Integer.parseInt(br.readLine());
        Integer dp[] = new Integer[count + 1];
        int[] arr = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        dp[0] = arr[0];
        int answer = dp[0];
        for(int i = 1 ; i < count ; i++){
            dp[i] = Math.max(dp[i-1] + arr[i], arr[i]);
            answer = Math.max(answer, dp[i]);
        }

        System.out.println(answer);
    }
}
