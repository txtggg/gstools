package cst.util.common.poi.excel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import cst.gu.util.string.StringUtil;
import cst.util.common.containers.Lists;
import cst.util.common.containers.Maps;
import cst.util.common.file.Files;
import cst.util.common.string.Strings;

/**
 * @author gwc
 * @version 18.3 通过poi操作excel,实现map list 与excel文件的互相操作
 */
public final class ExcelWriter {
//	private static final String regListMarker = "\\$\\{.*..*\\}";
//	private static final String regMapMarker = "\\$\\{.*\\}";
	private ExcelWriter() {
	}

	/**
	 * 将list内容顺序写入excel
	 * 
	 * @param list:从外到内,分别是sheet内容,row内容,cell内容
	 * @param xls:文件格式,true为*.xls;false为*.xlsx
	 * @return 文件以byte[]返回
	 */
	public static byte[] list2Excel(List<List<List<String>>> list, boolean xls) {
		Workbook wb = ExcelUtil.newWorkbook(xls);
		if (!Lists.isEmpty(list)) {
			for (int x = 0; x < list.size(); x++) {
				Sheet sheet = wb.createSheet();
				List<List<String>> sheetContent = list.get(x);
				if (!Lists.isEmpty(sheetContent)) {
					for (int y = 0; y < sheetContent.size(); y++) {
						Row row = sheet.createRow(y);
						List<String> rowContent = sheetContent.get(y);
						if (!Lists.isEmpty(rowContent)) {
							for (int z = 0; z < rowContent.size(); z++) {
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

	private static byte[] setMarkers(Workbook wb, List<Map<String, Object>> infos) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		return baos.toByteArray();
	}

	private static byte[] setMarkers(Sheet sh, List<Map<String, Object>> infos) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		return baos.toByteArray();
	}

	private static void setMarkers(Sheet sheet, Row row, Map<String, Object> infos) {
		String rowKey = null;
		Map<Integer,String> keyMap = Maps.newHashMap();
		// 检查行内容,抽取listmarker的主key,并替换mapMarker
		for (int x = 0,lc = row.getLastCellNum(); x < lc; x++) {
			Cell cell = row.getCell(x);
			if(cell != null){
				String content = ExcelUtil.getCellString(cell);
				String[] marker = checkMarker(content);
				if(marker != null){
					String listKey = marker[0];
					String mapKey = marker[1];
					if(listKey == null){
						if(mapKey !=null){
							cell.setCellValue(Strings.toString(infos.get(mapKey)));
						}
					}else{
						if(rowKey == null){
							rowKey = listKey;
						}else if(!rowKey.equals(listKey)){
							throw new RuntimeException("marker标记错误:"+content);
						}
						keyMap.put(x, mapKey);
					}
				}
			}
		}
		// 存在listMarker,需要处理
		if(rowKey != null){
			@SuppressWarnings("unchecked")
			List<Map<String,Object>> listInfo = (List<Map<String, Object>>) infos.get(rowKey); 
			if(Lists.isEmpty(listInfo)){// 如果为空,只需要把marker替换为空
				
			}
		}
	}
	
	/**
	 * 
	 * @param marker
	 * @return:null表示不是marker不需要处理,markers[0]代表list的key,markers[1]代表map的key
	 */
	private static String[] checkMarker(String marker){
		String[] markers = null;
		String markerEx = "marker标记错误:"+marker;
		if(!Strings.isNotBlank(marker)){
			int start = marker.indexOf("${");
			int d = marker.indexOf(".");
			int ld = marker.lastIndexOf(".");
			if(d != ld){
				throw new RuntimeException(markerEx);
			}
			int end = marker.indexOf("}");
			int len = marker.length() -1;
			if(start == 0 && end == len ){//是marker格式的内容
				if(d != -1){ // ${*.*}
					String lkey = marker.substring(2, d);
					String mkey = marker.substring(d+1, len);
					if(Strings.isNotBlank(lkey) && Strings.isNotBlank(mkey) ){
						markers = new String[2];
						markers[0] = lkey;
						markers[1] = mkey;
					}else{
						throw new RuntimeException(markerEx);
					}
				}else{// ${*}
					String mkey = marker.substring(2, len);
					if(Strings.isNotBlank(mkey)){
						markers = new String[2];
						markers[1] = mkey;
					}else{
						throw new RuntimeException(markerEx);
					}
				}
			}
		}
		return markers;
	}
	
	
	private static String getMarkerErrorInfo(){
		return null;
	}

	public static byte[] templateExport(InputStream template, boolean xls, List<Map<String, Object>> infos) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		Workbook wb = ExcelUtil.getWorkbook(template, xls);
		try {
			int sc = wb.getNumberOfSheets();
			for (int si = 0; si < sc; si++) {
				Sheet sh1 = wb.getSheetAt(si);

				int rows = sh1.getLastRowNum();
				Map<String, Object> m0 = infos.get(si);
				for (int x = rows; x >= 0; x--) {
					Row row = sh1.getRow(x);
					if (row != null) {
						int cols = row.getLastCellNum();
						String mkey = null;
						Map<Integer, String> cmap = Maps.newHashMap();
						for (int y = 0; y < cols; y++) {
							Cell cell = row.getCell(y);
							if (cell != null) {
								String content = ExcelUtil.getCellString(cell);
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
							if (!Lists.isEmpty(rowsInfo)) {
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
							} else { // 如果标记信息为空,将标记信息列设置为空(避免导出空标签)
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
