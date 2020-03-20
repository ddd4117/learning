import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

class Point{
    int y, x;

    public Point(int y, int x) {
        this.y = y;
        this.x = x;
    }
}

public class Main {
    private static int[] dy = {-1, 0, 1, 0};
    private static int[] dx = {0, 1, 0, -1};
    private static Queue<Point> waterQueue = new ArrayDeque<>();
    private static Queue<Point> hogQueue = new ArrayDeque<>();
    private static int row;
    private static int col;
    private static char[][] map;
    private static boolean[][] visited;
    public static void main(String args[]) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int[] arr = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        row = arr[0];
        col = arr[1];
        map = new char[row][col];
        visited = new boolean[row][col];
        for (int i = 0; i < row; i++) {
            String line = br.readLine();
            for (int j = 0; j < col; j++) {
                char ch = line.charAt(j);
                if (ch == 'S') {
                    visited[i][j] = true;
                    hogQueue.add(new Point(i, j));
                } else if (ch == '*') {
                    waterQueue.add(new Point(i, j));
                }
                map[i][j] = line.charAt(j);
            }
        }
        int time = 1;
        while (true) {
            waterBfs();
            int flag = hogBfs();
            if (flag == -1) {
                System.out.println(time);
                break;
            } else if (flag == 0) {
                System.out.println("KAKTUS");
                break;
            } else if (flag == 1) {

            }
            time++;
        }
    }

    private static int hogBfs() {
        int size = hogQueue.size();
        if (hogQueue.isEmpty()) {
            return 0;
        }

        for (int i = 0; i < size; i++) {
            Point hog = hogQueue.poll();
            for (int next = 0; next < 4; next++) {
                int nextY = hog.y + dy[next];
                int nextX = hog.x + dx[next];

                if (nextY < 0 || nextY >= row || nextX < 0 || nextX >= col) {
                    continue;
                }
                if(visited[nextY][nextX]){
                    continue;
                }
                if (map[nextY][nextX] == '*' || map[nextY][nextX] == 'X') {
                    continue;
                }
                if (map[nextY][nextX] == 'D') {
                    return -1;
                }
                visited[nextY][nextX] = true;
                hogQueue.add(new Point(nextY, nextX));
            }
        }
        return 1;
    }

    private static void waterBfs() {
        int size = waterQueue.size();
        for (int i = 0; i < size; i++) {
            Point water = waterQueue.poll();

            for (int next = 0; next < 4; next++) {
                int nextY = water.y + dy[next];
                int nextX = water.x + dx[next];

                if (nextY < 0 || nextY >= row || nextX < 0 || nextX >= col) {
                    continue;
                }
                if (map[nextY][nextX] == '*' || map[nextY][nextX] == 'X' || map[nextY][nextX] == 'D') {
                    continue;
                }
                map[nextY][nextX] = '*';
                waterQueue.add(new Point(nextY, nextX));
            }
        }
    }
}
