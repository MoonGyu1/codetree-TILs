import java.io.*;
import java.util.*;

public class Main {
	static int INF = Integer.MAX_VALUE;
	
	static int[] dx = {0, 0, 1, -1};
	static int[] dy = {1, -1, 0, 0};
	
	static class Point extends Object {
		int t, x, y;
		
		Point(int t, int x, int y) {
			this.t = t;
			this.x = x;
			this.y = y;
		}
		
		
		public boolean equals(Point p) {
			return this.t == p.t && this.x == p.x && this.y == p.y;
		}
	}
	
	static boolean inRange(int N, int x, int y) {
		return 0 <= x && x < N && 0 <= y && y < N;
	}
	
	static Point getNextPoint(int t, int x, int y, int d, int M, Point UL, Point UR, Point DL, Point DR) {
//		경계선 처리
//		t=4 (윗면에서)
//		x=0 && 위로d=3 -> t=3, r=0, c=c
//		x=M-1 && 아래로d=2 -> t=2, r=0, c=c
//		y=0 && 왼쪽으로d=1 -> t=1, r=0, c=r
//		y=M-1 && 오른쪽으로d=0 -> t=0, r=0, c=M-r-1
//
//		t=2 (남쪽면에서)
//		위로d=3 -> t=4, r=M-1, c=c
//		아래로d=2 -> t=5, r=dl.x+1, c=dl.c + c
//		왼쪽으로d=1 -> t=1, r=r, c=M-1
//		오른쪽으로d=0 -> t=0, r=r, c=0
//
//		t=0 (동쪽면에서)
//		위로d=3 -> t=4, r=M-c-1, c=M-1
//		아래로d=2 -> t=5, r=dr.x - c, c= dr.c + 1
//		왼쪽으로d=1 -> t=2, r=r, c=M-1
//		오른쪽으로d=0 -> t=3, r=r, c=0
		if(t == 4) {
			if(x == 0 && d == 3) {
				return new Point(3, 0, M-y-1);
			}
			if(x == M-1 && d == 2) {
				return new Point(2, 0, y);
			}
			if(y == 0 && d == 1) {
				return new Point(1, 0, x);
			}
			if(y == M-1 && d == 0) {
				return new Point(0, 0, M-x-1);
			}
		}
		
		if(t == 2) {
			if(x == 0 && d == 3) {
				return new Point(4, M-1, y);
			}
			if(x == M-1 && d == 2) {
				return new Point(5, DL.x+1, DL.y+y);
			}
			if(y == 0 && d == 1) {
				return new Point(1, x, M-1);
			}
			if(y == M-1 && d == 0) {
				return new Point(0, x, 0);
			}
		}
		
//		t=0 (동쪽면에서)
//				위로d=3 -> t=4, r=M-c-1, c=M-1
//				아래로d=2 -> t=5, r=dr.x - c, c= dr.c + 1
//				왼쪽으로d=1 -> t=2, r=r, c=M-1
//				오른쪽으로d=0 -> t=3, r=r, c=0
		if(t == 0) {
			if(x == 0 && d == 3) {
				return new Point(4, M-y-1, M-1);
			}
			if(x == M-1 && d == 2) {
				return new Point(5, DR.x - y, DR.y + 1);
			}
			if(y == 0 && d == 1) {
				return new Point(2, x, M-1);
			}
			if(y == M-1 && d == 0) {
				return new Point(3, x, 0);
			}
		}
		
		if(t == 1) {
			if(x == 0 && d == 3) {
				return new Point(4, y, 0);
			}
			if(x == M-1 && d == 2) {
				return new Point(5, UL.x+y, UL.y-1);
			}
			if(y == 0 && d == 1) {
				return new Point(3, x, M-1);
			}
			if(y == M-1 && d == 0) {
				return new Point(2, x, 0);
			}
		}
		
		if(t == 3) {
			if(x == 0 && d == 3) {
				return new Point(4, 0, M-y-1);
			}
			if(x == M-1 && d == 2) {
				return new Point(5, UL.x-1, UL.y+M-y-1);
			}
			if(y == 0 && d == 1) {
				return new Point(0, x, M-1);
			}
			if(y == M-1 && d == 0) {
				return new Point(1, x, 0);
			}
		}
		
//
//		t=1 (서쪽면에서)
//		위로d=3 -> t=4, r=c, c=0
//		아래로d=2 -> t=5, r=ul.x + c, c= ul.c - 1
//		왼쪽으로d=1 -> t=3, r=r, c=M-1
//		오른쪽으로d=0 -> t=2, r=r, c=0
//
//		t=3 (북쪽면에서)
//		위로d=3 -> t=4, r=0, c=M-c-1
//		아래로d=2 -> t=5, r=ul.x - 1, c= ul.c + M-c-1
//		왼쪽으로d=1 -> t=0, r=r, c=M-1
//		오른쪽으로d=0 -> t=1, r=r, c=0
		
		return new Point(t, x+dx[d], y + dy[d]);
	}
	
