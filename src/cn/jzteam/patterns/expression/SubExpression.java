package cn.jzteam.patterns.expression;

public class SubExpression extends NonterminalExpression implements IExpression{
	// 构造器
	public SubExpression(IExpression left,IExpression right) {
		super(left,right);
	}
	// 对终结符进行减法运算
	@Override
	public Long interpreter(Context ctx) {
		return super.left.interpreter(ctx) - super.right.interpreter(ctx);
	}
}
