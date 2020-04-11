package cn.jzteam.algorithm.leetcode.question0887;

public class Solution {
    public static int superEggDrop(int K, int N) {
        // 先留下2个鸡蛋，其余的都用来折半削减楼层高度
        int res = N;
        int times = 0;
        if (K > 2) {
            int limit = K - 2;
            while (limit-- > 0 && res >= 3) {
                res = res / 2; // 保证res>=1
                times++;
            }
        }
        if (K >= 2) {
            // 用两个鸡蛋测试剩余楼层 n(n+1)/2 >= res
            for (int i = 1; i<=res; i++) {
                if (i * (i + 1) / 2 >= res) {
                    return i + times;
                }
            }
        } else {
            return N;
        }

        return 0;
    }

    public static void main(String[] args) {
        System.out.println(superEggDrop(1, 2));
        System.out.println(superEggDrop(2, 6));
        System.out.println(superEggDrop(3, 14));
        System.out.println(superEggDrop(1, 3));
        System.out.println(superEggDrop(3, 1));
        System.out.println(superEggDrop(9, 2));
        System.out.println(superEggDrop(9, 3));
        System.out.println(superEggDrop(9, 4));
        System.out.println(superEggDrop(9, 6));
    }
}
