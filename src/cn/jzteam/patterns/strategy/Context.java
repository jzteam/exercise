package cn.jzteam.patterns.strategy;

public class Context {
	// 封装策略
	private Strategy strategy;
	// 构造器中设置策略
	public Context(Strategy strategy){
		this.strategy = strategy;
	}
	// 执行策略
	public void execute(){
		strategy.plan();
	}
}
