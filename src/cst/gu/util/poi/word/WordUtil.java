package cst.gu.util.poi.word;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


import cst.gu.util.file.FileUtil;
import cst.gu.util.string.StringMarkerUtil;
import cst.gu.util.string.StringUtil;
import cst.util.common.file.FileTypeException;

/**
 * @author guweichao 20170919 
 * 使用*.doc或*.docx另存为xml文件后,调用本方法
 * 因为word中每个字都可以设置格式,可能导致${*}的marker被格式标签切断,从而marker失效,数据出现错误
 * 一个marker必须作为一个整体设置格式,否则字符串会被word的格式标签切断
 * 即使重新为整体设置格式,marker中间还是会有标签,导致无法正常替换
 * 如果发生marker不能工作,需要删除此标签,重新输入,并整体设置格式,
 * 由于word格式过于复杂,请制作好模板后,在markers中放入所有的marker数据,进行测试生成的文件.确认每一个marker都正常工作,数据都出现
 * 
 */
public class WordUtil {
	private static String exInfo = "请使用xml作为模板(doc或者docx设置好标记信息(${key} ${list.key})后,另存为xml文件)";

	public static String templateExport(String filePath, Map<String, Object> markers) {
		return templateExport(new File(filePath), markers);
	}

	/**
	 * 模板导出 使用xml作为模板 doc或者docx设置好标记信息(${key} ${list.key})后,另存为xml文件
	 * 
	 * @param file
	 */
	public static String templateExport(File file, Map<String, Object> markers) {
		if (file.getName().endsWith(".xml")) {
			String info = getTempFile(file);
			return StringMarkerUtil.setMarker(info, markers);
		} else {
			throw new FileTypeException(exInfo);
		}

	}

	/**
	 * 获取此xml文件的temp文件(因为要把list marker变为
	 * stringmarkerutil可以处理的文件,写入tmp文件,然后根据文件的修改时间,分析tmp是否需要重新生成)
	 * 
	 * @param file
	 */
	private static String getTempFile(File file) {
		File pd = file.getParentFile();
		File tmpDir = new File(pd, file.getName().substring(0, file.getName().length() - 4) + "xml");
		if (!tmpDir.exists()) {
			tmpDir.mkdirs();
		}
		File tfile = new File(tmpDir, StringUtil.toString(file.lastModified()));
		if (!tfile.exists()) {
			String info = FileUtil.getFileContent(file);
			if (!containsMarker(info)) {
				throw new RuntimeException("没有marker标记${*}," + exInfo);
			}
			if (containsListMarker(info)) {// 如果不含有listmarker 则不需要处理,直接使用源文件
				info = changeListMarker(info);
			}
			FileUtil.writeInfo(tfile, info);

		}
		return FileUtil.getFileContent(tfile);
	}

	/**
	 * 替换list marker
	 * 
	 * @param info
	 * @return
	 */
	private static String changeListMarker(String info) {
		StringBuilder sb = new StringBuilder();
		boolean b = true;

		while (b) {
			int x = info.indexOf("<w:tr>");
			int y = info.indexOf("</w:tr>");
			if (x == -1 || y == -1) {
				b = false;
			} else {
				if (x > y) {
					throw new RuntimeException("文件异常:" + exInfo);
				}
				sb.append(info.substring(0, x));
				String tmp = info.substring(x, y + 7);
				info = info.substring(y + 7);
				if (containsListMarker(tmp)) {
					String lname = null; // ${list.key}中 的list
					boolean sf = true;
					StringBuilder sub = new StringBuilder();
					while (sf) {
						int s = tmp.indexOf("${");
						if (s < 0) {
							sub.append(tmp);
							sf = false;
						} else {
							sub.append(tmp.substring(0, s));
							tmp = tmp.substring(s);
							int e = tmp.indexOf("}");
							if (e < 0) {
								throw new RuntimeException("文件异常:标记信息不完整,缺少'}'");
							}
							String marker = tmp.substring(0, e + 1);
							int d = marker.indexOf(".");
							if (d < 0) {
								throw new RuntimeException("在表格的一行中,不能同时使用两种marker:${key} 和 ${list.key}");
							}
							String sname = marker.substring(2, d);
							if (lname == null) {
								lname = sname;
							} else {
								if (!lname.equals(sname)) {// 一行中有两种list
									throw new RuntimeException(
											"在表格的一行中,不能同时使用两种marker:${" + lname + ".*} 和${" + sname + ".*}");
								}
							}
							sub.append("${" + marker.substring(d + 1, marker.length() - 1)).append("}");
							tmp = tmp.substring(e + 1);
						}
					}
					sb.append("$@{line.start.").append(lname).append("}");
					sb.append(sub);
					sb.append("$@{line.end}");
				} else {
					sb.append(tmp);
				}

			}

		}
		sb.append(info);
		return sb.toString();
	}

	/**
	 * 是否含有marker :${*} 注意:本方法无法判断${*} 和${*.*} 需要再次根据containsListMaker
	 * 来判断maker类型
	 */
	private static boolean containsMarker(String str) {
		String reg = "[\\s\\S]*\\$\\{.+\\}[\\s\\S]*";
		return Pattern.matches(reg, str);
	}

	/**
	 * 是否含有list类marker :${list.key}
	 */
	private static boolean containsListMarker(String str) {
		String reg = "[\\s\\S]*\\$\\{.+\\..+\\}[\\s\\S]*";
		return Pattern.matches(reg, str);
	}

	public static void main(String[] args) {

		Map<String, Object> mks = new HashMap<String, Object>();
		mks.put("tg1", "guweichao-title");
		mks.put("kb", "guweichao-kb");
		mks.put("kb2", "guweichao-kb2");
		List<Map<String, Object>> l1 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> l2 = new ArrayList<Map<String, Object>>();
		mks.put("kk", l1);
		mks.put("ks", l2);

		Map<String, Object> m1 = new HashMap<String, Object>();
		Map<String, Object> m2 = new HashMap<String, Object>();
		Map<String, Object> m3 = new HashMap<String, Object>();
		m1.put("t1", "ts1");
		m1.put("t2", "ts2");
		m2.put("t1", "t2s1");
		m2.put("t2", "t2s2");
		m3.put("t1", "t3s1");
		m3.put("t2", "t3s2");
		l1.add(m1);
		l1.add(m2);

		l2.add(m3);
		l2.add(m2);

		File wod = new File("D:\\mypc\\Desktop\\ttt.xml");
		String nw = templateExport(wod, mks);
		File nf = new File("D:\\mypc\\Desktop\\gt.doc");
		FileUtil.writeInfo(nf, nw);

	}
}
