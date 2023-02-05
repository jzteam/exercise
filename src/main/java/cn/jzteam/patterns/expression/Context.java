package cn.jzteam.patterns.expression;

import java.util.HashMap;
import java.util.Map;

public class Context {
	Map<String,Long> map = new HashMap<>();
	public Long get(String key){
		Long value = map.get(key);
		return  value == null ? 0L : value;
	}
	public void put(String key, Long value){
		map.put(key, value);
	}
}
