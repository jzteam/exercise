package cn.jzteam.patterns.decorator;

public class ConcreteDecorator extends Decorator{
	// 具体装饰者，设置被装饰对象
	public ConcreteDecorator(Component component) {
		super(component);
	}
	// 该装饰者的装饰方法1
	private void decorate1(){
		System.out.println("吊顶");
	}
	// 该装饰者的装饰方法2
	private void decorate2(){
		System.out.println("打仿瓷");
	}
	// 覆盖operate方法，完成装饰
	@Override
	public void operate(){
		this.decorate1();
		this.decorate2();
		super.operate();
	}
}
