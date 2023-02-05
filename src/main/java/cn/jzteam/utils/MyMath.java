package cn.jzteam.utils;

public class MyMath {
    // 最大公约数：辗转相除法（欧几里得算法）
    // Greatest Common Divisor
    public static int gcd (int a, int b) {
        if (a == 0 || b == 0) {
            return -1;
        }
        int temp;
        while ((temp = a%b) != 0) {
            a = b;
            b = temp;
        }
        return b;
    }

    public static void main(String[] args) {
        System.out.println(gcd(319, 377));
        System.out.println(gcd(2, 3));
    }
}
