package cn.jzteam.patterns.bridge;

public class ConcreteImplementor implements Implementor{
	@Override
	public void doSomething() {
		System.out.println("我会做something");
	}
	@Override
	public void doAnything() {
		System.out.println("我会做anything");
	}
}
