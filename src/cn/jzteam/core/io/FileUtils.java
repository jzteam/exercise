package cn.jzteam.core.io;


import java.io.File;
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

}
