import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();

        ArrayList<int[]>[] edge = new ArrayList[n+1];
        for(int i=1; i<=n; i++) {
            edge[i] = new ArrayList<>();
        }

        while(m-->0) {
            edge[sc.nextInt()].add(new int[]{sc.nextInt(), sc.nextInt()}); // v: [[v, w]]
        }

        int[] dist = new int[n+1];
        for(int i=1; i<=n; i++) {
            dist[i] = Integer.MAX_VALUE;
        }
        dist[1] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] == b[1] ? a[0] - b[0] : a[1] - b[1]);
        pq.add(new int[]{1, 0}); // v, w

        while(!pq.isEmpty()) {
            int[] tmp = pq.poll();
            int v = tmp[0];
            int w = tmp[1];

            for(int[] next : edge[v]) {
                int nextV = next[0];
                int nextW = next[1];

                if(w + nextW < dist[nextV]) {
                    dist[nextV] = w + nextW;
                    pq.add(new int[]{nextV, dist[nextV]});
                }
            }
        }

        for(int i=2; i<=n; i++) {
            System.out.println(dist[i] == Integer.MAX_VALUE ? -1 : dist[i]);
        }
    }
}