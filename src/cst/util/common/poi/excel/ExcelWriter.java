package cst.util.common.poi.excel;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import cst.util.common.containers.Lists;
import cst.util.common.containers.Maps;
import cst.util.common.string.Strings;

/**
 * @author gwc
 * @version 18.3 通过poi操作excel,实现map list 与excel文件的互相操作
 */
public final class ExcelWriter {
	private ExcelWriter() {
	}

	/**
	 * 将excel中marker替换为实际内容 不会改变原有流,而是返回新的byte[]
	 * 
	 * @param in:为避免调用后还使用,流未关闭,需要自行关闭
	 * @param xls
	 * @param info
	 * @param sheetIndex:从0开始
	 *            如果发生io异常,将包装后抛出为runtimeException
	 * @return
	 */
	public static byte[] setSheetMarkers(int sheetIndex, InputStream in, boolean xls, Map<String, Object> info) {
		Workbook wb = PoiUtil.getWorkbook(in, xls);
		setSheetMarkers(wb.getSheetAt(sheetIndex), info);
		return PoiUtil.workbook2ByteArray(wb);
	}

	/**
	 * 将excel中marker替换为实际内容 不会改变原有流,而是返回新的byte[]
	 * 
	 * @param in:为避免调用后还使用,流未关闭,需要自行关闭
	 * @param xls
	 * @param info
	 * @param sheetName
	 *            如果发生io异常,将包装后抛出为runtimeException
	 * @return
	 */
	public static byte[] setSheetMarkers(String sheetName, InputStream in, boolean xls, Map<String, Object> info) {
		Workbook wb = PoiUtil.getWorkbook(in, xls);
		setSheetMarkers(wb.getSheet(sheetName), info);
		return PoiUtil.workbook2ByteArray(wb);
	}

	public static byte[] setSheetMarkers(int sheetIndex, File file, Map<String, Object> info) {
		Workbook wb = PoiUtil.getWorkbook(file);
		setSheetMarkers(wb.getSheetAt(sheetIndex), info);
		return PoiUtil.workbook2ByteArray(wb);
	}

	public static byte[] setSheetMarkers(String sheetName, File file, Map<String, Object> info) {
		Workbook wb = PoiUtil.getWorkbook(file);
		setSheetMarkers(wb.getSheet(sheetName), info);
		return PoiUtil.workbook2ByteArray(wb);
	}

	public static byte[] setSheetMarkers(int sheetIndex, String fileFullName, Map<String, Object> info) {
		Workbook wb = PoiUtil.getWorkbook(fileFullName);
		setSheetMarkers(wb.getSheetAt(sheetIndex), info);
		return PoiUtil.workbook2ByteArray(wb);
	}

	public static byte[] setSheetMarkers(String sheetName, String fileFullName, Map<String, Object> info) {
		Workbook wb = PoiUtil.getWorkbook(fileFullName);
		setSheetMarkers(wb.getSheet(sheetName), info);
		return PoiUtil.workbook2ByteArray(wb);
	}

	/**
	 * 将excel中marker替换为实际内容 不会改变原有流,而是返回新的byte[]
	 * 
	 * @param in:为避免调用后还使用,流未关闭,需要自行关闭
	 * @param xls
	 * @param infos
	 *            如果发生io异常,将包装后抛出为runtimeException
	 * @return
	 */
	public static byte[] setMarkers(InputStream in, boolean xls, Map<String, Map<String, Object>> infos) {
		Workbook wb = PoiUtil.getWorkbook(in, xls);
		setWorkbookMarkers(wb, infos);
		return PoiUtil.workbook2ByteArray(wb);
	}

	/**
	 * 将excel中marker替换为实际内容 不会改变原有流,而是返回新的byte[]
	 * 
	 * @param in:为避免调用后还使用,流未关闭,需要自行关闭
	 * @param xls
	 * @param infos
	 *            如果发生io异常,将包装后抛出为runtimeException
	 * @return
	 */
	public static byte[] setMarkers(InputStream in, boolean xls, List<Map<String, Object>> infos) {
		Workbook wb = PoiUtil.getWorkbook(in, xls);
		setWorkbookMarkers(wb, infos);
		return PoiUtil.workbook2ByteArray(wb);
	}

