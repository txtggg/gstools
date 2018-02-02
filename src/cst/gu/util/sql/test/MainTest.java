package cst.gu.util.sql.test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cst.gu.util.container.Containers;
import cst.gu.util.datetime.LocalDateUtil;

/**
 * @author guweichao 20171102
 * 
 */
public class MainTest {
 
	public static void main(String[] args) {
//		Date d1 = LocalDateUtil.simpleParseDay("2018-02-05");
//		Date d2 = LocalDateUtil.simpleParseDay("2018-01-31");
//		System.out.println(LocalDateUtil.getWeeksBetween(d1, d2));
		
//		List<Object> l = Containers.newArrayList();
//		l.add(new Date());
//		l.add("Strrr");
//		l.add(15);
//		System.out.println(l.toArray().length);
//		 DecimalFormat format1_percent=new DecimalFormat("#0.0%");
//		 System.out.println(format1_percent.format(0.4436));
		List<Map<String,Object>> l1 = new ArrayList<Map<String,Object>>();
		Map<String,Object> m1 = new HashMap<String, Object>();
		l1.add(m1);
		m1.put("ss", "ddd");
		System.out.println(l1);
		m1 = null;
		System.out.println(l1);
		
	 
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
