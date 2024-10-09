import java.util.*;
import java.io.*;

public class Main {
	static int MAX_R = 1001;
	static int[] dist;
	static int n;
	static int[][] weight;
	
	public static void main(String[] args) throws Exception {
		// System.setIn(new FileInputStream("src/s202401_am_2/input5.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		int Q = Integer.parseInt(br.readLine());
		int m;
		
		HashMap<Integer, int[]> travelInfo = new HashMap<>(); // id: revenue, dest, deleted (매출, 목적지, 삭제여부)
		
		// {cost, id}
		PriorityQueue<int[]>travelList = new PriorityQueue<>((a, b) -> a[0] == b[0] ? a[1] - b[1] : b[0] - a[0]);
		int s = 0;
			
		while(Q --> 0) {			
			st = new StringTokenizer(br.readLine());
			String c = st.nextToken();
//			System.out.println("q: " + Q + " c: " + c);
			switch(c) {
				
				case "100": // 건설
					n = Integer.parseInt(st.nextToken());
					m = Integer.parseInt(st.nextToken());
					weight = new int[n][n];
					for(int i=0; i<n; i++) {
						for(int j=0; j<n; j++) {
							weight[i][j] = MAX_R;
						}
					}
					dist = new int[n];
					
					while(m --> 0) {
						int v1 = Integer.parseInt(st.nextToken());
						int v2 = Integer.parseInt(st.nextToken());
						int w = Integer.parseInt(st.nextToken());
						
//						System.out.println(v1 +" " +v2 + " " + w);
						
						weight[v1][v2] = Math.min(weight[v1][v2], w);
						weight[v2][v1] = Math.min(weight[v2][v1], w);
					}
					
					for(int i = 0; i < n; i++) {
						weight[i][i] = 0;
					}
					
//					for(int i=0; i<n; i++) {
//						System.out.println(Arrays.toString(weight[i]));
//					}
					
					getDist(s);
					
//					System.out.println("dist: " + Arrays.toString(dist));
					
					
					
					
					break;
				case "200": // 상품 생성 
					int id = Integer.parseInt(st.nextToken());
					int revenue = Integer.parseInt(st.nextToken());
					int dest = Integer.parseInt(st.nextToken());
					
					travelInfo.put(id, new int[] {revenue, dest, 0});
					
//					if(dist[dest] == MAX_R) {
//						continue;
//					}
					
					int cost = revenue - dist[dest];
//					if(cost < 0) continue;
					
					travelList.add(new int[] {cost, id});
					
					break;
				case "300": // 취소 
					id = Integer.parseInt(st.nextToken());
					if(travelInfo.containsKey(id)) {
						travelInfo.get(id)[2] = 1; // deleted
					}
//					
//					int[] travel = new int[4];
//					for(int[] t : travelList) {
//						if(t[1] == id) {
//							travel = t;
//							break;
//						}
//					}
//					
//					travelList.remove(travel);
					
					break;
				case "400": // 판매 
//					for(int[] pp :travelList) { 
//						System.out.println("cost: " + pp[0] + " id: " + pp[1]+ " deleted: " + (travelInfo.get(travelList.peek()[1])[2] == 1));
//					}
					
					while(!travelList.isEmpty() && travelInfo.get(travelList.peek()[1])[2] == 1) {
						travelList.poll();
					}
					
					if(travelList.isEmpty()) {				
						System.out.println("-1");
						break;
					}
					
					
					ArrayList<int[]> tmpList = new ArrayList<>();
					int num = -1;
					while(!travelList.isEmpty()) {
						int[] prod = travelList.poll(); // cost, id
						cost = prod[0];
						id = prod[1];
//						System.out.println("id: " + id)						
						boolean deleted = (travelInfo.get(id)[2] == 1);
						if(deleted) continue;
						
						if(cost >= 0) {
							num = id;
							break;
						}else {
							tmpList.add(prod);
						}
						
					}
					
				
					for(int[] t : tmpList) {
						travelList.add(t);
					}
					
					System.out.println(num);
		
					break;
				case "500": // 출발지 변경
					s = Integer.parseInt(st.nextToken());
					
					getDist(s);

				
					// 기존 판매상품 관리 (cost 재계산)
//					
//					tmpList = new ArrayList<>();
					// 내부 요소 직접 변경하는 경우 우선순위 반영 안 됨 
	
				
					for(int[] t : travelList) {
						int[] travel = travelInfo.get(t[1]);
						t[0] = travel[0] - dist[travel[1]];
					}
					
//					while(!travelList.isEmpty()) {
////						int[] travel = travelList.poll();
//						id = travelList.poll()[1];
//						
//						boolean deleted = travelInfo.get(id)[2] == 1;
//						if(deleted) continue;
//						
//						int[] travel = travelInfo.get(id);
//						revenue = travel[0];
//						dest = travel[1];
//						cost = revenue - dist[dest];
//						
//						tmpList.add(new int[] {cost, id});
//					}
////					
////					travelList.clear();
////					travelList = new PriorityQueue<>((a, b) -> a[0] == b[0] ? a[1] - b[1] : b[0] - a[0]);
//					for(int[] t : tmpList) {
//						travelList.add(new int[] {t[0], t[1]});
//					}
					
//					System.out.println("우선순위 테스트 " + travelList.size());
//					while(!travelList.isEmpty()) {
//						int[] t = travelList.poll();
// 						System.out.println(t[0] + " " + t[1]);
//					}
//					System.out.println(travelList.size() + " " + travelList.peek()[1]);
//					list = newList;
					
					break;
			}
		}
	}
	
	static void getDist(int s) {
		// dist 계산하기
		for(int i = 0; i < n; i++) {
			dist[i] = MAX_R;
		}
		dist[s] = 0;
//		boolean[] visited = new boolean[n];
		
		// w, v
		PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]);
		pq.add(new int[] {0, s});
//		visited[s] = true;
		
		while(!pq.isEmpty()) {
//			for(int[] pp: pq) {
//				System.out.println(Arrays.toString(pp));
//			}

			
			int[] tmp = pq.poll();
			int w = tmp[0];
			int v = tmp[1];
			
			for(int i=0; i<n; i++) {
				if(weight[v][i] != MAX_R && dist[i] > w + weight[v][i]) {//&& !visited[i] 
					dist[i] = w + weight[v][i];
					pq.add(new int[] {dist[i], i});
//					visited[i] = true;
				}
			}
			
		}
		
//		System.out.println("s: " + s + " dist: " + Arrays.toString(dist));
	}
}

// 예외 케이스: 상품삭제(deleted:1)