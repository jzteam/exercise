package cn.jzteam.work;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

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
	
	// 增量的行
	private static List<Row> rowList = new ArrayList<>();
	
	// 统计增量主行个数
	private static Map<String,Integer> custKeyCountMap = new HashMap<>();
	private static Map<String,Integer> newCustKeyCountMap = new HashMap<>();
	
	// 确定为新增主行
	private static List<String> diffNameList = new ArrayList<>();
	static {
		diffNameList.add("Salapa Bikas Bank Ltd");
		diffNameList.add("GULMI BIKAS BANK LIMITED");
		diffNameList.add("RARA DEVELOPMENT BANK");
		diffNameList.add("Mahalaxmi Bikash Bank Ltd.");
		diffNameList.add("Green Development Bank");
		diffNameList.add("Karnali Development Bank");
		diffNameList.add("RESUNGA BIKAS BANK LIMITED");
		diffNameList.add("Kastamandap Bikash Bank Ltd.");
		diffNameList.add("SAMABRIDHI BIKAS BANK LTD");
		diffNameList.add("NILGIRI VIKAS BANK");
		diffNameList.add("SAHARA BIKASH BANK LTD.");
		diffNameList.add("Lumbini Bikas Bank");
	}
		
	
	// 存储数据库中多余的记录
	private static Map<String,List<String>> result = new HashMap<>();
	
	
	
	public static void main(String[] args) throws Exception {
		String mysqlPath = "/Users/oker/Documents/work/other/sqlresult_1834301.xlsx";
		String custPath = "/Users/oker/Documents/work/other/Nepal Updated Bank List.xls";
		String resultPath = "/Users/oker/Documents/work/other/method2.xlsx";
		// 方案一
//		methodFirst();
		
		// 方案二
		Map<String, List<String>> handleFromMysql = handleFromMysql(mysqlPath);
		methodSecond(custPath,handleFromMysql,resultPath);
		
		// 方案三
//		Map<String, List<String>> handleFromMysql = handleFromMysql(mysqlPath);
//		Map<String, List<String>> handleFromCust = handleFromCust(custPath);
//		methodThree(handleFromMysql,handleFromCust,resultPath);
		
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
	public static void methodSecond(String path,Map<String,List<String>> map,String resultPath) throws Exception{
		
        InputStream input = new FileInputStream(path);
        // 读取一个工作簿
        Workbook workbook = WorkbookFactory.create(input);
        // 获取当前Sheet页的工作表【只支持一个Sheet页】
        Sheet sheet = workbook.getSheetAt(0);
        // 获取工作表的总行数
        int rowCount = sheet.getPhysicalNumberOfRows();
        
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
            String mysqlKey = getKeySmiliar(key, mysqlKeySet, null);
            
            boolean contains = diffNameList.contains(masterName);
            if(mysqlKey == null || contains){
            	// 判断为新增记录
            	addNewCust(currentRow,masterName);
            	
            	// 统计新增的主行
    	    	Integer integer = newCustKeyCountMap.get(masterName);
    	    	if(integer == null){
    	    		newCustKeyCountMap.put(masterName, 1);
    	    	}else{
    	    		newCustKeyCountMap.put(masterName, ++integer);
    	    	}
            	
            	continue;
            }
            
            List<String> mysqlList = map.get(mysqlKey);
            if(mysqlList == null){
            	System.out.println("异常信息：根据mysqlKey获取不到对应的list：mysqlKey="+mysqlKey);
            	continue;
            }
            
            // 默认是客户的新记录
            if(!compareBranchName(mysqlList,branchName,null)){
            	// 数据库没有该行
            	addNewCust(currentRow, masterName);
        	}
        } 
        
        
        makeWorkbook(rowList,resultPath);
        
        showStatistics();
        
        System.out.println("执行结束");
	}
	
	
	/**
	 * 方案三：输出mysql.xlsx，客户数据中没有的信息
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static void methodThree(Map<String,List<String>> mysqlMap,Map<String,List<String>> custMap,String resultPath) throws Exception{
		
		
		Set<String> custKeySet = custMap.keySet();
		
		// 遍历mysqlMap
		Set<Entry<String, List<String>>> mysqlSet = mysqlMap.entrySet();
		for (Entry<String, List<String>> entry : mysqlSet) {
			String mysqlKey = entry.getKey();
			List<String> mysqlList = entry.getValue();
			String mysqlName = mysqlKeyMap.get(mysqlKey);
			
			// 获取客户的key
			String custKey = getKeySmiliar(mysqlKey, custKeySet, null);
			
			if(custKey == null){
				// 数据库已经不存在这个主行了
				result.put(mysqlName, mysqlList);
				continue;
			}
			
			List<String> custList = custMap.get(custKey);
			
			// 比对custList和mysqlList
			
			//先处理custList的名称
			List<String> tempCustList = new ArrayList<>();
			for (String custBranchName : custList) {
				// 客户数据：大部分是减号分隔取最后；逗号分隔取最前（特点是括号结尾）
				String tempBranch = custBranchName;
				String[] branchSplit = tempBranch.split("-");
				if(branchSplit.length == 2){
					// 大多数情况
					tempBranch = branchSplit[1].trim().toLowerCase();
				}else if(branchSplit.length == 1){
					String[] ds = tempBranch.split(",");
					if(ds.length == 2 && ds[1].endsWith(")")){
						// 少数情况，支行在前，主行在后，逗号分隔，括号结尾
						tempBranch = ds[0].trim().toLowerCase();
					}else{
						tempBranch = tempBranch.trim().toLowerCase();
					}
				}else if(branchSplit.length > 2){
					// 极少情况
					tempBranch = tempBranch.trim().toLowerCase();
				}
				// 为统一分隔符，把中间的空格替换成逗号
				tempBranch = replaceBlank(tempBranch, ",");
				
				tempCustList.add(tempBranch);
			}
			
			for (String mysqlBranchName : mysqlList) {
				
				// mysql数据:为统一分隔符，把中间的空格替换成逗号
				String tempMysql = replaceBlank(mysqlBranchName.trim().toLowerCase(),",");
				
				// 标记:没有相同的
				boolean flag = false;
				
				for (String tempBranch : tempCustList) {
					
					if("Pokhara - Chipledhunga".equals(mysqlBranchName) && mysqlName.equals("Om Development Bank") && tempBranch.contains("chipledhunga")){
						System.out.println("Chipledhunga");
					}
					
					float similarityRatio = getBranchNameSimilarity(tempBranch, tempMysql);
					if(similarityRatio >= 0.9){
						flag = true;
						break;
					}
				}
				
				if(!flag){
					addLeftMysql(mysqlName,mysqlKey,mysqlBranchName);
				}
				
			}
			
		}
		
		makeXlsx(result,resultPath);
        
//        showStatistics();
        
        System.out.println("执行结束");
	}
	
	private static String replaceBlank(String temp,String separator){
		if(temp == null){
			return "";
		}
		temp = temp.trim();
		String[] split = temp.split(" ");
		temp = split[0];
		for(int i = 1;i<split.length;i++){
			if(!StringUtils.isEmpty(split[i]) && !split[i].equals("-")){
				temp += separator + split[i];
			}
		}
		return temp;
	}
	

	@SuppressWarnings("deprecation")
	private static void makeXlsx(Map<String, List<String>> result, String resultPath) throws IOException {
		// 声明一个工作簿【SXSSFWorkbook只支持.xlsx格式】
        Workbook workbook = new SXSSFWorkbook(1000);// 内存中只存放1000条
        // 生成一个表格
        Sheet sheet = workbook.createSheet("result");
        // 设置表格的默认宽度为28个字节
        sheet.setDefaultColumnWidth(28);
        // 生成一个样式【用于表格标题】
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);// 单元格上边框
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);// 单元格下边框
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);// 单元格左边框
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);// 单元格右边框
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 单元格垂直居中
        
        Set<Entry<String, List<String>>> entrySet = result.entrySet();
        
        int i = 0;
        for (Entry<String, List<String>> entry : entrySet) {
			String masterName = entry.getKey();
			List<String> branchList = entry.getValue();
			
			if(CollectionUtils.isEmpty(branchList)){
				Row row = sheet.createRow(i++);
				
				// 序号列
				Cell createCell = row.createCell(0);
				createCell.setCellValue(i+"");
				createCell.setCellStyle(cellStyle);
				
				// 主行名称
				Cell createCell2 = row.createCell(1);
				createCell2.setCellValue(masterName);
				createCell2.setCellStyle(cellStyle);
				
				continue;
			}
			
			for (String branchName : branchList) {
				
				Row row = sheet.createRow(i++);
				
				// 序号列
				Cell createCell = row.createCell(0);
				createCell.setCellValue(i+"");
				createCell.setCellStyle(cellStyle);
				
				// 主行名称
				Cell createCell2 = row.createCell(1);
				createCell2.setCellValue(masterName);
				createCell2.setCellStyle(cellStyle);
				
				// 支行名称
				Cell createCell3 = row.createCell(2);
				createCell3.setCellValue(branchName);
				createCell3.setCellStyle(cellStyle);
			}
			
		}
        
        
        FileOutputStream os = new FileOutputStream(resultPath);
        workbook.write(os);
        workbook.close();
		
	}

	private static void addLeftMysql(String mysqlName, String mysqlKey, String mysqlBranchName) {
	
		List<String> list = result.get(mysqlName);
		if(list == null){
			list = new ArrayList<>();
			result.put(mysqlName, list);
		}
		
		list.add(mysqlBranchName);
		
	}

	private static void showStatistics() {
		System.out.println();
        System.out.println("======增量支行==========");
        System.out.println();

        int total = 0;
        Set<Entry<String, Integer>> entrySet = custKeyCountMap.entrySet();
        System.out.println("增量支行有："+entrySet.size()+"个");
        for (Entry<String, Integer> entry : entrySet) {
        	total += entry.getValue();
        	System.out.println(entry.getKey()+"  -->  "+entry.getValue()+"个");
		}
        System.out.println("增量支行："+total+"个");
        
        System.out.println();
        System.out.println(">>>>>>新增主行>>>>>>>>>>>>");
        System.out.println();

        Set<Entry<String, Integer>> entrySet1 = newCustKeyCountMap.entrySet();
        System.out.println("新增主行有："+entrySet1.size()+"个");
        int newTotal = 0;
        for (Entry<String, Integer> entry : entrySet1) {
			System.out.println(entry.getKey()+"  -->  有"+entry.getValue()+"个");
			newTotal += entry.getValue();
		}
        System.out.println("新增主行共有支行："+newTotal+"个");
	}

	/**
	 * 判断为新增的支行，处理数据
	 * @param currentRow
	 * @param masterName
	 */
	private static void addNewCust(Row currentRow, String masterName) {
		//setRowColor(currentRow, cellStyle);
		
		// 如果该主行已经是确认过不相同的，那么就直接加入新增列表
    	rowList.add(currentRow);
    	
    	// 统计新增的支行
    	Integer integer = custKeyCountMap.get(masterName);
    	if(integer == null){
    		custKeyCountMap.put(masterName, 1);
    	}else{
    		custKeyCountMap.put(masterName, ++integer);
    	}
	}

	@SuppressWarnings("deprecation")
	private static void makeWorkbook(List<Row> rowList, String resultPath) throws IOException {
		if(CollectionUtils.isEmpty(rowList)){
			System.out.println("异常信息：rowList为空");
			return;
		}
		
		Workbook wb = new SXSSFWorkbook(1000);// 内存中只存放1000条
        // 生成一个表格
        Sheet st = wb.createSheet("result");
        
        // 设置表格的默认宽度为28个字节
        st.setDefaultColumnWidth(28);
        
        // 样式
        CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);// 单元格上边框
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);// 单元格下边框
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);// 单元格左边框
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);// 单元格右边框
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 单元格垂直居中
        
        for(int i = 0;i<rowList.size();i++){
        	Row createRow = st.createRow(i);
        	Row row = rowList.get(i);
        	
        	// 插入序号列
        	Cell firstCell = createRow.createCell(0);
        	firstCell.setCellValue(i+"");
        	
        	short lastCellNum = row.getLastCellNum();
        	// 除了第一列其他都是String
        	for(int n = 1;n<lastCellNum;n++){
        		Cell cell = row.getCell(n);
        		int cellType = cell.getCellType();
        		String stringCellValue = "";
        		if(cellType == Cell.CELL_TYPE_NUMERIC){
        			cell.setCellType(Cell.CELL_TYPE_STRING);
        			stringCellValue = cell.getRichStringCellValue().toString();
        		}else if(cellType == Cell.CELL_TYPE_FORMULA){
        			stringCellValue = String.valueOf(cell.getCellFormula());
        		}else if(cellType == Cell.CELL_TYPE_STRING){
        			stringCellValue = cell.getStringCellValue();
        		}
        		
        		Cell createCell = createRow.createCell(n);
        		createCell.setCellStyle(cellStyle);
        		createCell.setCellValue(stringCellValue);
        	}
        }
        
        FileOutputStream output = new FileOutputStream(resultPath);
        wb.write(output);
		wb.close();
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
			mysqlKeyMap.put(key, masterName);
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
            	custKeyMap.put(key, masterName);
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
	 * @param masterKeySet
	 */
	private static String getKeySmiliar(String key, Set<String> masterKeySet, Float minSmiliar) {
		float max = minSmiliar == null ? 0.7f : minSmiliar;
		String result = null;
		for (String temp : masterKeySet) {
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
	private static boolean compareBranchName(List<String> mysqlStrList, String branchName, Float similarLimit) {
		// 客户数据：大部分是减号分隔取最后；逗号分隔取最前（特点是括号结尾）
		String tempBranch = branchName;
		String[] branchSplit = tempBranch.split("-");
		if(branchSplit.length == 2){
			// 大多数情况
			tempBranch = branchSplit[1].trim().toLowerCase();
		}else if(branchSplit.length == 1){
			String[] ds = tempBranch.split(",");
			if(ds.length == 2 && ds[1].endsWith(")")){
				// 少数情况，支行在前，主行在后，逗号分隔，括号结尾
				tempBranch = ds[0].trim().toLowerCase();
			}else{
				tempBranch = tempBranch.trim().toLowerCase();
			}
		}else if(branchSplit.length > 2){
			// 极少情况
			System.out.println("cust的支行名称，极少情况：branchName="+branchName);
			tempBranch = tempBranch.trim().toLowerCase().replace("-", "");
		}
		// 为统一分隔符，把中间的空格替换成逗号
		tempBranch = replaceBlank(tempBranch, ",");
//		System.out.println(branchName + "  >>>  "+tempBranch);
		
		for (String mysqlStr : mysqlStrList) {
			// mysql数据:为统一分隔符，把中间的空格和减号替换成逗号
			String tempMysql = mysqlStr.trim().toLowerCase();
			tempMysql = replaceBlank(tempMysql,",");
			
			float similarityRatio = getBranchNameSimilarity(tempBranch, tempMysql);
			
//			System.out.println(mysqlStr + "  >>>  "+tempMysql);
//			System.out.println("相似度："+similarityRatio);
			
			if(similarLimit == null){
				// 因为拆分较细，支行名称较短，单词少，所以默认相似度较高，0.9f
				similarLimit = 0.9f;
			}
			
			if(similarityRatio >= similarLimit){
//				System.out.println();
				return true;
			}
			
		}
		
//		System.out.println();
		return false;
	}

	
	
	/**
	 * 比较拆分之后的支行名称
	 * 两者都有逗号，则统一去掉逗号再比对；
	 * 两者都没有逗号，则直接比对；
	 * 一者有，另一个没有，则再拆分分别比对
	 * 
	 * 有的是逗号分隔，有的是空格分隔
	 * @param tempBranch
	 * @param tempMysql
	 * @return
	 */
	private static float getBranchNameSimilarity(String tempBranch, String tempMysql) {
		if(StringUtils.isEmpty(tempBranch) || StringUtils.isEmpty(tempMysql)){
			return 0;
		}
		
		String[] custSplit = tempBranch.split(",");
		String[] mysqlSplit = tempMysql.split(",");
		if(custSplit.length == 1 && mysqlSplit.length == 1){
			// 都没有逗号
			return LevenshteinUtil.getSimilarityRatio(tempBranch, tempMysql);
		}else if(custSplit.length > 1 && mysqlSplit.length > 1){
			// 都有逗号
			String replaceBranch = tempBranch.replace(",", "");
			String replaceMysql = tempMysql.replace(",", "");
			return LevenshteinUtil.getSimilarityRatio(replaceBranch, replaceMysql);
		}else if(custSplit.length > 1 && mysqlSplit.length == 1){
			// 客户数据有逗号，数据库没有
			float max = 0.0f;
			for (String custName : custSplit) {
				float tempRatio = LevenshteinUtil.getSimilarityRatio(custName, tempMysql);
				if(tempRatio > max){
					max = tempRatio;
				}
			}
			return max;
		}else if(custSplit.length == 1 && mysqlSplit.length > 1){
			// 客户数据没有逗号，数据库有
			float max = 0.0f;
			for (String mysqlName : mysqlSplit) {
				float tempRatio = LevenshteinUtil.getSimilarityRatio(mysqlName, tempBranch);
				if(tempRatio > max){
					max = tempRatio;
				}
			}
			return max;
		}else{
			System.out.println("异常情况：tempBranch="+tempBranch+",tempMysql="+tempMysql);
			return 0;
		}
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
