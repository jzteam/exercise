package cn.jzteam.patterns.command;

public class ConcreteCommand1 implements Command{
	private Receiver receiver;
	// 设置接受者实例
	public void setReceiver(Receiver receiver){
		this.receiver = receiver;
	}
	@Override
	public void execute() {
		// 接受者干活
		receiver.doSomething1();
		receiver.doSomething2();
	}
}
