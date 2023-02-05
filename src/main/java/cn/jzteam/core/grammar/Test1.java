package cn.jzteam.core.grammar;

import java.util.ArrayList;
import java.util.List;

public class Test1 {
	
	public static void main(String[] args) {
//		Integer i = 0;
//		long x = 0;
//		String s = "dd";
//		// 引用类型class的isInstance，一致则返回true
//		// 基本类型的，都返回false
//		System.out.println(String.class.isInstance(s));
//		boolean instance = Integer.class.isInstance(i);
//		System.out.println(instance);
//
//		System.out.println("=======");
//		// isPrimitive判断class是否是基本类型
//		System.out.println(int.class.isPrimitive()); // true
//		System.out.println(Integer.class.isPrimitive()); // false
		test1();
	}


	public static void test1(){
		List<Long> list = new ArrayList<>();
		for(int i =0;i<49;i++){
			list.add(Long.valueOf(i));
		}

		list.subList(0,49);

//		list.subList(0,50);

		int pos = 0;
		List<Long> result = new ArrayList<>();
		while(list.size() > (pos + 10)) {
			List<Long> subIds = list.subList(pos, pos+=10);
			result.addAll(subIds);
		}
		if(list.size() > pos){
			List<Long> subIds = list.subList(pos, list.size());
			list.addAll(subIds);
		}

		list.forEach(x->{
			System.out.println(x);
		});
	}

}
