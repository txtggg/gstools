package cst.util.common.string;

import cst.gu.util.clazz.ClassUtil;

public class Objects { 
	
	private Objects() {
	}

	/**
	 * 获取windows classpath
	 * @return
	 */
	public static String getWindowsClassPath(){
		String path = ClassUtil.
				class
				.getClassLoader()
				.getResource("")
				.getPath();
		return path.substring(1);
	}
	
	public static Integer toInteger(Object o){
		if(o == null){
			return null;
		}
		Integer i = null;
		if(o instanceof Integer){
			i = (Integer) o;
		}else{
			try{
				String s = o.toString();
				if(s.length() == 0){
					return null;
				}
				i = Integer.valueOf(o.toString());
			}catch(Exception e){}
		}
		return i;
	}
}
