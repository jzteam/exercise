package cn.jzteam.patterns.observe;

import java.util.Observable;

public class HanFeiZi extends Observable implements IHanFeiZi {

	@Override
	public void eat() {
		System.out.println("我是被观察对象，我要吃饭了！");
		
		// 通知观察者
		super.setChanged();
		super.notifyObservers();
	}

}
