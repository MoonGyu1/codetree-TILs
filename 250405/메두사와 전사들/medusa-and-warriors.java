import java.util.*;
import java.io.*;

public class Main {
    static int N, M;
    static int SR, SC, ER, EC;
    static ArrayList<Warrior> warriors;
    static int[][] map;
    static boolean[][] isShown;

    public static void main(String[] args) throws IOException {
        // Runtime.getRuntime().gc();
        // long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        // System.out.print(usedMemory + " bytes");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        SR = Integer.parseInt(st.nextToken());
        SC = Integer.parseInt(st.nextToken());
        ER = Integer.parseInt(st.nextToken());
        EC = Integer.parseInt(st.nextToken());

        warriors = new ArrayList<>();

        st = new StringTokenizer(br.readLine());
        for(int i = 0; i < M; i++) {
            warriors.add(new Warrior(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())));
        }

        // System.out.println(N + " " + M);
        // System.out.println(SR + " " + SC + " " + ER + " " + EC);
        
        map = new int[N][N];
        for(int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        // usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        // System.out.print(usedMemory + " bytes");
        // System.out.println(Arrays.deepToString(map));

        // 0. 
        ArrayList<int[]> route = getRoutes();
        if(route.isEmpty()) {
            System.out.println(-1);
            return;
        }

        // usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        // System.out.print(usedMemory + " bytes");

        for(int[] r : route) {
            if(r[0] == ER && r[1] == EC) {
                System.out.println(0);
                return;
            }

            int mCnt = 0, sCnt = 0, aCnt = 0;

            // 1.
            SR = r[0]; SC = r[1];

            for(Warrior w : warriors) {
                if(w.r == SR && w.c == SC) {
                    w.dead = true;
                }
            }

            // usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            // System.out.print(usedMemory + " bytes");

            // 2.
            isShown = new boolean[N][N];
            int max = -1;
            for(int d : new int[]{0, 4, 8, 12}) { // 상하좌우
                boolean[][] tmpShown = new boolean[N][N];
                see(d, SR, SC, true, tmpShown);
                
                // System.out.println(Arrays.deepToString(tmpShown));

                for(Warrior w : warriors) {
                    if(w.dead) continue;

                    if(tmpShown[w.r][w.c]) {
                        int td = 0;
                        if(d == 0) {
                            if(w.c < SC) td = 1;
                            else if(w.c == SC) td = 2;
                            else td = 3;
                        } else if(d == 4) {
                            if(w.c < SC) td = 5;
                            else if(w.c == SC) td = 6;
                            else td = 7;
                        } else if(d == 8) {
                            if(w.r < SR) td = 9;
                            else if(w.r == SR) td = 10;
                            else td = 11;
                        } else {
                            if(w.r < SR) td = 13;
                            else if(w.r == SR) td = 14;
                            else td = 15;
                        }

                        // if(d == 4) System.out.println(td);

                        see(td, w.r, w.c, false, tmpShown);
                    }
                }

                int stunned = getShownCnt(tmpShown);
                if(stunned > max) {
                    isShown = tmpShown;
                    max = stunned;
                }
            }

            sCnt += max;
            // usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            // System.out.print(usedMemory + " bytes");
            // System.out.println(Arrays.deepToString(isShown));

            // 3.
            for(Warrior w : warriors) {
                // System.out.println("w: " + w.r + "," + w.c);

                if(w.dead || isShown[w.r][w.c]) continue;

                for(int[] d : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) { // 상하좌우
                    int x = w.r, y = w.c;
                    int nx = x + d[0], ny = y + d[1];

                    if(inRange(nx, ny) && !isShown[nx][ny] && getDist(nx, ny, SR, SC) < getDist(x, y, SR, SC)) {
                        w.r = nx; w.c = ny;
                        mCnt++;

                        // System.out.println(w.r + "," + w.c);
                        // 4.
                        if(w.r == SR && w.c == SC) {
                            w.dead = true;
                            aCnt++;
                        }
                        break;
                    }
                }

                if(w.dead) continue;

                for(int[] d : new int[][]{{0, -1}, {0, 1}, {-1, 0}, {1, 0}}) { // 좌우상하
                    int x = w.r, y = w.c;
                    int nx = x + d[0], ny = y + d[1];

                    if(inRange(nx, ny) && !isShown[nx][ny] && getDist(nx, ny, SR, SC) < getDist(x, y, SR, SC)) {
                        w.r = nx; w.c = ny;
                        mCnt++;

                        // System.out.println(w.r + "," + w.c);
                        // 4.
                        if(w.r == SR && w.c == SC) {
                            w.dead = true;
                            aCnt++;
                        }
                        break;
                    }
                }
            }

            // usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            // System.out.print(usedMemory + " bytes");

            // 5.
            System.out.println(mCnt + " " + sCnt + " " + aCnt);

            // break;
        }
    }

