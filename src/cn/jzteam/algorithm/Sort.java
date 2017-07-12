package cn.jzteam.algorithm;

import java.util.Arrays;

import cn.jzteam.algorithm.sort_model.DataWrap;

/**
 * 排序常用算法
 * @author Administrator
 *
 */
public class Sort {
    
    public static void main(String[] args) {
        
        Integer[] data = {23,45,-2,33,42,2,5,77,3,3,5};
        printData(data);
        // 选择排序
//        selectSort(data);
        // 冒泡排序
//        bubbleSort(data);
        // 快速排序
//        quickSort(data,0,data.length-1);
        
        // 插入排序
//        insertSort(data);
        
        // 折半插入排序
        binaryInsertSort(data);
        
        
        
        
//        shellSort(data);
        
        printData(data);
    }
    
    public static void printData(Comparable<?>[] data){
        for (Comparable<?> comparable : data) {
            System.out.print(comparable.toString()+",");
        }
        System.out.println();
    }
    
    // 交换元素
    public static void swap(Comparable<?>[] data,int i,int j){
        Comparable<?> temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }
    
    // 选择排序
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static  void selectSort(Comparable[] data){
        
        for(int i = 0; i<data.length; i++){
            int min = i;
            
            for(int j = i+1; j<data.length; j++){
                if(data[j].compareTo(data[min]) < 0){
                    min = j;
                }
            }
            
            if(min != i){
                swap(data,i,min);
            }
        }
        
    }
    
    // 冒泡排序
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void bubbleSort(Comparable[] data){
        
        for(int i = data.length - 1;i > 0;i--){
            
            for(int j = 0;j<i;j++){
                if(data[j].compareTo(data[j+1]) > 0){
                    swap(data,j,j+1);
                }
            }
            
        }
        
    }
    
    // 快速排序
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static  void quickSort(Comparable[] data,int start,int end){
        
        if(end <= start){
            return;
        }
        
        int i = start;
        int j = end + 1;
        Comparable base = data[i];
        
        while(true){
            
            while(i<end && data[++i].compareTo(base) <= 0);
            
            while(j>start && data[--j].compareTo(base) >= 0);
            
            if(j > i){
                swap(data,i,j);
            }else{
                break;
            }
            
        }
        
        swap(data,start,j);
        
        quickSort(data,start,j-1);
        
        quickSort(data,i,end);
        
    }
    
    // 插入排序
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void insertSort(Comparable[] data){
        for(int i = 1;i<data.length;i++){
            int j = i - 1;
            Comparable temp = data[i];
            while(j>= 0 && data[j].compareTo(temp) > 0){
                data[j+1] = data[j];
                j--;
            }
            data[j+1] = temp;
        }
    }
    
    // 折半插入排序
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void binaryInsertSort(Comparable[] data){
        for(int i = 1;i<data.length;i++){
            Comparable temp = data[i];
            int low = 0;
            int high = i -1;
            while(high>=low){
                int mid = (low + high)/2;
                if(data[mid].compareTo(temp) == 0){
                    low = mid + 1;
                    break;
                }else if(data[mid].compareTo(temp) < 0){
                    low = mid + 1;
                }else{
                    high = mid - 1;
                }
            }
            for(int j = i;j>low;j--){
                data[j] = data[j-1];
            }
            data[low] = temp;
        }
    }
    
    //?????
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void shellSort(Comparable[] data){
        int arrayLength = data.length;
        int h = 1;
        while (h <= arrayLength / 3) {
            h = h * 3 + 1;
        }
        while (h > 0) {
            for (int i = h; i < arrayLength; i++) {
                Comparable temp = data[i];
                if (data[i].compareTo(data[i - h]) < 0) {
                    int j = i - h;
                    for (; j >= 0 && data[j].compareTo(temp) > 0; j -= h) {
                        data[j + h] = data[j];
                    }
                    data[j + h] = temp;
                }
                System.out.println(java.util.Arrays.toString(data));
            }
            h = (h - 1) / 3;
        }
    }
    
    
    /**
     * 可比较的对象
     * @author Administrator
     *
     */
    class DataWrap implements Comparable<DataWrap>{
        int data;
        String flag;
        public DataWrap(int data,String flag){
            this.data = data;
            this.flag = flag;
        }
        public String toString(){
            return data + flag;
        }
        @Override
        public int compareTo(DataWrap dw) {
            return this.data > dw.data? 1 : (this.data == dw.data? 0 : -1);
        }
    }
}
