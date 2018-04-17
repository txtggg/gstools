package cst.util.common.poi.excel;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import cst.util.common.containers.Lists;
import cst.util.common.file.Files;

/**
 * @author gwc
 * @version 18.3 通过poi操作excel,实现map list 与excel文件的互相操作
 * @deprecated 开发中不可使用
 */
public final class ExcelWriter {
	private ExcelWriter(){}
	
	/**
	 * 将list内容顺序写入excel
	 * @param list:从外到内,分别是sheet内容,row内容,cell内容
	 * @param xls:文件格式,true为*.xls;false为*.xlsx
	 * @return 文件以byte[]返回
	 */
	public static byte[] list2Excel(List<List<List<String>>> list,boolean xls){
		Workbook wb = ExcelUtil.newWorkbook(xls);
		if(!Lists.isEmpty(list)){
			for(int x = 0;x<list.size();x++){
				Sheet sheet = wb.createSheet();
				List<List<String>> sheetContent = list.get(x);
				if(!Lists.isEmpty(sheetContent)){
					for(int y =0;y<sheetContent.size();y++){
						Row row = sheet.createRow(y);
						List<String> rowContent = sheetContent.get(y);
						if(!Lists.isEmpty(rowContent)){
							for(int z=0;z<rowContent.size();z++){
								Cell cell = row.createCell(z);
								String content = rowContent.get(z);
								cell.setCellValue(content);
							}
						}
					}
				}
			}
			
		}
		byte[] bytes = ExcelUtil.workbook2ByteArray(wb);
		Files.closeIO(wb);
		return bytes;
		 
	}
	
	public static void map2Excel(String filePath){
		
	}
	
	public static void setMarkers(){
		
	}
}

