import java.io.*;

public class Main {
    private static int START = 2992;
    private static int END = 9999;
    public static void main(String... args) throws Exception {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));//선언
        for (int idx = START; idx < END; ++idx) {
            int ten = 0, twelve = 0, hex = 0;
            for (int N = idx; N > 0; N /= 10) {
                ten += N % 10;
            }

            for (int N = idx; N > 0; N /= 12) {
                twelve += N % 12;
            }

            for (int N = idx; N > 0; N /= 16) {
                hex += N % 16;
            }
            if (ten == twelve && ten == hex) {
                bw.write(idx + "\n");
                bw.flush();
            }
        }
    }
}
