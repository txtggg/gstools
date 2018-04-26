package cst.gu.litedao.bean;

import java.lang.reflect.Field;
import java.util.Map;

import cst.gu.util.annotation.PrimaryKey;

/**
 * @author gwc
 * @version v201804
 */
public final class BeanAnnotations { 
	private BeanAnnotations(){}
	
	public static Field getPrimary(Object o) {
		Class<?> oc = o.getClass();
		Field[] fs = oc.getDeclaredFields();
		for (Field f : fs) {
			if (f.isAnnotationPresent(PrimaryKey.class)) {
				return f;
			}
		}
		return null;
	}
	
	public static  boolean isPrimaryKey(Field field) {
		return field.isAnnotationPresent(PrimaryKey.class);
	}
	
}
