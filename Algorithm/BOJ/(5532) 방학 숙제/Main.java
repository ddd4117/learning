import java.io.*;

public class Main {
    public static void main(String... args) throws Exception {
        int L,A,B,C,D;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        L = Integer.parseInt(br.readLine());
        A = Integer.parseInt(br.readLine());
        B = Integer.parseInt(br.readLine());
        C = Integer.parseInt(br.readLine());
        D = Integer.parseInt(br.readLine());

        int korean = (A / C) + ((A % C) == 0 ? 0 : 1);
        int math = (B / D) + ((B % D) == 0 ? 0 : 1);
        bw.write(L - Math.max(korean, math) + "\n");
        bw.flush();
        bw.close();
        br.close();
    }
}
