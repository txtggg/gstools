package cst.util.common.object;

import java.text.ParseException;

import cst.gu.util.clazz.ClassUtil;
import cst.gu.util.datetime.LocalDateUtil;
import cst.util.common.string.Strings;

public class Objects {

	protected Objects() {
	}

	/**
	 * 获取windows下指定class文件的所在路径
	 * 
	 * @return
	 */
	public static String getWindowsClassFilePath(Class<?> clz) {
		String path = clz.getResource("").getFile();
		return path.substring(1);
	}

	/**
	 * 获取windows classpath
	 * 
	 * @return
	 */
	public static String getWindowsClassPath() {
		String path = ClassUtil.class.getClassLoader().getResource("").getPath();
		return path.substring(1);
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

	public static String getString(Object o) {
		if (o == null) {
			return "";
		}
		return o.toString();
	}

	/**
	 * 
	 * @param o
	 * @return
	 * @deprecated
	 * @see getInteger
	 * 删除时间v2019
	 */
	public static Integer toInteger(Object o) {
		return getInteger(o);
	}

	public static Integer getInteger(Object o) {
		if (o == null) {
			return null;
		}
		Integer i = null;
		if (o instanceof Integer) {
			i = (Integer) o;
		} else {
			try {
				String s = o.toString();
				if (s.length() > 0) {
					i = Integer.valueOf(o.toString());
				}
			} catch (Exception e) {
			}
		}
		return i;
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
			if (charset == null) {
				rt = Strings.getBlob(valueS);

			} else {
				rt = Strings.getBlob(valueS, charset);
			}
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
