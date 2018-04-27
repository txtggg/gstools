package cst.gu.util.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;

import cst.gu.util.annotation.AnnoUtil;
import cst.gu.util.sql.test.LoggerUtil;
import cst.gu.util.string.StringUtil;

/**
 * 
 * @author guweichao@cybersoftek 20170324
 * @gu.static.util.bean 全部静态方法,因此 构造函数私有化 非线程安全
 */
public final class BeanUtil {
	private BeanUtil() {
	}

	/**
	 * 将bean中的字段转存入map<String,Object>中 ,格式为<字段名,值> 如果传入参数是null 则返回null
	 * 如果有注解@Column 则使用注解值代替字段名 如果属性值中有其他bean,则使用其@primaryKey
	 * 做值,如果没有@primaryKey,则设置其对象
	 */
	public static Map<String, Object> toMapWithAnnotation(Object bean) {
		if (bean == null){
			return null;
		}
		Class<? extends Object> clz = bean.getClass();
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = clz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if(!Modifier.isStatic(field.getModifiers())){// 注入非静态资源
				try {
					String anno = AnnoUtil.getColumnValue(field);
					Object v = field.get(bean);
					Field mf = AnnoUtil.getPrimary(v);
					if (mf != null) {// 如果获取的值是一个bean对象,则根据是否有id设置
						mf.setAccessible(true);
						v = mf.get(v);
					}
					if (StringUtil.isTrimBlank(anno)) { // 如果注解为空
						map.put(field.getName(), v);
					} else {
						map.put(anno, v);
					}
				} catch (Exception e) {
					LoggerUtil.errorLog(e);
				}
				
			}
		}
		return map;
	}

	/**
	 * 将bean中的字段转存入map<String,Object>中 ,格式为<字段名,值> 如果传入参数是null 则返回null
	 */
	public static Map<String, Object> toMap(Object bean) {
		if (bean == null){
			return null;
		}
		Class<?> clz = bean.getClass();
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = clz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if(!Modifier.isStatic(field.getModifiers())){// 注入非静态资源
				try {
					map.put(field.getName(), field.get(bean));
				} catch (Exception e) {
					LoggerUtil.errorLog(e);
				}
				
			}
		}
		return map;
	}

	/**
	 * 将map中的值放入指定的bean中 如果此bean属性值中有注解@Column
	 * 则使用注解值代替属性名称。（为了使bean与数据库关联起来，使用数据库的column代替bean的field）
	 * 如果此属性值是bean，则返回bean的一个对象，并将值设置到主键
	 * 只注入非static对象
	 * 
	 * @param <T>
	 * @param bean
	 */
	public static void fillValueWithAnnotation(Object bean, Map<?, ?> map) {
		if (bean == null || map == null || map.isEmpty()) {
			return;
		}
		Class<?> clz = bean.getClass();
		Field[] fields = clz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if(!Modifier.isStatic(field.getModifiers())){// 注入非静态资源
				try { // 本方法中传入bean,不会出现IllegalArgumentException ;
					// field.setAccessible(true),不会出现IllegalAccessException
					String fieldName;
					String anno = AnnoUtil.getColumnValue(field);
					if (StringUtil.isTrimBlank(anno)) {// 有注解 使用注解 ，无注解 使用字段名
						fieldName = field.getName();
					} else {
						fieldName = anno;
					}
					if (map.containsKey(fieldName)) {
						Object v = map.get(fieldName);
						if (v != null) {
							String type = field.getType().getName();
							// 如果字段类型与map中的类型不一致,要把值转为对应的bean的字段类型值
							
							/**
							 * 如果type是blob
							 */
							if(type.equals("java.sql.Blob")){
								if(!(v instanceof Blob)){
									v = StringUtil.string2Blob(v.toString(), "utf-8");
								}
							}else if (!type.equals(v.getClass().getName())) {
								v = StringUtil.string2ObjectWithAnno(field.getType(), v.toString());
							}
						}
						if(v == null){
							field.set(bean, null);
						}else{
							if(field.getType().getName().equals(v.getClass().getName())){
								field.set(bean, v);
							}
						}
					}
				} catch (Exception e) {
					LoggerUtil.errorLog(e);
				}
				
			}
		}
	}

	/**
	 * 将map中的值放入指定的bean中
	 * 
	 * @param <T>
	 * @param bean
	 */
	public static void fillValue(Object bean, Map<String, Object> map) {
		if (bean == null || map == null || map.isEmpty()) {
			return;
		}
		Class<? extends Object> clz = bean.getClass();
		Field[] fields = clz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if(!Modifier.isStatic(field.getModifiers())){// 注入非静态资源
				try { // 本方法中传入bean,不会出现IllegalArgumentException ;
					// field.setAccessible(true),不会出现IllegalAccessException
					String fieldName = field.getName();
					if(map.containsKey(fieldName)){
						Object v = map.get(fieldName);
						if (v != null) {
							String type = field.getType().getName();
							// 如果字段类型与map中的类型不一致,要把值转为对应的bean的字段类型值
							if(type.equals("java.sql.Blob")){
								if(!(v instanceof Blob)){
									v = StringUtil.string2Blob(v.toString(), "utf-8");
								}
							}else
							if (!type.equals(v.getClass().getName())) {
								v = StringUtil.string2Object(field.getType(), v.toString());
							}
						}
						field.set(bean, v);
					}
				} catch (Exception e) {
					LoggerUtil.errorLog(e);
				}
				
			}
		}
	}

	/**
	 * @author guweichao 20170920 将类转化为类似toString方法的样式,便于调试输出
	 * @param separator
	 *            两个field之间的分隔符
	 */
	public static String bean2String(Object o, String separator) {
		Class<?> clz = o.getClass();
		StringBuilder sb = new StringBuilder(clz.getSimpleName()).append(" [ ");
		StringBuilder sb2 = new StringBuilder();
		Field[] fields = clz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);// 压制检查，可以反射访问私有属性，并明显提高性能'
			if(!Modifier.isStatic(field.getModifiers())){// 注入非静态资源
				String fieldName = field.getName();
				try {
					sb2.append(separator).append(fieldName).append("=").append(field.get(o));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		sb.append(sb2.substring(separator.length())).append(" ]");
		return sb.toString();
	}
}
