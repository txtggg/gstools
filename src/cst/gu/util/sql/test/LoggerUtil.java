package cst.gu.util.sql.test;

import org.apache.log4j.Logger;


import cst.gu.util.string.StringUtil;

/**
 * @author guweichao 20171102
 * 
 */
public class LoggerUtil { 
	private static Logger log = Logger.getLogger(LoggerUtil.class);
	public static final String cutline = "---------------------------------------cutline------------------------------------------";
	public static void errorLog(Object... objects) {
		for (Object o : objects) {
			if (o instanceof Exception) {
				log.error(o);
				o = StringUtil.getExceptionInfo((Exception) o);
			}
			log.error(o);
		}
	}
	
	/**
	 * 保存日志到infolog.log并输出到控制台
	 * @param objects
	 */
	public static void infoLog(Object... objects) {
		for (Object o : objects) {
			if (o instanceof Exception) {
				log.info(o);
				o = StringUtil.getExceptionInfo((Exception) o);
			}
			log.info(o);
		}
	}
	
	
	/**
	 * 保存日志到debuglog.log并输出到控制台
	 * @param objects
	 */
	public static void debugLog(Object... objects) {
		for (Object o : objects) {
			if (o instanceof Exception) {
				log.debug(o);
				o = StringUtil.getExceptionInfo((Exception) o);
			}
			log.debug(o);
		}
	}
}
