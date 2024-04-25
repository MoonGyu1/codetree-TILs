import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;

public class Main {
    public static int n;
    public static int[][] map;
    public static int[][][] bomb = { // {{dx}, {dy}}
        {{-2, -1, 1, 2}, {0, 0, 0, 0}}, // 1
        {{0, -1, 0, 1}, {-1, 0, 1, 0}}, // 2
        {{-1, -1, 1, 1}, {-1, 1, 1, -1}}, // 3
    };
    public static ArrayList<int[]> pos = new ArrayList<>();
    public static int max = 0;

    public static boolean inRange(int x, int y) {
        if(x>=0 && x<n && y>=0 && y<n){
            return true;
        }
        return false;
    }

    // 해당 포지션의 폭탄을 고르고, 다음 포지션을 호출. 포지션이 마지막 인덱스인 경우 합을 계산하여 반환
    public static void count(int posIdx) {
        if(posIdx == pos.size()){
            int cnt = 0;
            for(int i=0; i<n; i++){
                for(int j=0; j<n; j++){
                    if(map[i][j] >= 1){
                        cnt++;
                    }
                }
            }
            max = Math.max(max, cnt);
            return;
        }

        int x = pos.get(posIdx)[0], y = pos.get(posIdx)[1];
        for(int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                if(inRange(x+bomb[i][0][j], y+bomb[i][1][j]))
                    map[x+bomb[i][0][j]][y+bomb[i][1][j]] += 1;
            }
            count(posIdx + 1);
            for(int j=0; j<4; j++){
                if(inRange(x+bomb[i][0][j], y+bomb[i][1][j]))
                    map[x+bomb[i][0][j]][y+bomb[i][1][j]] -= 1;
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        map = new int[n][n];
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                map[i][j] = sc.nextInt();
                if(map[i][j] == 1){
                    pos.add(new int[]{i, j});
                }
            }
        }

        // for(int i=0; i<n; i++){
        //     System.out.println(Arrays.toString(map[i]));
        // }
        // for(int i=0; i<pos.size(); i++){
        //     System.out.println(Arrays.toString(pos.get(i)));
        // }
        count(0);

        System.out.println(max);
    }
}