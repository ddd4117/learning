import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

class Solution {
    private static int N, T, P;

    public static void main(String... args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();
        int TC = Integer.parseInt(br.readLine());
        int tc = 1;

        while (tc <= TC) {
            StringTokenizer st = new StringTokenizer(br.readLine(), " ");

            N = Integer.parseInt(st.nextToken());
            T = Integer.parseInt(st.nextToken());
            P = Integer.parseInt(st.nextToken()) - 1;
            int answerCount[] = new int[T];
            int personalAnswer[] = new int[N];
            int map[][] = new int[N][T];
            for (int i = 0; i < N; i++) {
                st = new StringTokenizer(br.readLine(), " ");
                int count = 0;
                for (int j = 0; j < T; j++) {
                    int num = Integer.parseInt(st.nextToken());
                    map[i][j] = num;
                    if (num == 1) {
                        count++;
                        answerCount[j]++;
                    }
                }
                personalAnswer[i] = count;
            }
            int mainScore = 0, countOfCorrect = personalAnswer[P];
            for (int idx = 0; idx < T; idx++) {
                if(map[P][idx] == 1){
                    int count = N - answerCount[idx];
                    mainScore += count;
                }
            }
            int answer = 0;
            for (int i = 0; i < N; i++) {
                if (i == P) {
                    continue;
                }
                int score = 0;
                for (int j = 0; j < T; j++) {
                    if(map[i][j] == 1){
                        int count = N - answerCount[j];
                        score += count;
                    }
                }
                if(score > mainScore){
                    answer++;
                }
                else if(score == mainScore && personalAnswer[i] > countOfCorrect){
                    answer++;
                }else if(score == mainScore && personalAnswer[i] == countOfCorrect && i < P){
                    answer++;
                }
            }
            sb.append("#")
                    .append(tc)
                    .append(" ")
                    .append(mainScore)
                    .append(" ")
                    .append(answer + 1)
                    .append("\n");
            tc++;
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }
}
