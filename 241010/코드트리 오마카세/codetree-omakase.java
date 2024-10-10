import java.util.*;
import java.io.*;

public class Main {
	static HashMap<String, int[]> person = new HashMap<>(); // name: [idx, cnt]
	static HashMap<String, ArrayList<Integer>> sushi = new HashMap<>(); // name: [idxs]
	static int L; // 의자 
	static int Q; // 명령
	static int pCnt = 0;
	static int sCnt = 0;
	
	public static void main(String[] args) throws Exception {
		// System.setIn(new FileInputStream("src/s202302_pm_2/input3.txt"));

		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		L = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.
				nextToken());
		
		int beforeT = 0;
		
		for(int q = 0; q < Q; q++) {		
			st = new StringTokenizer(br.readLine());
			String c = st.nextToken();
			
			Integer t = Integer.parseInt(st.nextToken());
			
//			System.out.println("T: " + t);
//			System.out.println("before: ");
//			System.out.println("<<sushi>>: ");
//			for(Map.Entry<String, ArrayList<Integer>> entry : sushi.entrySet()) {
//				System.out.println(entry.getKey() + ": " + entry.getValue());
//			}
//			
//			System.out.println("<<person>>: ");
//			for(Map.Entry<String, int[]> entry : person.entrySet()) {
//				System.out.println(entry.getKey() + ": " + Arrays.toString(entry.getValue()));
//			}
			
			if(q == 0) {
				beforeT = t;
			}
			
			int overtime = t - beforeT;
			
			// sushi인덱스 변경(회전)
			eatAndRotate(overtime);
			
			switch(c) {
				case "100": // 초밥 생성
					Integer x = Integer.parseInt(st.nextToken());
					String name = st.nextToken();
					
					if(sushi.containsKey(name)) {
						sushi.get(name).add(x);
					} else {
						sushi.put(name, new ArrayList<>());
						sushi.get(name).add(x);
					}
					
					sCnt++;
					
					eat();
					
					break;
				case "200": // 사람 착석
					x = Integer.parseInt(st.nextToken());
					name = st.nextToken();
					Integer n = Integer.parseInt(st.nextToken());
					
					person.put(name, new int[]{x, n});
					
					pCnt++;
					
					eat();
					
					break;
				case "300": // 사진 촬영
					eat();
					
					System.out.println(pCnt + " " + sCnt);
					
					break;
			}
			
			
			beforeT = t;
			
//			System.out.println("after");
//			System.out.println("<<sushi>>: ");
//			for(Map.Entry<String, ArrayList<Integer>> entry : sushi.entrySet()) {
//				System.out.println(entry.getKey() + ": " + entry.getValue());
//			}
//			
//			System.out.println("<<person>>: ");
//			for(Map.Entry<String, int[]> entry : person.entrySet()) {
//				System.out.println(entry.getKey() + ": " + Arrays.toString(entry.getValue()));
//			}
		}
		
	}
	
	// 모든 초밥 회전 
	static void eatAndRotate(int overtime) {
//		System.out.println("overtime: " + overtime);
		
		for(Map.Entry<String, ArrayList<Integer>> entry : sushi.entrySet()) {
			String name = entry.getKey();
			boolean p = person.containsKey(name);
			
			int pIdx = p ? person.get(name)[0] : -1;
			
			ArrayList<Integer> idxes = entry.getValue();
			ArrayList<Integer> newIdxes = new ArrayList<Integer>();
			
			for(Integer idx : idxes) {
				int newIdx = (idx + overtime) % L;
				
				if(p && ((idx + overtime) / L > 1 || (idx + overtime >= L && idx > pIdx || pIdx <= newIdx) || (idx + overtime < L && idx < pIdx && pIdx <= newIdx))) {
					// 먹음 
					person.get(name)[1]--; // cnt--;
					sCnt--;
					
					if(person.get(name)[1] == 0) {
						pCnt--;
						person.remove(name);
					}
					
					continue;
				}
				
				newIdxes.add(newIdx);
			}
			
			sushi.put(entry.getKey(), newIdxes);
		}
	}
	
	// 앞자리에 있는 초밥 먹기 
	static void eat() {
		ArrayList<String> deleted = new ArrayList<>();
		for(Map.Entry<String, int[]> entry : person.entrySet()) {
			String name = entry.getKey();
			int[] tmp = entry.getValue();
	
			int idx = tmp[0];
			int cnt = tmp[1];
			
			if(!sushi.containsKey(name)) continue;
			
			ArrayList<Integer> idxes = sushi.get(name);
			while(true){
				int i = idxes.indexOf(idx);
				if(i == -1) break;
				
				idxes.remove(i);
				cnt--;
				sCnt--;
			}
		
//			System.out.println(name + " tmp[1]: " + tmp[1]);
			if(cnt == 0) {
//				System.out.println(name + "ate");
//				person.remove(name); //엔트리 순회하면서 삭제 불가
				deleted.add(name);
				pCnt--;
				
				sushi.remove(name);
			} else {
				person.get(name)[1] = cnt;
			}
		}
		
		for(String name : deleted) {
			person.remove(name);
		}
	}

}

//5 10  (L, Q)

//Q

//100 1 1 sam   생성, 초, x, 이름  
//100 2 2 teddy  
//100 3 2 june
//300 6           초, 사진 
//200 7 4 june 2   초, x, 이름, n 
//200 8 3 teddy 1
//300 9
//200 10 3 sam 1
//100 11 4 june
//300 13