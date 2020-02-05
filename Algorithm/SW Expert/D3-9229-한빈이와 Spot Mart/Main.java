import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class Main {
    private static int N, M;

    public static void main(String... args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();
        int TC = Integer.parseInt(br.readLine());
        int T = 1;

        while (T <= TC) {
            StringTokenizer st = new StringTokenizer(br.readLine(), " ");
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());
            int arr[] = new int[N];
            st = new StringTokenizer(br.readLine(), " ");
            for (int i = 0; i < N; i++) {
                arr[i] = Integer.parseInt(st.nextToken());
            }
            int max = -1;
            for (int i = 0; i < N; i++) {
                for (int j = i + 1; j < N; j++) {
                    int sum = arr[i] + arr[j];
                    if (sum <= M) {
                        max = Math.max(max, sum);
                    }
                }
            }
            sb.append("#")
                    .append(T)
                    .append(" ")
                    .append(max)
                    .append("\n");
            T++;
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }
}
