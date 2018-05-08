package cst.gu.litedao.bean;

import java.lang.reflect.Field;
import java.util.Map;

import cst.gu.litedao.config.LitedaoConfig;
import cst.util.common.object.Objects;

/**
 * @author gwc
 * @version v201804
 */
public final class Beans {
	private static final String charset = LitedaoConfig.charset();
	private Beans(){}

	public static void of(Map<String, Object> map) {
		
	}

	/**
	 * 
	 * @param id
	 * @param clz
	 * @return
	 */
	public static <T> T newBean(Object id, Class<T> clz) {
		try {
			T t = clz.newInstance();
			Field pk = BeanInfos.getBeanInfo(clz).pk;
			pk.set(t, Objects.changeType(id, pk.getType(), charset));
			return t;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

}
