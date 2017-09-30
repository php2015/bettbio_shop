package com.salesmanager.web.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.salesmanager.core.business.catalog.product.model.Product;

public class ProductPoi {
	private static HSSFWorkbook wb = new HSSFWorkbook(); 
	public boolean exportProudctByPoi(String fileUrl,String fileName,List<Product> pList) {  
	    Boolean flag=false;
	    try {  
	    	File file = new File(fileUrl);
	    	if(!file.exists()){
	    		file.createNewFile();
	    	}
	    	FileOutputStream os = new FileOutputStream(file);
	        //创建Excel工作薄  
	        HSSFWorkbook book = new HSSFWorkbook();  
	        //在Excel工作薄中建一张工作表  
	        HSSFSheet sheet = book.createSheet(fileName+".xsl");  
	        //设置单元格格式(文本)  
	        //HSSFCellStyle cellStyle = book.createCellStyle();  
	        //第一行为标题行  
	        HSSFRow row = sheet.createRow(0);//创建第一行  
	        HSSFCell cell0 = row.createCell(0);  
	        HSSFCell cell1 = row.createCell(1);  
	        HSSFCell cell2 = row.createCell(2);  
	        HSSFCell cell3 = row.createCell(3);  
	        HSSFCell cell4 = row.createCell(4);
	        HSSFCell cell5 = row.createCell(5);  
	        HSSFCell cell6 = row.createCell(6);  
	        //定义单元格为字符串类型  
	        cell0.setCellType(HSSFCell.CELL_TYPE_STRING);  
	        cell1.setCellType(HSSFCell.CELL_TYPE_STRING);  
	        cell2.setCellType(HSSFCell.CELL_TYPE_STRING);  
	        cell3.setCellType(HSSFCell.CELL_TYPE_STRING);  
	        cell4.setCellType(HSSFCell.CELL_TYPE_STRING);  
	        cell5.setCellType(HSSFCell.CELL_TYPE_STRING);  
	        cell6.setCellType(HSSFCell.CELL_TYPE_STRING);  
	        
	     // 创建单元格样式  
	        HSSFCellStyle cellStyle = wb.createCellStyle();  
	  
	        // 指定单元格居中对齐  
	        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
	  
	        // 指定单元格垂直居中对齐  
	        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
	  
	        // 指定当单元格内容显示不下时自动换行  
	        cellStyle.setWrapText(true);  
	  
	        // 设置单元格字体  
	        HSSFFont font = wb.createFont();  
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
	        font.setFontName("宋体");  
	        font.setFontHeight((short) 200);  
	        cellStyle.setFont(font);  
	        //在单元格中输入数据  
	        cell0.setCellValue("产品编号");  
	        cell1.setCellValue("销售名称");  
	        cell2.setCellValue("客服名称");  
	        cell4.setCellValue("商品状态");
	        cell5.setCellValue("商品质量总数"); 
	        cell3.setCellValue("店铺名称");
	        cell6.setCellValue("商品总数");
	        //循环导出数据到excel中  
	        List<Object> objList = new ArrayList<Object>();
	        objList.add(pList);
	        int i=0;
	        for(Object o : objList) {  
	            Object [] oj =(Object[]) o; ;  
	            //创建第i行  
	            HSSFRow rowi = sheet.createRow(i + 1);  
	            //在第i行的相应列中加入相应的数据  
	            rowi.createCell(0).setCellValue(oj[0].toString());  
	            rowi.createCell(1).setCellValue(oj[1].toString());  
	            rowi.createCell(2).setCellValue(oj[2].toString());  
	            rowi.createCell(3).setCellValue(oj[3].toString());
	            rowi.createCell(4).setCellValue(oj[4].toString()); 
	            rowi.createCell(5).setCellValue(oj[5].toString());
	            rowi.createCell(6).setCellValue(oj[6].toString());
	        }  
	        //写入数据  把相应的Excel 工作簿存盘  
	        book.write(os); 
	        flag=true;
	    } catch (IOException e) { 
	        flag=false;
	    }  
	    return flag;
	}  
	
}
