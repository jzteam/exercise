package cn.jzteam.core.jvm.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxyTest implements InvocationHandler{
	
	private Object obj;
	// 准备阶段，a已经初始为0了，静态初始化的时候执行static{}
	static {
		System.out.println("前置static，赋值之前："+JdkProxyTest.a);
		a = 4;
		System.out.println("前置static，赋值之后："+JdkProxyTest.a);
	}
	
	private final static int a;
	static{
		System.out.println("a="+a);
	}
	
	public void setObj(Object obj) {
		this.obj = obj;
	}

	public Object getProxy(){
		System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
		return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		try {
			System.out.println("我是jdk代理前置方法");
			method.invoke(obj, args);
			System.out.println("我是jdk代理后置方法");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		JdkProxyTest test = new JdkProxyTest();
		test.setObj(new Son());
		PersonInterface p = (PersonInterface)test.getProxy();
		p.say();
	}

}
interface PersonInterface{
	public void say();
	public void eat();
}
class Son implements PersonInterface{
	public void say(){
		System.out.println("我在say");
		eat();
	}
	
	public void eat(){
		System.out.println("我在eat");
	}
}

