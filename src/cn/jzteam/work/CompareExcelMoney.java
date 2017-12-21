package cn.jzteam.work;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class CompareExcelMoney {

    /**
     * 三天数据，每个用户只取第一次充值记录
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        String path01 = "/Users/oker/Documents/work/11-data_handle/recharge/18-19.xlsx";
        String path02 = "/Users/oker/Documents/work/11-data_handle/recharge/19-20.xlsx";
        String path03 = "/Users/oker/Documents/work/11-data_handle/recharge/20-21.xlsx";
        String outPath = "/Users/oker/Documents/work/11-data_handle/recharge/result.xlsx";

        Map<String,Map<String,String>> dataMap = new HashMap<>();

        FileInputStream in03 = new FileInputStream(path03);
        List<Map<String, String>> list03 = CompareExcelMoney.readExcel(in03, 1);
        addList2map(dataMap, list03);
        System.out.println("map03  size="+dataMap.size());

        FileInputStream in02 = new FileInputStream(path02);
        List<Map<String, String>> list02 = CompareExcelMoney.readExcel(in02, 1);
        addList2map(dataMap, list02);
        System.out.println("map02  size="+dataMap.size());

        FileInputStream in01 = new FileInputStream(path01);
        List<Map<String, String>> list01 = CompareExcelMoney.readExcel(in01, 1);
        addList2map(dataMap, list01);
        System.out.println("map01  size="+dataMap.size());

        List<Map<String,String>> list = new ArrayList<>();
        Set<Map.Entry<String, Map<String, String>>> entries = dataMap.entrySet();
        for(Map.Entry<String, Map<String, String>> entry : entries){
            list.add(entry.getValue());
        }

        System.out.println("list  size="+list.size());
        String[] headers = {"user_id","phone","总充值","总提现","净充值","返币"};

        FileOutputStream out = new FileOutputStream(outPath);

        CompareExcelMoney.export2007ExcelByPOI("result",headers,list,out);

        System.out.println("完成");
    }

    private static Map<String,Map<String,String>> addList2map(Map<String, Map<String, String>> dataMap, List<Map<String, String>> list){
        if(CollectionUtils.isEmpty(list)){
            System.out.println("map==null");
            return new HashMap<>();
        }

        list.forEach(x->{
            System.out.println(x.get("1"));
            dataMap.put(x.get("1").trim(),x);
        });

        return null;
    }

    private static void export2007ExcelByPOI(String title, String[] headers, List<Map<String, String>> dataMap,
                                            OutputStream outputStream) throws IOException {
        // 声明一个工作簿【SXSSFWorkbook只支持.xlsx格式】
        Workbook workbook = new SXSSFWorkbook(1000);// 内存中只存放1000条
        // 生成一个表格
        Sheet sheet = workbook.createSheet(title);
        // 设置表格的默认宽度为18个字节
        sheet.setDefaultColumnWidth(18);
        // 生成一个样式【用于表格标题】
        CellStyle headStyle = workbook.createCellStyle();
        // 设置样式
        headStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headStyle.setBorderTop(CellStyle.BORDER_THIN);// 单元格上边框
        headStyle.setBorderBottom(CellStyle.BORDER_THIN);// 单元格下边框
        headStyle.setBorderLeft(CellStyle.BORDER_THIN);// 单元格左边框
        headStyle.setBorderRight(CellStyle.BORDER_THIN);// 单元格右边框
        headStyle.setAlignment(CellStyle.ALIGN_CENTER);// 单元格水平居中
        headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 单元格垂直居中
        // 生成字体【用于表格标题】
        Font headFont = workbook.createFont();
        headFont.setFontHeightInPoints((short) 12);
        headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        // 把字体应用到当前样式
        headStyle.setFont(headFont);

        // 生成一个样式【用于Excel中的表格内容】
        CellStyle contentStyle = workbook.createCellStyle();
        // 设置样式【用于Excel中的表格内容】
        contentStyle.setBorderTop(CellStyle.BORDER_THIN);// 单元格上边框
        contentStyle.setBorderBottom(CellStyle.BORDER_THIN);// 单元格下边框
        contentStyle.setBorderLeft(CellStyle.BORDER_THIN);// 单元格左边框
        contentStyle.setBorderRight(CellStyle.BORDER_THIN);// 单元格右边框
        contentStyle.setAlignment(CellStyle.ALIGN_CENTER);// 单元格水平居中
        contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 单元格垂直居中
        contentStyle.setWrapText(true);// 单元格自动换行
        // 生成字体
        Font contentFont = workbook.createFont();
        contentFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前样式
        contentStyle.setFont(contentFont);

        // 产生表格标题行【表格的第一行】
        Row headRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headRow.createCell(i);
            // 设置单元格为文本格式
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellStyle(headStyle);
            RichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        // 遍历集合数据，产生EXCEL行【Excel表格的标题占用了一行】
        int index = 1;
        for (Map<String, String> temp : dataMap) {
            // 创建一行
            Row row = sheet.createRow(index);
            Set<String> keys = temp.keySet();
            for (int i = 0; i < keys.size(); i++) {
                Cell cell = row.createCell(i);
                cell.setCellStyle(contentStyle);
                String textValue = temp.get(String.valueOf(i+1));
                // 判断类型之后进行类型转换
                // 如果是字符串
                if (!"".equals(textValue)) {
                    RichTextString richString = new XSSFRichTextString(textValue);
                    // 设置单元格为文本格式
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cell.setCellValue(richString);
                }
            }
            // 行加1
            index++;
        }
        workbook.write(outputStream);
        outputStream.flush();
    }

    public static List<Map<String, String>> readExcel(InputStream in, int startRowIndex) throws EncryptedDocumentException, InvalidFormatException, FileNotFoundException, IOException{
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
                Map<String, String> mapValue = new HashMap<>();
                // 读取每一列的信息
                for (int colIndex = 0; colIndex < colCount; colIndex++) {
                    Cell cell = currentRow.getCell(colIndex);
                    String cellValue = null;
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC: // 数字
                                // 先看是否是日期格式
                                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                                    // 读取日期格式
                                    Date dateValue = cell.getDateCellValue();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                                    cellValue = sdf.format(dateValue);
                                } else {
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                    cellValue = cell.getRichStringCellValue().toString();
                                }
                                break;
                            case Cell.CELL_TYPE_STRING: // 字符串
                                if (!"".equals(cell.getStringCellValue().trim())) {
                                    cellValue = cell.getStringCellValue();
                                }
                                break;
                            case Cell.CELL_TYPE_BOOLEAN: // Boolean
                                cellValue = String.valueOf(cell.getBooleanCellValue());
                                break;
                            case Cell.CELL_TYPE_FORMULA: // 公式
                                cellValue = String.valueOf(cell.getCellFormula());
                                break;
                            case Cell.CELL_TYPE_BLANK: // 空值
                                break;
                            default:
                                break;
                        }
                        if (cellValue != null) {
                            mapValue.put(String.valueOf(colIndex + 1), cellValue);
                        }else{
                            mapValue.put(String.valueOf(colIndex + 1), "");
                        }
                    }
                }
                if (!mapValue.isEmpty()) {
                    dataList.add(mapValue);
                }
            } catch (RuntimeException e) {
//                catalinaLog.info(e.getMessage());
            }
        }
        return dataList;
    }
}
