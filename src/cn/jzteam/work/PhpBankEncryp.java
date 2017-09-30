package cn.jzteam.work;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class PhpBankEncryp {
	
	public static void main(String[] args) {
//		String param = "HREMINTAGLOBEX0000050000000000001082520090525163000"; //506298198687727
		String param = "AREVALO, M1002201010039200294071USDPHP00000000182502501"; //174162126162692
		System.out.println(hashCompute(param));
	}
	
	public static String hashCompute(String param){
		
		if(StringUtils.isEmpty(param)){
			throw new RuntimeException("参数不能为空");
		}
		
		// 1.每5个字符一组
		int length = param.length();
		int size = (length-1)/5+1;
		List<String> charList = new ArrayList<>(size);
		int i = 0;
		// 遍历截取，都是5个字符的组
		while(i<size-1){
			String temp = param.substring(i++*5,i*5);
			charList.add(temp);
		}
		// 最后一组可能不足5个字符，单独截取
		charList.add(param.substring(i*5));
		
		// 2.每组将每个字符替换成3位的ASCII码
		List<String> numberList = new ArrayList<>(size);
		for (String charStr : charList) {
			StringBuilder sb = new StringBuilder();
			char[] charArray = charStr.toCharArray();
			for (char c : charArray) {
				// 都是ACSII码值，不会超过3位数
				String intStr = c + 1000 +"";
				sb.append(intStr.substring(1));
			}
			
			String numberStr = sb.toString();
			if(sb.length() < 15){
				// 最多缺12个0，补全到15位
				numberStr = sb.append("000000000000").substring(0, 15);
			}
			numberList.add(numberStr);
		}
		
		// 3.乘法：每组乘两个数，第一个是（1、3、7）中的一个，按组序号获取；第二个是上一个结果的最后一个数字（第一组用1;其他组碰到0用1）
		List<Long> resultList = new ArrayList<>(size);
		long[] mulArray = {1,3,7};
		for(int x = 0;x<size;x++){
			long mulFirst = mulArray[x%3];
			long mulSecond = 1;
			if(x > 0){
				// 取个位
				Long tempResult = resultList.get(x-1) % 10;
				mulSecond = tempResult == 0 ? 1 : tempResult;
			}
			long mul = Long.valueOf(numberList.get(x));
			
			// 相乘
			long result = mul * mulFirst * mulSecond;
			
			resultList.add(result);
		}
		
		// 4.将每一组的乘法结果相加，取最后15位数字
		long sum = 0;
		for (long rt : resultList) {
			sum += rt;
		}
		String tempHash = sum + "";
		int hashLength = tempHash.length();
		if(hashLength < 15){
			throw new RuntimeException("计算出错，结果不到15位");
		}
		String hash = tempHash.substring(tempHash.length()-15,hashLength);
		
		
		
		// 中间步骤结果
		charList.forEach(System.out::println);
		System.out.println();
		numberList.forEach(System.out::println);
		System.out.println();
		resultList.forEach(System.out::println);
		System.out.println();
		System.out.println("sum="+tempHash);
		System.out.println();
		
		
		
		// 5.返回
		return hash;
	}

}
