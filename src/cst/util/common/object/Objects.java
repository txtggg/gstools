package cst.util.common.object;

import java.text.ParseException;

import cst.gu.util.clazz.ClassUtil;
import cst.gu.util.datetime.LocalDateUtil;
import cst.util.common.string.Strings;

/**
 * 
 * @author gwc
 * @version v20180427:更新getInteger方法,添加getInt方法
 */
public class Objects {
	
	

	protected Objects() {}
	
	
	public static boolean equals(Object o1, Object o2) {
		return o1 == o2 || (o1 != null && o1.equals(o2));
	}

	/**
	 * 
	 * @param o
	 * @return
	 * @deprecated
	 * @see getInteger 删除时间v2019.04
	 */
	public static Integer toInteger(Object o) {
		return getInteger(o);
	}

	/**
	 * @deprecated 命名与toString名称相同,可能造成误解,将在v2019删除此方法
	 * @see getString
	 * @param o
	 * @return
	 */
	public static String toString(Object o) {
		return getString(o);
	}

	/**
	 * @author gwc 当传入null值时,无论o的类型为何,一律返回Integer null 当传入Integer,返回自身
	 *         当传入Number,返回intValue() 其他类型,转为String之后使用Integer解析. ""解析为null而不是0
	 * @param o
	 * @return
	 * @exception 可能发生numberFormatException
	 */
	public static Integer getInteger(Object o) {
		if (o == null) {
			return null;
		} else if (o instanceof Integer) {
			return (Integer) o;
		} else if (o instanceof Number) {
			return ((Number) o).intValue();
		}
		String s = o.toString().trim();
		if (s.length() == 0) {
			return null;
		}
		return Integer.valueOf(s);
	}

	/**
	 * 尝试调用getInteger方法解析对象,如果发生异常,print异常后,返回null
	 * @param o
	 * @return
	 */
	public static Integer tryGetInteger(Object o) {
		return tryGetInteger(o,null);
	}
	
	/**
	 * 尝试调用getInteger方法解析对象,如果发生异常,print异常后,返回defaultValue
	 * @param o
	 * @return
	 */
	public static Integer tryGetInteger(Object o,Integer defaultValue) {
		Integer i = defaultValue;
		try {
			i = getInteger(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}


	/**
	 * 当传入null值时,无论o的类型为何,一律返回Integer null 当传入Integer,返回自身
	 * 当传入Number,返回longValue() 其他类型,转为String之后使用Long解析, ""解析为null而不是0
	 * 
	 * @param o
	 * @return
	 * @exception 可能发生numberFormatException
	 */
	public static Long getLong(Object o) {
		if (o == null) {
			return null;
		} else if (o instanceof Long) {
			return (Long) o;
		} else if (o instanceof Number) {
			return ((Number) o).longValue();
		}
		String s = o.toString().trim();
		if (s.length() == 0) {
			return null;
		}
		return Long.valueOf(s);
	}
	
	/**
	 * 尝试解析为Long,如果发生异常,则print并返回null
	 * @param o
	 * @return
	 */
	public static Long tryGetLong(Object o){
		return tryGetLong(o,null);
	}
	

	/**
	 * 尝试解析为Long,如果发生异常,则print并返回defaultValue
	 * @param o
	 * @return
	 */
	public static Long tryGetLong(Object o,Long defaultValue){
		Long l = defaultValue;
		try {
			l = getLong(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;
	}
	
	public static Double getDouble(Object o){
		if (o == null) {
			return null;
		} else if (o instanceof Double) {
			return (Double) o;
		} else if (o instanceof Number) {
			return ((Number) o).doubleValue();
		}
		String s = o.toString().trim();
		if (s.length() == 0) {
			return null;
		}
		return Double.valueOf(s);
	}
	
	public static Double tryGetDouble(Object o){
		return tryGetDouble(o,null);
	}
	
	public static Double tryGetDouble(Object o,Double defaultValue){
		Double d = defaultValue;
		try {
			d = getDouble(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}
	
	/**
	 * 
	 * @param o
	 * @return
	 */
	public static Float getFloat(Object o){
		if (o == null) {
			return null;
		} else if (o instanceof Float) {
			return (Float) o;
		} else if (o instanceof Number) {
			return ((Float) o).floatValue();
		}
		String s = o.toString().trim();
		if (s.length() == 0) {
			return null;
		}
		return Float.valueOf(s);
	}
	
	public static Float tryGetFloat(Object o){
		return tryGetFloat(o,null);
	}
	
	public static Float tryGetFloat(Object o,Float defaultValue){
		Float f = defaultValue;
		try {
			f = getFloat(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

	/**
	 * 获取windows下指定class文件的所在路径
	 * @return
	 */
	public static String getWindowsClassFilePath(Class<?> clz) {
		String path = clz.getResource("").getFile();
		return path.substring(1);
	}

	/**
	 * 获取windows classpath
	 * @return
	 */
	public static String getWindowsClassPath() {
		String path = ClassUtil.class.getClassLoader().getResource("").getPath();
		return path.substring(1);
	}

	public static String getString(Object o) {
		if (o == null) {
			return "";
		}
		return o.toString();
	}

	/**
	 * java常用类型间转换,例如 "1" <-> 1的转换
	 * 
	 * @param value
	 * @param targetType
	 * @return
	 * @deprecated:部分操作结果可能与字符集有关,不指定字符集可能导致结果不符合预期 除非底层数据永不改变操作系统和数据库版本等,
	 *                                              否则不应该使用此方法
	 * @see Strings
	 */
	public static <T> T changeType(Object value, Class<T> targetType) {
		return changeType(value, targetType, null);
	}

	/**
	 * 
	 * @param valueS
	 * @param targetType
	 * @param charset:使用的字符集,如果为null则使用默认字符集
	 * 此方法统一变为String后再执行解析,可以考虑后期优化速度
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T changeType(Object value, Class<T> targetType, String charset) {
		if (value == null) {
			return null;
		}
		Object rt = null;
		String className = targetType.getName();
		if (className.equals("java.lang.String")) {
			rt = value.toString();
		} else if (className.equals("int") ) {
			rt = tryGetInteger(value,0);
		}  else if ( className.equals("java.lang.Integer")) {
			rt = tryGetInteger(value);
		} else if (className.equals("double")  ) {
			rt = tryGetDouble(value,0D);
		} else if (  className.equals("java.lang.Double")) {
			rt = tryGetDouble(value);
		}else if (className.equals("long")  ) {
			rt = tryGetLong(value,0L);
		} else if ( className.equals("java.lang.Long")) {
			rt = tryGetLong(value);;
		}else if (className.equals("float")  ) {
			rt = tryGetFloat(value,0F);
		} else if (  className.equals("java.lang.Float")) {
			rt = tryGetFloat(value);
		}else if (className.equals("boolean") ) {
			rt = false;
			try {
				rt = Boolean.parseBoolean(value.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if (className.equals("boolean") || className.equals("java.lang.Boolean")) {
			try {
				rt = Boolean.valueOf(value.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (className.equals("java.sql.Blob")) {
				rt = Strings.getBlob(value.toString(), charset);
		} else if (className.equals("java.sql.Date")) {
			try {
				rt = new java.sql.Date(LocalDateUtil.tryParseDate(value.toString()).getTime());
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		} else if (className.equals("java.util.Date")) {
			try {
				rt = LocalDateUtil.tryParseDate(value.toString());
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		if (rt == null) {
			return null;
		}
		return (T) rt;
	}

}
