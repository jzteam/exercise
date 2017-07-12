package cn.jzteam.algorithm;

/**
 * 选择排序
 * @author Administrator
 *
 */
public class SelectSort {
	public static void main(String[] args) {
		int[] arr = new int[]{1, -2, 3, 10, -4, 7, 2, -5};
		arr = selectSort(arr);
		for(int i = 0;i < arr.length;i++){
			System.out.println(arr[i] + " ");
		}
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
}
