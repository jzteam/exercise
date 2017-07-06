package cn.jzteam.algorithm;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class DataFactory {
    
    public static void main(String[] args) {
        String path = "G:/study_test/algorithm/test.txt";
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)));
            
            long cnt = 10000000000L;
            while(--cnt > 0){
                double random = Math.random();
                System.out.println(random);
                //long data = (long)(random*10000000000L);
                String data = String.valueOf(random).substring(2, 12);
                bw.write(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bw != null){
                try {
                    bw.close();
                } catch (IOException e) {
                }
            }
        }
        System.out.println("写入结束");
    }

}
