package cn.jzteam.patterns.observe;

import java.util.Observable;
import java.util.Observer;

public class LiSi implements Observer{

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("我是观察者，发现敌人动向，我要报告");
	}

}
