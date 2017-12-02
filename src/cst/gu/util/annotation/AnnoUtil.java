package cst.gu.util.annotation;

import java.lang.reflect.Field;

/**
 * @author guweichao 20170410
 * 
 */
public class AnnoUtil {
	private AnnoUtil() {
	} // 静态util 不对外提供实例


	public static <T> String getColumnValue(Field field) {
		Column c = field.getAnnotation(Column.class);
		if (c == null) {
			return null;
		}
		return c.value();
	}
	
	public static  String getTableValue(Class<?> clz) {
		Table t = clz.getAnnotation(Table.class);
		if (t == null) {
			return null;
		}
		return t.value();
	}
	
	public static  boolean isPrimaryKey(Field field) {
		return field.isAnnotationPresent(PrimaryKey.class);
	}
	
	public static Field getPrimary(Object o) {
		Class<?> oc = o.getClass();
		Field[] fs = oc.getDeclaredFields();
		for (Field f : fs) {
			if (isPrimaryKey(f)) {
				return f;
			}
		}
		return null;
	}
	
	

}
