package cst.gu.util.poi.excel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import cst.gu.util.container.Containers;
import cst.gu.util.datetime.LocalDateUtil;
import cst.gu.util.exception.FileTypeException;
import cst.gu.util.file.FileUtil;
import cst.gu.util.string.StringUtil;
import cst.util.common.poi.excel.ExcelReader;

/**
 * 
 * @author guweichao 静态方法,不提供实例 作为poi工具类,依赖于poi的相关jar(xls-->hssf相关 xlsx--xssf相关)
 *         update 20170907 修复缺陷:模板导出无法获取excel内容中最后一行 update 201709011
 *         修复缺陷:getSheetInfo获取的list总是多出一列null 
 *         依赖 apache 的poi包
 *         @version 1.10.7 增加方法templateExport 支持模板导出
 *         @version 1.10.28 templateExport导出,支持导出到第一个sheet,减少map层数
 *         @version 2.11.2 修复缺陷(当导出的数据为空时,标记信息没有被删除)\
 *         @deprecated 
 *         @see ExcelReader
 */
public class ExcelUtil {
	private ExcelUtil() {
	}

	private final static String exInfo = "文件格式错误,必须是.xls或xlsx文件";

	/**
	 * @param xls
	 *            excel格式{true:xls |false: xlsx}
	 * @return
	 */
	public static Workbook getWorkbook(boolean xls) {
		if (xls) {
			return new HSSFWorkbook();
		} else {
			return new XSSFWorkbook();
		}
	}

	/**
	 * 
	 * @param name
	 *            excel 名称
	 * @return 如果name以 .xls结束,则返回xls格式;如果以xlsx结束,返回xlsx格式的excel.其他情况返回null
	 */
	public static Workbook getWorkbook(String name) {
		if (!StringUtil.isTrimBlank(name)) {
			if (name.endsWith(".xls")) {
				return getWorkbook(true);
			}
			if (name.endsWith(".xlsx")) {
				return getWorkbook(false);
			}
		}
		throw new FileTypeException(exInfo);
	}

	/**
	 * 获取excel中的信息,每个sheet按顺序放入list
	 * @param in
	 * @param xls
	 * @return
	 */
	public static List<List<List<String>>> getExcelInfo(InputStream in, boolean xls) {
		Workbook wb = null ;
		try{
			wb = getWorkbook(in, xls);
			int sheets = wb.getNumberOfSheets();
			List<List<List<String>>> excel = new ArrayList<List<List<String>>>(sheets);
			for (int x = 0; x < sheets; x++) {
				Sheet sheet = wb.getSheetAt(x);
				excel.add(getSheetInfo(sheet));
			}
			return excel;
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			FileUtil.closeIO(wb,in);
		}
		return null;
	}
	
	/**
	 * 获取excel中的信息,每个sheet按名称存入map
	 * @param in
	 * @param xls
	 * @return
	 */
	public static Map<String,List<List<String>>> getExcelInfo2(InputStream in, boolean xls){
		Workbook wb = null ;
		try{
			wb = getWorkbook(in, xls);
			int sheets = wb.getNumberOfSheets();
			Map<String,List<List<String>>> excel = Containers.newHashMap(sheets);
			for (int x = 0; x < sheets; x++) {
				Sheet sheet = wb.getSheetAt(x);
				excel.put(sheet.getSheetName(), getSheetInfo(sheet));
			}
			return excel;
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			FileUtil.closeIO(wb,in);
		}
		return null;
	}

	public static List<List<String>> getSheetInfo(InputStream in, boolean xls, int sheetIndex){
		Workbook wb = null;
		try{
			wb = getWorkbook(in, xls);
			Sheet sheet = wb.getSheetAt(sheetIndex);
			List<List<String>> sheetInfo = getSheetInfo(sheet);
			return sheetInfo;
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			FileUtil.closeIO(wb,in);
		}
		return null;
	}

	public static List<List<String>> getSheetInfo(InputStream in, boolean xls, String sheetName) {
		Workbook wb = null;
		try{
			wb = getWorkbook(in, xls);
			Sheet sheet = wb.getSheet(sheetName);
			List<List<String>> sheetInfo = getSheetInfo(sheet);
			return sheetInfo;
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			FileUtil.closeIO(wb,in);
		}
		return null;
	}

