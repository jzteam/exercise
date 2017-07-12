package cn.jzteam.algorithm.sort_model;

import java.util.Arrays;

/**
 * ��������
 * 
 * @author shkstart 2013-11-27
 */
public class MultiKeyRadixSort {
	public static void radixSort(int[] data, int radix, int d) {
		System.out.println("��ʼ����");
		int arrayLength = data.length;
		int[] temp = new int[arrayLength];
		int[] buckets = new int[radix];
		for (int i = 0, rate = 1; i < d; i++) {
			// ����count���飬��ʼͳ�Ƶڶ����ؼ���
			Arrays.fill(buckets, 0);
			// ��data�����Ԫ�ظ��Ƶ�temp�����н��л���
			System.arraycopy(data, 0, temp, 0, arrayLength);
			for (int j = 0; j < arrayLength; j++) {
				int subKey = (temp[j] / rate) % radix;
				buckets[subKey]++;
			}
			for (int j = 1; j < radix; j++) {
				buckets[j] = buckets[j] + buckets[j - 1];
			}
			for (int m = arrayLength - 1; m >= 0; m--) {
				int subKey = (temp[m] / rate) % radix;
				data[--buckets[subKey]] = temp[m];
			}
			System.out.println("��" + rate + "λ���ӹؼ�������"
					+ java.util.Arrays.toString(data));
			rate *= radix;
		}
	}

	public static void main(String[] args) {
		int[] data = { 1100, 192, 221, 12, 13 };
		System.out.println("����֮ǰ��\n" + java.util.Arrays.toString(data));
		radixSort(data, 10, 4);
		System.out.println("����֮��\n" + java.util.Arrays.toString(data));
	}
}
