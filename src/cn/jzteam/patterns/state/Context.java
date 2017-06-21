package cn.jzteam.patterns.state;

public class Context {
	// 定义一共有几个状态
	public static final State STATE1 = new ConcreteState1();
	public static final State STATE2 = new ConcreteState2();
	// 持有当前状态
	private State currentState;
	// 状态的所有方法（本身逻辑方法和切换其他状态逻辑方法）
	public void handle1(){
		this.currentState.handle1();
	}
	public void handle2(){
		this.currentState.handle2();
	}
	// 当前状态的getter/setter
	public State getCurrentState() {
		return currentState;
	}
	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}
}
