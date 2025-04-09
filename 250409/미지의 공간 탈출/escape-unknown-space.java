import java.util.*;

/**
 * 그래프 버전
 */
// 시간복잡도:
public class Main {
	
	static final int INF = Integer.MAX_VALUE;

	static final int[] dx = {0, 1, 0, -1};
	static final int[] dy = {1, 0, -1, 0};

	// 이상현상 클래스
	static class TimeEvent {
		int x, y, d, v;
		boolean alive = true;

		TimeEvent(int x, int y, int d, int v) {
			this.x = x;
			this.y = y;
			this.d = d;
			this.v = v;
		}
	}

	// 그래프 생성 - 범위 내 모든 칸 연결 (장애물 처리 X)
	static int[][] buildGraph(int N, int M, int F, int[][] space, int[][][] walls, TimeEvent[] events, int[][] spaceIds, int[][][] wallIds) {
		int totalNode = N * N + M * M * 4;
		int[][] graph = new int[totalNode][4]; // 동남서북
		for(int i = 0; i < totalNode; i++) {
			Arrays.fill(graph[i], -1);
		}

		// 미지의 공간 간선 연결
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(space[i][j] == 3) continue;

				int id = spaceIds[i][j];

				for(int d = 0; d < 4; d++) {
					int nx = i + dx[d], ny = j + dy[d];

					if(nx < 0|| ny < 0 || nx >= N || ny >= N) continue;
					if(space[nx][ny] == 3) continue;

					graph[id][d] = spaceIds[nx][ny];
				}
			}
		}

		// 시간의 벽 간선 연결
		for(int s = 0; s < 5; s++) {
			for(int i = 0; i < M; i++) {
				for(int j = 0; j < M; j++) {
					int id = wallIds[s][i][j];

					for(int d = 0; d < 4; d++) {
						int nx = i + dx[d], ny = j + dy[d];

						// if(id == 97 && d == 0) {
						// 	System.out.println(nx + " " + ny + " " + space[nx][ny]);
						// }
						if (nx < 0 || ny < 0 || nx >= M || ny >= M) continue;

						graph[id][d] = wallIds[s][nx][ny];
					}
				}
			}
		}

		// 시간의 벽 우측 간선 연결
		for(int s = 0; s < 4; s++) {
			for(int i = 0; i < M; i++) {
				int lid = wallIds[s][i][M-1];
				int rid = wallIds[(s+3)%4][i][0];
				
				graph[lid][0] = rid;
				graph[rid][2] = lid;
			}
		}

		// 시간의 벽 좌측 간선 연결
		for(int s = 0; s < 4; s++) {
			for(int i = 0; i < M; i++) {
				int rid = wallIds[s][i][0];
				int lid = wallIds[(s+1)%4][i][M-1];

				graph[rid][2] = lid;
				graph[lid][0] = rid;
			}
		}

		// 시간의 벽 위 <-> 아래 간선 연결
		// 동
		for(int j = 0; j < M; j++) {
			int did = wallIds[0][0][j];
			int uid = wallIds[4][M-j-1][M-1];
			
			graph[did][3] = uid;
			graph[uid][0] = did;
		}

		// 남
		for(int j = 0; j < M; j++) {
			int did = wallIds[1][0][j];
			int uid = wallIds[4][M-1][j];

			// if(uid == 97) {
			// 	System.out.println(j + " " + did + " " + walls[1][0][j] + " " + walls[4][M-1][j]);
			// 	System.out.println(Arrays.deepToString(walls[1]));
			// }

			graph[did][3] = uid;
			graph[uid][1] = did;
		}

		// 서
		for(int j = 0; j < M; j++) {
			int did = wallIds[2][0][j];
			int uid = wallIds[4][j][0];

			graph[did][3] = uid;
			graph[uid][2] = did;
		}

		// 북
		for(int j = 0; j < M; j++) {
			int did = wallIds[3][0][j];
			int uid = wallIds[4][0][M-j-1];

			graph[did][3] = uid;
			graph[uid][3] = did;
		}

		// 미지의 공간 <-> 시간의 벽 간선 연결
		int sx = -1, sy = -1;
		
		outer:
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(space[i][j] == 3) {
					sx = i; sy = j;
					break outer;
				}
			}
		}
		
		// 동
		for(int j = 0; j < M; j++) {
			int wid = wallIds[0][M-1][j];
			int sid = spaceIds[sx+M-j-1][sy+M];

			graph[wid][1] = sid;
			graph[sid][2] = wid;
		}

		// 남
		for(int j = 0; j < M; j++) {
			int wid = wallIds[1][M-1][j];
			int sid = spaceIds[sx+M][sy+j]; // y로 해서 틀림

			graph[wid][1] = sid;
			graph[sid][3] = wid;
		}

		// 서
		for(int j = 0; j < M; j++) {
			int wid = wallIds[2][M-1][j];
			int sid = spaceIds[sx+j][sy-1];

			graph[wid][1] = sid;
			graph[sid][0] = wid;
		}

		// 북
		for(int j = 0; j < M; j++) {
			int wid = wallIds[3][M-1][j];
			int sid = spaceIds[sx-1][sy+M-j-1];

			graph[wid][1] = sid;
			graph[sid][1] = wid;
		}
		
		return graph;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		// 미지의 공간의 한 변의 길이, 시간의 벽 한 변의 길이, 시간 이상 현상의 개수 입력받기
		int N = sc.nextInt();
		int M = sc.nextInt();
		int F = sc.nextInt();

		// 평면도 입력
		int[][] space = new int[N][N];
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				space[i][j] = sc.nextInt();
			}
		}

		// 단면도 입력
		int[][][] walls = new int[5][M][M]; // 동남서북위

		// 입력 순서: 동서남북위
		for(int s = 0; s < 5; s++) {
			for(int i = 0; i < M; i++) {
				for(int j = 0; j < M; j++) {
					walls[s == 1 ? 2 : s == 2 ? 1 : s][i][j] = sc.nextInt();
				}
			}
		}

		// 이상 현상 입력
		TimeEvent[] events = new TimeEvent[F];
		for(int i = 0; i < F; i++) {
			int x = sc.nextInt(), y = sc.nextInt();
			int d = sc.nextInt();
			d = d == 1 ? 2 : d == 2 ? 1 : d;
			int v = sc.nextInt();

			events[i] = new TimeEvent(x, y, d, v);
			// space[x][y] = 1; // 장애물 처리 // 
		}

		// 각 셀의 번호 생성
		int n = 0;

		int[][] spaceIds = new int[N][N];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(space[i][j] == 3) continue;
				spaceIds[i][j] = n;
				n++;
			}
		}

		int[][][] wallIds = new int[5][M][M];
		for(int s = 0; s < 5; s++) {
			for(int i = 0; i < M; i++) {
				for(int j = 0; j < M; j++) {
					wallIds[s][i][j] = n;
					n++;
				}
			}
		}

		// 그래프 생성
		int[][] graph = buildGraph(N, M, F, space, walls, events, spaceIds, wallIds);

		// System.out.println(Arrays.toString(graph[93]));

		// 다익스트라
		class Node {
			int id;
			String r;
			Node(int id, String r){
				this.id = id;
				this.r = r;
			}
		}
		// Queue<Integer> q = new ArrayDeque<>();
		Queue<Node> q = new ArrayDeque<>();
		
		int totalNode = N * N + M * M * 4;
		int[] dist = new int[totalNode];
		Arrays.fill(dist, -1); // 장애물,시간확산은 INF, 미방문은 -1, 그 외는 방문
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(space[i][j] == 1) {
					dist[spaceIds[i][j]] = INF;
				}
			}
		}

		for(int s = 0; s < 5; s++) {
			for(int i = 0; i < M; i++) {
				for(int j = 0; j < M; j++) {
					if(walls[s][i][j] == 1) { // 주의: wallIds와 헷갈려서 틀림
						dist[wallIds[s][i][j]] = INF;
					}
				}
			}
		}

		for(int i = 0; i < F; i++) {
			TimeEvent e = events[i];
			dist[spaceIds[e.x][e.y]] = INF; // 주의: space가 아니라 dist를 INF 처리 해야함
		}

		// System.out.println("dist: " + " " + dist[57]);
		// System.out.println(walls[0][0][2]);

		// 시작점, 도착점 탐색
		int sid = -1, eid = -1;

		outer1:
		for(int i = 0; i < M; i++) {
			for(int j = 0; j < M; j++) {
				if(walls[4][i][j] == 2) {
					sid = wallIds[4][i][j];
					break outer1;
				}
			}
		}

		outer2:
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(space[i][j] == 4) {
					eid = spaceIds[i][j];
					break outer2;
				}
			}
		}
		
		// q.add(sid);
		q.add(new Node(sid, sid + ", "));
		dist[sid] = 0;
		
		// System.out.println(Arrays.toString(graph[97]));
		for(int turn = 1; ; turn++) {
			// 1) 이상현상 업데이트
			for(int i = 0; i < F; i++) {
				TimeEvent e = events[i];
				
				if(turn % e.v != 0) continue;
				if(!e.alive) continue;
				
				// 주의 %아니라 /연산
				int nx = e.x + dx[e.d] * (turn / e.v);
				int ny = e.y + dy[e.d] * (turn / e.v);

				// if(turn == 14) {
				// 	System.out.println("spread: " + nx + " " + ny);
				// }
				
				if(nx < 0 || ny < 0 || nx >= N || ny >= N) {
					e.alive = false;
					continue;
				}

				if(space[nx][ny] != 0) {
					e.alive = false;
					continue;
				}
				
				// if(turn == 14) {
				// 	System.out.println("spread: " + nx + " " + ny);
				// }
				// space[nx][ny] = 1;
				dist[spaceIds[nx][ny]] = INF;
			}

			// 2) 도달 가능 셀 탐색
			// ArrayList<Integer> next = new ArrayList<>();
			ArrayList<Node> next = new ArrayList<>();
			String route;


			// if(turn == 13) System.out.println(dist[6]);
			// if(turn == 14) System.out.println(dist[6]);

			while(!q.isEmpty()) {
				// int id = q.poll();
				Node nd = q.poll();
				route = nd.r;
				// if(nd.id==eid) System.out.println(route);
				int id = nd.id;
				// if(id == 29) {
				// 	System.out.println(Arrays.toString(graph[29]));
				// 	// System.out.println(Arrays.toString(graph[29]));
				// }
				// if(id == 14) {
				// 	System.out.println(dist[6]);
				// }
				// if(id == eid) {
				// 	System.out.println(nd.r);
				// }
				
				for(int d = 0; d < 4; d++) {
					int nid = graph[id][d];
					
					if(nid == -1) continue;
					if(dist[nid] == INF) continue;
					if(dist[nid] != -1) continue;
					
					// next.add(nid);
					next.add(new Node(nid, nd.r + " " + nid + ", "));
					dist[nid] = turn;
				}
			}
			
			if(next.isEmpty()) break;
			// if(dist[eid] != -1) break;
			
			// for(int id : next) {
			// 	q.add(id);
			// }
			for(Node id : next) {
				q.add(id);
			}
		}

		// 정답 출력
		System.out.println(dist[eid]);
		// System.out.println(Arrays.toString(dist));
	}
}
