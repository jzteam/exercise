package cn.jzteam.core.grammar;

public class Test1 {
	
	public static void main(String[] args) {
		Integer i = 0;
		long x = 0;
		String s = "dd";
		// 引用类型class的isInstance，一致则返回true
		// 基本类型的，都返回false
		System.out.println(String.class.isInstance(s));
		boolean instance = Integer.class.isInstance(i);
		System.out.println(instance);
		
		System.out.println("=======");
		// isPrimitive判断class是否是基本类型
		System.out.println(int.class.isPrimitive()); // true
		System.out.println(Integer.class.isPrimitive()); // false
	}

}
