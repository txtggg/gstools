package cst.gu.litedao.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import cst.gu.util.annotation.Column;
import cst.gu.util.annotation.PrimaryKey;
import cst.gu.util.annotation.Table;

/**
 * @author gwc
 * @version v201804
 */
public final class BeanAnnotations { 
	private BeanAnnotations(){}
	
	/**
	 * 获取bean的主键(@PrimaryKey)
	 * 如果没有找到注解,返回null
	 * @param bean
	 * @return
	 */
	public static Field getPrimary(Object bean) {
		return getPrimary(bean.getClass());
	}
	
	/**
	 * 获取bean的主键(@PrimaryKey)
	 * 如果没有找到注解,返回null
	 * @param bean
	 * @return
	 */
	public static Field getPrimary(Class<?> beanClass) {
		Field[] fs = beanClass.getDeclaredFields();
		for (Field f : fs) {
			if (f.isAnnotationPresent(PrimaryKey.class)) {
				if(Modifier.isStatic(f.getModifiers())){
					throw new RuntimeException("bean的主键(@PrimaryKey)不能是static类型");
				}
				return f;
			}
		}
		return null;
	}
	
	/**
	 * 获取@Primary字段,如果没有,返回默认的第一个字段(排除static字段,bean的主键不应该是)
	 * @param bean
	 * @return
	 */
	public static Field getPrimaryOrFirst(Object bean) {
		return getPrimaryOrFirst(bean.getClass());
	}
	
	/**
	 * 获取@Primary字段,如果没有,返回默认的第一个字段(排除static字段,bean的主键不应该是)
	 * @param o
	 * @return
	 */
	public static Field getPrimaryOrFirst(Class<?> beanClass) {
		Field[] fs = beanClass.getDeclaredFields();
		for (Field f : fs) {
			if (f.isAnnotationPresent(PrimaryKey.class)) {
				if(Modifier.isStatic(f.getModifiers())){
					throw new RuntimeException("bean的主键(@PrimaryKey)不能是static类型");
				}
				return f;
			}
		}
		if(fs!= null && fs.length >0){
			return fs[0];
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
	public static String getColumnOrField(Field field) {
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
	 * 获取字段的@Table值,如果没有注解则返回null
	 * @param clz
	 * @return
	 */
	public static  String getTable(Object bean) {
		return getTable(bean.getClass());
	}

	/**
	 * 获取字段的@Table值,如果没有注解则返回classname
	 * @param clz
	 * @return
	 */
	public static  String getTableOrClass(Class<?> clz) {
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
	public static  String getTableOrClass(Object bean) {
		return getTableOrClass(bean.getClass());
	}
}
