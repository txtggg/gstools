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
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cst.gu.util.datetime.LocalDateUtil;
import cst.util.common.containers.Lists;
import cst.util.common.file.Files;

class ExcelUtil {
	private final static String exInfo = "文件格式错误,必须是.xls或xlsx文件";

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

	static Workbook getWorkbook(InputStream in, boolean xls) throws IOException {
		if (xls) {
			return new HSSFWorkbook(in);
		} else {
			return new XSSFWorkbook(in);
		}
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