	private static List<List<String>> getSheetInfo(Sheet sheet) {
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

	private static String getCellString(Cell cell) {
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

	private static Workbook getWorkbook(InputStream in, boolean xls) throws IOException {
		if (xls) {
			return new HSSFWorkbook(in);
		} else {
			return new XSSFWorkbook(in);
		}
	}

	/**
	 * 导出excel
	 * 
	 * @param sheetName
	 *            如果为null或"",使用默认值 Sheet1
	 * @throws IOException
	 * @Deprecated 请使用模板导出
	 * @see templateExport
	 */
	@Deprecated
	public static byte[] exportExcel(Workbook wb, String sheetName, List<CellStyleInfo> cellStyleInfos)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (CellStyleInfo csi : cellStyleInfos) {
			writeInfo(wb, sheetName, csi);
		}
		wb.write(baos);
		baos.close();
		wb.close();
		return baos.toByteArray();
	}

	/**
	 * 导出excel
	 * 
	 * @param sheetName
	 *            如果为null或"",使用默认值 Sheet1
	 * @throws IOException
	 * @Deprecated 请使用模板导出
	 * @see templateExport
	 */
	@Deprecated
	public static byte[] exportExcel(Workbook wb, String sheetName, CellStyleInfo... cellStyleInfos)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (CellStyleInfo csi : cellStyleInfos) {
			writeInfo(wb, sheetName, csi);
		}
		wb.write(baos);
		baos.close();
		wb.close();
		return baos.toByteArray();
	}

	@Deprecated
	private static void writeInfo(Workbook wb, String sheetName, CellStyleInfo csi) {
		List<CellInfo> cells = csi.cells;
		CellStyle cs = csi.cellstyle;
		Sheet sheet;
		if (StringUtil.isBlank(sheetName)) {
			sheetName = "Sheet1";
		}
		sheet = wb.getSheet(sheetName);
		if (sheet == null) {
			sheet = wb.createSheet(sheetName);
		}

		List<int[]> widths = csi.widths;
		for (int[] width : widths) {
			sheet.setColumnWidth(width[0], width[1]);
		}

		for (int x = 0, len = cells.size(); x < len; x++) {
			CellInfo ci = cells.get(x);
			int firstRow = ci.getFirstRow();
			int firstCol = ci.getFirstCol();
			int lastRow = ci.getLastRow();
			int lastCol = ci.getLastCol();
			Row row = sheet.getRow(firstRow);
			if (row == null) {
				row = sheet.createRow(firstRow);
			}
			Short height = csi.heights.get(firstRow);
			if (height != null && height > 0) {
				row.setHeight(height);
			}
			Cell cell = row.getCell(firstCol);
			if (cell == null) {
				cell = row.createCell(firstCol);
			}
			cell.setCellValue(ci.getName());
			cell.setCellStyle(cs);
			if (lastRow > firstRow || lastCol > firstCol) {
				CellRangeAddress cra = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
				setMergeStyle(sheet, cs, cra);
				sheet.addMergedRegion(cra);
			}
		}
	}

