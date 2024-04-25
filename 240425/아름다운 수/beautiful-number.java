import java.util.Scanner;

public class Main {
    public static int n;
    public static String[] opt = new String[]{"1", "22", "333", "4444"};
    public static int answer = 0;

    // 주어진 숫자에 가능한 옵션을 각각 더하면서 정답이면 카운트 추가하고 재귀 호출, 정답이 아니면 멈추는 함수
    public static void make(String num) {
        if(num.length() > n){
            return;
        }
        
        if(num.length() == n)
            answer++;

        for(int i=0; i<4; i++){
            num += opt[i];
            make(num);
            num = num.substring(0, num.length() - opt[i].length());
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        make("");

        System.out.println(answer);
    }
}