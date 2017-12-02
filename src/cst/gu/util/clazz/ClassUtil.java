package cst.gu.util.clazz;

/**
 * @author guweichao 20170411 为Object类转为其他类时处理默认情况
 * 
 * 将Object转为数值类型时,可能发生RuntimeException。如果不需要处理这些异常直接使用0来代替,则可以使用tryToX方法来处理
 * 将Object转为String时,将null转为""
 */
public  class ClassUtil { 
	
	public static String getWindowsClassPath(){
		String path = ClassUtil.
				class
				.getClassLoader()
				.getResource("")
				.getPath();
		return path.substring(1);
	}
	 
}
