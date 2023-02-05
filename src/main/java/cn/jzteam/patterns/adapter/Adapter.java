package cn.jzteam.patterns.adapter;

public class Adapter extends Adaptee implements Target {
	// 通过源角色的方式，实现目标角色的方法，完成转换
	@Override
	public void request() {
		super.doSomething();
	}
}
