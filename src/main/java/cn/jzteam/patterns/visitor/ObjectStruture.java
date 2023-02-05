package cn.jzteam.patterns.visitor;

import java.util.ArrayList;
import java.util.List;

public class ObjectStruture {
	// 构造元素集合
	public List<IElement> getList(){
		List<IElement> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			if(i%2 == 0){
				list.add(new ConcreteElement1());
			}else{
				list.add(new ConcreteElement2());
			}
		}
		return list;
	}
	
	public static void main(String[] args) {
		ObjectStruture objectStruture = new  ObjectStruture();
		List<IElement> list = objectStruture.getList();
		Visitor visitor = new Visitor();
		for (IElement ele : list) {
			ele.accept(visitor);
		}
	}
}
