package cst.gu.util.annotation;

import java.lang.reflect.Field;

/**
 * @author guweichao 20170410
 * 
 */
public class AnnoBeanUtil {
	private AnnoBeanUtil() {
	} // 静态util 不对外提供实例

	/**
	 * 获取field对应的数据库column
	 * 如果没有注解,使用fieldname
	 * @param field
	 * @return
	 */
	public static String getColumn(Field field) {
		String column = AnnoUtil.getColumnValue(field);
		if (column == null) {
			column = field.getName();
		}
		return column;
	}

	/**
	 * 获取class对应的数据库表名
	 * 有注解使用注解,没有默认使用simplename
	 * @param clz
	 * @return
	 */
	public static String getTable(Class<?> clz) {
		String tbn = AnnoUtil.getTableValue(clz);
		if (tbn == null) {
			tbn = clz.getSimpleName();
		}
		return tbn;
	}

}
