package cst.util.common.string;

import cst.gu.util.clazz.ClassUtil;

public class Objects {

	Objects() {
	}
	
	/**
	 * 获取windows下指定class文件的所在路径
	 * @return
	 */
	public static String getWindowsClassFilePath(Class<?> clz) {
		String path = clz.getResource ("").getFile ();
		return path.substring(1);
	}

	/**
	 * 获取windows classpath
	 * @return
	 */
	public static String getWindowsClassPath() {
		String path = ClassUtil.class.getClassLoader().getResource("").getPath();
		return path.substring(1);
	}

	public static String toString(Object o) {
		if (o == null) {
			return "";
		}
		return o.toString();
	}

	public static Integer toInteger(Object o) {
		if (o == null) {
			return null;
		}
		Integer i = null;
		if (o instanceof Integer) {
			i = (Integer) o;
		} else {
			try {
				String s = o.toString();
				if (s.length() > 0) {
					i = Integer.valueOf(o.toString());
				}
			} catch (Exception e) {
			}
		}
		return i;
	}
}
