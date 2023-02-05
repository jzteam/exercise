package cn.jzteam.algorithm.leetcode.interview;


/**
 * 在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。输入一个数组，求出这个数组中的逆序对的总数。
 *
 *  
 *
 * 示例 1:
 *
 * 输入: [7,5,6,4]
 * 输出: 5
 *  
 *
 * 限制：
 *
 * 0 <= 数组长度 <= 50000
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/shu-zu-zhong-de-ni-xu-dui-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Question0051 {
    // 归并排序：先分成两半，左边内部逆序对数量 + 右边内部逆序对数量 + 左右合并时右边提前插入别住左边的数量
    public static int reversePairs(int[] nums) {
        return mergeSort(nums, 0, nums.length-1);
    }

    private static int mergeSort(int[] nums, int l, int r) {
        if (l>=r) {
            return 0;
        }

        // 归
        int m = (l + r) /2;
        int leftCount = mergeSort(nums, l, m);
        int rightCount = mergeSort(nums, m+1, r);
        // 并
        int mergeCount = 0;
        int lpos = l;
        int rpos = m+1;
        int[] temp = new int[r-l+1];
        int tempPos = 0;
        while (lpos <= m && rpos <= r) {
            // 只要任一方越界，就停止循环
            if (nums[lpos] <= nums[rpos]) {
                temp[tempPos++] = nums[lpos];
                lpos++;
            } else {
                temp[tempPos++] = nums[rpos];
                rpos++;
                // 只要右边插入了，就会贡献逆序对数量
                mergeCount += m - lpos + 1;
            }
        }
        // 循环结束还未越界的部分，就直接拼接到temp后面了
        for (int i=lpos;i<=m;i++) {
            temp[tempPos++] = nums[i];
        }
        for (int i=rpos;i<=r;i++) {
            temp[tempPos++] = nums[i];
        }
        // 此时 l->r 已经排序完成，把结果覆盖到nums对应位置
        if (tempPos != r-l+1) {
            System.out.println("error tempPos="+tempPos + ", r="+r + ", l="+l);
        }
        System.arraycopy(temp, 0, nums, l, tempPos);
        return leftCount + rightCount + mergeCount;
    }



    public int reversePairs1(int[] nums) {
        // 暴力破解 O(n^2)，超时
        int num=0;
        for (int i=0;i<nums.length;i++) {
            for (int j=i+1;j<nums.length;j++) {
                if (nums[i] > nums[j]) {
                    num++;
                }
            }
        }
        return num;
    }

    public static void main(String[] args) {
        System.out.println(reversePairs(new int[]{7,5,6,4}));
        System.out.println(reversePairs(new int[]{7}));
        System.out.println(reversePairs(new int[]{7,7,1}));
    }
}
