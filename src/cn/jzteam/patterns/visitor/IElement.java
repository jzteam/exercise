package cn.jzteam.patterns.visitor;

public interface IElement {
	// 元素的业务逻辑
	public void doSomething();
	// 允许谁来访问
	public void accept(IVisitor visitor);
}
