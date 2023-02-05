package cn.jzteam.core.grammar;

public class SwitchTest {
	
	public static void main(String[] args) {
		
		System.out.println(test1(1));
		
	}
	
	public static String test1(int type){
		switch(type){
		case 0:
			System.out.println("type=0");
			break;
		case 1:
			return "test";
		case 2:
			System.out.println("type=2");
			break;
		case 3:
			System.out.println("type=3");
			break;
		case 4:
			System.out.println("type=4");
			break;
		}
		
		return "all";
	}

}
