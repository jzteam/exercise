package cn.jzteam.patterns.expression;

public class TerminalExpression implements IExpression{
	// 变量名，可以从环境中取得操作数
	private String key;
	public TerminalExpression(String key){
		this.key = key;
	}
	// 终结符的实现方法就是取得操作数
	@Override
	public Long interpreter(Context ctx) {
		return ctx.get(key);
	}
}
