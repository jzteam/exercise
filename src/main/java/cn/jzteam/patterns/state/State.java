package cn.jzteam.patterns.state;

public abstract class State {
	// 环境对象
	protected Context context;
	public void setContext(Context context){
		this.context = context;
	}
	// 状态的切换方法
	public abstract void handle1();
	public abstract void handle2();
}
