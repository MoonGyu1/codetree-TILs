import java.io.*;
import java.util.*;

public class Main {
	static int L; // 격자 크기 
	static int N; // 기사 수 
	static int Q; // 왕의 명령 수 
	
	static int[][] map;
	static int[][] position; // 기사들의 위치
	
	static int[][] person; // 기사 i: [r, c, h, w, k, currentK]
	
	// 0, 1, 2, 3 : 상우하좌
	static int[] dx = new int[]{-1, 0, 1, 0};
	static int[] dy = new int[]{0, 1, 0, -1};
	
	public static void main(String[] args) throws Exception {
		// System.setIn(new FileInputStream("src/s202301_am_1/input3.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		L = Integer.parseInt(st.nextToken());
		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());
		
		map = new int[L+1][L+1];
		position = new int[L+1][L+1];
		
		person = new int[N+1][6];
		
		for(int i = 1; i <= L; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=1; j<=L; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		for(int i=1; i<= N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<5; j++) {
				person[i][j] = Integer.parseInt(st.nextToken());
			}
			person[i][5] = person[i][4]; // currentK
		}
		
//		for(int i=1; i<= L; i++) {
//			System.out.println(Arrays.toString(map[i]));
//		}
//		
//		for(int i=1; i<= N; i++) {
//			System.out.println(Arrays.toString(person[i]));
//		}
		
		while(Q-->0) {
		
			 
			st = new StringTokenizer(br.readLine());
			int pn = Integer.parseInt(st.nextToken());
			int pd = Integer.parseInt(st.nextToken());
			
//			 String[] s= {"위" , "오른", "아래", "왼"};
//			 System.out.println("Q" +Q+ ": "+ pn + "번 기사가 " + s[pd] + "쪽으로 한칸 ");
//			 if(Q==71) {
//				 System.out.println(person[1][0] + " " + person[1][1]);
//			 }
			
			if(person[pn][5] == 0) continue; // 기사가 탈락한 경우 
			
			// 자기자신 canMove 체크 
			 if(!canMove(pn, pd)) continue;
			
			// 주변 canMove 체크 
			 ArrayList<Integer> ps = getRelatedPn(pn, pd);
			 boolean canMove = true;
			
//			 System.out.println(ps);
			 for(Integer p : ps) {
				 if(!canMove(p, pd)) { // 움직일 수 없는 경우 
					 canMove = false;
					 break;
				 }
			 }
//			 System.out.println(canMove);
			 
			 if(!canMove) continue;
			
			// 이동
			 move(pn, pd, true);
			 for(Integer p : ps) {
				 move(p, pd, p==pn);
			 }
//			 
//			 for(int i=1; i<=N; i++) {
//				 System.out.println("p: " + i + " k: " + person[i][5]);
//
//			 }
			 
		}
		
		int totalD = 0; // 대미지의 합 
		for(int[] p : person) {
			if(p[5] > 0) { // 생존 
				totalD += p[4] - p[5];
			}
		}
		