	public static byte[] setMarkers(File file, List<Map<String, Object>> infos) {
		Workbook wb = PoiUtil.getWorkbook(file);
		setWorkbookMarkers(wb, infos);
		return PoiUtil.workbook2ByteArray(wb);
	}

	public static byte[] setMarkers(File file, Map<String, Map<String, Object>> infos) {
		Workbook wb = PoiUtil.getWorkbook(file);
		setWorkbookMarkers(wb, infos);
		return PoiUtil.workbook2ByteArray(wb);
	}

	/**
	 * 
	 * @param fileFullName
	 * @param infos:根据sheet的顺序,对应的marker内容
	 * @return
	 */
	public static byte[] setMarkers(String fileFullName, List<Map<String, Object>> infos) {
		return setMarkers(new File(fileFullName), infos);
	}

	/**
	 * 
	 * @param fileFullName
	 * @param infos:根据sheet名称,对应的marker内容
	 * @return
	 */
	public static byte[] setMarkers(String fileFullName, Map<String, Map<String, Object>> infos) {
		return setMarkers(new File(fileFullName), infos);
	}

	/**
	 * 新建一个excel 将list内容顺序写入excel
	 * 
	 * @param list:从外到内,分别是sheet内容,row内容,cell内容
	 * @param xls:文件格式,true为*.xls;false为*.xlsx
	 * @return 文件以byte[]返回
	 */
	public static byte[] list2Excel(List<List<List<String>>> list, boolean xls) {
		Workbook wb = PoiUtil.newWorkbook(xls);
		list2Workbook(list, wb, true);
		return PoiUtil.workbook2ByteArray(wb);
	}

	/**
	 * 将list内容顺序写入excel
	 * 
	 * @param list:从外到内,分别是sheet内容,row内容,cell内容
	 * @param xls:文件格式,true为*.xls;false为*.xlsx
	 * @param overwrite
	 *            是覆盖sheet内容还是追加新sheet写入内容
	 * @return 文件以byte[]返回
	 */
	public static byte[] list2Excel(List<List<List<String>>> list, InputStream excel, boolean xls, boolean overwrite) {
		Workbook wb = PoiUtil.getWorkbook(excel, xls);
		list2Workbook(list, wb, overwrite);
		return PoiUtil.workbook2ByteArray(wb);
	}

	/**
	 * 将list内容顺序写入excel
	 * 
	 * @param list:从外到内,分别是sheet内容,row内容,cell内容
	 * @param xls:文件格式,true为*.xls;false为*.xlsx
	 * @param overwrite
	 *            是覆盖sheet内容还是追加新sheet写入内容
	 * @return 文件以byte[]返回
	 */
	public static byte[] list2Excel(List<List<List<String>>> list, File excel, boolean overwrite) {
		Workbook wb = PoiUtil.getWorkbook(excel);
		list2Workbook(list, wb, overwrite);
		return PoiUtil.workbook2ByteArray(wb);
	}

	/**
	 * 将list内容顺序写入excel
	 * 
	 * @param list:从外到内,分别是sheet内容,row内容,cell内容
	 * @param xls:文件格式,true为*.xls;false为*.xlsx
	 * @param overwrite
	 *            是覆盖sheet内容还是追加新sheet写入内容
	 * @return 文件以byte[]返回
	 */
	public static byte[] list2Excel(List<List<List<String>>> list, String fileFullName, boolean overwrite) {
		Workbook wb = PoiUtil.getWorkbook(fileFullName);
		list2Workbook(list, wb, overwrite);
		return PoiUtil.workbook2ByteArray(wb);
	}

