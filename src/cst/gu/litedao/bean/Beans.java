package cst.gu.litedao.bean;

import java.util.Map;

/**
 * @author gwc
 * @version v201804
 */
public final class Beans {

	public static void of(Map<String, Object> map) {

	}

	public static <T> T newBean(Object id, Class<T> clz) {
		try {
			T t = clz.newInstance();
			return t;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

}
