import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static void main(String... args) throws Exception{
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(bf.readLine());
        ArrayDeque<Integer> queue = new ArrayDeque();
        for(int i = 1 ; i <= N ; i++){
            queue.add(i);
        }
        while(queue.size() != 1){
            ((ArrayDeque) queue).pollFirst();
            int a = queue.pollFirst();
            queue.add(a);
        }
        System.out.println(queue.getFirst());
    }
}
