package cn.jzteam.patterns.visitor;

public class ConcreteElement2 implements IElement{
	@Override
	public void doSomething() {
		System.out.println("element2的业务逻辑");
	}
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
}
