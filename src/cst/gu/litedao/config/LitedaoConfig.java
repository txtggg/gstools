package cst.gu.litedao.config;

/**
 * @author gwc
 * 应该在basedao等基础类中,使用static代码块调用,仅执行一次
 * @version v201804
 */
public abstract class LitedaoConfig {
	private static String defaultCharset = null;
	
	/**
	 * 设置litedao的字符集编码格式.默认null,使用不带字符集的方法,可能导致系统升级时乱码
	 * @param charset
	 */
	public static void setCharset(String charset){
		defaultCharset = charset;
	}
	
	public static String charset(){
		return defaultCharset;
	}

}
