import java.io.*;
import java.util.*;

/**
 * 해설 버전
 *
 * 시간복잡도: O(N^4 + N^2*M)
 */
public class Main {

	/**
	 * 상수 정의
	 */
	static final int INF = (int) 1e9 + 10; // 1e9 == 10^9 (10억)

	// 방향 배열 - 상하좌우
	static final int[] dx = {-1, 1, 0, 0};
	static final int[] dy = {0, 0, -1, 1};

	/**
	 * 위치를 나타내는 클래스
	 */
	static class Point {
		int x, y;

		Point(int x, int y){
			this.x = x;
			this.y = y;
		}
	}

	/**
	 * 쌍을 나타내는 클래스
	 */
	static class Pair<F, S> {
		F first;
		S second;

		Pair(F first, S second) {
			this.first = first;
			this.second = second;
		}
	}

	/**
	 * 두 점 사이의 맨하튼 거리 계산
 	 */
	static int calculateDistance(Point a, Point b) {
		return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
	}

	/**
	 * 다익스트라 - (x, y)부터 다른 칸까지의 최단 거리 계산
 	 */
	static int[][] computeDistances(int x, int y, int N, int[][] map) {
		int[][] dist = new int[N][N];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				dist[i][j] = map[i][j] == 1 ? INF : -1;
			}
		}

		Queue<Point> q = new ArrayDeque<>();
		q.offer(new Point(x, y));
		dist[x][y] = 0;

		while(!q.isEmpty()) {
			Point p = q.poll();
			int cx = p.x, cy = p.y;

			for(int d = 0; d < 4; d++) {
				int nx = cx + dx[d];
				int ny = cy + dy[d];

				if(nx < 0 || nx >= N || ny < 0 || ny >= N) continue;

				// 장애물이 있거나 이미 방문한 경우
				// 주의: 별도 visited 배열 필요 X
				if(dist[nx][ny] == INF || dist[nx][ny] != -1) continue;

				q.offer(new Point(nx, ny));
				dist[nx][ny] = dist[cx][cy] + 1;
			}
		}

		return dist;
	}

	/**
	 * 위쪽 바라볼 때 전사 수 반환
	 */
	static int sightUp(int x, int y, int N, int[][] warriorCountMap, int[][] sightMap, boolean isTest) {
		// 시야에 포함하기
		for(int i = x-1; i >= 0; i--) {
			// 주의: i를 활용하여 (y - N) 또는 (y + N) 형태로 표현 가능
			int left = Math.max(0, y - (x - i));
			int right = Math.min(N-1, y + (x - i));
			for(int j = left; j <= right; j++) {
				sightMap[i][j] = 1;
			}
		}

		// 정면에 전사가 있는 경우 처리
		boolean obstructionFound = false;
		for(int i = x-1; i >= 0; i--) {
			if(obstructionFound) {
				sightMap[i][y] = 0;
			}

			if(warriorCountMap[i][y] > 0) {
				obstructionFound = true;
			}
		}

		// 좌우에 전사가 있는 경우 처리
		for(int i = x-1; i >= 1; i--) {
			int left = Math.max(0, y - (x - i));
			int right = Math.min(N-1, y + (x - i));

			// 좌
			for(int j = left; j < y; j++) {
				if(sightMap[i][j] == 0 || warriorCountMap[i][j] > 0) { // 시야 차단된 경우
					if(j > 0) {
						sightMap[i-1][j-1] = 0;
					}
					sightMap[i-1][j] = 0;
				}
			}

			// 우
			for(int j = y + 1; j <= right; j++) {
				if(sightMap[i][j] == 0 || warriorCountMap[i][j] > 0) { // 시야 차단된 경우
					if(j+1 < N) {
						sightMap[i-1][j+1] = 0;
					}
					sightMap[i-1][j] = 0;
				}
			}
		}

		// 전사 수 계산하기
		int coverage = 0;
		for(int i = x-1; i >= 0; i--) {
			int left = Math.max(0, y - (x - i));
			int right = Math.min(N-1, y + (x - i));
			for(int j = left; j <= right; j++) {
				if(sightMap[i][j] == 1) {
					coverage += warriorCountMap[i][j];
				}
			}
		}

		// 테스트 모드인 경우 되돌리기
		if(isTest) {
			for(int i = x-1; i >= 0; i--) {
				int left = Math.max(0, y - (x - i));
				int right = Math.min(N-1, y + (x - i));
				for(int j = left; j <= right; j++) {
					sightMap[i][j] = 0;
				}
			}
		}

		return coverage;
	}

	/**
	 * 아래쪽 바라볼 때 전사 수 반환
	 */
	static int sightDown(int x, int y, int N, int[][] warriorCountMap, int[][] sightMap, boolean isTest) {
		// 시야에 포함하기
		for(int i = x+1; i < N; i++) {
			int left = Math.max(0, y - (i - x));
			int right = Math.min(N-1, y + (i - x));
			for(int j = left; j <= right; j++) {
				sightMap[i][j] = 1;
			}
		}

		// 정면에 전사가 있는 경우 처리
		boolean obstructionFound = false;

		for(int i = x+1; i < N; i++) {
			if(obstructionFound) {
				sightMap[i][y] = 0;
			}

			if(warriorCountMap[i][y] > 0) {
				obstructionFound = true;
			}
		}

		// 좌우에 전사가 있는 경우 처리
		for(int i = x+1; i < N-1; i++) {
			int left = Math.max(0, y - (i - x));
			int right = Math.min(N-1, y + (i - x));

			// 좌
			for(int j = left; j < y; j++) {
				if(sightMap[i][j] == 0 || warriorCountMap[i][j] > 0) { // 시야 차단된 경우
					if(j > 0) {
						sightMap[i+1][j-1] = 0;
					}
					sightMap[i+1][j] = 0;
				}
			}

			// 우
			for(int j = y + 1; j <= right; j++) {
				if(sightMap[i][j] == 0 || warriorCountMap[i][j] > 0) { // 시야 차단된 경우
					if(j+1 < N) {
						sightMap[i+1][j+1] = 0;
					}
					sightMap[i+1][j] = 0;
				}
			}
		}

		// 전사 수 계산하기
		int coverage = 0;
		for(int i = x+1; i < N; i++) {
			int left = Math.max(0, y - (i - x));
			int right = Math.min(N-1, y + (i - x));
			for(int j = left; j <= right; j++) {
				if(sightMap[i][j] == 1) {
					coverage += warriorCountMap[i][j];
				}
			}
		}

		// 테스트 모드인 경우 되돌리기
		if(isTest) {
			for(int i = x+1; i < N; i++) {
				int left = Math.max(0, y - (i - x));
				int right = Math.min(N-1, y + (i - x));
				for(int j = left; j <= right; j++) {
					sightMap[i][j] = 0;
				}
			}
		}

		return coverage;
	}

	/**
	 * 왼쪽 바라볼 때 전사 수 반환
	 */
	static int sightLeft(int x, int y, int N, int[][] warriorCountMap, int[][] sightMap, boolean isTest) {
		// 시야에 포함하기
		for(int j = y-1; j >= 0; j--) {
			int top = Math.max(0, x - (y - j));
			int bottom = Math.min(N-1, x + (y - j));
			for(int i = top; i <= bottom; i++) {
				sightMap[i][j] = 1;
			}
		}

		// 정면에 전사가 있는 경우 처리
		boolean obstructionFound = false;

		for(int j = y-1; j >= 0; j--) {
			if(obstructionFound) {
				sightMap[x][j] = 0;
			}

			if(warriorCountMap[x][j] > 0) {
				obstructionFound = true;
			}
		}

		// 위아래에 전사가 있는 경우 처리
		for(int j = y-1; j > 0; j--) {
			int top = Math.max(0, x - (y - j));
			int bottom = Math.min(N-1, x + (y - j));

			// 위
			for(int i = top; i < x; i++) {
				if(sightMap[i][j] == 0 || warriorCountMap[i][j] > 0) { // 시야 차단된 경우
					if(i > 0) {
						sightMap[i-1][j-1] = 0;
					}
					sightMap[i][j-1] = 0;
				}
			}

			// 아래
			for(int i = x + 1; i <= bottom; i++) {
				if(sightMap[i][j] == 0 || warriorCountMap[i][j] > 0) { // 시야 차단된 경우
					if(i+1 < N) {
						sightMap[i+1][j-1] = 0;
					}
					sightMap[i][j-1] = 0;
				}
			}
		}

		// 전사 수 계산하기
		int coverage = 0;
		for(int j = y-1; j >= 0; j--) {
			int top = Math.max(0, x - (y - j));
			int bottom = Math.min(N-1, x + (y - j));
			for(int i = top; i <= bottom; i++) {
				if(sightMap[i][j] == 1) {
					coverage += warriorCountMap[i][j];
				}
			}
		}

		// 테스트 모드인 경우 되돌리기
		if(isTest) {
			for(int j = y-1; j >= 0; j--) {
				int top = Math.max(0, x - (y - j));
				int bottom = Math.min(N-1, x + (y - j));
				for(int i = top; i <= bottom; i++) {
					sightMap[i][j] = 0;
				}
			}
		}

		return coverage;
	}

	/**
	 * 오른쪽 바라볼 때 전사 수 반환
	 */
	static int sightRight(int x, int y, int N, int[][] warriorCountMap, int[][] sightMap, boolean isTest) {
		// 시야에 포함하기
		for(int j = y+1; j < N; j++) {
			int top = Math.max(0, x - (j - y));
			int bottom = Math.min(N-1, x + (j - y));
			for(int i = top; i <= bottom; i++) {
				sightMap[i][j] = 1;
			}
		}

		// 정면에 전사가 있는 경우 처리
		boolean obstructionFound = false;

		for(int j = y+1; j < N; j++) {
			if(obstructionFound) {
				sightMap[x][j] = 0;
			}

			if(warriorCountMap[x][j] > 0) {
				obstructionFound = true;
			}
		}

		// 위아래에 전사가 있는 경우 처리
		for(int j = y+1; j < N-1; j++) {
			int top = Math.max(0, x - (j - y));
			int bottom = Math.min(N-1, x + (j - y));

			// 위
			for(int i = top; i < x; i++) {
				if(sightMap[i][j] == 0 || warriorCountMap[i][j] > 0) { // 시야 차단된 경우
					if(i > 0) {
						sightMap[i-1][j+1] = 0;
					}
					sightMap[i][j+1] = 0;
				}
			}

			// 아래
			for(int i = x + 1; i <= bottom; i++) {
				if(sightMap[i][j] == 0 || warriorCountMap[i][j] > 0) { // 시야 차단된 경우
					if(i+1 < N) {
						sightMap[i+1][j+1] = 0;
					}
					sightMap[i][j+1] = 0;
				}
			}
		}

		// 전사 수 계산하기
		int coverage = 0;
		for(int j = y+1; j < N; j++) {
			int top = Math.max(0, x - (j - y));
			int bottom = Math.min(N-1, x + (j - y));
			for(int i = top; i <= bottom; i++) {
				if(sightMap[i][j] == 1) {
					coverage += warriorCountMap[i][j];
				}
			}
		}

		// 테스트 모드인 경우 되돌리기
		if(isTest) {
			for(int j = y+1; j < N; j++) {
				int top = Math.max(0, x - (j - y));
				int bottom = Math.min(N-1, x + (j - y));
				for(int i = top; i <= bottom; i++) {
					sightMap[i][j] = 0;
				}
			}
		}

		return coverage;
	}

	/**
	 * 최적의 방향 선택하여 시야각 내 전사 수 반환
	 */
	static int chooseBestSight(int x, int y, int N, int[][] warriorCountMap, int[][] sightMap) {
		int maxCoverage = -1;
		int bestSight = -1;

		// 상하좌우
		for(int d = 0; d < 4; d++) {
			// 시야 맵 초기화
			for(int i = 0; i < N; i++) {
				for(int j = 0; j < N; j++) {
					sightMap[i][j] = 0;
				}
			}

			int coverage;
			if(d == 0) {
				coverage = sightUp(x, y, N, warriorCountMap, sightMap, true);
			} else if(d == 1) {
				coverage = sightDown(x, y, N, warriorCountMap, sightMap, true);
			} else if(d == 2) {
				coverage = sightLeft(x, y, N, warriorCountMap, sightMap, true);
			} else {
				coverage = sightRight(x, y, N, warriorCountMap, sightMap, true);
			}

			if(coverage > maxCoverage) {
				maxCoverage = coverage;
				bestSight = d;
			}
		}

		// 실제 시야맵 설정
		if(bestSight == 0) {
			sightUp(x, y, N, warriorCountMap, sightMap, false);
		} else if(bestSight == 1) {
			sightDown(x, y, N, warriorCountMap, sightMap, false);
		} else if(bestSight == 2) {
			sightLeft(x, y, N, warriorCountMap, sightMap, false);
		} else {
			sightRight(x, y, N, warriorCountMap, sightMap, false);
		}

		return maxCoverage;
	}

	/**
	 * 전사들의 이동 & 공격 후 <이동한 전사 수, 공격한 전사 수> 반환
	 */
	static Pair<Integer, Integer> moveWarriors(int N, int M, int px, int py, Point[] warriors, int[][] sightMap) {
		int totalMoved = 0;
		int totalAttacked = 0;

		for(int i = 0; i < M; i++) {
			int x = warriors[i].x, y = warriors[i].y;

			// 주의: 사라진 전사 또는 시야각 내 전사는 pass
			if(x == -1 || sightMap[x][y] == 1) continue;

			boolean moved = false;

			int currentDist = calculateDistance(warriors[i], new Point(px, py));
			for(int d = 0; d < 4; d++) {
				int nx = x + dx[d], ny = y + dy[d];

				if(nx < 0 || nx >= N || ny < 0 || ny >= N) continue;
				if(sightMap[nx][ny] == 1) continue;

				int nextDist = calculateDistance(new Point(nx, ny), new Point(px, py));
				if(currentDist <= nextDist) continue;

				x = nx; y = ny;
				moved = true;
				totalMoved++;
				break;
			}

			if(moved) {
				currentDist = calculateDistance(new Point(x, y), new Point(px, py));

				for(int d = 0; d < 4; d++) {
					// 주의: 상하좌우 -> 좌우상하로 변경 가능
					int oppositeDir = (d + 2) % 4;
					int nx = x + dx[oppositeDir], ny = y + dy[oppositeDir];

					if(nx < 0 || nx >= N || ny < 0 || ny >= N) continue;
					if(sightMap[nx][ny] == 1) continue;

					int nextDist = calculateDistance(new Point(nx, ny), new Point(px, py));
					if(currentDist <= nextDist) continue;

					x = nx; y = ny;
					totalMoved++;
					break;
				}
			}

			warriors[i] = new Point(x, y);
		}

		// 공격한 전사 수 계산
		for(int i = 0; i < M; i++) {
			int x = warriors[i].x, y = warriors[i].y;
			if(x == -1) continue;

			if(x == px && y == py) {
				totalAttacked++;
				warriors[i] = new Point(-1, -1);
			}
		}

		return new Pair<>(totalMoved, totalAttacked);
	}

	/**
	 * 각 칸당 전사들 수 업데이트
	 */
	static int[][] updateWarriorCountMap(int N, int M, Point[] warriors) {
		int[][] warriorCountMap = new int[N][N];

		for(int i = 0; i < M; i++) {
			int x = warriors[i].x, y = warriors[i].y;
			if(x == -1) continue;

			warriorCountMap[x][y]++;
		}

		return warriorCountMap;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());

		st = new StringTokenizer(br.readLine());
		int cx = Integer.parseInt(st.nextToken());
		int cy = Integer.parseInt(st.nextToken());
		int ex = Integer.parseInt(st.nextToken());
		int ey = Integer.parseInt(st.nextToken());

		Point[] warriors = new Point[M];
		st = new StringTokenizer(br.readLine());
		for(int i = 0; i < M; i++) {
			warriors[i] = new Point(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
		}

		int[][] map = new int[N][N];
		for(int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j = 0; j < N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		// 종료 지점으로부터 모든 칸까지의 거리 계산
		int[][] distanceMap = computeDistances(ex, ey, N, map);

		// 도달할 수 없는 경우 종료 (-1 출력)
		if(distanceMap[cx][cy] == -1) {
			System.out.println(-1);
			return;
		}

		// 종료 지점에 도달할 때까지
		while(true) {
			// 메두사 한 칸 이동
			for(int i = 0; i < 4; i++) {
				int nx = cx + dx[i], ny = cy + dy[i];

				if(nx < 0 || nx >= N || ny < 0 || ny >= N) continue;

				/**
				 *  주의: 다익스트라 맵으로 최단 경로를 보장할 수 있는 이유
				 *
				 *  도달 가능한 경로가 있을 때,
				 *  1) 같은 행 또는 열에 있는 경우:
				 *  	상하좌우 중 오직 1개의 방향만 거리가 줄어듦
				 *  2) 행과 열 모두 다른 경우:
				 *  	상하좌우 중 최대 2개 방향이 거리가 줄어듦
				 *		-> 맨하튼 거리이므로, 둘 중 어느쪽을 택해도 최단 경로임
				 */
				if(distanceMap[cx][cy] <= distanceMap[nx][ny]) continue;

				cx = nx; cy = ny;
				break;
			}

			// 종료 지점에 도달한 경우 종료 (0 출력)
			if(cx == ex && cy == ey) {
				System.out.println(0);
				return;
			}

			// 현재 위치 전사 사라짐
			for(int i = 0; i < M; i++) {
				if(warriors[i].x == cx && warriors[i].y == cy) {
					warriors[i] = new Point(-1, -1);
				}
			}

			// 현재 전사 수 업데이트, 시야맵 초기화
			int[][] warriorCountMap = updateWarriorCountMap(N, M, warriors);
			int[][] sightMap = new int[N][N];

			// 최적의 시야 방향 선택 후 시야각 내 전사 수 계산
			int totalSight = chooseBestSight(cx, cy, N, warriorCountMap, sightMap);

			// 전사들의 이동 & 공격 후 각 전사 수 계산
			Pair<Integer, Integer> result = moveWarriors(N, M, cx, cy, warriors, sightMap);

			// 이동한 전사 수, 시야각 내 전사 수, 공격한 전사 수 출력
			System.out.println(result.first + " " + totalSight + " " + result.second);
		}
	}
}