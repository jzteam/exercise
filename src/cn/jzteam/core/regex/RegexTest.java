package cn.jzteam.core.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
	
	public static void main(String[] args) {
		
		test1();
		
	}
	
	private static void test1(){
		String reg = "(((0[1-9]|[12][0-9]|3[01])/((0[13578]|1[02]))|((0[1-9]|[12][0-9]|30)/(0[469]|11))|(0[1-9]|[1][0-9]|2[0-8])/(02))/([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3}))|(29/02/(([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00)))";
		String reg1 = "([0-9]{1,2})(/|-|\\s)+([0-9]{1,2})(/|-|\\s)+([0-9]{4})";
		Pattern pattern = Pattern.compile(reg1);
		Matcher matcher = pattern.matcher("25  06 - 2017");
		boolean matches = matcher.matches();
		System.out.println("匹配结果："+matches);
		int groupCount = matcher.groupCount();
		String group1 = matcher.group(1);
		String group2 = matcher.group(3);
		String group3 = matcher.group(5);
		
		System.out.println(groupCount+"_"+group1+"_"+group2+"_"+group3);
	}

}
