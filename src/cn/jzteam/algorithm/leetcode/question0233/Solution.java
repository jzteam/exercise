package cn.jzteam.algorithm.leetcode.question0233;

/** 数字1的个数
 * 给定一个整数 n，计算所有小于等于 n 的非负整数中数字 1 出现的个数。
 *
 * 示例:
 *
 * 输入: 13
 * 输出: 6
 * 解释: 数字 1 出现在以下数字中: 1, 10, 11, 12, 13 。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/number-of-digit-one
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution {
    // 题解：
    public static int countDigitOne(int n) {
        // 因为i是从1开始的，n 必须大于1，后面才能正常计算
        if (n<=0){
            return 0;
        }
        if (n==1){
            return 1;
        }
        long total = 0;

        // 因为i可能比n还要大，如果n超过32位，则i可能出现负数。所以必须声明为long型。
        // i表示位，出现规律则是 i*10 一循环
        // i<n，包含最高位
        for(long i=1;i<=n;i*=10) {
            // i 可以表示个位、十位、百位...
            // 个位：每10个数就出现 1 次 1
            // 十位：每100个数就出现 10 次 1
            // 百位：每1000个数就出现 100 次 1
            // ......
            // 因为起点不统一，终点的规律就不确定，所以修改起点，每个位的统计全部以出现过1之后的第一个数开始。
            // 如此，个位，就要从2开始数，十位就要从20开始数，百位就要从200开始数。
            // 同时所有位上的变化规律都是 0~9 这10个数，所以n表示n个数，+1就是把0包含进来。
            // 针对i位来看，每隔i*10个数，i位上出现1的数字一定有i个。
            // 但是最后一组可能不满足i*10个数，所以不会统计上，于是需要特殊处理。
            // 如 115，十位出现1的个数口算即得（100以下有10个，100以上有6个）共16个
            // 公式可得 (115+1)/100 = 1组，即10个
            // 此时就要看最后一组满足 i*10个数。抛去i以上的高位，如果剩下的数 >= 2*i，则也会出现i个1，否则就是 Math.max(left-i, 0)个
            long temp = ((n + 1) / (i * 10)) * i;
            // 每个位上最后一组1的个数：抛去i之上的高位
            long left = (n+1) % (i*10); // 余数也是 0~(i*10-1)之间的数字的个数（注意不是数字而是个数）
            long temp1 = Math.min(i, Math.max(left-i, 0));
            // System.out.println(i+" 位上整组出现1的次数为："+temp + ", 补充最后一组："+temp1 + ", 一共：" + (temp+temp1));

            total += temp+temp1;
        }
        // 题目没有提示数据太大，所以直接强转为int
        return (int)total;
    }

    // 个人方案：排列组合
    // 屏蔽最高位，1出现的次数：count = 累加器1~(w-1) [i*Ci_w-1*9^(w-1-i)]，w表示位数
    // 包括最高位：
    //      最高位取1的情况：累加器0~(w-1) [(i+1)*Ci_w-1*9^(w-1-i)]，w表示位数
    //      最高位取最大值的情况：递归(去掉最高位后的数字)
    //      最高位其他可能：除掉1和最大值两种情况剩下的可能数量 * count
    public static int count(int n) {
        if (n < 0) {
            return 0;
        }
        // 计算位数
        int w = (n+"").length();
        if (w == 1) {
            return n > 0 ? 1 : 0;
        }
        int sum = 0;
        // 计算屏蔽最高位的情况
        for (int i=1;i<=w-1;i++) {
            sum += i * c(i, w-1) * Math.pow(9, w-1-i);
        }

        // 计算最高位不取最大值，且不取1的情况
        int gn = n / (int)Math.pow(10, w-1);
        if (gn > 2) {
            sum += (gn-2) * sum; // 此情况依赖 屏蔽最高位的总数，所以放前面，直接使用sum变量（此时sum还没包括其他情况的数据）
        }
        // 计算最高位取最大值
        if (gn > 1) {
            // 最高位不是1，但是取值为1的情况
            for (int i=0;i<=w-1;i++) {
                sum += (i+1) * c(i, w-1) * Math.pow(9, w-1-i);
            }
            // 最高位不是1，取最大值的情况
            sum += count(n % (int)Math.pow(10, w-1));
        } else {
            // 最高位就是1，会比最高位不是1的情况多出 rem+1个1，+1是因为取余的到的数字不包括0这种情况
            int rem = n % (int) Math.pow(10, w - 1);
            sum += count(rem);
            sum += rem+1;
        }
        return sum;
    }

    public static int c (int selected, int total) {
        int num = 1;
        while (total > selected) {
            num *= total--;
        }
        return num;
    }

    public static void main(String[] args) {
        System.out.println(countDigitOne(10)); // 2
        System.out.println(countDigitOne(13)); // 6
        System.out.println(countDigitOne(999)); // 300
        System.out.println(countDigitOne(141)); //
        System.out.println(countDigitOne(1410065408)); // 1737167499
    }
}