		System.out.println(totalD);
	}
	
	// 기사가 이동할 때, 함께 이동해야하는 기사 번호 
	static ArrayList<Integer> getRelatedPn(int p, int d) {
		// position 맵 새로 만들기
		position = new int[L+1][L+1];
		for(int i = 1; i<=N; i++) {
			if(person[i][5] == 0) continue; // 탈락한 기사 
//			System.out.println("i: " +i);

			int r = person[i][0], c = person[i][1], h = person[i][2], w = person[i][3];
			for(int j = r; j <= r+h-1; j++) {
				for(int k = c; k<= c+w-1; k++) {
//					System.out.println(j + " " + k);
					position[j][k] = i;
				}
			}
		}
		
//		for(int i=1; i<= L; i++) {
//			System.out.println(Arrays.toString(position[i]));
//		}
		
		HashSet<Integer> set = new HashSet<>();
		
		// d에 따라 나눠서 판단
		int r = person[p][0], c = person[p][1], h = person[p][2], w = person[p][3];
		
		if(d == 0) { // 상 
			for(int i = r-1; i >= 1; i--) {
				boolean isWall = false;
				boolean isEmpty = true;
				ArrayList<Integer> tmpP = new ArrayList<>();
				
				for(int j = c; j <= c + w -1; j++) {
					if(!inRange(i, j) || map[i][j] == 2) {
						isWall = true;
					} else {
						if(position[i][j] != 0) tmpP.add(position[i][j]);
						if(position[i][j] != 0) isEmpty = false;
					}
				}
				
				if(isWall || isEmpty) break;
				
				for(Integer tp : tmpP) {
					set.add(tp);
				}
			}
		} else if(d == 1) { // 우
			for(int j = c+w; j <= L; j++) {
				boolean isWall = false;
				boolean isEmpty = true;
				ArrayList<Integer> tmpP = new ArrayList<>();
				
				for(int i = r; i <= r + h -1; i++) {
					if(!inRange(i, j) || map[i][j] == 2) {
						isWall = true;
					} else {
						if(position[i][j] != 0) tmpP.add(position[i][j]);
						if(position[i][j] != 0) isEmpty = false;
					}
				}
				
				if(isWall || isEmpty) break;
				
				for(Integer tp : tmpP) {
					set.add(tp);
				}
			}
		} else if(d == 2) { // 하 
			for(int i = r+h; i <= L; i++) {
				boolean isWall = false;
				boolean isEmpty = true;
				ArrayList<Integer> tmpP = new ArrayList<>();
				
				for(int j = c; j <= c + w -1; j++) {
					if(!inRange(i, j) || map[i][j] == 2) {
						isWall = true;
					} else {
						if(position[i][j] != 0) tmpP.add(position[i][j]);
						if(position[i][j] != 0) isEmpty = false;
					}
				}
				
				if(isWall || isEmpty) break;
				
				for(Integer tp : tmpP) {
					set.add(tp);
				}
			}
		} else if(d == 3) { // 좌
			for(int j = c-1; j >= 1; j--) {
				boolean isWall = false;
				boolean isEmpty = true;
				ArrayList<Integer> tmpP = new ArrayList<>();
				
				for(int i = r; i <= r + h -1; i++) {
					if(!inRange(i, j) || map[i][j] == 2) {
						isWall = true;
					} else {
						if(position[i][j] != 0) tmpP.add(position[i][j]);
						if(position[i][j] != 0) isEmpty = false;
					}
				}
				
				if(isWall || isEmpty) break;
				
				for(Integer tp : tmpP) {
					set.add(tp);
				}
			}
		}
		
		ArrayList<Integer> ps = new ArrayList<>(set);
		
		return ps;
	}
	
	static boolean canMove(int p, int d) {
		// d 방향으로 1만큼 이동
		int r = person[p][0], c = person[p][1];
		int h = person[p][2], w = person[p][3];
		
		int next_r = r + dx[d], next_c = c + dy[d];
		 
		for(int i = next_r; i <= next_r + h-1; i++) {	
			for(int j = next_c; j <= next_c + w-1; j++) {
				if(!inRange(i, j) || map[i][j] == 2) return false;
			}
		}
			
		return true;
	}
	
	static void move(int p, int d, boolean c) {
		// 기사 p를 d로 이동
		
		// r, c 업데이트
		int newR = person[p][0] + dx[d], newC = person[p][1] + dy[d];
		person[p][0] = newR; // r
		person[p][1] = newC; // c
		
		if(c) return; // 명령 받은 기사는 대미지 업데이트 X
		
		// 맵 기준으로 구간 내에 함정 있으면 currentD 업데이트
		int h = person[p][2], w = person[p][3];
		for(int i = newR; i <= newR + h-1; i++) {
			for(int j = newC; j <= newC + w-1; j++) {
				if(map[i][j] == 1) {
					person[p][5]--;
				}
			}
		}
		
		// p[5] < 0이면 0
		if(person[p][5] < 0) person[p][5] = 0;
	}
	
	static boolean inRange(int x, int y) {
		return 1 <= x && x <= L && 1 <= y && y <= L; 
	}

}

//1 2
//2 1
//3 3