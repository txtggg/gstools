package cst.util.common.poi.excel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cst.gu.util.datetime.LocalDateUtil;
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
		return PoiUtil.workBook2List(PoiUtil.getWorkbook(excel, xls));
	}

	/**
	 * 获取excel的信息,放入list中<sheet<row<column>>>
	 * 
	 * @return
	 * @throws IOException
	 */
	public static List<List<List<String>>> excel2List(String filePath) {
		return PoiUtil.workBook2List(PoiUtil.getWorkbook(filePath));
	}

	/**
	 * 将excel的信息按sheet的名称转入Map中
	 * @param excel
	 * @param xls
	 * @return
	 * @throws IOException
	 */
	public static Map<String, List<List<String>>> excel2Map(InputStream excel, boolean xls) throws IOException {
		Workbook wb = PoiUtil.getWorkbook(excel, xls);
		int s = wb.getNumberOfSheets();
		Map<String, List<List<String>>> emap = Maps.newHashMapSized(s);
		for (int x = 0; x < s; x++) {
			Sheet sheet = wb.getSheetAt(x);
			emap.put(sheet.getSheetName(), PoiUtil.sheet2List(wb.getSheetAt(x)));
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
		Workbook wb = PoiUtil.getWorkbook(filePath);
		int s = wb.getNumberOfSheets();
		Map<String, List<List<String>>> emap = Maps.newHashMapSized(s);
		for (int x = 0; x < s; x++) {
			Sheet sheet = wb.getSheetAt(x);
			emap.put(sheet.getSheetName(), PoiUtil.sheet2List(wb.getSheetAt(x)));
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
		Workbook wb = PoiUtil.getWorkbook(excel, xls);
		return PoiUtil.sheet2List(wb.getSheetAt(index));
	}

	public static List<List<String>> getSheet(InputStream excel, boolean xls, String sheetName) throws IOException {
		Workbook wb = PoiUtil.getWorkbook(excel, xls);
		return PoiUtil.sheet2List(wb.getSheet(sheetName));
	}

	public static List<List<String>> getSheet(String filePath, String sheetName) {
		return PoiUtil.sheet2List(PoiUtil.getWorkbook(filePath).getSheet(sheetName));
	}
	
	public static List<List<String>> getSheet(String filePath, int index) {
		return PoiUtil.sheet2List(PoiUtil.getWorkbook(filePath).getSheetAt(index));
	}
}class PoiUtil {
	private final static String exInfo = "文件格式错误,必须是.xls或xlsx文件";
	
	static void copyRowStyle(Row source, Row target) {
		CellStyle r1cs = source.getRowStyle();
		if (r1cs != null) {
			target.setRowStyle(r1cs);
		}
		for (int x = 0; x < source.getLastCellNum(); x++) {
			Cell c2 = source.getCell(x);
			if (c2 != null) {
				CellStyle c2s = c2.getCellStyle();
				Cell cell = target.getCell(x);
				if (cell == null) {
					cell = target.createCell(x);
				}
				if (c2s != null) {
					cell.setCellStyle(c2s);
				}
			}
		}
		target.setHeight(source.getHeight());
		copyRowMerge(source, target);
	}

	/**
	 * 复制行的合并单元格
	 */
	static void copyRowMerge(Row source, Row target) {
		Sheet sh = source.getSheet();
		int tr = target.getRowNum();
		int sr = source.getRowNum();
		for (int x = 0, s = sh.getNumMergedRegions(); x < s; x++) {
			CellRangeAddress cra = sh.getMergedRegion(x);
			int fr = cra.getFirstRow();
			int lr = cra.getLastRow();
			if (tr >= fr && tr <= lr && fr < lr) {
				throw new RuntimeException("本方法不能有跨行的合并单元格[" + "firstRow:" + fr + ",lastRow:" + lr + "]");
			}
			if ((sr >= fr && sr <= lr)) {
				if (fr < lr) {
					throw new RuntimeException("本方法不能有跨行的合并单元格[" + "firstRow:" + fr + ",lastRow:" + lr + "]");
				}
				int fc = cra.getFirstColumn();
				int lc = cra.getLastColumn();
				sh.addMergedRegion(new CellRangeAddress(tr, tr, fc, lc));
			}
		}
	}
	
	/**
	 * 在指定的行插入一行
	 * @param sh
	 * @param row
	 * @return
	 */
	static Row insertRow(Sheet sh, int row) {
		int lastRow = sh.getLastRowNum();
//		Row row0 = sh.getRow(row);
		if(row > lastRow){
			sh.shiftRows(row, lastRow, 1, true, false);
		}
		Row nrow= sh.createRow(row);
//		copyRowStyle(row0, nrow);
		return nrow;
	}

	static List<List<String>> sheet2List(Sheet sheet) {
		int rows = sheet.getLastRowNum();
		List<List<String>> list = new ArrayList<List<String>>(rows);
		for (int x = 0; x <= rows; x++) {
			Row row = sheet.getRow(x);
			if (row == null || row.getLastCellNum() == 0) {
				list.add(new ArrayList<String>());
			} else {
				int cols = row.getLastCellNum();
				List<String> slist = new ArrayList<String>(cols);
				list.add(slist);
				for (int y = 0; y < cols; y++) {
					Cell cell = row.getCell(y);
					slist.add(getCellString(cell));
				}
			}
		}
		return list;
	}

	static String getCellString(Cell cell) {
		String content = null;
		if (cell == null) {
			return null;
		} else {
			int cft = cell.getCellType();
			switch (cft) {
			case Cell.CELL_TYPE_BLANK:
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				content = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				content = String.valueOf(cell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				content = String.valueOf(cell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					content = LocalDateUtil.simpleFormatDay(cell.getDateCellValue());
				} else {
					content = String.valueOf(cell.getNumericCellValue());
				}
				break;
			case Cell.CELL_TYPE_STRING:
				content = cell.getStringCellValue();
				// default : content = cell.getStringCellValue();
			}
			return content;
		}
	}

	static Workbook newWorkbook(boolean xls) {
		if (xls) {
			return new HSSFWorkbook();
		} else {
			return new XSSFWorkbook();
		}
	}

	static Workbook getWorkbook(InputStream in, boolean xls) {
		Workbook wb = null;
		try{
			if (xls) {
				wb = new HSSFWorkbook(in);
			} else {
				wb = new  XSSFWorkbook(in);
			}
		}catch(IOException e){
			throw new RuntimeException(e);
		}
		return wb;
	}

	static Workbook getWorkbook(String file) {
		boolean xls;
		if (file.endsWith(".xls")) {
			xls = true;
		} else if (file.endsWith(".xlsx")) {
			xls = false;
		} else {
			throw new FileTypeException(exInfo);
		}
		FileInputStream excel = null;
		Workbook wb = null;
		try {
			excel = new FileInputStream(new File(file));
			wb = getWorkbook(excel, xls);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Files.closeIO(excel);
		}
		return wb;
	}

	static List<List<List<String>>> workBook2List(Workbook wb) {
		int s = wb.getNumberOfSheets();
		List<List<List<String>>> elist = Lists.newArrayListSized(s);
		for (int x = 0; x < s; x++) {
			elist.add(PoiUtil.sheet2List(wb.getSheetAt(x)));
		}
		Files.closeIO(wb);
		return elist;
	}

	static byte[] workbook2ByteArray(Workbook wb) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			wb.write(baos);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Files.closeIO(baos);
		}
		return baos.toByteArray();
	}
}