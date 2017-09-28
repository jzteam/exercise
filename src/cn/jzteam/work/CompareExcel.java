package cn.jzteam.work;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import cn.jzteam.algorithm.LevenshteinUtil;

public class CompareExcel {
	
	private static Map<String,String> mysqlKeyMap = new HashMap<>();
	private static Map<String,String> custKeyMap = new HashMap<>();
	
	// 用于查询单个主行的增量记录
	private static Map<String,String> custName2key = new HashMap<>();
	// 数据库中银行结构：主行key -> 支行list
	private static Map<String,List<String>> mysqlKey2all = new HashMap<>();
	// 客户xls的银行结构：主行key -> 支行list
	private static Map<String,List<String>> custKey2all = new HashMap<>();
		
	
	
	
	public static void main(String[] args) throws Exception {
		String mysqlPath = "/Users/oker/Documents/work/other/sqlresult_1834301.xlsx";
		String custPath = "/Users/oker/Documents/work/other/Nepal Updated Bank List.xls";
		String resultPath = "/Users/oker/Documents/work/other/method2.xlsx";
		// 方案一
//		methodFirst();
		
		// 方案二
		Map<String, List<String>> handleFromMysql = handleFromMysql(mysqlPath);
		methodSecond(custPath,handleFromMysql,resultPath);
	
	}
	
