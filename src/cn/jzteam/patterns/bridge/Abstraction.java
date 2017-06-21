package cn.jzteam.patterns.bridge;

public abstract class Abstraction {
	private Implementor implementor;
	public Abstraction(Implementor implementor){
		this.implementor = implementor;
	}
	public void request(){
		this.implementor.doSomething();
	}
	public Implementor getImp(){
		return this.implementor;
	}
}
