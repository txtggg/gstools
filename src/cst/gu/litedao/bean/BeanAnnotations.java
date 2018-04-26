package cst.gu.litedao.bean;

import java.lang.reflect.Field;

import cst.gu.util.annotation.Column;
import cst.gu.util.annotation.PrimaryKey;
import cst.gu.util.annotation.Table;

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
	
	/**
	 * 获取字段的@Column值,如果没有注解则返回null
	 * @param field
	 * @return
	 */
	public static String getColumn(Field field) {
		Column c = field.getAnnotation(Column.class);
		if (c == null) {
			return null;
		}
		return c.value();
	}
	
	/**
	 * 获取字段的@Column值,如果没有注解则fieldname
	 * @param field
	 * @return
	 */
	public static String getColumnField(Field field) {
		String c = getColumn(field);
		if(c == null){
			c = field.getName();
		}
		return c;
	}
	
	/**
	 * 获取字段的@Table值,如果没有注解则返回null
	 * @param clz
	 * @return
	 */
	public static  String getTable(Class<?> clz) {
		Table t = clz.getAnnotation(Table.class);
		if (t == null) {
			return null;
		}
		return t.value();
	}
	

	/**
	 * 获取字段的@Table值,如果没有注解则返回classname
	 * @param clz
	 * @return
	 */
	public static  String getTableClass(Class<?> clz) {
		Table t = clz.getAnnotation(Table.class);
		if (t == null) {
			return null;
		}
		return t.value();
	}
}
