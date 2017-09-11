package cn.jzteam.core.io;


public class Test {

	public static void main(String[] args) {
		/*String path = "E:/test";
		File file = new File(path);
		System.out.println(file.getParent());
		boolean b = file.mkdirs();
		System.out.println("����" + path + "  " + b);*/
		
		String src = "G:/��Ӱ/������_bd.mp4";
		String desc = "F:/test/my.mp4";
		FileUtils.multiThreadCopy(src, desc);
		
	}

}
