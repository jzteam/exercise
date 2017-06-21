package cn.jzteam.patterns.flyweight;

import java.util.HashMap;
import java.util.Map;

public class FlyweightFactory {
	// 池容器
	private static Map<String,Flyweight> pool = new HashMap<>();
	// 享元工厂
	public static Flyweight getFlyweight(String outer){
		Flyweight flyweight = null;
		if(pool.containsKey(outer)){
			flyweight = pool.get(outer);
		}else{
			flyweight = new Flyweight(outer);
			pool.put(outer, flyweight);
		}
		return flyweight;
	}
}
