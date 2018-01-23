package cn.jzteam.tools.poi;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 操作excel
 * @author oker
 *
 */
public class ExcelUtil {

    private static final Logger catalinaLog = Logger.getLogger(ExcelUtil.class);

    private static final String EXCEL_2003 = "xls";

    private static final String EXCEL_2007 = "xlsx";
    
    /**
     * 从EXCEL表中获取数据信息
     * @param multipartFile
     * @param startRowIndex
     * @return
     * @throws RuntimeException
     * @throws IOException 
     * @throws InvalidFormatException 
     */
	public static List<Map<String, String>> processDataFormExcel(MultipartFile multipartFile, int startRowIndex)
            throws Exception {
        // 校验文件信息
        Map<String, String> result = validateFile(multipartFile);
        if ("0".equals(result.get("code"))) {
            throw new RuntimeException(result.get("msg")); // 操作异常
        } else if ("1".equals(result.get("code"))) {
            throw new RuntimeException(result.get("msg")); // 系统异常
        }
        
        // 获取文件名称
        //String fileName = multipartFile.getOriginalFilename();

        //InputStream input = new ByteArrayInputStream(multipartFile.getBytes());
        
        return readExcel(multipartFile.getInputStream(),startRowIndex);
    }

    /**
     * 读取excel到list，每一行对应一个Map<列序号，内容>，列序号从"1"开始
     * 注：公式被被输出成公式String，而不是公式的值
     * @param in
     * @param startRowIndex
     * @return
     */
	public static List<Map<String, String>> readExcel(InputStream in,int startRowIndex) throws Exception{
    	// 接受解析出的集合对象
        List<Map<String, String>> dataList = new ArrayList<Map<String,String>>();
        // 声明一个工作簿
        Workbook workbook = WorkbookFactory.create(in);
        // 获取当前Sheet页的工作表【只支持一个Sheet页】
        Sheet sheet = workbook.getSheetAt(0);
        // 获取工作表的总行数
        int rowCount = sheet.getPhysicalNumberOfRows();
        if (startRowIndex > rowCount) {
            throw new RuntimeException("数据的起始行数超过了总行数，请检查！");
        }
        // 获取标题行
        Row titleRow = sheet.getRow(0);
        if (titleRow == null) {
            throw new RuntimeException("标题行为空，请检查！");
        }

        // 获取总列数
        int colCount = titleRow.getLastCellNum();
        // 从第startRowIndex行开始，第1行是标题栏
        for (int rowIndex = startRowIndex; rowIndex < rowCount; rowIndex++) {
            try {
                Row currentRow = sheet.getRow(rowIndex);
                // 定义返回的数据：key：列数，从1 开始；object ：列对应的值 一个Map，一行数据
                Map<String, String> rowMap = new HashMap<>();
                // 空行标记
                boolean isBlankRow = true;
                // 读取每一列的信息
                for (int colIndex = 0; colIndex < colCount; colIndex++) {
                    Cell cell = currentRow.getCell(colIndex);
                    if (cell == null) {
                        // 空单元格，map中不存放
                        continue;
                    }
                    // 单元格内容为空，也在Map中占一个位置
                    String cellValue = getCellValue(cell);
                    // 列序号约定：从1开始
                    rowMap.put(String.valueOf(colIndex + 1), cellValue);
                    // 空行标记
                    if (cellValue != null) {
                        isBlankRow = false;
                    }
                }
                // 不要空行
                if (!isBlankRow && !rowMap.isEmpty()) {
                    dataList.add(rowMap);
                }
            } catch (RuntimeException e) {
                catalinaLog.info(e.getMessage());
            }
        }
        return dataList;
    }

