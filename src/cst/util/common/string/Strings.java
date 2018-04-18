package cst.util.common.string;

public class Strings extends Objects {
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

	public static boolean isNotBlank(String s) {
		return !isBlank(s);
	}

}
