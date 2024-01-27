import java.util.Scanner;

public class Main {
    public static final int MAX_N = 100;
    public static int n, m;
    public static int[][] grid = new int[MAX_N][MAX_N];

    public static int[] seq = new int[MAX_N];
    
    public static boolean isHappy() {
        int sameCnt = 1, maxCnt = 1;
        for(int i = 1; i<n; i++){
            if(seq[i-1] == seq[i])
                sameCnt++;
            else
                sameCnt = 1;
            
            maxCnt = Math.max(maxCnt, sameCnt);
        }
        return maxCnt >= m;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        m = sc.nextInt();

        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                grid[i][j] = sc.nextInt();
            }
        }
        int happyCnt = 0;

        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++)
                seq[j] = grid[i][j];
            if(isHappy())
                happyCnt++;
        }

        for(int j=0; j<n; j++){
            for(int i=0; i<n; i++)
                seq[i] = grid[i][j];
            if(isHappy())
                happyCnt++;
        }

        System.out.print(happyCnt);
    }
}