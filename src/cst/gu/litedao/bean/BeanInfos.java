package cst.gu.litedao.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import cst.util.common.cache.softref.SoftRefCache;
import cst.util.common.containers.Lists;

/**
 * @author gwc
 * @version v201805
 * bean的反射信息
 */
public final class BeanInfos {
	@SuppressWarnings("rawtypes")
	private static SoftRefCache<String, BeanInfo> cache = SoftRefCache.getInstance();

	private BeanInfos() {
	}

	private static <T> BeanInfo<T> saveBeanInfo(Class<T> clz) {
		BeanInfo<T> bi = getBeanInfo(clz);
		cache.put(clz.getName(),bi);
		return bi;
	}

	
	/**
	 * @author gwc
	 * 获取bean的反射信息,不使用缓存
	 * @param clz
	 * @return
	 */
	public static <T> BeanInfo<T> getBeanInfo(Class<T> clz) {
		BeanInfo<T> bi = new BeanInfo<T>();
		bi.clz = clz;
		bi.table = BeanAnnotations.getTableOrClass(clz);
		List<Field> fs = Lists.newArrayList();
		List<String> cs = Lists.newArrayList();
		for (Field f : clz.getDeclaredFields()) {
			//目前isemp中使用hibernate,bean中的子节点使用set表示,为了兼容hibernate,不反射set字段
			if (!Modifier.isStatic(f.getModifiers()) &&!f.getType().getName().equals("java.util.Set")) {
				f.setAccessible(true);
				if (BeanAnnotations.isPrimaryKey(f)) {
					bi.pk = f;
					bi.pc = BeanAnnotations.getColumnOrField(bi.pk);
				} else {
					fs.add(f);
					cs.add(BeanAnnotations.getColumnOrField(f));
				}
			}
		}
		if (bi.pk == null && fs.size() > 0) {
			bi.pk = fs.get(0);
			bi.pc = cs.get(0);
			fs.remove(0);
			cs.remove(0);
		}
		bi.fields = fs.toArray(new Field[fs.size()]);
		bi.columns = fs.toArray(new String[cs.size()]);
		return bi;
	}
	
	/**
	 * 获取bean的反射信息,使用缓存
	 * @param clz
	 * @return
	 */
	public static <T> BeanInfo<T> getCachedBeanInfo(Class<T> clz) {
		@SuppressWarnings("unchecked")
		BeanInfo<T> bi = (BeanInfo<T>) cache.get(clz.getName());
		if (bi == null) {
			bi = saveBeanInfo(clz);
		}
		return bi;
	}

}

class BeanInfo<T> {
	Class<T> clz;//bean对应的class
	String table;//@Table的值
	Field pk;
	String pc;
	Field[] fields;
	String[] columns;
}
