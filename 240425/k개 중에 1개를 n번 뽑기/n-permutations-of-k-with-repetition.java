import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static int k, n;
    public static ArrayList<Integer> a = new ArrayList<>();

    public static void print() {
        for(int i = 0; i < a.size(); i++)
            System.out.print(a.get(i) + " ");
        System.out.println();
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

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        k = sc.nextInt();
        n = sc.nextInt();

        chooseNum(1);
    }
}