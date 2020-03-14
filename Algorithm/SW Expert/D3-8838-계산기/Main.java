import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class Main {
    public static void main(String args[]) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();
        int TC = Integer.parseInt(br.readLine());
        int T = 1;

        while (T <= TC) {
            int size = Integer.parseInt(br.readLine());
            int[] arr = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            int dp = arr[0];
            for (int i = 1; i < size; i++) {
                dp = Math.max(arr[i] * dp, arr[i] + dp);
            }
            sb.append("#")
                    .append(T)
                    .append(" ")
                    .append(dp)
                    .append("\n");
            T++;
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }
}
