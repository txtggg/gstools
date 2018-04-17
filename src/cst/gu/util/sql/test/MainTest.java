package cst.gu.util.sql.test;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cst.gu.util.container.Containers;
import cst.gu.util.datetime.LocalDateUtil;
import cst.util.common.containers.Lists;
import cst.util.common.file.Files;
import cst.util.common.poi.excel.ExcelWriter;

/**
 * @author guweichao 20171102
 * 
 */
public class MainTest {
 
	public static void main(String[] args) {
		
		String fileName = "C:\\Users\\gwc\\Desktop\\download\\gwc.xls";
		File file = new File(fileName);
		List<String> row1 = Lists.newArrayList("guweichao","wangweichao","刘腾飞");
		List<String> row2 = Lists.newArrayList("guweichao1","wangweichao2","刘腾飞2");
		List<String> row3 = Lists.newArrayList("guweichao2","wangweichao3","刘腾飞3");
		List<List<String>> sheet1 = Lists.newArrayList(row1);
		List<List<String>> sheet2 = Lists.newArrayList(row1,row2);
		List<List<String>> sheet3 = Lists.newArrayList(row2,row3);
		List<List<String>> sheet4 = Lists.newArrayList(row3,row2,row1);
		List<List<List<String>>> excel = Lists.newArrayList(sheet1,sheet2,sheet4,sheet3);
		byte[] bytes = ExcelWriter.list2Excel(excel, true);
		Files.writeInfo(file, bytes);
	}
	public static void ts() {
		Map<String,Object> map = new HashMap<String, Object>();
		byte[] abyDate = (byte[]) map.get("byte_abyData");
		byte[] abyHead = (byte[]) map.get("byte_abyHead");
		
		checkNull(abyDate);
		checkNull(abyHead);
		byte[] byte_allStruct = new byte[28 + abyDate.length + abyHead.length];
		System.out.println(new String(byte_allStruct));
	}
	
	public static void checkNull(Object o){
		if(o == null){
			throw new IllegalStateException("npe");
		}
	}
}
