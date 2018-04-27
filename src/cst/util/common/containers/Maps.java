package cst.util.common.containers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Blob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cst.gu.litedao.bean.Beans;
import cst.gu.util.sql.test.LoggerUtil;
import cst.gu.util.string.StringUtil;


/**
 * @author gwc
 * @version 18.3
 */
public class Maps {

	public static <K, V> HashMap<K,V> newHashMap(){
		return new HashMap<K, V>();
	}
	
	public static <K, V> HashMap<K,V> newHashMapSized(int size){
		return new HashMap<K, V>(size);
	}
	
	/**
	 * 
	 * @param map
	 * @return map == null || map.isEmpty()
	 */
	public static <K,V> boolean isEmpty(Map<K,V> map){
		return map == null || map.isEmpty();
	}
	
	/**
	 * 
	 * @param map
	 * @return !isEmpty(map)
	 */
	public static <K,V> boolean isNotEmpty(Map<K,V> map){
		return !isEmpty(map);
				
	}
	
	/**
	 * 将map中v取出,按照entryset的迭代顺序放入list中
	 * @param map
	 * @return
	 */
	public static <K,V> List<V> getList(Map<K,V> map){
		if(map == null){
			return null;
		}
		List<V> list = Lists.newArrayList();
		Set<Entry<K, V>> entrys = map.entrySet();
		for (Entry<K, V> entry : entrys) {
			list.add(entry.getValue());
		}
		return list;
				
	}
	
	/**
	 * 将bean的非静态字段按照字段名,字段值映射为map
	 * @param bean
	 * @return:如果bean是null,则返回null,其他情况返回map
	 * 此方法只是简单的转换key,value.如果使用litedao将bean同步数据库请使用Beans
	 * @see Beans
	 */
	public static Map<Object,Object> of(Object bean){
		if (bean == null){
			return null;
		}
		Class<?> clz = bean.getClass();
		Map<Object,Object> map = newHashMap();
		Field[] fields = clz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if(!Modifier.isStatic(field.getModifiers())){// 注入非静态资源
				try {
					map.put(field.getName(), field.get(bean));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return map;
	}
	
	/**
	 * 将map中的值放入指定的bean中
	 * 
	 * @param <T>
	 * @param bean
	 * @deprecated 开发中
	 */
	public static void toBean(Map<String, Object> map,Object bean) {
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
	 
}
