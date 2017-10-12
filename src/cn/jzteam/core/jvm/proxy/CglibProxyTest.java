package cn.jzteam.core.jvm.proxy;

import java.lang.reflect.Method;

import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.Factory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.objenesis.SpringObjenesis;

public class CglibProxyTest {
	
	public static void main(String[] args) {
		
		test2();
	}
	
	// Spring 内部方式
	public static void test2(){
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(Doctor.class);
		enhancer.setUseCache(false);
		enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
		Class<?>[] types = new Class<?>[1];
		Callback[] callbacks = new Callback[1];
		callbacks[0] = new TestInterceptor();
		types[0] = TestInterceptor.class;
		enhancer.setCallbackTypes(types);
		
		Class<?> createClass = enhancer.createClass();
		Object newInstance = new SpringObjenesis().newInstance(createClass,false);
		
		((Factory) newInstance).setCallbacks(callbacks);
		
		System.out.println(newInstance.getClass().getName());
		System.out.println(newInstance instanceof Person);
		System.out.println("父类："+newInstance.getClass().getSuperclass());
		
		Person person = (Person)newInstance;
		
		person.eat();
	}
	
	// 常用方式
	public static void test1(){
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(Person.class);
		
		Person person = (Person)enhancer.create();
		
		person.eat();
	}
	

}
class Person {
	public void say(){
		System.out.println("我是person");
	}
	
	public void eat(){
		System.out.println("我在吃，但是我还要说");
		say();
	}
}

class Student extends Person{
	private int m;
	public int n;
	public void say(){
		System.out.println("我是学生");
	}
	
	public void eat(){
		System.out.println("我是学生，我在吃，但是我还要说");
		say();
	}
}

class Doctor extends Student{
	private int m;
//	public int n;
	public void say(){
		System.out.println("我是博士，父类的n="+n);
	}
	
	public void eat(){
		System.out.println("我是博士，我在吃，但是我还要说");
		say();
	}
}

class TestInterceptor implements MethodInterceptor{

	@Override
	public Object intercept(Object proxy, Method targetMethod, Object[] args, MethodProxy methodProxy) throws Throwable {
		System.out.println("开启事务proxy="+proxy.getClass().getName());
		System.out.println("methodProxyName="+methodProxy.getSuperName());
		System.out.println("targetMethodName="+targetMethod.getName()+","+targetMethod.getDeclaringClass().getName());
		// methodProxy是Doctor$$CGlibProxy$$1234时，
		// invoke 判断接收者是否是一种Doctor 
		// invokeSuper 判断接收者是否是一种Doctor$$CGlibProxy$$1234
		methodProxy.invokeSuper(new Student(), args);
		
		System.out.println("提交事务");
		return null;
	}
	
}