	public static void main(String[] args) throws Exception {
		// System.setIn(new FileInputStream("src/삼성_2024_하_오전_1/input1.txt"));
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		// 1.
		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		int F = Integer.parseInt(st.nextToken());
		
		Point UL = new Point(-1, -1, -1);
		Point UR = new Point(-1, -1, -1);
		Point DL = new Point(-1, -1, -1);
		Point DR = new Point(-1, -1, -1);
		Point S = new Point(-1, -1, -1);
		Point E = new Point(-1, -1, -1);
		
		// 2
		int[][] map = new int[N][N];
		for(int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j = 0; j < N; j++) {
				map[i][j]= Integer.parseInt(st.nextToken());
				
				if(map[i][j] == 3 && UL.x == -1) {
					UL = new Point(4, i, j);
					UR = new Point(4, i, j + M - 1);
					DL = new Point(4, i + M - 1, j);
					DR = new Point(4, i + M - 1, j + M - 1);
				} else if(map[i][j] == 4) {
					E = new Point(5, i, j);
				}
			}
		}
		
		// 3
		int[][][] walls = new int[5][M][M];
		for(int k = 0; k < 5; k++) {
			for(int i = 0; i < M; i++) {
				st = new StringTokenizer(br.readLine());
				for(int j = 0; j < M; j++) {
					walls[k][i][j] = Integer.parseInt(st.nextToken());
					
					if(walls[k][i][j] == 2) {
						S = new Point(k, i, j);
					}
				}
			}
		}
		
		int[][] spreads = new int[F][4];
		for(int i = 0; i < F; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j = 0; j < 4; j++) {
				spreads[i][j] = Integer.parseInt(st.nextToken());
			}
			map[spreads[i][0]][spreads[i][1]] = 1;
		}
		
//		System.out.println(Arrays.deepToString(map));
//		System.out.println(Arrays.deepToString(walls));
//		System.out.println(Arrays.deepToString(spreads));
//		System.out.println(UL.x + " "  + UL.y);
//		System.out.println(UR.x + " "  + UR.y);
//		System.out.println(DL.x + " "  + DL.y);
//		System.out.println(DR.x + " "  + DR.y);
//		
//		System.out.println(S.t + " " + S.x + " "  + E.y);
//		System.out.println(E.t + " " + E.x + " "  + E.y);
		
		// 5
		class Node {
			Point p;
			String route;
			Node(Point p, String route) {
				this.p = p;
				this.route = route;
			}
		}
		Set<Integer> s = new HashSet<>();
//		Queue<Point> q = new ArrayDeque<>();
		Queue<Node> q = new ArrayDeque<>();
		
		int[][][] dist = new int[6][N][N];
		for(int t = 0; t < 6; t++) {
			for(int i = 0; i < N; i++) {
				for(int j = 0; j < N; j++) {
					dist[t][i][j] = INF;
				}
			}
		}
		
		
//		q.add(S);	
		q.add(new Node(S, ", (" + S.t + "," + S.x + "," + S.y + ")"));
		dist[S.t][S.x][S.y] = 0;
		
		// 6
		while(!q.isEmpty()) {
			// 1)
//			Point p = q.poll();
			Node n = q.poll();
			Point p = n.p;
			
			int t = p.t, x = p.x, y = p.y;
			int dt = dist[t][x][y];
			
//			System.out.println("polled: " + t + " " + x + " "  + y);
			
			// 2)
			if(p.equals(E)) {
//				System.out.println(n.route);
				System.out.println(dt);
				return;
			}
			
			// 3)
			if(!s.contains(dt)) {
				s.add(dt);
				
				for(int i = 0; i < F; i++) {
					int T = dt + 1;
					int r = spreads[i][0], c = spreads[i][1], d = spreads[i][2], v = spreads[i][3]; 
					
					if(T % v == 0) {
						int nr = r + dx[d], nc = c + dy[d];
						
						if(inRange(N, nr, nc) && map[nr][nc] != 1 && map[nr][nc] != 4 && map[nr][nc] != 3) {
							map[nr][nc] = 1;
							spreads[i][0] = nr; spreads[i][1] = nc;
						}
					}
				}
			}
			
			// 4)
//			System.out.println("current: " + t + " " + x + " "  + y);
			for(int d = 0; d < 4; d++) {
//				System.out.println("d: " + (d == 0 ? "동" : d == 1 ? "서" : d == 2 ? "남" :  "북"));
				Point np = getNextPoint(t, x, y, d, M, UL, UR, DL, DR);
				int nt = np.t, nx = np.x, ny = np.y;
//				System.out.println(nt + " " + nx + " "  + ny);
//				System.out.println(map[nx][ny]);
//				if(nt != 5) System.out.println(walls[nt][nx][ny]);
//				System.out.println(dist[nt][nx][ny]);
//				System.out.println(inRange(N, nx, ny) && !(nt == 5 && (map[nx][ny] == 1 || map[nx][ny] == 3) && !(nt != 5 && walls[nt][nx][ny] == 1)) && dist[nt][nx][ny] == INF);
				
				if(!inRange(N, nx, ny)) continue;
				if(nt == 5 && (map[nx][ny] == 1 || map[nx][ny] == 3)) continue;
//				System.out.println("pass?");
				if(nt != 5 && walls[nt][nx][ny] == 1) continue;
//				if(nt != 5) System.out.println(walls[nt][nx][ny] );
//				System.out.println(dist[nt][nx][ny]);
				if(dist[nt][nx][ny] != INF) continue;
				
				
//				System.out.println("not pass");
				
//				q.add(new Point(nt, nx, ny));
				q.add(new Node(new Point(nt, nx, ny), n.route + ", (" + nt + "," + nx + "," + ny + ")"));
				dist[nt][nx][ny] = dt + 1;
			}
		}
		
//		System.out.println(Arrays.deepToString(dist));

//		System.out.println(Arrays.deepToString(dist[3]));
		System.out.println(-1);
	}
}
