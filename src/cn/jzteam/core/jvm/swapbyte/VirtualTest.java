package cn.jzteam.core.jvm.swapbyte;

public class VirtualTest extends Base{
	
	public void say(){
		super.say();
		System.out.println("VirtualTest  say");
	}
	
	public void eat(){
		System.out.println("VirtualTest  eat");
	}
	
	public static void main(String[] args) {
		VirtualTest test = new VirtualTest();
		test.say();
		
	}

}
class Base extends Gao{
	public void say(){
		System.out.println("Base  say");
	}
	
	public void eat(){
		System.out.println("Base  eat");
	}
	
}
class Gao {
	public void say(){
		System.out.println("Gao  say");
	}
	
	public void eat(){
		System.out.println("Gao  eat");
	}
}
