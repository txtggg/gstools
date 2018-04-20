package test.cst.util.common.poi.excel;


import java.util.List;
import java.util.Map;

import org.junit.Test;

import cst.util.common.containers.Lists;
import cst.util.common.containers.Maps;
import cst.util.common.file.Files;
import cst.util.common.poi.excel.ExcelWriter;
import cst.util.common.string.Objects;

public class ExcelWriterTest {
	private static final String path = Objects.getWindowsClassFilePath(ExcelWriterTest.class);

	@SuppressWarnings("unchecked")
	@Test
	public void testList2Excel() {
		System.out.println("testList2Excel");
		String xlsname = "testList2Excel.xls";
		String xlsxname = "testList2Excel.xlsx";
		List<String> row1 = Lists.newArrayList("s1 row1 col1","s1 row1 col2","s1 row1 col3","中文");
		List<String> row2 = Lists.newArrayList("s1 row2 col1","s1 row2 col2","s1 row2 col3");
		List<String> row3 = Lists.newArrayList("s1 row3 col1","s1 row3 col2","s1 row3 col3");
		List<List<String>> sh1 = Lists.newArrayList(row1,row2,row3);
		List<List<String>> sh2 = Lists.newArrayList(row2,row3);
		List<List<String>> sh3 = Lists.newArrayList(row3);
		List<List<List<String>>> infos = Lists.newArrayList(sh1,sh2,sh3);
		byte[] bytes = ExcelWriter.list2Excel(infos, true);
		Files.writeInfo(path+xlsname,bytes);
		bytes = ExcelWriter.list2Excel(infos, false);
		Files.writeInfo(path+xlsxname,bytes);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSetMarkersList() {
		System.out.println("testSetMarkersList");
		String xlsnamesucc = "测试setMarkers_成功.xls";
		String xlsnamefail = "测试setMarkers_失败.xls";
		String xlsxnamesucc = "测试setMarkers_成功.xlsx";
		String xlsxnamefail = "测试setMarkers_失败.xlsx";
		String outFile1 = "测试setMarkers_listout.xls";
		String outFile2 = "测试setMarkers_listout.xlsx";
		
		Map<String,Object> sheetInfo = Maps.newHashMap();
		Map<String,Object> sheet2Info = Maps.newHashMap();
		Map<String,Object> sheet3Info = Maps.newHashMap();
		List<Map<String,Object>> sheets = Lists.newArrayList(sheetInfo,sheet2Info,sheet3Info);
		sheetInfo.put("zhao", "赵先生");
		sheetInfo.put("wang", "王先生");
		sheetInfo.put("谷", "谷先生");
		
		
		Map<String,Object> rowInfo = Maps.newHashMap();
		Map<String,Object> rowInfo2 = Maps.newHashMap();
		rowInfo.put("weichao", "魏朝");
		rowInfo.put("wang", "王");
		rowInfo2.put("weichao", "魏朝2");
		rowInfo2.put("wang", "王2");
		List<Map<String,Object>> rows = Lists.newArrayList(rowInfo,rowInfo2);
		
		sheetInfo.put("gu", rows);
		sheet2Info.put("chen", rows);
		sheet3Info.put("zhao", "zhao包");
		
		byte[] bytes = ExcelWriter.setMarkers(path+xlsnamesucc, sheets);
		Files.writeInfo(path+outFile1, bytes);
		
		bytes = ExcelWriter.setMarkers(path+xlsxnamesucc, sheets);
		Files.writeInfo(path+outFile2, bytes);
	
		//失败marker测试
//		bytes = ExcelWriter.setMarkers(path+xlsnamefail, sheets);
//		Files.writeInfo(path+outFile1, bytes);
//		
//		bytes = ExcelWriter.setMarkers(path+xlsxnamefail, sheets);
//		Files.writeInfo(path+outFile2, bytes);
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSetMarkersMap() {
		System.out.println("testSetMarkersMap");
		String xlsnamesucc = "测试setMarkers_成功.xls";
		String xlsnamefail = "测试setMarkers_失败.xls";
		String xlsxnamesucc = "测试setMarkers_成功.xlsx";
		String xlsxnamefail = "测试setMarkers_失败.xlsx";
		String outFile1 = "测试setMarkers_mapout.xls";
		String outFile2 = "测试setMarkers_mapout.xlsx";
		
		Map<String,Object> sheetInfo = Maps.newHashMap();
		Map<String,Object> sheet2Info = Maps.newHashMap();
		Map<String,Object> sheet3Info = Maps.newHashMap();
//		List<Map<String,Object>> sheets = Lists.newArrayList(sheetInfo,sheet2Info,sheet3Info);
		Map<String,Map<String,Object>> sheets = Maps.newHashMap();
		sheets.put("sh1", sheetInfo);
		sheets.put("工作表2", sheet2Info);
		sheets.put("sh4", sheet3Info);
		sheetInfo.put("zhao", "赵先生");
		sheetInfo.put("wang", "王先生");
		sheetInfo.put("谷", "谷先生");
		
		
		Map<String,Object> rowInfo = Maps.newHashMap();
		Map<String,Object> rowInfo2 = Maps.newHashMap();
		rowInfo.put("weichao", "魏朝");
		rowInfo.put("wang", "王");
		rowInfo2.put("weichao", "魏朝2");
		rowInfo2.put("wang", "王2");
		List<Map<String,Object>> rows = Lists.newArrayList(rowInfo,rowInfo2);
		
		sheetInfo.put("gu", rows);
		sheet2Info.put("chen", rows);
		sheet3Info.put("zhao", "zhao包");
		
		byte[] bytes = ExcelWriter.setMarkers(path+xlsnamesucc, sheets);
		Files.writeInfo(path+outFile1, bytes);
		
		bytes = ExcelWriter.setMarkers(path+xlsxnamesucc, sheets);
		Files.writeInfo(path+outFile2, bytes);
	
		//失败marker测试
//		bytes = ExcelWriter.setMarkers(path+xlsnamefail, sheets);
//		Files.writeInfo(path+outFile1, bytes);
//		
//		bytes = ExcelWriter.setMarkers(path+xlsxnamefail, sheets);
//		Files.writeInfo(path+outFile2, bytes);
		
	}

}
