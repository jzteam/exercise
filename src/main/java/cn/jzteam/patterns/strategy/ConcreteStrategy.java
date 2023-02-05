package cn.jzteam.patterns.strategy;

public class ConcreteStrategy extends Strategy{
	@Override
	public void plan() {
		System.err.println("周日学习");
	}
}
