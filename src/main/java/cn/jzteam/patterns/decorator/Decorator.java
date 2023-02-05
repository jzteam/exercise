package cn.jzteam.patterns.decorator;

public abstract class Decorator implements Component{
	// 被装饰对象
	private Component component;
	// 设置被装饰对象
	public Decorator(Component component){
		this.component = component;
	}
	// 实现Component，完成is-a关系
	@Override
	public void operate(){
		component.operate();
	}
}
