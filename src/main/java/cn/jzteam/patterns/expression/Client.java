package cn.jzteam.patterns.expression;

public class Client {
	public static void main(String[] args) {
		// 公式
		String expStr = "a+b-c";
		// 实参
		Context context = new Context();
		context.put("a", 5L);
		context.put("b", 10L);
		context.put("c", 11L);
		// 获取公式表达式
		Calculator calculator = new Calculator(expStr);
		// 运算
		Long run = calculator.run(context);
		System.out.println(run);
	}
}
