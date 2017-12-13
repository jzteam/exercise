package cn.jzteam.algorithm;

import java.util.Arrays;

public class SelectSortTest {
    public static void main(String[] args) {
        int[] arr = new int[]{1, -2, 3, 10, -4, 7, 2, -5};
//		arr = selectSort(arr);
//		for(int i = 0;i < arr.length;i++){
//			System.out.println(arr[i] + " ");
//		}

        quickSort(arr,0,arr.length-1);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 描述：冒泡交换次数较多，而选择排序交换次数较少
     * @param arr
     * @return
     * @return int[]
     * @exception
     * @createTime：2017年7月11日
     * @author: Administrator
     */
    public static int[] selectSort(int[] arr){
        for(int i = 0;i < arr.length -1;i++){
            int min = i;
            for(int j = i+1;j < arr.length;j++){
                if(arr[min] > arr[j]){
                    min = j;
                }
            }
            if(min != i){
                int temp = arr[i];
                arr[i] = arr[min];
                arr[min] = temp;
            }

        }
        return arr;

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
            // 左边探索
            while(i<end && data[++i] <= base);
            // 右边探索
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
