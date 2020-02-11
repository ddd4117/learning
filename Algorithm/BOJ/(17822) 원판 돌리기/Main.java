import java.io.*;
import java.util.*;

public class Main {
    static int N, M, T;
    static int arr[][];
    static int dy[] = {-1, 0, 1, 0}; //상, 우, 하, 좌
    static int dx[] = {0, 1, 0, -1};
    static boolean visited[][];

    public static void main(String... args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        T = Integer.parseInt(st.nextToken());
        arr = new int[N][M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine(), " ");
            for (int j = 0; j < M; j++) {
                arr[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int i = 0; i < T; i++) {
            st = new StringTokenizer(br.readLine(), " ");
            int x = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());
            boolean flag = false;
            int sum = 0;
            int count = 0;
            rotate(x, d, k);
            visited = new boolean[N][M];
            for (int row = 0; row < N; row++) {
                for (int col = 0; col < M; col++) {
                    if (arr[row][col] <= 0 || visited[row][col]) {
                        continue;
                    }
                    sum += arr[row][col];
                    count++;
                    visited[row][col] = true;
                    if (isCalculated(row, col)) {
                        flag = true;
                    }
                }
            }
            if (!flag) {
                plusMinus((double) sum / (double) count);
            }
        }

        int sum = 0;
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < M; col++) {
                if (arr[row][col] <= 0) {
                    continue;
                }
                sum += arr[row][col];
            }
        }
        bw.write(sum + "\n");
        bw.flush();
    }

    private static void rotate(int x, int d, int k) {
        for (int row = x; row <= N; row += x) {
            int circularNum = row - 1;
            for (int times = 0; times < k; times++) {
                if (d == 0) {
                    int temp = arr[circularNum][M - 1];

                    for (int i = M - 1; i > 0; --i) {
                        arr[circularNum][i] = arr[circularNum][i - 1];
                    }

                    arr[circularNum][0] = temp;
                } else {
                    int temp = arr[circularNum][0];

                    for (int i = 0; i < M - 1; ++i) {
                        arr[circularNum][i] = arr[circularNum][i + 1];
                    }

                    arr[circularNum][M - 1] = temp;
                }
            }
        }
    }

    private static boolean isCalculated(int y, int x) {
        List<Point> points = new ArrayList<>();
        Queue<Point> queue = new ArrayDeque<>();

        queue.add(new Point(y, x));
        points.add(new Point(y, x));

        boolean flag = false;
        while (!queue.isEmpty()) {
            Point point = queue.poll();
            int currentX = point.x;
            int currentY = point.y;
            int value = arr[currentY][currentX];

            for (int next = 0; next < 4; next++) {
                int nextY = currentY + dy[next];
                int nextX = currentX + dx[next];
                if (nextX == -1) {
                    nextX = M - 1;
                } else if (nextX == M) {
                    nextX = 0;
                }

                if (nextY < 0 || nextY >= N || nextX < 0 || nextX >= M) {
                    continue;
                }
                if (visited[nextY][nextX]) {
                    continue;
                }
                if (value == arr[nextY][nextX]) {
                    visited[nextY][nextX] = true;
                    queue.add(new Point(nextY, nextX));
                    points.add(new Point(nextY, nextX));
                }
            }
        }

        if (points.size() >= 2) {
            flag = true;
            for (int i = 0; i < points.size(); ++i) {
                Point point = points.get(i);
                arr[point.y][point.x] = -1;
            }
        }

        return flag;
    }

    private static void plusMinus(double avg) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                int value = arr[i][j];
                if (value == -1) {
                    continue;
                }
                if (value < avg) {
                    arr[i][j] += 1;
                } else if (value > avg) {
                    arr[i][j] -= 1;
                }
            }
        }
    }
}

class Point {
    public int x;
    public int y;

    public Point(int y, int x) {
        this.x = x;
        this.y = y;
    }
}