    // dir = 0 - 1,2,3, 4 - 5,6,7, 8 - 9,10,11, 12 - 13,14,15
    // 상 - 좌상,상,우상, 하 - 좌하,하,우하, 좌 - 상좌,좌,하좌, 우 - 상우,우,하우
    static void see(int dir, int r, int c, boolean flag, boolean[][] shown) {
        if(dir == 0 || dir == 1 || dir == 2 || dir == 3) {
            int fromy = Math.max(0, (dir == 0 || dir == 1) ? c-1 : c);
            int toy = Math.min((dir == 0 || dir == 3) ? c+1 : c, N-1);

            for(int x = r-1; x >= 0; x--) {
                for(int y = fromy; y <= toy; y++) {
                    shown[x][y] = flag;
                }
                if(dir == 0 || dir == 1) fromy = Math.max(0, fromy -1);
                if(dir == 0 || dir == 3) toy = Math.min(toy + 1, N-1);
            }
        } else if(dir == 4 || dir == 5 || dir == 6 || dir == 7) {
            int fromy = Math.max(0, (dir == 4 || dir == 5) ? c-1 : c);
            int toy = Math.min(N-1, (dir == 4 || dir == 7) ? c+1 : c);

            for(int x = r+1; x < N; x++) {
                for(int y = fromy; y <= toy; y++) {
                    shown[x][y] = flag;
                }
                if(dir == 4 || dir == 5) fromy = Math.max(0, fromy -1);
                if(dir == 4 || dir == 7) toy = Math.min(toy + 1, N-1);
            }
        }
        // 좌 - 상좌,좌,하좌 
        else if(dir == 8 || dir == 9 || dir == 10 || dir == 11) {
            int fromx = Math.max(0, (dir == 8 || dir == 9) ? r-1 : r);
            int tox = Math.min(N-1, (dir == 8 || dir == 11) ? r+1 : r);

            for(int y = c-1; y >= 0; y--) {
                for(int x = fromx; x <= tox; x++) {
                    shown[x][y] = flag;
                }
                if(dir == 8 || dir == 9) fromx = Math.max(0, fromx -1);
                if(dir == 8 || dir == 11) tox = Math.min(N-1, tox + 1);
            }
        }
        // 우 - 상우,우,하우
        else if(dir == 12 || dir == 13 || dir == 14 || dir == 15) {
            int fromx = Math.max(0, (dir == 12 || dir == 13) ? r-1 : r);
            int tox = Math.min(N-1, (dir == 12 || dir == 15) ? r+1 : r);

            for(int y = c+1; y < N; y++) {
                for(int x = fromx; x <= tox; x++) {
                    shown[x][y] = flag;
                }
                if(dir == 12 || dir == 13) fromx = Math.max(0, fromx -1);
                if(dir == 12 || dir == 15) tox = Math.min(N-1, tox + 1);
            }
        }
    }

    static int getShownCnt(boolean[][] shown) {
        int cnt = 0;

        for(Warrior w : warriors) {
            if(!w.dead && shown[w.r][w.c]) {
                cnt++;
            }
        }

        return cnt;
    }

    static class Warrior {
        int r, c;
        boolean dead = false;

        Warrior(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    static ArrayList<int[]> getRoutes() {
        class Node {
            int x, y;
            String route;

            Node(int x, int y, String route) {
                this.x = x;
                this.y = y;
                this.route = route;
            }
        }

        Queue<Node> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[N][N];

        q.add(new Node(SR, SC, ""));
        visited[SR][SC] = true;

        // Runtime.getRuntime().gc();
        // long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        // System.out.print(usedMemory + " bytes(bfs)");

        while(!q.isEmpty()) {
            Node node = q.poll();
            int x = node.x, y = node.y;
            String route = node.route;

            if(x == ER && y == EC) {
                // usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                // System.out.print(usedMemory + " bytes(bfs end)");

                String[] tmp = route.split("/");
                ArrayList<int[]> result = new ArrayList<>();

                for(String t : tmp) {
                    if(t.isEmpty()) continue;
                    String[] tmp2 = t.split(",");
                    result.add(new int[]{Integer.parseInt(tmp2[0]), Integer.parseInt(tmp2[1])});
                }
                return result;
            }

            // 상하좌우
            for(int[] dist : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
                int nx = x + dist[0], ny = y + dist[1];
                if(inRange(nx, ny) && map[nx][ny] == 0 && !visited[nx][ny]) {
                    q.add(new Node(nx, ny, route + "/" + nx + "," + ny));
                    visited[nx][ny] = true;
                }
            }
        }

        return new ArrayList<>();
    }

    static boolean inRange(int x, int y) {
        return 0 <= x && x < N && 0 <= y && y < N;
    }

    static int getDist(int x1, int y1, int x2, int y2) {
        return Math.abs(x1-x2) + Math.abs(y1-y2);
    }
}