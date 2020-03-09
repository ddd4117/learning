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
            String str = br.readLine();
            int[] arr = new int[10];
            int len = str.length();
            for(int i = 0; i < len; i++){
                arr[Integer.parseInt(String.valueOf(str.charAt(i)))]++;
            }
            int cnt = 0;
            for(int i =0 ; i< 10 ;i++){
                cnt += (arr[i] % 2 == 0 ? 0 : 1);
            }
            sb.append("#")
                    .append(T)
                    .append(" ")
                    .append(cnt)
                    
