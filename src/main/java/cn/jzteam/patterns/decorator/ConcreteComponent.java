package cn.jzteam.patterns.decorator;

public class ConcreteComponent implements Component {
	@Override
	public void operate() {
		System.out.println("打扫卧室");
	}
}
