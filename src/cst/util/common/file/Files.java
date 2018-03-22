package cst.util.common.file;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author guweichao 20170411 为Object类转为其他类时处理默认情况
 * 
 *         将Object转为数值类型时,可能发生RuntimeException。如果不需要处理这些异常直接使用0来代替,
 *         则可以使用tryToX方法来处理 将Object转为String时,将null转为""
 */
public class Files { 
	
	/**
	 * @author gwc
	 * 关闭Closeable接口,注意关闭顺序,从外层包装流开始,先开后关
	 * @param Closeable
	 */
	public static void closeIO(Closeable ... Iclose){
		for(Closeable c: Iclose ){
			if( c != null ){
				try{
					c.close();
				}catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
		}
	}

}
