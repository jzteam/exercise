package cn.jzteam.core.io;


import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileUtils {

	/**
	 * ʹ���߳�����ɶ��̸߳����ļ����ļ���
	 * @param src
	 * @param desc
	 */
	public static void multiThreadCopy(String src, String desc) {
		
		File file = new File(src);
		if (!file.exists()) {
			System.out.println(src + " not found");
			return;
		}
		File descFile = new File(desc);
		if (file.isFile()) {
			if(!descFile.exists()){
				String parent = descFile.getParent();
				File pFile = new File(parent);
				if(!pFile.exists()){
					pFile.mkdirs();
				}
			}
			long total = file.length();
			long temp = total / CopyRunnable.THREAD_SIZE;
			ExecutorService es = Executors.newFixedThreadPool(CopyRunnable.THREAD_SIZE);
			for (int i = 0; i < CopyRunnable.THREAD_SIZE; i++) {
				es.submit(new CopyRunnable(i * temp, (i + 1) * temp, src, desc), "thread " + i);
			}
		} else {
			if (!descFile.exists())
				descFile.mkdirs();
			String[] list = file.list();
			for (String path : list) {
				String desc1 = desc + "/" + path;
				System.out.println("desc1: " + desc1);
				multiThreadCopy(src+"/"+path, desc1);
			}
		}
	}

	/**
	 * 计算文件大小
	 * @param file
	 * @return
	 */
	public static long sizeOf(File file){
		try(FileInputStream fis = new FileInputStream(file)){
			return fis.available();
		}catch (IOException e){
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 把文件内容逐行拼接成字符串
	 * @param file
	 * @return
	 */
	public static String readFileToString(File file){
		StringBuilder sb = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))){
			String line;
			while((line = br.readLine()) != null)
				sb.append(line).append("\n");
			return sb.toString();
		}catch (IOException e){
			e.printStackTrace();
			return "";
		}
	}

}
