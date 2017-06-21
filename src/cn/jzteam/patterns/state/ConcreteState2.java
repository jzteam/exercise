package cn.jzteam.patterns.state;

public class ConcreteState2 extends State{
	@Override
	public void handle1() {
		// 设置当前状态为state1
		super.context.setCurrentState(Context.STATE1);
		super.context.handle1();
	}
	@Override
	public void handle2() {
		// 本状态要处理的逻辑
	}
}
