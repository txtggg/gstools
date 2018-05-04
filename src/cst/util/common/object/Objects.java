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
	 * 尝试调用getInteger方法解析对象,如果发生异常,print异常后,返回
	 * 
	 * @param o
	 * @return
	 */
	public static Integer tryGetInteger(Object o) {
		Integer i = null;
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
		} else if (o instanceof Integer) {
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
	 * 尝试解析为Long,;如果发生异常,则print并返回null
	 * @param o
	 * @return
	 */
	public static Long tryGetLong(Object o){
		Long l = null;
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
		} else if (o instanceof Integer) {
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
		Double d = null;
		try {
			d = getDouble(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
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
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T changeType(Object value, Class<T> targetType, String charset) {
		if (value == null) {
			return null;
		}
		Object rt = null;
		String className = targetType.getName();
		String valueS = value.toString();
		if (className.equals("java.lang.String")) {
			rt = valueS;
		} else if (className.equals("int") || className.equals("java.lang.Integer")) {
			rt = Integer.valueOf(valueS);
		} else if (className.equals("double") || className.equals("java.lang.Double")) {
			rt = Double.valueOf(valueS);
		} else if (className.equals("long") || className.equals("java.lang.Long")) {
			rt = Long.valueOf(valueS);
		} else if (className.equals("float") || className.equals("java.lang.Float")) {
			rt = Float.valueOf(valueS);
		} else if (className.equals("boolean") || className.equals("java.lang.Boolean")) {
			rt = Boolean.valueOf(valueS);
		} else if (className.equals("java.sql.Blob")) {
				rt = Strings.getBlob(valueS, charset);
		} else if (className.equals("java.sql.Date")) {
			try {
				rt = new java.sql.Date(LocalDateUtil.tryParseDate(valueS).getTime());
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		} else if (className.equals("java.util.Date")) {
			try {
				rt = LocalDateUtil.tryParseDate(valueS);
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
