package cst.util.common.string;

import java.sql.Blob;
import java.text.ParseException;

import javax.sql.rowset.serial.SerialBlob;

import cst.gu.util.datetime.LocalDateUtil;
import cst.util.common.object.Objects;

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
	 * @param str
	 * @param charsetName
	 * @return
	 * @throws Exception
	 */
	public static Blob getBlob(String str, String charsetName) {

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
	 *             除非底层数据永不改变操作系统和数据库版本等,否则不应该使用此方法
	 */
	public static Blob getBlob(String str) {

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
