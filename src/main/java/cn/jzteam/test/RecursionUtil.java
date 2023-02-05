package cn.jzteam.test;

public class RecursionUtil {

    public static void main(String[] args) {
        System.out.println("开始：");
        System.out.println(RecursionUtil.result(10L));
    }

    public static Long result(long n) {
        if (n <= 2) {
            return 1L;
        }
        return result(n - 1) + result(n - 2);
    }

}
