package cn.jzteam.algorithm.sort_model;

/**
 * �鲢����
 * 
 * @author shkstart 
 * 2013-11-27
 */
public class MergeSort {
	public static void mergeSort(DataWrap[] data) {
		// �鲢����
		sort(data, 0, data.length - 1);
	}

	// ��������left��right��Χ������Ԫ�ؽ��й鲢����
	private static void sort(DataWrap[] data, int left, int right) {
		if(left < right){
			//�ҳ��м�����
			int center = (left + right)/2;
			sort(data,left,center);
			sort(data,center+1,right);
			//�ϲ�
			merge(data,left,center,right);
		}
	}

	// ������������й鲢���鲢ǰ���������Ѿ����򣬹鲢����Ȼ����
	private static void merge(DataWrap[] data, int left, int center, int right) {
		DataWrap[] tempArr = new DataWrap[data.length];
		int mid = center + 1;
		int third = left;
		int temp = left;
		while (left <= center && mid <= right) {
			if (data[left].compareTo(data[mid]) <= 0) {
				tempArr[third++] = data[left++];
			} else {
				tempArr[third++] = data[mid++];
			}
		}
		while (mid <= right) {
			tempArr[third++] = data[mid++];
		}
		while (left <= center) {
			tempArr[third++] = data[left++];
		}
		while (temp <= right) {
			data[temp] = tempArr[temp++];
		}
	}

	public static void main(String[] args) {
		DataWrap[] data = { new DataWrap(9, ""), new DataWrap(-16, ""),
				new DataWrap(21, "*"), new DataWrap(23, ""),
				new DataWrap(-30, ""), new DataWrap(-49, ""),
				new DataWrap(21, ""), new DataWrap(30, "*"),
				new DataWrap(30, "") };
		System.out.println("����֮ǰ��\n" + java.util.Arrays.toString(data));
		mergeSort(data);
		System.out.println("����֮��\n" + java.util.Arrays.toString(data));
	}
}
