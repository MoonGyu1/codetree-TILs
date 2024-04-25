import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static int k, n;
    public static ArrayList<Integer> a = new ArrayList<Integer>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        k = sc.nextInt();
        n = sc.nextInt();

        chooseNum(1);   
    }

    public static void print() {
        System.out.printf("%d %d\n", a.get(0), a.get(1));
    }

    public static void chooseNum(int currIdx) {
        if(currIdx == n + 1){
            print();
            return;
        }

        for(int i = 1; i < k+1; i++){
            a.add(i);
            chooseNum(currIdx + 1);
            a.remove(a.size() - 1);
        }
        return;
    }
}