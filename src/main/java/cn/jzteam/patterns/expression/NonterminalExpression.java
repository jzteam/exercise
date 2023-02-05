package cn.jzteam.patterns.expression;

public abstract class NonterminalExpression {
	// 加法、减法都是需要两个操作数，所以定义两个
	protected IExpression left;
	protected IExpression right;
	public NonterminalExpression(IExpression... expression){
		left = expression[0];
		right = expression[1];
	}
}
