package cst.util.common.string;

import java.sql.Blob;
import java.text.ParseException;

import javax.sql.rowset.serial.SerialBlob;

import cst.gu.util.datetime.LocalDateUtil;

public abstract class Strings extends Objects {
	Strings() {
	}

	/**
	 * 获取正则中有特殊含义的字符
	 * 
	 * @return
	 */
	public static String[] getRegSpecial() {
		String[] regs = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
		return regs;
	}

	/**
	 * 转换特殊含义字符,使之成为只匹配字面字符(使*只匹配*而不是匹配任意)
	 * 
	 * @param pattern
	 * @param without:要保留的特殊字符,此字符不被替换
	 * @return
	 */
	public static String changeRegSpecial(String pattern, String... without) {
		if (isNotBlank(pattern)) {
			String[] regSpecial = getRegSpecial();
			for (String key : regSpecial) {
				boolean chg = true;
				for (String wo : without) {
					if (wo.equals(key)) {
						chg = false;
						break;
					}
				}
				if (chg && pattern.contains(key)) {
					pattern = pattern.replace(key, "\\" + key); // 注意:\\需要第一个替换，否则replace方法替换时会有逻辑bug
				}
			}
		}
		return pattern;
	}

	public static boolean isBlank(String s) {
		return s == null || s.isEmpty();
	}

	public static boolean isTrimBlank(String s) {
		return s == null || s.trim().isEmpty();
	}

	public static boolean isNotBlank(String s) {
		return !isBlank(s);
	}

	public static boolean isNotTrimBlank(String s) {
		return !isTrimBlank(s);
	}
	
	
	/**
	 * 
	 * @param value
	 * @param targetType
	 * @return
	 * @deprecated:部分操作结果可能与字符集有关,不指定字符集可能导致结果不符合预期
	 * @see Strings
	 */
	public static <T> T string2Object(String value, Class<T> targetType) {
		return string2Object(value, targetType,null);
	}

	/**
	 * 
	 * @param value
	 * @param targetType
	 * @param charset:使用的字符集,如果为null则使用默认字符集
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T string2Object(String value, Class<T> targetType, String charset) {
		if (value == null) {
			return null;
		}
		Object rt = null;
		String className = targetType.getName();
		if (className.equals("java.lang.String")) {
			rt = value;
		} else if (className.equals("int") || className.equals("java.lang.Integer")) {
			rt = Integer.valueOf(value);
		} else if (className.equals("double") || className.equals("java.lang.Double")) {
			rt = Double.valueOf(value);
		} else if (className.equals("long") || className.equals("java.lang.Long")) {
			rt = Long.valueOf(value);
		} else if (className.equals("float") || className.equals("java.lang.Float")) {
			rt = Float.valueOf(value);
		} else if (className.equals("boolean") || className.equals("java.lang.Boolean")) {
			rt = Boolean.valueOf(value);
		} else if (className.equals("java.sql.Blob")) {
			if (charset == null) {
				rt = string2Blob(value);

			} else {
				rt = string2Blob(value, "utf-8");
			}
		} else if (className.equals("java.sql.Date")) {
			try {
				rt = new java.sql.Date(LocalDateUtil.tryParseDate(value).getTime());
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		} else if (className.equals("java.util.Date")) {
			try {
				rt = LocalDateUtil.tryParseDate(value);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		if (rt == null) {
			return null;
		}
		return (T) rt;

	}

	/**
	 * 
	 * @param str
	 * @param charsetName
	 * @return
	 * @throws Exception
	 */
	public static Blob string2Blob(String str, String charsetName) {

		if (!isBlank(str)) {
			try {
				return new SerialBlob(str.getBytes(charsetName));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param str
	 * @param charsetName
	 * @return
	 * @throws Exception
	 * @deprecated 不指定charset转blob,在不同系统下可能结果不同.不建议使用
	 */
	public static Blob string2Blob(String str) {

		if (!isBlank(str)) {
			try {
				return new SerialBlob(str.getBytes());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}
}
