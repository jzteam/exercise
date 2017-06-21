package cn.jzteam.patterns.expression;

import java.util.Stack;

public class Calculator {
	// 公式表达式
	private IExpression expression;
	// 解析公式，获取公式表达式
	public Calculator(String expStr){
		// 栈，计算时缓存用
		Stack<IExpression> stack = new Stack<>();
		// 公式拆解成字符数组
		char[] charArray = expStr.toCharArray();
		// 加减法使用的操作数
		IExpression left = null;
		IExpression right = null;
		// 遍历公式字符，进行封装表达式
		for (int i = 0; i < charArray.length; i++) {
			switch(charArray[i]){
			case '+'://加法
				left = stack.pop();
				right = new TerminalExpression(String.valueOf(charArray[++i]));
				// 把加法结果放进栈中
				stack.push(new AddExpression(left,right));
				break;
			case '-'://减法
				left = stack.pop();
				right = new TerminalExpression(String.valueOf(charArray[++i]));
				// 把减法结果放进栈中
				stack.push(new SubExpression(left,right));
				break;
			default:
				// 把公式中变量放进栈中
				stack.push(new TerminalExpression(String.valueOf(charArray[i])));
			}
		}
		//循环结束，把栈中的结果返回
		this.expression = stack.pop();
	}
	// 传入具体值，执行计算
	public Long run(Context ctx){
		return this.expression.interpreter(ctx);
	}
}
