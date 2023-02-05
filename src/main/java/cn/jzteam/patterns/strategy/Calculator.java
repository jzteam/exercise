package cn.jzteam.patterns.strategy;

public enum Calculator {
	// 策略一：加法
	ADD("+"){
		public int exec(int a,int b){
			return a+b;
		}
	},
	// 策略二：减法
	SUB("-"){
		public int exec(int a,int b){
			return a-b;
		}
	};
	// 策略枚举的值
	private String value = "";
	private Calculator(String value){
		this.value = value;
	}
	public String getValue(){
		return this.value;
	}
	// 策略抽象方法
	public abstract int exec(int a,int b);
}
