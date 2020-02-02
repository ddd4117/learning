import java.io.*;
import java.util.StringTokenizer;

public class Main {
    private static int R, C;
    static char arr[][];
    static int visited[] = new int[26];
    static int dy[] = {-1, 0, 1, 0};
    static int dx[] = {0, 1, 0, -1};
    static int answer = 0;
    public static void main(String... args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        arr = new char[R][C];
        for (int i = 0; i < R; i++) {
            String str = br.readLine();
            for (int j = 0; j < C; j++) {
                arr[i][j] = str.charAt(j);
            }
        }
        dfs(0, 0, 1);
        bw.write(answer + "\n");
        bw.flush();
        bw.close();
        br.close();
    }

    private static void dfs(int y, int x, int count) {
        answer = Math.max(count, answer);
        visited[arr[y][x] - 'A'] = 1;

        for (int dir = 0; dir < 4; dir++) {
            int nextY = y + dy[dir];
            int nextX = x + dx[dir];
            if (nextY < 0 || nextY >= R || nextX < 0 || nextX >= C) {
                continue;
            }
            if (visited[arr[nextY][nextX] - 'A'] == 1) {
                continue;
            }
            dfs(nextY, nextX, count + 1);
        }
        visited[arr[y][x] - 'A'] = 0;
    }
}