	/** 
	 * 方案一：输出txt
	 * @throws Exception
	 */
	public static void methodFirst() throws Exception{
		String mysqlPath = "/Users/oker/Documents/work/other/sqlresult_1834301.xlsx";
		String custPath = "/Users/oker/Documents/work/other/Nepal Updated Bank List.xls";
		
		// 解析
		Map<String, List<String>> handleFromMysql = handleFromMysql(mysqlPath);
		Map<String, List<String>> handleFromCust = handleFromCust(custPath);
		
		// 遍历map比对
		Set<Entry<String, List<String>>> custEntrySet = handleFromCust.entrySet();
		for (Entry<String, List<String>> entry : custEntrySet) {
			List<String> listCust = entry.getValue();
			String key = entry.getKey();
			
			List<String> listMysql = handleFromMysql.get(key);
			
			if(CollectionUtils.isEmpty(listMysql)){
				//System.out.println("========> cust新增主行，key="+key);
				// 主行对不上，详细分析
				
				Set<String> mysqlMasterKeySet = handleFromMysql.keySet();
				// 打印匹配度最高的主行
				handleMasterSmiliar(key,mysqlMasterKeySet);
				
				continue;
			}
			
			// 比对list
			compareList(listCust, listMysql);
			
		}
		
		
		// 打印 不同
		String result = "/Users/oker/Documents/work/other/result.txt";
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(result)));
		int num = 0;
		for (Entry<String, List<String>> entry : custEntrySet) {
			List<String> value = entry.getValue();
			for (String str : value) {
				try {
					bw.write(num++ + "  " + str);
					bw.newLine();
				} catch (Exception e) {
					System.out.println("bw出错");
				}
			}
		}
		bw.close();
	}
	
	/**
	 * 方案二：输出cust.xlsx，增量的记录使用黄色背景
	 * @param path
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static void methodSecond(String path,Map<String,List<String>> map,String resultPath) throws Exception{
		
        InputStream input = new FileInputStream(path);
        // 读取一个工作簿
        Workbook workbook = WorkbookFactory.create(input);
        // 获取当前Sheet页的工作表【只支持一个Sheet页】
        Sheet sheet = workbook.getSheetAt(0);
        // 获取工作表的总行数
        int rowCount = sheet.getPhysicalNumberOfRows();
        
        // 定义增量的记录样式：相同的style都是同一个对象，改一个对象就是改了所有cell的样式
        // 设置背景色
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
		cellStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);// 单元格上边框
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);// 单元格下边框
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);// 单元格左边框
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);// 单元格右边框
//		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 单元格水平居中
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 单元格垂直居中
		
		
		// 数据库中的主行key集合
		Set<String> mysqlKeySet = map.keySet();
		
        
        // 从第startRowIndex行开始，第1行是标题栏
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
        	// 行
            Row currentRow = sheet.getRow(rowIndex);
            
            // masterName
            String masterName = currentRow.getCell(1).getStringCellValue().trim();
            // branchName
            String branchName = currentRow.getCell(2).getStringCellValue().trim();
            
           // 主行作为key（统一去掉最后的点和ltd/limited），最后去掉所有空格
            String key = makeKey(masterName);
            
            // 通过模糊匹配来配对主行名称
            String mysqlKey = getMysqlKeySmiliar(key, mysqlKeySet, null);
			
            List<String> mysqlList = map.get(mysqlKey);
            if(mysqlList == null){
            	// 数据库没有该行
            	setRowColor(currentRow, cellStyle);
            	continue;
            }
            
            // 默认是客户的新记录
            boolean flag = true;
            // 遍历list比对支行名称
            for (String mysqlStr : mysqlList) {
            	if(compareBranchName(mysqlStr,branchName,null)){
            		// 表示数据库中含有该记录，该记录不是客户的新纪录
            		flag = false;
            		break;
            	}
			}
            
            if(flag){
            	// 数据库没有该行
            	setRowColor(currentRow, cellStyle);
            }
        } 
        
        FileOutputStream output = new FileOutputStream(resultPath);
        workbook.write(output);
        
        System.out.println("执行结束");
	}
	

	/**
	 * 解析数据库导出的xlsx
	 * @param path
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static Map<String,List<String>> handleFromMysql(String path) throws Exception{
		
        // 接受解析出的集合对象
		Map<String,List<String>> data = new HashMap<>();
		
		Map<String,String> master = new HashMap<>();

        InputStream input = new FileInputStream(path);
        
        // 读取一个工作簿
        Workbook workbook = WorkbookFactory.create(input);
        // 获取当前Sheet页的工作表【只支持一个Sheet页】
        Sheet sheet = workbook.getSheetAt(0);
        // 获取工作表的总行数
        int rowCount = sheet.getPhysicalNumberOfRows();
        
        // 使用id为key归纳关系
        for (int rowIndex = 2; rowIndex < rowCount; rowIndex++) {
        	// 从第startRowIndex行开始，第1行是表头，第2行是标题栏
            Row currentRow = sheet.getRow(rowIndex);
            // id
            Cell idCell = currentRow.getCell(0);
            idCell.setCellType(Cell.CELL_TYPE_STRING);
            String id = idCell.getRichStringCellValue().toString().trim();
            // superior_id
            Cell superiorCell = currentRow.getCell(13);
            superiorCell.setCellType(Cell.CELL_TYPE_STRING);
            String superiorId = superiorCell.getRichStringCellValue().toString().trim();
            // name_en
            String nameEn = currentRow.getCell(3).getStringCellValue().trim();
            
            // key为主行ID
            String key = superiorId;
            
            if(superiorId.equals("0")){
            	key = id;
            	// 记录主行的id和名称的映射
            	master.put(id, nameEn);
            }
            
            List<String> list = data.get(key);
        	if(list == null){
        		list = new ArrayList<>();
        		data.put(key, list);
        	}
        	list.add(nameEn);
        }  
        
        // key的值：由id转成nameEn
        Map<String,List<String>> map = new HashMap<>();
        Set<Entry<String, List<String>>> entrySet = data.entrySet();
        for (Entry<String, List<String>> entry : entrySet) {
			String idKey = entry.getKey();
			List<String> value = entry.getValue();
			String masterName = master.get(idKey);
			if(StringUtils.isEmpty(masterName)){
				System.out.println();
				System.out.println("======!!!!!!=======没有主行id记录：idKey="+idKey+"  ============");
				System.out.println();
				continue;
			}
			
			// map中的key是主行名称全小些，去掉最后的点和ltd/limited，最后去掉所有空格
			String key = makeKey(masterName);
			map.put(key,value);
			
			// 打印主行名称使用
//			mysqlKeyMap.put(key, masterName);
		}
        
        return map;
	}
	
	
	/**
	 * 解析客户给的xls
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static Map<String,List<String>> handleFromCust(String path) throws Exception{
		
        // 接受解析出的集合对象
		Map<String,List<String>> data = new HashMap<>();
		
        InputStream input = new FileInputStream(path);
        // 读取一个工作簿
        Workbook workbook = WorkbookFactory.create(input);
        // 获取当前Sheet页的工作表【只支持一个Sheet页】
        Sheet sheet = workbook.getSheetAt(0);
        // 获取工作表的总行数
        int rowCount = sheet.getPhysicalNumberOfRows();
        
        
        // 从第startRowIndex行开始，第1行是标题栏
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
        	// 行
            Row currentRow = sheet.getRow(rowIndex);
            // masterName
            String masterName = currentRow.getCell(1).getStringCellValue().trim();
            // branchName
            String branchName = currentRow.getCell(2).getStringCellValue().trim();
            
            // 主行作为key（统一去掉最后的点和ltd/limited），最后去掉所有空格
            String key = makeKey(masterName);
            
            List<String> list = data.get(key);
            if(list == null){
            	list = new ArrayList<>();
            	data.put(key, list);
//            	custKeyMap.put(key, masterName);
            }
            list.add(branchName);
        }  
        
        return data;
	}
	
	
	
	private static void setRowColor(Row row, CellStyle style){
		
		int colNum = row.getLastCellNum();
		for(int i = 0;i<colNum;i++){
			Cell cell = row.getCell(i);
			if(cell == null){
				System.out.println("========>没有获取到cell,i="+i);
				continue;
			}
			cell.setCellStyle(style);
		}
	}
	
	
	
	
	private static void handleMasterSmiliar(String key, Set<String> mysqlMasterKeySet) {
		float max = 0.7f;
		String result = null;
		for (String temp : mysqlMasterKeySet) {
			float similarityRatio = LevenshteinUtil.getSimilarityRatio(temp, key);
			if(similarityRatio > max){
				result = temp;
				max = similarityRatio;
			}
		}
		
		if(result == null){
			return;
		}
		
		// 获取原名称
		String mysqlName = mysqlKeyMap.get(result);
		String custName = custKeyMap.get(key);
		
//		System.out.println("=====> "+ custName + " <<>> " + mysqlName + " ||  主行匹配度最高：max="+max+"   ,target="+key+" >> result="+result);
		System.out.println(custName);
		System.out.println(mysqlName);
		System.out.println();
	}

	
	
	
	
	
	/**
	 * 从set中获取跟key最大相似度的String
	 * @param key
	 * @param mysqlMasterKeySet
	 */
	private static String getMysqlKeySmiliar(String key, Set<String> mysqlMasterKeySet, Float minSmiliar) {
		float max = minSmiliar == null ? 0.7f : minSmiliar;
		String result = null;
		for (String temp : mysqlMasterKeySet) {
			float similarityRatio = LevenshteinUtil.getSimilarityRatio(temp, key);
			if(similarityRatio > max){
				result = temp;
				max = similarityRatio;
			}
		}
		
		return result;
	}
	
	/**
	 * 同一主行，匹配支行，匹配上返回true，否则false
	 * @param mysqlStr
	 * @param branchName
	 * @return
	 */
	private static boolean compareBranchName(String mysqlStr, String branchName, Float similarLimit) {
		// 客户数据：大部分是减号分隔取最后；逗号分隔取最前（特点是括号结尾）
		String tempBranch = branchName;
		String[] branchSplit = tempBranch.split("-");
		if(branchSplit.length == 2){
			// 大多数情况
			tempBranch = branchSplit[1].trim().toLowerCase().replace(" ", "");
		}else if(branchSplit.length == 1){
			String[] ds = tempBranch.split(",");
			if(ds.length == 2 && ds[1].endsWith(")")){
				// 只有主行，少数情况
				tempBranch = ds[0].trim().toLowerCase().replace(" ", "");
			}else{
				tempBranch = tempBranch.trim().toLowerCase().replace(" ", "");
			}
		}else if(branchSplit.length > 2){
			// 极少情况
			tempBranch = tempBranch.trim().toLowerCase().replace(" ", "");
		}
		
		// mysql数据
		String tempMysql = mysqlStr.trim().toLowerCase().replace(" ", "");
		
		float similarityRatio = LevenshteinUtil.getSimilarityRatio(tempBranch, tempMysql);
		
		if(similarLimit == null){
			// 支行名称匹配，默认相似度0.9
			similarLimit = 0.9f;
		}
		
		return similarityRatio >= similarLimit;
	}

	
	
	
	/**
	 * 规则：全小写，如果有./ltd/limited等结尾，要去掉，过滤调中间空格
	 * @param str
	 * @return
	 */
	private static String makeKey(String str){
		String key = str.toLowerCase();
		if(key.endsWith(".")){
			key = key.substring(0,key.length()-1).trim();
		}
		if(key.endsWith("ltd")){
			key = key.substring(0,key.length()-3).trim();
		}else if(key.endsWith("limited")){
			key = key.substring(0,key.length()-7).trim();
		}
		return key.replace(" ", "");
	}
	
	/**
	 * 支行list比对
	 * @param aList
	 * @param bList
	 */
	public static void compareList(List<String> aList,List<String> bList){
		if(aList == null || bList == null){
			System.out.println("参数不能为空");
			return;
		}
		
		// 使用删除方式最方便
		Iterator<String> ait = aList.iterator();
		
		while(ait.hasNext()){
			String a = ait.next();
			if(StringUtils.isEmpty(a)){
				continue;
			}
			
			Iterator<String> bit = bList.iterator();
			while(bit.hasNext()){
				String b = bit.next();
				if(StringUtils.isEmpty(b)){
					continue;
				}
				
				if(a.toLowerCase().contains(b.trim().toLowerCase())){
					//System.out.println("相同删除：a="+a+",b="+b);
					bit.remove();
					ait.remove();
					break;
				}
				
			}
			
		}
		
	}

}
