package cn.jzteam.work;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class PhpBankEncryp {
	
	private static final String PARTNER_CODE = "INTAGLOBEX";
	
	public static void main(String[] args) throws IOException {
//		String param = "HREMINTAGLOBEX0000050000000000001082520090525163000"; //506298198687727
//		String param = "AREVALO, M1002201010039200294071USDPHP00000000182502501"; //174162126162692
//		System.out.println(hashCompute(param));
		
		List<PhpBankParamBO> data = makeData(9);
		makeFile(data);
		System.out.println("写入完成");
		
	}
	
	public static List<PhpBankParamBO> makeData(int num){
		
		List<PhpBankParamBO> list = new ArrayList<>();
		for(int i = 0;i<num;i++){
			PhpBankParamBO bo = new PhpBankParamBO();
			bo.setTransactionDate(new Date());
			bo.setApplicationNumber("1234564321");
			bo.setRemitterLastName(i+"jizhi");
			bo.setBeneficiaryLastName(i+"jzteam_weixin_qq_momo_linkin");
			bo.setFundingAmount(11.22);
			bo.setFundingCurrency("PHP");
			bo.setSettlementAmount(22.11);
			bo.setSettlementCurrency("USD");
			bo.setSettlementMode("01");
			bo.setBankCode("CBC");
			bo.setBankName("CHINA BANKING CORPORATION");
			bo.setAccountNumber("1889999");
			
			list.add(bo);
		}
		
		return list;
	}
	
	/**
	 * 写入文件
	 * @param list
	 * @throws IOException
	 */
	public static void makeFile(List<PhpBankParamBO> list) throws IOException{
		// 简单验证
		if(CollectionUtils.isEmpty(list)){
			throw new RuntimeException("路径和list不能为空");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
		String format = sdf.format(new Date());
		String filename = PARTNER_CODE +format+"001.txt";
		String path = "/Users/oker/Documents/work/other/"+filename;
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)));
		
		// 遍历list计算总金额
		BigDecimal totalAmount = BigDecimal.valueOf(0.00D);
		for (PhpBankParamBO param : list) {
			// 累加金额:暂时使用settlementAmount
			totalAmount = totalAmount.add(BigDecimal.valueOf(param.getSettlementAmount()));
		}
		
		// 获取header record
		String headerRecord = makeHeaderRecord(list.size(), totalAmount);
		bw.write(headerRecord);
		bw.newLine();
		
		long totalHash = 0L;
		// 遍历list写入detail record
		for (PhpBankParamBO param : list) {
			// 获取 detail record
			String makeDetailRecord = makeDetailRecord(param);
			bw.write(makeDetailRecord);
			bw.newLine();
			
			// 累加hash
			totalHash += Long.valueOf(param.getHash());
		}
		
		// 写入最后一行的总hash
		String makeTotalHashRecord = makeTotalHashRecord(totalHash+"");
		bw.write(makeTotalHashRecord);
		bw.flush();
		bw.close();
		
	}
	
	private static String makeTotalHashRecord(String totalHash){
		if(totalHash  == null){
			throw new RuntimeException("total hash 不能为空");
		}
		
		if(totalHash.length() < 15){
			totalHash = "00000000000"+totalHash;
		}
		
		return "THASH"+totalHash.substring(totalHash.length() - 15,totalHash.length());
	}
	
	private static String makeHeaderRecord(int size,BigDecimal totalAmount){
		// Record Type : H
		
		// Data Center : REM
		
		// Business Partner Code : INTAGLOBEX
		
		// Total Count : 交易记录数，5位
		String count = "0000" + size;
		count = count.substring(count.length()-5, count.length());
		
		// Total Amount : 交易总金额，17位
		BigDecimal setScale = totalAmount.setScale(2, RoundingMode.DOWN);
		String replace = "0000000000000" + setScale.toString().replace(".", "");
		String amount = replace.substring(replace.length() - 17,replace.length());
		
		// Transmission Date : 文件发送日期yyyyMMdd
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = sdf.format(date);
		String transmissionDate = dateStr.substring(0, 8);
		
		// Transmission Time : 文件发送时间HHmmss
		String transmissionTime = dateStr.substring(8);
		
		// hash
		String hashParamStr = "HREM"+PARTNER_CODE+count+amount+dateStr;
		String hash = hashCompute(hashParamStr);
		
		StringBuilder sb = new StringBuilder();
		sb.append("H|REM|").append(PARTNER_CODE).append("|")
			.append(count).append("|")
			.append(amount).append("|")
			.append(transmissionDate).append("|")
			.append(transmissionTime).append("|")
			.append(hash);
		
		return sb.toString();
	}
	
	private static String makeDetailRecord(PhpBankParamBO param){
		// 验证
		validateParam(param);
		
		// 拼串
		// Beneficiary Last Name
		String beneficiaryLastName = param.getBeneficiaryLastName();
		if(beneficiaryLastName.length() > 10){
			beneficiaryLastName = beneficiaryLastName.substring(0, 10);
		}
		
		// Transaction Date
		Date transactionDate = param.getTransactionDate();
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
		String dateFormat = sdf.format(transactionDate);
		
		// Settlement Amount
		String settlementAmountStr = "";
		if(param.getSettlementAmount() != null){
			BigDecimal decimal = BigDecimal.valueOf(param.getSettlementAmount());
			BigDecimal setScale = decimal.setScale(2, RoundingMode.DOWN);
			String replace = "00000000000000" + setScale.toString().replace(".", "");
			// 15位
			settlementAmountStr = replace.substring(replace.length() - 15, replace.length());
		}
		
		// 拼接
		StringBuilder hashStringSb = new StringBuilder();
		hashStringSb.append(beneficiaryLastName)
			.append(dateFormat)
			.append(param.getApplicationNumber())
			.append(param.getFundingCurrency() == null?"":param.getFundingCurrency())
			.append(param.getSettlementCurrency() == null?"":param.getSettlementCurrency())
			.append(settlementAmountStr)
			.append(param.getSettlementMode());
		
		// 计算hash
		String hash = hashCompute(hashStringSb.toString());
		param.setHash(hash);
		
		// 拼接record
		StringBuilder sb = new StringBuilder();
		
		// Record Type
		sb.append("D|");
		sb.append(dateFormat).append("|");
		//Application Number
		sb.append(param.getApplicationNumber()).append("|");
		//remitterLastName
		sb.append(param.getRemitterLastName()).append("|");
		// beneficiaryLastName
		sb.append(param.getBeneficiaryLastName()).append("|");
		
		if(param.getFundingCurrency() != null){
			sb.append(param.getFundingCurrency()).append("|");
		}
		if(param.getFundingAmount() != null){
			sb.append(param.getFundingAmount()).append("|");
		}
		if(param.getSettlementCurrency() != null){
			sb.append(param.getSettlementCurrency()).append("|");
		}
		if(settlementAmountStr != ""){
			sb.append(settlementAmountStr).append("|");
		}
		sb.append(param.getSettlementMode()).append("|");
		
		if(!StringUtils.isEmpty(param.getBankCode())){
			sb.append(param.getBankCode()).append("|");
		}
		if(!StringUtils.isEmpty(param.getBankName())){
			sb.append(param.getBankName()).append("|");
		}
		if(!StringUtils.isEmpty(param.getAccountNumber())){
			sb.append(param.getAccountNumber()).append("|");
		}
		
		if(!StringUtils.isEmpty(param.getGoldCardNumber())){
			sb.append(param.getGoldCardNumber()).append("|");
		}
		
		if(!StringUtils.isEmpty(param.getOutletCode())){
			sb.append(param.getOutletCode()).append("|");
		}
		
		sb.append(hash);
		return sb.toString();
	}
	
	
	/**
	 * 参数非空校验
	 * @param param
	 */
	private static void validateParam(PhpBankParamBO param){
		if(param == null){
			throw new RuntimeException("param不能为空");
		}
		if(param.getTransactionDate() == null){
			throw new RuntimeException("transactionDate不能为空");
		}
		if(StringUtils.isEmpty(param.getApplicationNumber())){
			throw new RuntimeException("applicationNumber不能为空");
		}
		if(StringUtils.isEmpty(param.getRemitterLastName())){
			throw new RuntimeException("remitterLastName不能为空");
		}
		if(StringUtils.isEmpty(param.getBeneficiaryLastName())){
			throw new RuntimeException("beneficiaryLastName不能为空");
		}
		if(param.getFundingAmount() == null){
			throw new RuntimeException("fundingAmount不能为空");
		}
		if(StringUtils.isEmpty(param.getFundingCurrency())){
			throw new RuntimeException("fundingCurrency不能为空");
		}
		if(param.getSettlementAmount() == null){
			throw new RuntimeException("settlementAmount不能为空");
		}
		if(StringUtils.isEmpty(param.getSettlementCurrency())){
			throw new RuntimeException("settlementCurrency不能为空");
		}
		if(StringUtils.isEmpty(param.getSettlementMode())){
			throw new RuntimeException("settlementMode不能为空");
		}
		
		if("01".equals(param.getSettlementMode())){
			if(StringUtils.isEmpty(param.getAccountNumber())){
				throw new RuntimeException("accountNumber不能为空");
			}
			if(!"CBC".equals(param.getBankCode())){
				throw new RuntimeException("the fixed value of ‘CBC’");
			}
			if(!"CHINA BANKING CORPORATION".equals(param.getBankName())){
				throw new RuntimeException("the fixed value of ‘CHINA BANKING CORPORATION’");
			}
		}else if("02".equals(param.getSettlementMode())){
			if(StringUtils.isEmpty(param.getAccountNumber())){
				throw new RuntimeException("accountNumber不能为空");
			}
			if(StringUtils.isEmpty(param.getBankCode())){
				throw new RuntimeException("bankCode不能为空");
			}
			if(StringUtils.isEmpty(param.getBankName())){
				throw new RuntimeException("bankName不能为空");
			}
		}else if("03".equals(param.getSettlementMode())){
			// 没有额外字段
		}else if("08".equals(param.getSettlementMode())){
			if(!"ML".equals(param.getOutletCode()) && !"CEBUANA".equals(param.getOutletCode())){
				throw new RuntimeException("Currently available Outlet Partners are ML and CEBUANA");
			}
		}else if("11".equals(param.getSettlementMode())){
			if(StringUtils.isEmpty(param.getGoldCardNumber())){
				throw new RuntimeException("goldCardNumber不能为空");
			}
		}else{
			throw new RuntimeException("settlementMode错误");
		}
	}
	
	/**
	 * 根据字符串生成hash值
	 * @param param
	 * @return
	 */
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
//		charList.forEach(System.out::println);
//		System.out.println();
//		numberList.forEach(System.out::println);
//		System.out.println();
//		resultList.forEach(System.out::println);
//		System.out.println();
//		System.out.println("sum="+tempHash);
//		System.out.println();
		
		
		
		// 5.返回
		return hash;
	}

}
