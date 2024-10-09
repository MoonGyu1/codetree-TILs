import java.io.*;
import java.util.*;

public class Main {
	static int N; // 격자 크기
	static int M; // 턴
	static int P; // 산타 수 (번호: 1~p)
	static int C; // 루돌프 힘
	static int D; // 산타 힘
	static int[][] map; // 격자
	static int[][] santa; // id: {out, dizzy, score, x,y}
	static int rr; // 루돌프
	static int rc; // 루돌프 컬
	static int turn; // 현재

	// route: 0~7
	static int[] dx = new int[] { -1, 0, 1, 0, -1, 1, 1, -1 }; // 상우하좌 우상 우하 좌하 좌상
	static int[] dy = new int[] { 0, 1, 0, -1, 1, 1, -1, -1 };

	static int live; // 남아있는 산타

	public static void main(String[] args) throws Exception {
		// System.setIn(new FileInputStream("src/s202302_pm1/input2.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		// n~d입력 받기
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		P = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		D = Integer.parseInt(st.nextToken());

		// map을 -1로 초기화
		map = new int[N + 1][N + 1];
		for (int i = 1; i <= N; i++) {
			for (int j = 0; j <= N; j++) {
				map[i][j] = -1;
			}
		}

		santa = new int[P + 1][5];

		// int rr, rc = 루돌프 초기 위치 맵 & 배열 에 저장 (루돌프:0번)
		st = new StringTokenizer(br.readLine());
		rr = Integer.parseInt(st.nextToken());
		rc = Integer.parseInt(st.nextToken());
		map[rr][rc] = 0;

		// for(p)
		// santa[i] 초기화,xy
		for (int i = 0; i < P; i++) {
			st = new StringTokenizer(br.readLine());
			int pn = Integer.parseInt(st.nextToken());
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());

			map[r][c] = pn; // 산타위치 저장
			santa[pn] = new int[] { 0, 0, 0, r, c };
		}

		live = P;

////		System.out.println("before: "+ turn);
//		for(int i=1; i<=N; i++) {
//			System.out.println(Arrays.toString(map[i]));
//		}
		
		for (int k = 1; k <= M; k++) {
			turn = k;
			if (live == 0)
				break;

			// 1. 루돌프 이동
			int[] sPos = getNearestSantaPos();
//			System.out.println(sPos[0] + " " +sPos[1]);
			moveR(sPos[0], sPos[1]);
//			if(turn == 6) {
////				System.out.println("r move");
//				for(int i=1; i<=N; i++) {
//					System.out.println(Arrays.toString(map[i]));
//				}
//			}

			// 2. 산타 이동
			for (int i = 1; i <= P; i++) {
				moveS(i);
			}
//	
//		// 3. 턴 종료 후 점수 부여
			for (int i = 1; i <= P; i++) {
				if (santa[i][0] == 0) {
					santa[i][2] += 1;
				}
			}
			

//			System.out.println("turn: "+ turn);
//			for(int i=1; i<=N; i++) {
//				System.out.println(Arrays.toString(map[i]));
//			}
//			
//			System.out.println("score: ");
//			for(int i=1; i<=P; i++) {
//				System.out.printf("%d ", santa[i][2]);
//			}
//			System.out.println();
		}

