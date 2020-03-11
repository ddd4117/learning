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
            String[] str = br.readLine().split(" ");
            StringBuilder answer = new StringBuilder();
            for(String s : str){
                answer.append(s.charAt(0));
            }
            sb.append("#")
                    .append(T)
                    .append(" ")
                    .append(answer.toString().toUpperCase())
                    .append("\n");
            T++;
        }
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }
}
