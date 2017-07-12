package cn.jzteam.base.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class IOTest {

    public static void main(String[] args) throws IOException {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\zhujz\\桌面\\test\\data.txt")));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:\\zhujz\\桌面\\test\\ids.txt")));
            String line = "";
            while ((line = br.readLine()) != null) {
                bw.write(line);
                bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null)
                br.close();
            if (bw != null)
                bw.close();
        }

        System.out.println("结束");
    }
}