    /**
     * 将数据导出到Excel 2007版本
     * 
     * @param title :表格的标题名称
     * @param headers ：表格属性列名称的数组
     * @param dataMap ：需要导出的数据
     * @param outputStream ：导出的Excel文件存储的位置
     * @throws IOException 
     */
	public static <T> void export2007ExcelByPOI(String title, String[] headers, List<Map<String, T>> dataMap,
            OutputStream outputStream) throws IOException  {
        // 1、声明一个工作簿【SXSSFWorkbook只支持.xlsx格式】
        Workbook workbook = new SXSSFWorkbook(1000);// 内存中只存放1000条
        // 2、生成一个表格
        Sheet sheet = workbook.createSheet(title);
        // 设置表格的默认宽度为18个字节
        sheet.setDefaultColumnWidth(18);

        // 3、生成一个样式【用于表格标题】
        CellStyle headStyle = workbook.createCellStyle();
        {
            // 设置样式
            headStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
            headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headStyle.setBorderTop(BorderStyle.THIN);// 单元格上边框
            headStyle.setBorderBottom(BorderStyle.THIN);// 单元格下边框
            headStyle.setBorderLeft(BorderStyle.THIN);// 单元格左边框
            headStyle.setBorderRight(BorderStyle.THIN);// 单元格右边框
            headStyle.setAlignment(HorizontalAlignment.CENTER);// 单元格水平居中
            headStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 单元格垂直居中
            // 生成字体并应用到表格标题样式
            Font headFont = workbook.createFont();
            headFont.setFontHeightInPoints((short) 12);
            headFont.setBold(true);
            headStyle.setFont(headFont);
        }

        // 4、生成一个样式【用于Excel中的表格内容】
        CellStyle contentStyle = workbook.createCellStyle();
        {
            // 设置样式【用于Excel中的表格内容】
            contentStyle.setBorderTop(BorderStyle.THIN);// 单元格上边框
            contentStyle.setBorderBottom(BorderStyle.THIN);// 单元格下边框
            contentStyle.setBorderLeft(BorderStyle.THIN);// 单元格左边框
            contentStyle.setBorderRight(BorderStyle.THIN);// 单元格右边框
            contentStyle.setAlignment(HorizontalAlignment.CENTER);// 单元格水平居中
            contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 单元格垂直居中
            contentStyle.setWrapText(true);// 单元格自动换行
            // 生成字体并应用到表格内容样式
            Font contentFont = workbook.createFont();
            contentFont.setBold(false);
            contentStyle.setFont(contentFont);
        }

        // 5、产生表格标题行【表格的第一行】
        Row headRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headRow.createCell(i);
            // 单元格内容格式：字符串
            // 不用设置内容格式，setCellValue会根据类型设置默认的内容格式
            // cell.setCellType(CellType.STRING);
            // 单元格样式
            cell.setCellStyle(headStyle);
            // 字符串 -> 富文本；如果不包装成富文本，String可能被当成公式被设置
            RichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        // 6、遍历集合数据，产生EXCEL行【Excel表格的标题占用了一行】
        for(int r = 1; r<dataMap.size(); r++) {
            Map<String, T> temp = dataMap.get(r);
            // 创建一行
            Row row = sheet.createRow(r);
            Set<String> keys = temp.keySet();
            for (int col = 0; col < keys.size(); col++) {
                Cell cell = row.createCell(col);
                // 单元格样式
                cell.setCellStyle(contentStyle);
                // 列序号约定：从1开始
                String key = String.valueOf(col+1+"");
                Object value = temp.get(key);
                // 判断类型之后进行类型转换，设置时不用设置内容格式，setCellValue会根据类型设置默认的内容格式
                if (value == null) {
                    cell.setCellValue("");
                } else if (value instanceof Integer) {
                    cell.setCellValue((Integer) value);
                } else if (value instanceof Long) {
                    cell.setCellValue((Long) value);
                } else if (value instanceof Double) {
                    cell.setCellValue((Double) value);
                } else if (value instanceof Boolean) {
                    cell.setCellValue((Boolean) value);
                } else if (value instanceof Date) {
                    DateFormat defaultFormatter = new SimpleDateFormat("yyyy-MM-dd");
                    RichTextString richString = new XSSFRichTextString(defaultFormatter.format((Date) value));
                    cell.setCellValue(richString);
                } else if (value instanceof byte[]) {
                    byte[] pictureData = (byte[]) value;
                    // 有图片时候，设置行高
                    row.setHeightInPoints(60);
                    // 设置图片所在的列为80px
                    sheet.setColumnWidth(r, (short) (35.7 * 80));
                    XSSFClientAnchor anchorOther = new XSSFClientAnchor(0, 0, 1023, 255, (short) col, r, (short) col, r);
                    anchorOther.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
                    // 声明一个画图的顶级管理器
                    Drawing patriarch = sheet.createDrawingPatriarch();
                    patriarch.createPicture(anchorOther, workbook.addPicture(pictureData, HSSFWorkbook.PICTURE_TYPE_JPEG));
                } else {
                    RichTextString richString = new XSSFRichTextString(value.toString());
                    cell.setCellValue(richString);
                }
            }
        }
        workbook.write(outputStream);
        outputStream.flush();
    }

