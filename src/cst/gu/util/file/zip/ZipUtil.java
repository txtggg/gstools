package cst.gu.util.file.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import cst.gu.util.file.FileUtil;

/**
 * 
 * @author guweichao 20171009 依赖于apache 的ant包
 *
 */
public class ZipUtil {

	private static final String separator = File.separator;
	private static final int BUFFER = 10240;

	/**
	 * gbk文件编码格式调用(中文windows)
	 * @param zipPath  要压缩的文件(文件或文件夹)
	 * @param zipFile  压缩后的文件
	 * @return
	 */
	public static boolean zipGbk(File zipPath, File zipFile) {
		return zip(zipPath,zipFile,"gbk");
	}
	
	/**
	 * gbk文件编码格式调用(中文windows)
	 * @param zipPath  要压缩的文件(文件或文件夹)
	 * @param zipFile  压缩后的文件
	 * @return
	 */
	public static boolean zipGbk(String zipPath, String zipFile) {
		return zip(zipPath,zipFile,"gbk");
	}
	
	/**
	 *	utf8文件编码格式调用(一般linux)
	 * @param zipPath  要压缩的文件(文件或文件夹)
	 * @param zipFile  压缩后的文件
	 * @return
	 */
	public static boolean zipUtf8(File zipPath, File zipFile) {
		return zip(zipPath,zipFile,"utf8");
	}
	
	/**
	 *	utf8文件编码格式调用(一般linux)
	 * @param zipPath  要压缩的文件(文件或文件夹)
	 * @param zipFile  压缩后的文件
	 * @return
	 */
	public static boolean zipUtf8(String zipPath, String zipFile) {
		return zip(zipPath,zipFile,"utf8");
	}
	
	/**
	 * 
	 * @param zipPath
	 *            要压缩的文件(文件或文件夹)
	 * @param zipFile
	 *            压缩后的文件
	 * @param charset
	 *            文件字符集,(一般中文windows为gbk,linux为utf8),为避免乱码,应该根据具体情况指定
	 */
	public static boolean zip(File zipPath, File zipFile, String charset) {
		boolean r = true;
		CheckedOutputStream cos = null;
		ZipOutputStream zos = null;
		try {
			cos = new CheckedOutputStream(new FileOutputStream(zipFile), new CRC32());
			zos = new ZipOutputStream(cos);
			zos.setEncoding(charset);// File srcFile, File destFile
			zipFile(zipPath, zos, "");
		} catch (IOException e) {
			r = false;
			e.printStackTrace();
		} finally {
			FileUtil.closeIO(zos);
		}
		return r;

	}
	
	/**
	 * 
	 * @param zipPath
	 *            要压缩的文件(文件或文件夹)
	 * @param zipFile
	 *            压缩后的文件
	 * @param charset
	 *            文件字符集,(一般中文windows为gbk,linux为utf8),为避免乱码,应该根据具体情况指定
	 */
	public static boolean zip(String zipPath, String zipFile, String charset) {
		return zip(new File(zipPath),new File(zipFile),charset);
	}

	private static void zipFile(File file, ZipOutputStream zos, String basePath) throws IOException {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			basePath = basePath + file.getName() + separator;
			if (files == null || files.length == 0) {
				ZipEntry ze = new ZipEntry(basePath);
				zos.putNextEntry(ze);
				zos.closeEntry();
			} else {
				for (File f : files) {
					zipFile(f, zos, basePath);
				}
			}
		} else {
			ZipEntry entry = new ZipEntry(basePath + file.getName());
			zos.putNextEntry(entry);
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1) {
				zos.write(data, 0, count);
			}
			FileUtil.closeIO(bis,zos);
		}
	}

	/**
	 * 
	 * @param zipFile
	 *            要解压的文件
	 * @param unzipPath
	 *            解压路径
	 * @param deletePath
	 *            当指定的路径(unzipPath)已存在时,是否删除此文件夹 [选择是,则强制删除已存在文件夹;
	 *            选择否,则已存在时直接抛出异常]
	 * @return
	 */
	public static boolean unzip(File zipFile, File unzipPath, boolean deletePath) {
		String path = unzipPath.getAbsolutePath();
		byte[] buffers = new byte[BUFFER];
		if (unzipPath.exists()) {
			if (deletePath) {
				unzipPath.delete();
			} else {
				throw new RuntimeException("指定的路径文件夹已存在,无法创建同名文件夹");
			}
		}
		unzipPath.mkdirs();
		ZipOutputStream zos = null;
		ZipFile zip = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			zip = new ZipFile(zipFile);
			zos = new ZipOutputStream(zipFile);
			@SuppressWarnings("unchecked")
			Enumeration<ZipEntry> entrys = zip.getEntries();
			while (entrys.hasMoreElements()) {
				ZipEntry entry = entrys.nextElement();
				String epath = path + separator + entry.getName();
				if (epath.endsWith(separator)) { // 文件夹 ,创建文件夹
					new File(epath).mkdirs();
				} else {
					bos = new BufferedOutputStream(new FileOutputStream(epath));
					bis = new BufferedInputStream(zip.getInputStream(entry));
					int count;
					while ((count = bis.read(buffers, 0, BUFFER)) != -1) {
						bos.write(buffers, 0, count);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			FileUtil.closeIO(zos,bos,bis);
		}
		return true;
	}

	/**
	 * 
	 * @param zipFile
	 *            要解压的文件
	 * @param unzipPath
	 *            解压路径
	 * @return
	 */
	public static boolean unzip(File zipFile, String unzipPath, boolean deletePath) {
		return unzip(zipFile, new File(unzipPath), deletePath);
	}


}