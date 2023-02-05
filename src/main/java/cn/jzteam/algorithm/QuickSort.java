package cn.jzteam.algorithm;

import java.util.Arrays;

public class QuickSort {

    public static void main(String[] args) {
        int[] arr = new int[]{1, -2, 3, 10, -4, 7, 2, -5};

        quickSort(arr,0,arr.length-1);
        System.out.println(Arrays.toString(arr));
    }



    public static void quickSort(int[] data,int start,int end){
        if(end <= start){
            return;
        }

        int i = start;
        int j = end + 1;
        int base = data[start];

        // 无限循环找到 j和i 的位置
        while(true){
            // 左边探索：使用前减减是为了控制当条件不满足时，i还是当前这个值；i初始值是没有+1，是因为base就是start值，比对的值正好要取++之后的
            while(i<end && data[++i] <= base);
            // 右边探索：使用前减减是为了控制当条件不满足时，j还是当前这个值；j初始值是end+1，是为了让前减减也能拿到end这个值
            while(j>start && data[--j] >= base);

            if(j>i){
                data[j] = data[j]^data[i];
                data[i] = data[j]^data[i];
                data[j] = data[j]^data[i];
            }else{
                break;
            }
        }

        // 此时，j处的值一定比base小或者j==start；i处的值一定比base大或者i==end
        // 所以交换一下j和start的值（其实如果相等就不用交换了）
        data[j] = data[j]^data[start];
        data[start] = data[j]^data[start];
        data[j] = data[j]^data[start];

        // 交换之后，start->j-1这个数组一定都比base小；i->end这个数组一定都比base大
        quickSort(data,start,j-1);
        quickSort(data,i,end);

    }
}
