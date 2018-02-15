package cst.gu.util.string;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @author guweichao 20170411 为Object类转为其他类时处理默认情况
 * 
 * 将Object转为数值类型时,可能发生RuntimeException。如果不需要处理这些异常直接使用0来代替,则可以使用tryToX方法来处理
 * 将Object转为String时,将null转为""
 */
public abstract class ObjectUtil {
	
	
	/**
	 * 将null 转为 "" 其他不变
	 * @param o
	 * @return
	 * @see obj2String
	 * @deprecated
	 */
	public static String toString(Object o){
		if(o == null){ 
			return "";}
		return o.toString();
	}
	
	/**
	 * 将null 转为 "" 
	 * 调用Object的toString方法
	 * @param o
	 * @return
	 */
	public static String obj2String(Object o){
		if(o == null){ 
			return "";}
		return o.toString();
	}
	
	/**
	 * 转化Object为int 如果发生异常 则返回0
	 * @param o
	 * @return
	 */
	public static int tryToInt(Object o){
		int i = 0;
		try{
			i = Integer.parseInt(o.toString().trim());
		}catch(Exception e){
		}
		return i;
	}
	
	/**
	 * 转化Object为Integer 如果无法转化 则返回null
	 * @param o
	 * @return
	 */
	public static Integer toInteger(Object o){
		if(o == null){
			return null;
		}
		String s = o.toString().trim();
		if(s.length() > 0){
			try{
				return Integer.valueOf(s);
			}catch(Exception e){
			}
		}
		return null;
	}
	
	/**
	 * 转化Object为long 如果发生异常 则返回0
	 * @param o
	 * @return
	 */
	public static long tryToLong(Object o){
		long l = 0;
		try{
			l = Long.parseLong(o.toString().trim());
		}catch(Exception e){
		}
		return l;
	}
	
	
	/**
	 * 转化Object为Long 如果无法转换 则返回null
	 * @return
	 */
	public static Long toLong(Object o){
		if(o == null){
			return null;
		}
		String s = o.toString().trim();
		if(s.length() > 0){
			return Long.valueOf(s);
		}
		return null;
	}
	
	
	/**
	 * 转化Object为double 如果发生异常 则返回0
	 * @param o
	 * @return
	 */
	public static double tryToDouble(Object o){
		double dd = 0;
		try{
			dd = Double.parseDouble(o.toString());
		}catch(Exception e){
		}
		return dd;
	}
	
	/**
	 * 转化Object为ouble 如果无法转换 则返回null
	 * @param o
	 * @return
	 */
	public static Double toDouble(Object o){
		if(o == null){
			return null;
		}
		String s = o.toString().trim();
		if(s.length() > 0){
			return Double.valueOf(o.toString());
		}
		return null;
	}
	
	
	/**
	 * 转化Object为float 如果发生异常 则返回0
	 * @param o
	 * @return
	 */
	public static float tryToFloat(Object o){
		float ff = 0;
		try{
			ff = Float.parseFloat(o.toString());
		}catch(Exception e){
		}
		return ff;
	}
	
	/**
	 * 转化Object为loat 如果发无法转换 则返回null
	 * @param o
	 * @return
	 */
	public static Float toFloat(Object o){
		if(o == null){
			return null;
		}
		String s = o.toString().trim();
		if(s.length() > 0){
			return Float.valueOf(o.toString());
		}
		return null;
	}
	
	
	
	/**
	 * 转化Object为boolean 
	 * 忽略大小写
	 * "true" | "yes"  转为true 其他为 false
	 * @param o
	 * @return
	 */
	public static boolean toBoolean(Object o){
		if(o != null){
			String s = o.toString().trim().toLowerCase();
			return "true".equals(s) || "yes".equals(s);
		}
		return false;
		
	}
	
	
	
	/**
	 * 合并数组 :数组中包含数组，将其中数据提取出来 全部合并进入一个数组
	 * 可用于 Object ... obj的方法参数,使用 此方法将数组合并以便可以被正确获取
	 * @param obj
	 * @return
	 */
	public static Object[] mergeArrays(Object...obj){
		List<Object> list = new ArrayList<Object>();
		for(Object o : obj){
			object2List(list, o);
		}
		return list.toArray();
	}
	
	private static void object2List(List<Object> list,Object objs){
		if(objs != null && objs.getClass().isArray()){
			int len = Array.getLength(objs);
			for(int x = 0; x < len;x++){
				object2List(list,Array.get(objs, x));
			}
		}else{
			list.add(objs);
		}
	}
}
