import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

class Solution {
    public static void main(String... args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        int TC = Integer.parseInt(br.readLine());
        int tc = 0;
        while(tc++ < TC){
            int size = Integer.parseInt(br.readLine());
            String read = br.readLine();
            String write = br.readLine();
            int answer = 0;
            for(int i = 0; i < size; i++){
                if(read.charAt(i) == write.charAt(i)){
                    answer++;
                }
            }
            bw.write("#" + tc + " " + answer + "\n");
            bw.flush();
        }
    }
}
