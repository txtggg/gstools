package cst.gu.util.string;

import java.lang.reflect.Field;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import cst.gu.util.annotation.AnnoUtil;
import cst.gu.util.datetime.LocalDateUtil;

/**
 * @author guweichao 20170328 继承ObjectUtil 可以直接使用StringUtil来得到ObjectUtil中的功能
 * 
 * @version 2.11.2 为部分方法增加重载,提供方便
 */
public class StringUtil extends ObjectUtil {
	private StringUtil() {
	}

	/**
	 * 获取java中正则中特殊的字符
	 * 
	 * @return
	 */
	public static String[] getRegSpecial() {
		String[] regs = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
		return regs;
	}

	/**
	 * 转义正则特殊字符 (String中部分方法使用的正则,但使用时只想使用字符串本身,则调用此方法转义)
	 * 
	 * @param pattern
	 * @return
	 */
	public static String changeRegSpecial(String pattern) {
		if (isNotBlank(pattern)) {
			for (String key : getRegSpecial()) {
				if (pattern.contains(key)) {
					pattern = pattern.replace(key, "\\" + key); // 注意:\\需要第一个替换，否则replace方法替换时会有逻辑bug
				}
			}
		}
		return pattern;
	}

	/**
	 * String 转为指定的类型，如果有bean注解，生成bean对象，并将值注入主键属性
	 * 
	 * @param type
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static <T> Object string2ObjectWithAnno(Class<T> type, String value) throws Exception {
		String className = type.getName();
		if (isTrimBlank(value) || className.equals("java.lang.String")) {
			return value;
		}
		value = value.trim();
		String beanName = AnnoUtil.getTableValue(type);// 如果此对象含有Table注解，则视为bean，生成带有主键值的对象
		if (beanName != null) {
			T t = type.newInstance();
			Field[] fields = type.getDeclaredFields();
			boolean b = false;
			for (Field f : fields) {
				f.setAccessible(true);
				if (AnnoUtil.isPrimaryKey(f)) {
					Object id = string2Object(f.getType(), value);
					if (tryToInt(id) != 0) {
						f.set(t, id);
					}
					b = true;
					break;
				}
			}
			if (b) {
				return t;
			}
			return null;
		}
		return string2Object(type, value);
	}

	public static <T> Object string2Object(Class<T> type, String value) throws Exception {
		String className = type.getName();
		if (isTrimBlank(value) || className.equals("java.lang.String")) {
			return value;
		}
		value = value.trim();
		if (className.equals("int")) {
			return Integer.parseInt(value);
		}
		if (className.equals("java.lang.Integer")) {
			return Integer.valueOf(value);
		}

		if (className.equals("double")) {
			return Double.parseDouble(value);
		}
		if (className.equals("java.lang.Double")) {
			return Double.valueOf(value);
		}

		if (className.equals("long")) {
			return Long.parseLong(value);
		}
		if (className.equals("java.lang.Long")) {
			return Long.valueOf(value);
		}

		if (className.equals("float")) {
			return Float.parseFloat(value);
		}
		if (className.equals("java.lang.Float")) {
			return Float.valueOf(value);
		}

		if (className.equals("boolean")) {
			return Boolean.parseBoolean(value);
		}
		if (className.equals("java.lang.Boolean")) {
			return Boolean.valueOf(value);
		}

		if (className.equals("java.sql.Blob")) {
			return string2Blob(value, "utf-8");
		}

		if (className.equals("java.sql.Date")) {
			return new java.sql.Date(LocalDateUtil.tryParseDate(value).getTime());
		}

		if (className.equals("java.util.Date")) {
			return LocalDateUtil.tryParseDate(value);
		}
		return null;

	}
	
	/**
	 * 所有字段都为空(null或者"")
	 * @param str
	 * @return
	 */
	public static boolean allBlank(String... strs) {
		for (String str : strs) {
			if (str != null && str.length() > 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 至少有一个参数为空(null或者trim后为空)
	 * @param str
	 * @return
	 */
	public static boolean containsBlank(String... strs) {
		for (String str : strs) {
			if (str == null || str.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 所有字段trim后都为空(null或者"")
	 * @param str
	 * @return
	 */
	public static boolean allTrimBlank(String... strs) {
		for (String str : strs) {
			if (str != null && str.trim().length() > 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 至少有一个参数为空(null或者trim后为空)
	 * @param str
	 * @return
	 */
	public static boolean containsTrimBlank(String... strs) {
		for (String str : strs) {
			if (str == null || str.trim().isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断字段为空白(=null 或者length=0 )
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 判断字段为空白(=null 或者trim()之后length=0 )
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isTrimBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 
	 * @param str
	 * @return !isBlank(str)
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	/**
	 * 
	 * @param str
	 * @return !isTrimBlank(str)
	 */
	public static boolean isNotTrimBlank(String str) {
		return !isTrimBlank(str);
	}

	/**
	 * 
	 * @param str
	 * @param charsetName
	 *            编码 建议使用GsFinalStrings.CharSet_*
	 * @return
	 * @throws Exception
	 */
	public static Blob string2Blob(String str, String charsetName) {
		if (!isBlank(str)) {
			try {
				return new SerialBlob(str.getBytes(charsetName));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 由于将blob.length()强转为 int 类.因此对于长度超过int.MaxValue的blob 转化会发生错误
	 * 
	 * @param blob
	 * @param charsetName
	 * @return 默认返回"" 不会返回null
	 * @throws Exception
	 */
	public static String blob2String(Blob blob, String charsetName) {
		String content = "";
		if (blob != null) {
			try {
				int len = (int) blob.length();
				if (len > 0) {
					byte[] bytes = blob.getBytes(1L, len);
					content = new String(bytes, charsetName);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return content;
	}

	/**
	 * 截取字符串的前{length}部分,解决空指针,下标越界问题
	 * (mystrings,3)-->mys;(mystrings,100)-->mystrings
	 * 
	 * @param length
	 *            要截取的长度,如果此长度大于字符串的问题,则返回字符串本身
	 * @param str
	 * @return
	 */
	public static String subTop(Object o, int length) {
		if (o == null) {
			return null;
		}
		String str = o.toString();
		int len = str.length();
		int end = len > length ? length : len;
		return str.substring(0, end);
	}

	public static String getExceptionInfo(Exception ex) {
		StringBuilder sb = new StringBuilder();
		sb.append(ex.getMessage()).append("\r\n");
		StackTraceElement[] trace = ex.getStackTrace();
		for (StackTraceElement s : trace) {
			sb.append("\tat ").append(s).append("\r\n");
		}
		return sb.toString();
	}

	/**
	 * 将字符串拼接起来
	 * 
	 * @param objs
	 * @return
	 */
	public static String concat(Object... objs) {
		StringBuilder sb = new StringBuilder();
		for (Object obj : objs) {
			if (obj == null) {
				sb.append("");
			} else {
				sb.append(obj);
			}
		}
		return sb.toString();
	}

	/**
	 * 将字符串拼接起来
	 * 
	 * @param objs
	 *            要拼接的字符串
	 * @param separator
	 *            没个字符串间的分隔符
	 * @param withEmpty
	 *            : 空字符串(null或"")是否包含在内 ,如果是true 可能返回
	 *            ,,,str1,,str2,str3类似的字符串(分隔符间是空内容)
	 * @return
	 */
	public static String concatWithSeparator(String separator, boolean withEmpty, Object... objs) {
		if (withEmpty) {
			return join1(separator, objs);
		} else {
			return join2(separator, objs);
		}
	}

	public static String concatWithSeparator(String separator, boolean withEmpty, String... strs) {
		Object[] objs = new Object[strs.length];
		for (int x = 0; x < strs.length; x++) {
			objs[x] = strs[x];
		}
		if (withEmpty) {
			return join1(separator, objs);
		} else {
			return join2(separator, objs);
		}
	}

	/**
	 * 
	 * @return
	 */
	private static String join1(String separator, Object... objs) {
		StringBuilder sb = new StringBuilder();
		boolean f = true;
		for (Object o : objs) {
			if (f) {
				if (o == null) {
					sb.append("");
				} else {
					sb.append(o);
				}
				f = false;
			} else {
				sb.append(separator).append(o);
			}
		}
		return sb.toString();
	}

	private static String join2(String separator, Object... objs) {
		StringBuilder sb = new StringBuilder();
		boolean f = true;
		for (Object o : objs) {
			if (o == null) {
				o = "";
			}
			if (!(o == null || o.equals(""))) {
				if (f) {
					sb.append(o);
					f = false;
				} else {
					sb.append(separator).append(o);
				}
			}
		}
		return sb.toString();

	}

	/**
	 * 使用多个分隔符切割字符串
	 * 
	 * @param str
	 *            要分割的字符串
	 * @param separators
	 *            使用的分隔符,不能有空(""或null)
	 * @return
	 */
	public static List<String> split(String str, String... separators) {
		List<String> list = new ArrayList<String>();
		while (str != null) {
			str = stringSplitToList(list, str, separators);
		}
		return list;
	}

	/**
	 * @author guweichao 20170828
	 * @param v1
	 * @param v2
	 * @param split
	 *            版本号中使用的分隔符
	 * @return 如果版本相等 则返回 0, 如果 v1 > v2 返回 1 ,否则返回 -1 以分隔符切割,从大版本号开始向后比较,如果为空 视为
	 *         0, 如 3.4.0.2 > 3.3.8.229
	 */
	public static int compareVersion(String v1, String v2, String split) {
		if (isTrimBlank(v1)) {
			if (isTrimBlank(v2)) { // v1 v2 都为空 返回 0
				return 0;
			}
			return -1; // v1 空 , v2不空 ,则 v1 < v2 返回 -1
		} else {
			if (isTrimBlank(v2)) { // v1不空,v2 空,返回1
				return 1;
			}
			if (v1.trim().equals(v2.trim())) {
				return 0;
			}
			if (isBlank(split)) {
				int iv1 = tryToInt(v1);
				int iv2 = tryToInt(v2);
				if (iv1 == iv2){
					return 0;}
				if (iv1 > iv2) {
					return 1;
				}
				return -1;
			}
			List<String> v1s = split(v1, split);
			List<String> v2s = split(v2, split);
			for (int x = 0; x < v1s.size() && x < v2s.size(); x++) {
				int iv1 = tryToInt(v1s.get(x));
				int iv2 = tryToInt(v2s.get(x));
				if (iv1 == iv2) {
					continue;
				}
				if (iv1 > iv2) {
					return 1;
				} else {
					return -1;
				}
			}
			if (v1s.size() == v2s.size()) {
				return 0;
			} else if (v1s.size() > v2s.size()) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	private static String stringSplitToList(List<String> list, String str, String... separators) {
		int min = -1;
		for (String s : separators) {
			int x = str.indexOf(s);
			if (x != -1) {
				if (min == -1) {
					min = x;
				} else {
					if (x < min) {
						min = x;
					}
				}
			}
		}
		if (min == -1) {
			list.add(str);
			return null;
		} else {
			list.add(str.substring(0, min));
			if (min + 1 < str.length()) {
				return str.substring(min + 1);
			} else {
				return null;
			}
		}
	}
}
