package cn.jzteam.patterns.bridge;

public class RefinedAbstraction extends Abstraction{
	public RefinedAbstraction(Implementor implementor) {
		super(implementor);
	}
	// 真正使用的方法
	public void request(){
		super.request();
		super.getImp().doAnything();
		System.out.println("我修正了父类行为");
	}
}