	private static void setMergeStyle(Sheet sheet, CellStyle cs, CellRangeAddress cra) {
		for (int fr = cra.getFirstRow(), lr = cra.getLastRow(); fr <= lr; fr++) {
			Row row = sheet.getRow(fr);
			if (row == null) {
				row = sheet.createRow(fr);
			}
			for (int fc = cra.getFirstColumn(), lc = cra.getLastColumn(); fc <= lc; fc++) {
				Cell cell = row.getCell(fc);
				if (cell == null) {
					cell = row.createCell(fc);
				}
				cell.setCellStyle(cs);
			}
		}
	}

	
	/**
	 * 导出数据到第一个sheet
	 * @param template
	 *            excel模板
	 * @param xls
	 *            excel文件格式,true:*.xls|false:xlsx
	 * @param infos
	 *           sheet1的标记信息 注意模板最后一行如果是${a.b}
	 *            则需要在最后一行的下一行输入一个空格,留作复制行的缓存
	 * @return
	 */
	public static byte[] templateExport(InputStream template, boolean xls, Map<String, Object> infos) {
		List<Map<String,Object>> list = Containers.newArrayListSize(1);
		list.add(infos);
		return templateExport(template,xls ,list);
	}
	/**
	 * @param template
	 *            excel模板
	 * @param xls
	 *            excel文件格式,true:*.xls|false:xlsx
	 * @param infos
	 *            list中根据index分别存储对应sheet的标记信息注意模板最后一行如果是${a.b},则需要在最后一行的下一行输入一个空格,留作复制行的缓存
	 * @return
	 */
	public static byte[] templateExport(InputStream template, boolean xls, List<Map<String, Object>> infos) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Workbook wb = null;
		try {
			if (xls) {
				wb = new HSSFWorkbook(template);
			} else {
				wb = new XSSFWorkbook(template);
			}
			int sc = wb.getNumberOfSheets();
			for (int si = 0; si < sc; si++) {
				Sheet sh1 = wb.getSheetAt(si);

				List<CellRangeAddress> mergeList = new ArrayList<CellRangeAddress>();
				for (int cr = 0, crc = sh1.getNumMergedRegions(); cr < crc; cr++) {
					mergeList.add(sh1.getMergedRegion(cr));
				}
				int rows = sh1.getLastRowNum();
				Map<String, Object> m0 = infos.get(si);
				for (int x = rows; x >= 0; x--) {
					Row row = sh1.getRow(x);
					if (row != null) {
						int cols = row.getLastCellNum();
						String mkey = null;
						Map<Integer, String> cmap = Containers.newHashMap();
						for (int y = 0; y < cols; y++) {
							Cell cell = row.getCell(y);
							if (cell != null) {
								String content = getCellString(cell);
								if (!StringUtil.isTrimBlank(content)) {
									int start = content.indexOf("${");
									int d = content.indexOf(".");
									int end = content.indexOf("}");
									if (start == 0 && end == content.length() - 1) {
										if (d > start && d < end) {
											String tempkey = content.substring(2, d);
											if (mkey == null) {
												mkey = tempkey;
											} else {
												if (!mkey.equals(tempkey)) {
													StringBuilder sb = new StringBuilder("标记信息错误:位于第").append(si + 1)
															.append("个sheet,第").append(x + 1).append("行,第")
															.append(y + 1).append("列[一行信息中不能对应两个list:(${").append(mkey)
															.append(".*},${").append(tempkey).append(".*})");
													throw new RuntimeException(sb.toString());
												}
											}
											cmap.put(y, content.substring(d + 1, end));
										} else {
											cell.setCellValue(StringUtil.toString(m0.get(content.substring(2, end))));
										}
									}
								}
							}

						}

						if (mkey != null) {
							@SuppressWarnings("unchecked")
							List<Map<String, Object>> rowsInfo = (List<Map<String, Object>>) m0.get(mkey);
							if (!Containers.isEmpty(rowsInfo)) {
								for (int r = 0, s = rowsInfo.size(); r < s; r++) {
									Map<String, Object> rowMap = rowsInfo.get(r);
									Set<Entry<Integer, String>> entrys = cmap.entrySet();
									if (r == 0) {
										for (Entry<Integer, String> entry : entrys) {
											Cell cell = row.getCell(entry.getKey());
											cell.setCellValue(StringUtil.toString(rowMap.get(entry.getValue())));
										}
									} else {
										Row nrow = insertRow(sh1, r + x);
										copyRowStyle(row, nrow);
										for (Entry<Integer, String> entry : entrys) {
											int ci = entry.getKey();
											Cell cell = nrow.getCell(ci);
											if (cell == null) {
												cell = nrow.createCell(ci);
											}
											cell.setCellValue(StringUtil.toString(rowMap.get(entry.getValue())));
										}
									}
								}
							}else{ // 如果标记信息为空,将标记信息列设置为空(避免导出空标签)
								Set<Entry<Integer, String>> entrys = cmap.entrySet();
								for (Entry<Integer, String> entry : entrys) {
									Cell cell = row.getCell(entry.getKey());
									cell.setCellValue("");
								}
							}

						}
					}
				}
			}

			try {
				template.close();
				wb.write(baos);
				baos.close();
				wb.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (template != null) {
				try {
					template.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}

	}
	
	/**
	 * 导出数据到第一个sheet
	 * @param template
	 *            excel模板
	 * @param infos
	 *           sheet1的信息 注意模板最后一行如果是${a.b}
	 *            则需要在最后一行的下一行输入一个空格,留作复制行的缓存
	 */
	public static byte[] templateExport(File template,Map<String, Object> infos) {
		List<Map<String,Object>> list = Containers.newArrayListSize(1);
		list.add(infos);
		return templateExport(template, list);
	}

	/**
	 * @param template
	 *            excel模板
	 * @param infos
	 *            根据index分别存储对应sheet的标记信息 注意模板最后一行如果是${a.b}
	 *            则需要在最后一行的下一行输入一个空格,留作复制行的缓存
	 */
	public static byte[] templateExport(File template, List<Map<String, Object>> infos) {
		String name = template.getName();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(template);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		if (name.endsWith(".xls")) {
			return templateExport(fis, true, infos);
		} else if (name.endsWith(".xlsx")) {
			return templateExport(fis, false, infos);
		} else {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			throw new FileTypeException(exInfo);
		}
		// return null;
	}
	
	/**
	 * @param template
	 *            excel模板
	 * @param infos
	 *            sheet1的标记信息 注意模板最后一行如果是${a.b}
	 *            则需要在最后一行的下一行输入一个空格,留作复制行的缓存
	 */
	public static byte[] templateExport(String template, Map<String, Object> infos) {
		return templateExport(new File(template), infos);
	}

	/**
	 * @param template
	 *            excel模板
	 * @param infos
	 *            根据index分别存储对应sheet的标记信息 注意模板最后一行如果是${a.b}
	 *            则需要在最后一行的下一行输入一个空格,留作复制行的缓存
	 */
	public static byte[] templateExport(String template, List<Map<String, Object>> infos) {
		return templateExport(new File(template), infos);
	}

	private static Row insertRow(Sheet sh, int row) {
		sh.shiftRows(row, sh.getLastRowNum(), 1, true, false);
		return sh.createRow(row);
	}

	/**
	 * 复制行的样式
	 */
	private static void copyRowStyle(Row source, Row target) {
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
	private static void copyRowMerge(Row source, Row target) {
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
}