		// 답안 출
		System.out.print(santa[1][2]);
		for(int i=2; i<=P; i++) {
			System.out.printf(" %d", santa[i][2]);
		}
	}

	// 가장 가까운 산타 위치 좌표 반환
	static int[] getNearestSantaPos() {
		int x = 0;
		int y = 0;
		int minDist = Integer.MAX_VALUE;

		for (int i = 1; i <= N; i++) {
			for (int j = 1; j <= N; j++) {
				int santaId = map[i][j];
				
				if (santaId == -1 || santaId == 0)
					continue;
				if (santa[santaId][0] == 1)
					continue; // 탈락

				int currentDist = getDistance(rr, rc, i, j);
//				System.out.println(currentDist + " " + minDist + " " + i + " " + j);
				if (currentDist < minDist) {
					x = i;
					y = j;
					minDist = currentDist;
				} else if (currentDist == minDist && i > x) {
					x = i;
					y = j;
					minDist = currentDist;
				} else if (currentDist == minDist && i == x && j > y) {
					x = i;
					y = j;
					minDist = currentDist;
				}
			}
		}

//		System.out.println("turn: " + turn + " x: " + x + " y: " + y);
		return new int[] { x, y };
	}

	static int getDistance(int x1, int y1, int x2, int y2) {
		return (int) (Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	static void moveR(int santaX, int santaY) {
		int minDist = Integer.MAX_VALUE;
		int x = 0;
		int y = 0;
		int route = -1;

		for (int i = 0; i < 8; i++) {
			if (inRange(rr + dx[i], rc + dy[i]) && getDistance(rr + dx[i], rc + dy[i], santaX, santaY) < minDist) {
				minDist = getDistance(rr + dx[i], rc + dy[i], santaX, santaY);
				x = rr + dx[i];
				y = rc + dy[i];
				route = i;
			}
		}

		// 돌진
		int santaId = map[x][y];
		map[rr][rc] = -1;
		rr = x;
		rc = y;
		map[x][y] = 0;
		if (santaId != -1) { // 충돌
			santa[santaId][2] += C; // 스코어 획득

			rollout(santaId, x, y, x + C * dx[route], y + C * dy[route], route);
			santa[santaId][1] = turn + 1;// turn + 1까지 기절
		}
	}

	static void moveS(int santaId) {
		int[] s = santa[santaId];
		
//		if(turn == 3) {
//			System.out.println(santaId+" " + Arrays.toString(s));
//			};
			
		if (s[1] >= turn)//주의  
			return; // 기절
		if (s[0] == 1)
			return; // 탈락

		int bx = s[3], by = s[4]; // 산타 원래 위치 
		int x = s[3];
		int y = s[4];

		int currentDist = getDistance(x, y, rr, rc);

		int minDist = currentDist;
		int route = -1;

		int nextX = x;
		int nextY = y;

		for (int i = 0; i < 4; i++) {
			if (inRange(x + dx[i], y + dy[i]) && getDistance(x + dx[i], y + dy[i], rr, rc) < minDist
					&& map[x + dx[i]][y + dy[i]] <= 0) { // 주의, 루돌프여도 이동 가능 
				minDist = getDistance(x + dx[i], y + dy[i], rr, rc);
				nextX = x + dx[i];
				nextY = y + dy[i];
				route = i;
			}
		}

		// // 돌진

		map[bx][by] = -1;

		s[3] = nextX;
		s[4] = nextY;

		int id = map[nextX][nextY];

		if (id == 0) {// 사슴과 충돌
//			System.out.println("collision " + santaId);
			santa[santaId][2] += D; // 스코어 획득

			route = (route + 2) % 4; // 반대 방향
			rollout(santaId, bx, by, nextX + D * dx[route], nextY + D * dy[route], route);
			santa[santaId][1] = turn + 1;// turn + 1까지 기절
		} else {
			map[nextX][nextY] = santaId; // 주의: 루돌프면업데이트 X
		}
		

	}

	static void rollout(int santaId, int bx, int by, int nx, int ny, int route) { // x,y는 착지할 위치
		if (!inRange(nx, ny)) {
			santa[santaId][0] = 1; // 탈
			live--;
			return;
		}

		int nextSantaId = map[nx][ny];
		
		map[nx][ny] = santaId;
		santa[santaId][3] = nx;
		santa[santaId][4] = ny;

		if (nextSantaId != -1) { // 빈칸이 아니었으면
			rollout(nextSantaId, 1,1, nx + dx[route], ny + dy[route], route);
		}
	}

	static boolean inRange(int x, int y) {
		return 1 <= x && x <= N && 1 <= y && y <= N;
	}
}

// 5 7(턴) 4 2(루돌프 힘) 2(산타 힘)
// 3 2
// 1 1 3
// 2 3 5
// 3 5 1
// 4 4 4

// 0 0 2 0