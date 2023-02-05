package cn.jzteam.patterns.command;

public class ConcreteReceiver extends Receiver{
	@Override
	public void doSomething1() {
		System.out.println("周六搬家");
	}
	@Override
	public void doSomething2() {
		System.out.println("周日学习");
	}
}
