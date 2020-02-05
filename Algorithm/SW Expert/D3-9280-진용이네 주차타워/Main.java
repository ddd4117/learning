import java.io.*;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
    private static int N, M;
    private static int[] park;

    public static void main(String... args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();
        int TC = Integer.parseInt(br.readLine());
        int T = 1;
        while (T <= TC) {
            int answer = 0;
            StringTokenizer st = new StringTokenizer(br.readLine(), " ");
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());

            Queue<Integer> queue = new ArrayDeque<>();
            int R[] = new int[N + 1];
            park = new int[N + 1];
            int W[] = new int[M + 1];
            for (int i = 1; i <= N; ++i) {
                R[i] = Integer.parseInt(br.readLine());
            }

            for (int i = 1; i <= M; ++i) {
                W[i] = Integer.parseInt(br.readLine());
            }

            for (int i = 0; i < M * 2; ++i) {
                int idx = Integer.parseInt(br.readLine());
                if (idx > 0) {
                    int parkIdx = availableParking();
                    if (parkIdx == -1) {
                        queue.add(idx);
                        continue;
                    }
                    park[parkIdx] = idx;
                    answer += R[parkIdx] * W[idx];
                } else {
                    idx = -idx;
                    int parkIdx = getParkingIdx(idx);
                    park[parkIdx] = 0;
                    if (!queue.isEmpty()) {
                        int waitingCarIdx = ((ArrayDeque<Integer>) queue).pop();
                        park[parkIdx] = waitingCarIdx;
                        answer += R[parkIdx] * W[waitingCarIdx];
                    }
                }
            }
            sb.append("#")
                    .append(T)
                    .append(" ")
                    .append(answer)
                    .append("\n");
            T++;
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }

    private static int availableParking() {
        for (int i = 1; i <= N; i++) {
            if (park[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    private static int getParkingIdx(int idx) {
        for (int i = 1; i <= N; i++) {
            if (park[i] == idx) {
                return i;
            }
        }
        return -1;
    }
}
