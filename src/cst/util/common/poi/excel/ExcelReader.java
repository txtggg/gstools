package cst.util.common.poi.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import cst.util.common.containers.Lists;
import cst.util.common.containers.Maps;
import cst.util.common.file.Files;

/**
 * @author gwc
 * @version 18.3 通过poi操作excel,实现map list 与excel文件的互相操作
 */
public final class ExcelReader {
	private ExcelReader() {
	}
 

	/**
	 * @param excel:excel文件的流
	 * @param xls
	 *            : 是xls文件还是xlsx文件
	 * @return
	 * @throws IOException
	 *             注意inputstream是外部传入的,因此需要自行关闭
	 * 
	 */
	public static List<List<List<String>>> excel2List(InputStream excel, boolean xls) throws IOException {
		return ExcelReaders.workBook2List(ExcelReaders.getWorkbook(excel, xls));
	}

	/**
	 * 获取excel的信息,放入list中<sheet<row<column>>>
	 * 
	 * @return
	 * @throws IOException
	 */
	public static List<List<List<String>>> excel2List(String filePath) {
		return ExcelReaders.workBook2List(ExcelReaders.getWorkbook(filePath));
	}

	/**
	 * 将excel的信息按sheet的名称转入Map中
	 * @param excel
	 * @param xls
	 * @return
	 * @throws IOException
	 */
	public static Map<String, List<List<String>>> excel2Map(InputStream excel, boolean xls) throws IOException {
		Workbook wb = ExcelReaders.getWorkbook(excel, xls);
		int s = wb.getNumberOfSheets();
		Map<String, List<List<String>>> emap = Maps.newHashMapSized(s);
		for (int x = 0; x < s; x++) {
			Sheet sheet = wb.getSheetAt(x);
			emap.put(sheet.getSheetName(), ExcelReaders.sheet2List(wb.getSheetAt(x)));
		}
		Files.closeIO(wb);
		return emap;
	}

	/**
	 * 将excel的信息按sheet的名称转入Map中
	 * @param excel
	 * @param xls
	 * @return
	 * @throws IOException
	 */
	public static Map<String, List<List<String>>> excel2Map(String filePath)  {
		Workbook wb = ExcelReaders.getWorkbook(filePath);
		int s = wb.getNumberOfSheets();
		Map<String, List<List<String>>> emap = Maps.newHashMapSized(s);
		for (int x = 0; x < s; x++) {
			Sheet sheet = wb.getSheetAt(x);
			emap.put(sheet.getSheetName(), ExcelReaders.sheet2List(wb.getSheetAt(x)));
		}
		Files.closeIO(wb);
		return emap;
	}

	/**
	 * 
	 * @param sheet
	 *            存储sheet信息的list
	 * @param keyIndex
	 *            第几行用作key,从0开始 注意,key行的长度小于内容行,多余的内容会丢失;反之,会放入null
	 * @return 将sheet内容从list,转为按制定行做key的map
	 */
	public static List<Map<String, String>> sheetList2Map(List<List<String>> sheet, int keyIndex) {
		int s = sheet.size();
		List<Map<String, String>> elist = Lists.newArrayListSized(s);
		List<String> keylist = sheet.get(keyIndex);
		int ks = keylist.size();
		for (int x = keyIndex + 1; x < s; x++) {
			List<String> row = sheet.get(x);
			for (int y = 0; y < ks; y++) {
				Map<String, String> emap = Maps.newHashMapSized(ks);
				emap.put(keylist.get(y), Lists.tryGet(row, y));
				elist.add(emap);
			}
		}
		return elist;
	}

	/**
	 * 
	 * @param excel
	 *            excel文件流
	 * @param xls
	 *            excel格式,*.xls还是*.xlsx
	 * @param index
	 *            从0开始
	 * @return
	 * @throws IOException
	 */
	public static List<List<String>> getSheet(InputStream excel, boolean xls, int index) throws IOException {
		Workbook wb = ExcelReaders.getWorkbook(excel, xls);
		return ExcelReaders.sheet2List(wb.getSheetAt(index));
	}

	public static List<List<String>> getSheet(InputStream excel, boolean xls, String sheetName) throws IOException {
		Workbook wb = ExcelReaders.getWorkbook(excel, xls);
		return ExcelReaders.sheet2List(wb.getSheet(sheetName));
	}

	public static List<List<String>> getSheet(String filePath, String sheetName) {
		return ExcelReaders.sheet2List(ExcelReaders.getWorkbook(filePath).getSheet(sheetName));
	}
	
	public static List<List<String>> getSheet(String filePath, int index) {
		return ExcelReaders.sheet2List(ExcelReaders.getWorkbook(filePath).getSheetAt(index));
	}


}