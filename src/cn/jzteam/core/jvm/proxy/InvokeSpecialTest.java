package cn.jzteam.core.jvm.proxy;

public class InvokeSpecialTest {
	
	public static void main(String[] args) {
		Parent sub = new Sub();
		System.out.println(sub.get());
	}

}
class Parent{
	private static int a = 1;
	public int get(){
		return a;
	}
}
class Sub extends Parent{
	private int a = 2;
	public int get(){
		super.get();
		return a;
	}
	
	public void test(){
		// 都是 aload_0  即this
		super.get();// invokespecial Method cn/jzteam/core/jvm/proxy/Parent.get:()I
		((Parent)this).get();// invokevirtual Method cn/jzteam/core/jvm/proxy/Parent.get:()I
		// 第一个编译期就确定是父类的代码引用了，传入对象是this而已
		// 第二个在运行期才解析，得到的是Sub的代码引用
	}
}