	private static void list2Workbook(List<List<List<String>>> list, Workbook wb, boolean overwrite) {
		if (!Lists.isEmpty(list)) {
			int ls = list.size();
			int ws = wb.getNumberOfSheets();
			for (int x = 0; x < ls; x++) {
				Sheet sheet;
				if (overwrite && x < ws) {
					sheet = wb.getSheetAt(x);
				} else {
					sheet = wb.createSheet();
				}
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
	}

	private static void setWorkbookMarkers(Workbook wb, List<Map<String, Object>> infos) {
		for (int x = 0, sw = wb.getNumberOfSheets(), si = infos.size(); x < sw && x < si; x++) {
			setSheetMarkers(wb.getSheetAt(x), infos.get(x));
		}
	}

	private static void setWorkbookMarkers(Workbook wb, Map<String, Map<String, Object>> infos) {
		for (int x = 0, sw = wb.getNumberOfSheets(); x < sw; x++) {
			Sheet sh = wb.getSheetAt(x);
			Map<String, Object> info = infos.get(sh.getSheetName());
			if (info != null) {
				setSheetMarkers(sh, info);
			}
		}
	}

	private static void setSheetMarkers(Sheet sh, Map<String, Object> sheetInfo) {
		for (int x = 0, s = sh.getLastRowNum(); x <= s; x++) {
			setRowMarkers(sh.getRow(x), sheetInfo);
		}
	}

	private static void setRowMarkers(Row row, Map<String, Object> sheetInfo) {
		if (row == null) {
			return;
		}
		String rowKey = null;
		Map<Integer, String> keyMap = Maps.newHashMap();
		// 检查行内容,抽取listmarker的主key,并替换mapMarker
		for (int x = 0, lc = row.getLastCellNum(); x < lc; x++) {
			Cell cell = row.getCell(x);
			if (cell != null) {
				String content = PoiUtil.getCellString(cell);
				String[] marker = checkMarker(content);
				if (marker != null) {
					String listKey = marker[0];
					String mapKey = marker[1];
					if (listKey == null) {
						if (mapKey != null) {
							cell.setCellValue(Strings.getString(sheetInfo.get(mapKey)));
						}
					} else {
						if (rowKey == null) {
							rowKey = listKey;
						} else if (!rowKey.equals(listKey)) {
							throw new RuntimeException("marker标记错误:" + content);
						}
						keyMap.put(x, mapKey);
					}
				}
			}
		}
		// 存在listMarker,需要处理
		if (rowKey != null) {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> listInfo = (List<Map<String, Object>>) sheetInfo.get(rowKey);
			if (Lists.isEmpty(listInfo)) {// 如果为空,只需要把marker替换为空
				String tmp = null;
				for (Entry<Integer, String> e : keyMap.entrySet()) {
					row.getCell(e.getKey()).setCellValue(tmp);
				}
			} else {
				Map<String, Object> map0 = listInfo.get(0);
				for (Entry<Integer, String> e : keyMap.entrySet()) {
					row.getCell(e.getKey()).setCellValue(Strings.getString(map0.get(e.getValue())));
				}
				int r = row.getRowNum();
				for (int x = 1, s = listInfo.size(); x < s; x++) {
					Map<String, Object> map = listInfo.get(x);
					Row rowx = PoiUtil.insertRow(row.getSheet(),row, r + x);
					for (Entry<Integer, String> e : keyMap.entrySet()) {
						int cellNum = e.getKey();
						Cell cell = rowx.getCell(cellNum);
						if (cell == null) {
							cell = rowx.createCell(cellNum);
						}
						cell.setCellValue(Strings.getString(map.get(e.getValue())));
					}

				}
			}
		}
	}

	/**
	 * 
	 * @param marker
	 * @return:null表示不是marker不需要处理,markers[0]代表list的key,markers[1]代表map的key
	 */
	private static String[] checkMarker(String marker) {
		String[] markers = null;
		String markerEx = "marker标记错误:" + marker;
		if (Strings.isNotBlank(marker)) {
			int start = marker.indexOf("${");
			int d = marker.indexOf(".");
			int ld = marker.lastIndexOf(".");
			if (d != ld) {
				throw new RuntimeException(markerEx);
			}
			int end = marker.indexOf("}");
			int len = marker.length() - 1;
			if (start == 0 && end == len) {// 是marker格式的内容
				if (d != -1) { // ${*.*}
					String lkey = marker.substring(2, d);
					String mkey = marker.substring(d + 1, len);
					if (Strings.isNotBlank(lkey) && Strings.isNotBlank(mkey)) {
						markers = new String[2];
						markers[0] = lkey;
						markers[1] = mkey;
					} else {
						throw new RuntimeException(markerEx);
					}
				} else {// ${*}
					String mkey = marker.substring(2, len);
					if (Strings.isNotBlank(mkey)) {
						markers = new String[2];
						markers[1] = mkey;
					} else {
						throw new RuntimeException(markerEx);
					}
				}
			}
		}
		return markers;
	}
}
