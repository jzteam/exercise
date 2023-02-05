package cn.jzteam.patterns.memento;

public class Originator {
	// 发起人的内部状态
	private String state = "";
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	// 创建一个备忘录
	public IMemento createMemento(){
		return new Memento(this.state);
	}
	// 恢复一个备忘录
	public void restoreMemento(IMemento memento){
		this.setState(((Memento)memento).getState());
	}
	// 内置类，备忘录对外完全封闭
	private class Memento implements IMemento{
		private String state = "";
		private Memento(String state){
			this.state = state;
		}
		private String getState(){
			return this.state;
		}
		private void setState(String state){
			this.state = state;
		}
	}
}
