package cn.jzteam.patterns.flyweight;

public class Flyweight {
	// 内部状态
	private String inner;
	// 外部状态，不可变
	private final String outer;
	
	public Flyweight(String outer){
		this.outer = outer;
	}
	public String getInner() {
		return inner;
	}
	public void setInner(String inner) {
		this.inner = inner;
	}
	public String getOuter() {
		return outer;
	}
}
