import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;

public class Main {
    private static StringBuilder answer = new StringBuilder();
    public static void main(String args[]) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Deque<Integer> deque = new ArrayDeque<>();
        int size = Integer.parseInt(br.readLine());
        for(int i = 1 ; i <= size; i++){
            deque.add(i);
        }

        while(deque.size() > 1){
            answer.append(deque.pollFirst()).append(" ");
            deque.add(deque.pollFirst());
        }
        answer.append(deque.pollLast());
        System.out.println(answer);
    }
}
