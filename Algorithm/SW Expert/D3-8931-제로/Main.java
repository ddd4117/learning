import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.Queue;

class Solution {
    private static int N;

    public static void main(String... args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();
        int TC = Integer.parseInt(br.readLine());
        int T = 1;

        while (T <= TC) {
            Queue<Integer> queue = new ArrayDeque<>();
            N = Integer.parseInt(br.readLine());
            for(int i = 0; i < N ; i++){
                int num = Integer.parseInt(br.readLine());
                if(num == 0){
                    ((ArrayDeque<Integer>) queue).pop();
                    continue;
                }
                ((ArrayDeque<Integer>) queue).push(num);
            }
            int sum = 0;
            while(!queue.isEmpty()){
                sum += ((ArrayDeque<Integer>) queue).pop();
            }
            sb.append("#")
                    .append(T)
                    .append(" ")
                    .append(sum)
                    .append("\n");
            T++;
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }
}
