package cn.jzteam.work;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.jzteam.utils.JsonUtil;

public class OtherTest {
	
	public static void main(String[] args) throws IOException {
		String path = "/Users/oker/Documents/work/改造/list.txt";
		String path2 = "/Users/oker/Documents/work/改造/list2.txt";
		
		List<String> list = loadTxt(path);
		List<String> list2 = loadTxt(path2);
		
		System.out.println("===============线上环境多的");
		List<String> more = new ArrayList<>();
		for (String remote : list) {
			if(!list2.contains(remote)){
//				more.add(remote);
				System.out.println(remote);
			}
		}
		System.out.println("===============测试环境多的");
		List<String> more1 = new ArrayList<>();
		for (String local : list2) {
			if(!list.contains(local)){
				more.add(local);
			}
		}
//		System.out.println("list中多出字段："+JsonUtil.toJson(more));
//		System.out.println("list2中多出字段："+JsonUtil.toJson(more1));
	}
	
	private static List<String> loadTxt(String path) throws IOException{
		
		FileInputStream file = new FileInputStream(path);
		BufferedReader br = new BufferedReader(new InputStreamReader(file));
		String line = null;
		List<String> list = new ArrayList<>();
		while((line = br.readLine()) != null){
			list.add(line.trim());
		}
		System.out.println("从"+path+"解析出"+list.size()+"条");
		return list;
	}

}
