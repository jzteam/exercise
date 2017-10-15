package cn.jzteam.tools.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class PhoneConsumeUtil {

	
	public static void main(String[] args) throws IOException {
		String path = "/Users/oker/Downloads/jzteam/phone_consume/";
		String filename = "2017";
		// filename+"共通话时长："+minCount+"分"+secCount+"秒");
		for(int i = 5;i<=10;i++){
			String time = filename + i;
			if(i < 10){
				time = filename + "0" + i;
			}
			System.out.println(time+"共通话时长："+get(path+time));
		}
	}
	
	private static String get(String path) throws IOException{
		int minCount = 0;
		int secCount = 0;
		
		FileInputStream fis = new FileInputStream(path);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String line = null;
		while((line = br.readLine()) != null){
			String[] split = line.split("\t");
			String time = split[4];
			//System.out.println(time);
			
			boolean contains = time.contains("分");
			if(contains){
				minCount += Integer.valueOf(time.substring(0, 2));
				secCount += Integer.valueOf(time.substring(3, 5));
			}else{
				secCount += Integer.valueOf(time.substring(0, 2));
			}
		}
		br.close();
		
		minCount += secCount / 60;
		secCount = secCount % 60;
		return minCount+"分"+secCount+"秒";
	}
}
