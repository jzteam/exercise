package cn.jzteam.patterns.visitor;

public interface IVisitor {
	public void visit(ConcreteElement1 ele1);
	public void visit(ConcreteElement2 ele2);
}
