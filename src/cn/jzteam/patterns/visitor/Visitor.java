package cn.jzteam.patterns.visitor;

public class Visitor implements IVisitor{
	@Override
	public void visit(ConcreteElement1 ele1) {
		System.out.println("我处理ele1的数据");
		ele1.doSomething();
	}
	@Override
	public void visit(ConcreteElement2 ele2) {
		System.out.println("我处理ele2的数据");
		ele2.doSomething();
	}
	
	public static void main(String[] args) {
		
	}
}
