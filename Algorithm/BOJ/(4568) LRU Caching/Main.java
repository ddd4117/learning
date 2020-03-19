import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;

public class Main {
    public static void main(String args[]) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuffer sb = new StringBuffer();
        int TC = 1;
        while (true) {
            String line = br.readLine();
            if (line.equals("0")) {
                break;
            }
            sb.append("Simulation ")
                    .append(TC)
                    .append("\n");
            String[] command = line.split(" ");
            LRUCache<Character, Character> lru = new LRUCache<>(Integer.parseInt(command[0]));
            String str = command[1];
            for (int i = 0; i < str.length(); i++) {
                char ch = str.charAt(i);
                if (ch == '!') {
                    lru.getAll().forEach(entry -> sb.append(entry.getValue()));
                    sb.append("\n");
                    continue;
                }
                lru.put(ch, ch);
            }
            TC++;
        }
        bw.write(sb.toString());
        bw.flush();
    }
}

class LRUCache<K, V> {
    private static final float hashTableLoadFactor = 0.75f;
    private LinkedHashMap<K, V> map;
    private int cacheSize;

    public LRUCache(int cacheSize) {
        this.cacheSize = cacheSize;
        int hashTableCapacity = (int) Math.ceil(cacheSize / hashTableLoadFactor) + 1;
        map = new LinkedHashMap<K, V>(hashTableCapacity, hashTableLoadFactor, true) {
            private static final long serialVersionUID = 1;

            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > LRUCache.this.cacheSize;
            }
        };
    }

    public void put(K key, V value) {
        map.put(key, value);
    }

    public synchronized Collection<Map.Entry<K, V>> getAll() {
        return new ArrayList<>(map.entrySet());
    }
}
