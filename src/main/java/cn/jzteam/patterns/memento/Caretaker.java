package cn.jzteam.patterns.memento;

public class Caretaker {
	// 保存一个备忘录
	private IMemento memento;
	public IMemento getMemento() {
		return memento;
	}
	public void setMemento(IMemento memento) {
		this.memento = memento;
	}
}
