import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    private static StringBuilder sb = new StringBuilder();
    private static int tree[][] = new int[26][2];
    public static void main(String args[]) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int num = Integer.parseInt(br.readLine());

        for(int i = 0; i < num; i++){
            String[] line = br.readLine().split(" ");
            int parent = line[0].toCharArray()[0] -'A';
            int left = line[1].toCharArray()[0] == '.' ? -1 :line[1].toCharArray()[0] -'A';
            int right = line[2].toCharArray()[0] == '.' ? -1 : line[2].toCharArray()[0] -'A';

            tree[parent][0] =  left;
            tree[parent][1] = right;
        }
        preorder(0);
        sb.append("\n");
        inorder(0);
        sb.append("\n");
        postorder(0);
        System.out.println(sb);
    }
    //LVR
    private static void inorder(int num){
        if(tree[num][0] != -1) {
            inorder(tree[num][0]);
        }
        sb.append((char)(num + 'A'));
        if(tree[num][1] != -1){
            inorder(tree[num][1]);
        }
    }

    //LRV
    private static void postorder(int num){
        if(tree[num][0] != -1) {
            postorder(tree[num][0]);
        }
        if(tree[num][1] != -1){
            postorder(tree[num][1]);
        }
        sb.append((char)(num + 'A'));
    }
    //VLR
    private static void preorder(int num){
        sb.append((char)(num + 'A'));
        if(tree[num][0] != -1) {
            preorder(tree[num][0]);
        }
        if(tree[num][1] != -1){
            preorder(tree[num][1]);
        }
    }
}
