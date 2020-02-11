import java.io.*;
import java.util.*;

public class Main {
    static int N, M;
    static int map[][];
    static int dy[] = {-1, 0, 1, 0}; //상, 우, 하, 좌
    static int dx[] = {0, 1, 0, -1};
    static Map<Integer, List<Point>> hashMap = new HashMap<>();
    static boolean visited[][];
    static int MST[][];
    static ArrayList<Edge> arr = new ArrayList<>();

    public static void main(String... args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        map = new int[N][M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine(), " ");
            for (int j = 0; j < M; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }


        visited = new boolean[N][M];
        int idx = 1;
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < M; col++) {
                if (map[row][col] == 0 || visited[row][col]) {
                    continue;
                }
                bfs(idx++, row, col);
            }
        }

        MST = new int[idx][idx];
        initMst(idx);
        hashMap.keySet().forEach(Main::buildBridge);
        Collections.sort(arr);

        int parent[] = new int[idx];
        int sum = 0;
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
        }
        int count = 0;
        for (int i = 0; i < arr.size(); i++) {
            if (Kruskal.findParent(parent, arr.get(i).node[0] - 1, arr.get(i).node[1] - 1) == 0) {
                sum += arr.get(i).distance;
                count++;
                Kruskal.unionParent(parent, arr.get(i).node[0] - 1, arr.get(i).node[1] - 1);
            }
        }
        System.out.println(count == idx - 2 ? sum : -1);
    }

    private static void bfs(int idx, int row, int col) {
        Queue<Point> queue = new ArrayDeque<>();
        List<Point> list = new ArrayList<>();
        queue.add(new Point(row, col));
        list.add(new Point(row, col));
        visited[row][col] = true;
        map[row][col] = idx;
        while (!queue.isEmpty()) {
            Point point = queue.poll();
            for (int next = 0; next < 4; next++) {
                int nextY = point.y + dy[next];
                int nextX = point.x + dx[next];
                if (nextY < 0 || nextY >= N || nextX < 0 || nextX >= M) {
                    continue;
                }
                if (visited[nextY][nextX] || map[nextY][nextX] == 0) {
                    continue;
                }
                map[nextY][nextX] = idx;
                visited[nextY][nextX] = true;
                queue.add(new Point(nextY, nextX));
                list.add(new Point(nextY, nextX));
            }
        }
        hashMap.put(idx, list);
    }

    private static void buildBridge(int idx) {
        List<Point> points = hashMap.get(idx);
        points.forEach(point -> {
            for (int next = 0; next < 4; next++) {
                int count = 0;
                int currentY = point.y;
                int currentX = point.x;
                boolean flag = false;
                int target = 0;
                while (true) {
                    int nextY = currentY + dy[next];
                    int nextX = currentX + dx[next];
                    if (nextY < 0 || nextY >= N || nextX < 0 || nextX >= M) {
                        break;
                    }
                    int value = map[nextY][nextX];
                    if (value == idx) {
                        break;
                    }
                    if (value == 0) {
                        count++;
                        currentX = nextX;
                        currentY = nextY;
                        continue;
                    }
                    flag = true;
                    target = value;
                    break;
                }
                if (flag && count >= 2) {
                    if (count < MST[idx][target]) {
                        arr.add(new Edge(idx, target, count));
                    }
                }
            }
        });
    }

    private static void initMst(int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    MST[i][j] = 0;
                    continue;
                }
                MST[i][j] = 9999;
            }
        }
    }
}

class Kruskal {
    public static int getParent(int[] arr, int x) {
        if (arr[x] == x) return x;
        return arr[x] = getParent(arr, arr[x]);
    }

    public static void unionParent(int[] arr, int a, int b) {
        a = getParent(arr, a);
        b = getParent(arr, b);

        if (a < b) arr[b] = a;
        else arr[a] = b;
    }

    public static int findParent(int[] arr, int a, int b) {
        a = getParent(arr, a);
        b = getParent(arr, b);

        if (a == b) return 1;
        else return 0;
    }
}


class Edge implements Comparable<Edge> {
    int node[] = new int[2];
    int distance;

    public Edge(int a, int b, int distance) {
        this.node[0] = a;
        this.node[1] = b;
        this.distance = distance;
    }

    @Override
    public int compareTo(Edge o) {
        if (this.distance < o.distance) return -1;
        else if (this.distance > o.distance) return 1;
        return 0;
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
