package cst.util.common.file;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author gwc
 * @version v201804
 * File类辅助
 */
public class Files {
	public static String getFileContent(File file) {
		StringBuilder sb = new StringBuilder();
		FileReader fr = null;
		BufferedReader reader = null;
		try {
			fr = new FileReader(file);
			reader = new BufferedReader(fr);
			String tmp;
			while ((tmp = reader.readLine()) != null) {
				sb.append(tmp).append("\r\n");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			closeIO(reader,fr);
		}
		return sb.toString();
	}

	public static String getFileContent(File file, String charset) {
		StringBuilder sb = new StringBuilder();
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, charset);
			br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\r\n");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			closeIO(br,isr,fis);
		}

		return sb.toString();
	}

	public static String getFileContent(String file) {
		return getFileContent(new File(file));
	}

	public static boolean writeInfo(File file, String info) {
		return writeInfo(file,info.getBytes());
	}
	
	public static boolean writeInfo(File file, byte[] info) {
		if (file != null && info != null) {
			FileOutputStream fos = null;
			try {
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
				fos = new FileOutputStream(file);
				fos.write(info);
				return true;
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				closeIO(fos);
			}

		}
		throw new RuntimeException("文件和信息必须都不为null");
	}
	
	public static boolean writeInfo(String fileFullName, byte[] info) {
		return writeInfo(new File(fileFullName),info);
	}
	
	public static boolean writeInfo(String fileFullName, String info) {
		return writeInfo(new File(fileFullName),info);
	}

	public static byte[] getByte(File file) {
		if (file == null || !file.exists()) {
			return null;
		}
		byte[] buffer = null;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			closeIO(fis,bos);
		}
		return buffer;
	}
	
	/**
	 * @author guweichao
	 * 关闭Closeable接口,注意关闭顺序,从外层包装流开始,先开后关
	 * @param Iclose
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
