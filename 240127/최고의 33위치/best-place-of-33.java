import java.util.Scanner;

public class Main {
    public static final int MAX_N = 20;

    public static int n;
    public static int[][] grid = new int[MAX_N][MAX_N];

    public static int getNumOfGold(int rs, int cs, int re, int ce) {
        int numOfGold = 0;

        for(int row = rs; row <= re; row++) {
            for(int col = cs; col <= ce; col++){
                numOfGold += grid[row][col];
            }
        }

        return numOfGold;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int maxGold = 0;

        n = sc.nextInt();
        for(int row = 0; row < n; row++)
            for(int col = 0; col < n; col++)
                grid[row][col] = sc.nextInt();
        
        for(int row = 0; row < n; row++){
            for(int col = 0; col < n; col++){
                if(row + 2 >= n || col + 2 >= n)
                    continue;
                
                int numOfGold = getNumOfGold(row, col, row + 2, col + 2);

                maxGold = Math.max(maxGold, numOfGold);
            }
        }

        System.out.println(maxGold);
    }
}