package cn.jzteam.algorithm;

public class BucketSort {
	
	
	public static void main(String[] args) {
		int[] data = new int[]{123,124,4,125,128,120,126,124,124,4,4,4,40};
		
		basket(data);
		
		for (int i : data) {
			System.out.print(i+",");
		}
		System.out.println();
	}

	public static void basket(int data[])// data为待排序数组
	{
		int n = data.length;
		int bask[][] = new int[10][n];
		int index[] = new int[10];
		
		int max = Integer.MIN_VALUE;
		// 获取数组中所有元素中的最大位数
		for (int i = 0; i < n; i++) {
			max = max > (Integer.toString(data[i]).length()) ? max : (Integer.toString(data[i]).length());
		}
		
		String str;
		for (int i = max - 1; i >= 0; i--) {
			
			for (int j = 0; j < n; j++) {
				str = "";
				// 给不足max位的元素填补0
				if (Integer.toString(data[j]).length() < max) {
					for (int k = 0; k < max - Integer.toString(data[j]).length(); k++)
						str += "0";
				}
				str += Integer.toString(data[j]);
				
				// i位上的数字，相同则放在同一列上，列上的位置，保持原有排序
				bask[str.charAt(i) - '0'][index[str.charAt(i) - '0']++] = data[j];
			}
			
			// 将二维数组中的元素依次放进data中，相当于data中的顺序重组一遍
			int pos = 0;
			for (int j = 0; j < 10; j++) {
				for (int k = 0; k < index[j]; k++) {
					data[pos++] = bask[j][k];
				}
			}
			
			// 清空辅助数组index
			for (int x = 0; x < 10; x++)
				index[x] = 0;
		}
	}

}
