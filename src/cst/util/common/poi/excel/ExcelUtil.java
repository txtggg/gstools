package cst.util.common.poi.excel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import cst.util.common.file.Files;

class ExcelUtil {
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
		;
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
		if(row > lastRow){
			sh.shiftRows(row, lastRow, 1, true, false);
		}
		return sh.createRow(row);
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
			elist.add(ExcelUtil.sheet2List(wb.getSheetAt(x)));
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