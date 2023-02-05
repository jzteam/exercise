package cn.jzteam.patterns.mediator;

public abstract class Mediator {
	// 同事对象
	protected ConcreteColleague1 colleague1;
	protected ConcreteColleague2 colleague2;
	// 注入（可以通过注解，就不用setter/getter）
	public ConcreteColleague1 getColleague1() {
		return colleague1;
	}
	public void setColleague1(ConcreteColleague1 colleague1) {
		this.colleague1 = colleague1;
	}
	public ConcreteColleague2 getColleague2() {
		return colleague2;
	}
	public void setColleague2(ConcreteColleague2 colleague2) {
		this.colleague2 = colleague2;
	}
	// 中介者的逻辑处理
	public abstract void doSomething1();
	public abstract void dosomething2();
	
}
