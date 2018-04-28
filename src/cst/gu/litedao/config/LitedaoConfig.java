package cst.gu.litedao.config;

/**
 * @author gwc
 * @version v201804
 */
public abstract class LitedaoConfig {
	private static String defaultCharset = null;
	private static boolean cache = false;
	
	public static void setCharset(String charset){
		defaultCharset = charset;
	}
	
	public static String charset(){
		return defaultCharset;
	}

	public static void setCache(boolean useCache){
		cache = useCache;
	}
	
	public static boolean useCache(){
		return cache;
	}
}
