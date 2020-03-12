import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Main
{
    public static void main(String args[]) throws Exception
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();
        int TC = Integer.parseInt(br.readLine());
        int T = 1;

        while (T <= TC) {
            int max = -1;
            int min = 987654321;
            String[] str = br.readLine().split(" ");
            for(String s : str){
                int num = Integer.parseInt(s);
                int sum = 0;
                while(num > 0){
                    sum += (num % 10);
                    num /= 10;
                }
                max = Math.max(max, sum);
                min = Math.min(min, sum);
            }
            sb.append("#")
                    .append(T)
                    .append(" ")
                    .append(max)
                    .append(" " )
                    .append(min)
                    .append("\n");
            T++;
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }
}
