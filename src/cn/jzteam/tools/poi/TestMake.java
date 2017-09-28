package cn.jzteam.tools.poi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class TestMake {
	
	public static void make(OutputStream outputStream) throws IOException  {
        // 声明一个工作簿【SXSSFWorkbook只支持.xlsx格式】
        Workbook workbook = new SXSSFWorkbook(1000);// 内存中只存放1000条
        // 生成一个表格
        Sheet sheet = workbook.createSheet("test");
        // 设置表格的默认宽度为18个字节
        sheet.setDefaultColumnWidth(18);
        // 生成一个样式【用于Excel中的表格内容】
        CellStyle contentStyle = workbook.createCellStyle();
        contentStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        contentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        // 产生表格标题行【表格的第一行】
        Row headRow = sheet.createRow(0);
        headRow.setRowStyle(contentStyle);
        
        Row test = sheet.createRow(1);
        CellStyle contentStyle1 = workbook.createCellStyle();
        contentStyle1.setFillForegroundColor(HSSFColor.GREEN.index);
        contentStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        test.setRowStyle(contentStyle1);
        
        
        workbook.write(outputStream);
        
        workbook.close();
	}
	
	public static void update(InputStream in, OutputStream outputStream) throws Exception  {
        // 声明一个工作簿【SXSSFWorkbook只支持.xlsx格式】
        Workbook workbook = WorkbookFactory.create(in);// 内存中只存放1000条
        // 生成一个表格
        Sheet sheet = workbook.getSheetAt(0);
        
        // 生成一个样式【用于Excel中的表格内容】
        Row headRow = sheet.getRow(0);
        CellStyle contentStyle = headRow.getRowStyle();
        contentStyle.setFillForegroundColor(HSSFColor.RED.index);
        contentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //headRow.setRowStyle(contentStyle);
        
        Row test = sheet.getRow(1);
        Cell createCell = test.createCell(0);
        createCell.setCellValue("jzteam");
        CellStyle contentStyle1 = test.getRowStyle();
        contentStyle1.setFillForegroundColor(HSSFColor.BLUE.index);
        contentStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        test.setRowStyle(contentStyle);
        
        workbook.write(outputStream);
        
        workbook.close();
	}

	public static void main(String[] args) throws Exception {
		
		String path = "/Users/oker/Documents/work/other/test1.xlsx";
		String dest = "/Users/oker/Documents/work/other/test2.xlsx";
		
//		make(new FileOutputStream(path));
		
		update(new FileInputStream(path),new FileOutputStream(dest));
		
	}
	
}
