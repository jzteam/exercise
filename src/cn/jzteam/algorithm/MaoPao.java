package cn.jzteam.algorithm;

public class MaoPao {

    public static void main(String[] args) {

        int[] arr = { 9, 2, 7, 6, 23 };

        System.out.print("before:");
        print(arr);

        for (int i = arr.length - 1; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                if (arr[j] < arr[j + 1]) {
                    arr[j] = arr[j + 1] ^ arr[j];
                    arr[j + 1] = arr[j + 1] ^ arr[j];
                    arr[j] = arr[j + 1] ^ arr[j];
                }
            }
        }

        System.out.print("after:");
        print(arr);

    }

    private static void print(int[] arr) {

        if (arr == null) {
            System.out.println("arr==null");
        }

        for (int i : arr) {
            System.out.print(i + "  ");
        }

    }


}
