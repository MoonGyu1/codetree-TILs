import java.util.Scanner;

public class Main {
    public static int n;
    public static String[] opt = new String[]{"1", "22", "333", "4444"};
    public static int answer = 0;

    public static void make(String num) {
        if(num.length() > n){
            return;
        }
        
        answer++;

        for(int i=0; i<4; i++){
            num += opt[i];
            make(num);
            num = num.substring(0, num.length() - opt[i].length() + 1);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        make("");

        System.out.println(answer-1);
    }
}