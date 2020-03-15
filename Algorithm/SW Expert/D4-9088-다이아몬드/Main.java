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
            int[] arr = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
            int[] diamons = new int[arr[0]];

            for(int i = 0 ; i < arr[0]; i++){
                diamons[i] = Integer.parseInt(br.readLine());
            }

            Arrays.sort(diamons);
            int max = 0;
            for(int i = 0 ; i < arr[0]; i++){
                int count = 1;
                int currentDiamond = diamons[i];
                for(int j = i + 1; j < arr[0]; j++){
                    if(diamons[j] - currentDiamond <= arr[1]){
                        count++;
                    }
                }
                max = Math.max(max, count);
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
