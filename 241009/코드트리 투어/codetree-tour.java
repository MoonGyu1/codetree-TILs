import java.util.*;
import java.io.*;

public class Main {
	static int MAX_V = 2000;
	static int[] dist;
	static int n;
	static int[][] w;
	
	public static void main(String[] args) throws Exception {
		// System.setIn(new FileInputStream("src/s202401_am_2/input3.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		StringTokenizer st;
		
		int Q = Integer.parseInt(br.readLine());
		int m;
		
		HashMap<Integer, int[]> travel = new HashMap<>(); // id: revenue, dest, deleted
		PriorityQueue<int[]> list = new PriorityQueue<>((a, b) -> a[0] == b[0] ? a[1] - b[1] : b[0] - a[0]);
		int s = 0;
		
		while(Q --> 0) {			
			st = new StringTokenizer(br.readLine());
			String c = st.nextToken();
//			System.out.println("q: " + Q + " c: " + c);
			switch(c) {
				
				case "100": // 건설
					n = Integer.parseInt(st.nextToken());
					m = Integer.parseInt(st.nextToken());
					w = new int[n][n];
					for(int i=0; i<n; i++) {
						for(int j=0; j<n; j++) {
							w[i][j] = Integer.MAX_VALUE;
						}
					}
					dist = new int[n];
					
					while(m --> 0) {
						int v1 = Integer.parseInt(st.nextToken());
						int v2 = Integer.parseInt(st.nextToken());
						int ww = Integer.parseInt(st.nextToken());
						
						w[v1][v2] = Math.min(w[v1][v2], ww);
						w[v2][v1] = Math.min(w[v2][v1], ww);
					}
					
					// dist 계산하기
					for(int i = 0; i < n; i++) {
						w[i][i] = 0;
						dist[i] = Integer.MAX_VALUE;
					}
					dist[s] = 0;
					boolean[] visited = new boolean[n];
					
					// w, v
					PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]);
					pq.add(new int[] {0, s});
					visited[s] = true;
					
					while(!pq.isEmpty()) {
//						for(int[] pp: pq) {
//							System.out.println(Arrays.toString(pp));
//						}
		
						
						int[] vv = pq.poll();
						int tmpW = vv[0];
						int tmpV = vv[1];
						
						for(int i=0; i<n; i++) {
							if(w[tmpV][i] != Integer.MAX_VALUE && !visited[i] && dist[i] > tmpW + w[tmpV][i]) {
								dist[i] = tmpW + w[tmpV][i];
								pq.add(new int[] {dist[i], i});
								visited[i] = true;
							}
						}
						
					}
					
//					System.out.println("dist: " + Arrays.toString(dist));
					
					
//					
//					for(int i=0; i<n; i++) {
//						System.out.println(Arrays.toString(w[i]));
//					}
					
					break;
				case "200": // 상품 생성 
					int id = Integer.parseInt(st.nextToken());
					int revenue = Integer.parseInt(st.nextToken());
					int dest = Integer.parseInt(st.nextToken());
					
					travel.put(id, new int[] {revenue, dest, 0});
					
//					if(dist[dest] == Integer.MAX_VALUE) {
//						continue;
//					}
					
					int cost = revenue - dist[dest];
//					if(cost < 0) continue;
					
					list.add(new int[] {cost, id});
					
					break;
				case "300": // 취소 
					id = Integer.parseInt(st.nextToken());
					if(travel.containsKey(id)) {
						travel.get(id)[2] = 1;
					}
					
					break;
				case "400": // 판매 
//					for(int[] pp : list) { 
//						System.out.println("cost: " + pp[0] + " id: " + pp[1]);
//					}
//					
					while(!list.isEmpty() && travel.get(list.peek()[1])[2] == 1) {
						list.poll();
					}
					
					PriorityQueue<int[]> tmpList = new PriorityQueue<>((a, b) -> a[0] == b[0] ? a[1] - b[1] : b[0] - a[0]);
					
					boolean flag = true;
					while(!list.isEmpty()) {
						int[] l = list.poll(); // cost, id
						cost = l[0];
						id = l[1];
						
						if(cost >= 0) {
							bw.write(String.format("%d\n", id));
							bw.flush();
							flag = false;
							break;
						}else {
							tmpList.add(l);
						}
						
					}
					for(int[] tt : tmpList) {
						list.add(tt);
					}
//					list = tmpList;
					if(flag) {
						bw.write("-1\n");
					}
		
					
					bw.flush();
					
					break;
				case "500": // 출발지 변경
					s = Integer.parseInt(st.nextToken());
					
					for(int i = 0; i < n; i++) {
						dist[i] = Integer.MAX_VALUE;
					}
					
					dist[s] = 0;
					visited = new boolean[n];
					
					// w, v
					pq = new PriorityQueue<>((a, b) -> a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]);
					pq.add(new int[] {0, s});
					visited[s] = true;
					
					while(!pq.isEmpty()) {
//						for(int[] pp: pq) {
//							System.out.println(Arrays.toString(pp));
//						}
						
						int[] vv = pq.poll();
						int tmpW = vv[0];
						int tmpV = vv[1];
						
						for(int i=0; i<n; i++) {
							if(w[tmpV][i] != Integer.MAX_VALUE && !visited[i] && dist[i] > tmpW + w[tmpV][i]) {
								dist[i] = tmpW + w[tmpV][i];
								pq.add(new int[] {dist[i], i});
								visited[i] = true;
							}
						}
						
					}
					
					PriorityQueue<int[]> newList = new PriorityQueue<>((a, b) -> a[0] == b[0] ? a[1] - b[1] : b[0] - a[0]);
					while(!list.isEmpty()) {
						int[] tmp = list.poll();
						id = tmp[1];
						int[] t = travel.get(id);
						int r = t[0];
						int d = t[1];
						cost = r - dist[d];
						
						newList.add(new int[] {cost, id});
					}
					
					for(int[] tt : newList) {
						list.add(tt);
					}
//					list = newList;
					
					break;
			}
		}
		
		
		
		
		
		
		// 맨 하단 
		bw.flush();
		bw.close();
	}
}