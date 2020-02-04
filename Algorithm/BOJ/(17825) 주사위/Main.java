import java.io.*;
import java.util.StringTokenizer;

public class Main {
    private static int T = 10;
    private static int HORSE_SIZE = 4;
    private static int answer = 0;
    private static int FINAL = 32;
    private static int map[][] = {
            {0, 1, 2, 3, 4, 5},
            {2, 2, 3, 4, 5, 9},
            {4, 3, 4, 5, 9, 10},
            {6, 4, 5, 9, 10, 11},
            {8, 5, 9, 10, 11, 12},
            {10, 6, 7, 8, 20, 29},
            {13, 7, 8, 20, 29, 30},
            {16, 8, 20, 29, 30, 31},
            {19, 20, 29, 30, 31, 32},
            {12, 10, 11, 12, 13, 14},
            {14, 11, 12, 13, 14, 15},
            {16, 12, 13, 14, 15, 16},
            {18, 13, 14, 15, 16, 17},
            {20, 18, 19, 20, 29, 30},
            {22, 15, 16, 17, 24, 25},
            {24, 16, 17, 24, 25, 26},
            {26, 17, 24, 25, 26, 27},
            {28, 24, 25, 26, 27, 28},
            {22, 19, 20, 29, 30, 31},
            {24, 20, 29, 30, 31, 32},
            {25, 29, 30, 31, 32, 32},
            {26, 20, 29, 30, 31, 32},
            {27, 21, 20, 29, 30, 31},
            {28, 22, 21, 20, 29, 30},
            {30, 23, 22, 21, 20, 29},
            {32, 26, 27, 28, 31, 32},
            {34, 27, 28, 31, 32, 32},
            {36, 28, 31, 32, 32, 32},
            {38, 31, 32, 32, 32, 32},
            {30, 30, 31, 32, 32, 32},
            {35, 31, 32, 32, 32, 32},
            {40, 32, 32, 32, 32, 32},
            {0, 32, 32, 32, 32, 32}
    };
    private static int horse[] = {0,0,0,0};
    private static int dice[] = new int[T];

    public static void main(String... args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        for(int i = 0 ; i < T; i++){
            dice[i] = Integer.parseInt(st.nextToken());
        }
        dfs(0, 0);

        bw.write(answer + "\n");
        bw.flush();
        bw.close();
        br.close();
    }

    private static void dfs(int idx, int sum){
        if(idx == T){
            answer = Math.max(answer, sum);
            return;
        }

        for(int i = 0; i < HORSE_SIZE; i++){
            if(horse[i] == FINAL) {
                continue;
            }
            int next = map[horse[i]][dice[idx]];
            if(existHorse(i, next)){
                continue;
            }
            int prevLocation = horse[i];
            horse[i] = next;
            dfs(idx + 1, sum + map[horse[i]][0]);
            horse[i] = prevLocation;
        }
    }

    private static boolean existHorse(int idx, int nextLocation){
        if(nextLocation == FINAL){
            return false;
        }
        for(int i = 0; i < HORSE_SIZE; i++){
            if(i == idx){
                continue;
            }
            if(horse[i] == nextLocation){
                return true;
            }
        }
        return false;
    }
}