    /**
     * 获取一个单元格内容，返回字符串。不处理图片
     * @param cell
     * @return
     */
	public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }

        // getCellTypeEnum 在将来的4.0会被rename为getCell
        switch (cell.getCellTypeEnum()) {
            case NUMERIC: // 数字
                // 先看是否是日期格式
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 读取日期格式
                    Date dateValue = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
                    cellValue = sdf.format(dateValue);
                } else {
                    // 这样可以保持原貌，先冻结成字符串，再取出来（如果当作数字取出，可能会带小数）
                    cell.setCellType(CellType.STRING);
                    cellValue = cell.getRichStringCellValue().toString();
                }
                break;
            case STRING: // 字符串
                cellValue = cell.getStringCellValue().trim();
                break;
            case BOOLEAN: // Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA: // 公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case BLANK: // 空值
                break;
            default:
                break;
        }

        return cellValue;
    }

    /**
     * 校验文件基本信息（文件名，大小）
     * @param multipartFile
     * @return
     */
    public static Map<String, String> validateFile(MultipartFile multipartFile) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            // 默认是文件正常
            result.put("code", "2");
            result.put("msg", "文件校验正常！");
            // 获取文件名称
            String fileName = multipartFile.getOriginalFilename();
            // 校验文件的类型
            if (!fileName.endsWith(EXCEL_2003) && !fileName.endsWith(EXCEL_2007)) {
                result.put("code", "0");
                result.put("msg", "请选择后缀类型为.xlsx的Excel文件！");
            } else {
                // 校验文件的大小
                // 获取上传文件的大小【fis.available()单位是byte】
                double dataSize = (double) multipartFile.getSize() / (1024 * 1024);
                if (Double.compare(dataSize, 0D) == 0) {
                    result.put("code", "0");
                    result.put("msg", "请选择需要上传的文件！");
                }
                if (Double.compare(dataSize, 10) > 0) {
                    result.put("code", "0");
                    result.put("msg", "上传的文件超过10M,请拆分后上传！");
                }
            }
        } catch (RuntimeException e) {
            result.put("code", "1");
            result.put("msg", "系统出现错误，请稍后再试！");
        }
        return result;
    }


    /**
     * 在excel中取出一列，逗号隔开拼接成一行，输出到文件中
     * @param inFilePath
     * @param outFilePath
     * @param startRowIndex
     * @param colIndex
     * @throws Exception
     */
    public static void handleForSql(String inFilePath,String outFilePath,int startRowIndex,int colIndex) throws Exception{

        // 接受解析出的集合对象
        StringBuilder sb = new StringBuilder();
        // 声明一个工作簿
        Workbook workbook = WorkbookFactory.create(new File(inFilePath));
        // 获取当前Sheet页的工作表【只支持一个Sheet页】
        Sheet sheet = workbook.getSheetAt(0);
        // 获取工作表的总行数
        int rowCount = sheet.getPhysicalNumberOfRows();
        if (startRowIndex > rowCount) {
            throw new RuntimeException("数据的起始行数超过了总行数，请检查！");
        }
        // 获取标题行
        Row titleRow = sheet.getRow(0);
        if (titleRow == null) {
            throw new RuntimeException("标题行为空，请检查！");
        }

        // 获取总列数
        int colCount = titleRow.getLastCellNum();
        // 从第startRowIndex行开始，第1行是标题栏
        for (int rowIndex = startRowIndex; rowIndex < rowCount; rowIndex++) {
            try {
                Row currentRow = sheet.getRow(rowIndex);
                // 定义返回的数据：key：列数，从1 开始；object ：列对应的值 一个Map，一行数据
                Map<String, String> rowMap = new HashMap<>();
                // 空行标记
                boolean isBlankRow = true;
                // 读取每一列的信息
                Cell cell = currentRow.getCell(colIndex);
                if (cell == null) {
                    // 空单元格，map中不存放
                    continue;
                }
                // 单元格内容为空，也在Map中占一个位置
                String cellValue = getCellValue(cell);
                if(StringUtils.isEmpty(cellValue)) {
                    continue;
                }
                sb.append(cellValue).append(",");
            } catch (RuntimeException e) {
                catalinaLog.info(e.getMessage());
            }
        }
        final FileOutputStream fos = new FileOutputStream(outFilePath);
        final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write(sb.toString());
        bw.close();
        System.out.println("写入完成");
    }


    public static void main(String[] args) throws Exception {

//        String path = "/Users/oker/Documents/开发任务/批量汇款/xlsx_template/Australia_test.xlsx";
//        List<Map<String, String>> list = readExcel(new FileInputStream(path), 0);
//        list.forEach(System.out::println);
        String path = "/Users/oker/Documents/work/14-other/发币统计/";
        String input = "0118_1.xlsx";
        String output = "0118_1.txt";
        handleForSql(path+input,path+output,1,0);

    }

}