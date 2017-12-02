package cst.gu.util.string;

import java.io.File;
import java.util.List;
import java.util.Map;


import cst.gu.util.collection.ListUtil;
import cst.gu.util.file.FileUtil;

/**
 * @author guweichao 20170828 替换文本中的marker
 */
public class StringMarkerUtil {
	
	public static String setMarker(File file, Map<String, Object> markers) {
		String content = FileUtil.getFileContent(file);
		return setMarker(content,markers);
	}
	public static String setMarker(File file, Map<String, Object> markers,String charset) {
		String content = FileUtil.getFileContent(file,charset);
		return setMarker(content,markers);
	}
	
	public static String setMarker(String content, Map<String, Object> markers) {
		int[] flag = { 0 };
		while (flag[0] == 0) {
			content = copyLine(content, markers, flag);
		}
		flag[0] = 0;
		while (flag[0] == 0) {
			content = replaceFirst(content, markers, flag);
		}
		return content;
	}

	private static String copyLine(String content, Map<String, Object> markers, int[] flag) {
		int lineStart = content.indexOf("$@{line.start.");
		int lineEnd = content.indexOf("$@{line.end}");
		if (lineEnd > lineStart && lineStart > -1) {
			String c0 = content.substring(lineStart, lineEnd + 12);
			String sub = content.substring(lineStart + 14, lineEnd);
			int keyEnd = sub.indexOf("}");
			if (keyEnd > -1) {
				String listKey = sub.substring(0, keyEnd);
				sub = sub.substring(keyEnd + 1);
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> list = (List<Map<String, Object>>) markers.get(listKey);
				int[] arr = { 0 };
				sub = replaceList(sub, list, arr);
				return content.replace(c0, sub);
			}
		}
		flag[0] = 1;
		return content;
	}

	private static String replaceList(String content, List<Map<String, Object>> list, int[] flag) {
		if (!ListUtil.isEmpty(list) && StringUtil.isNotBlank(content)) {
			int[] arr = { 0 };
			StringBuilder sb = new StringBuilder();
			for (int x = 0; x < list.size(); x++) {
				Map<String, Object> mapx = list.get(x);
				String sub = content;
				arr[0] = 0;
				while (arr[0] != 1) {
					sub = replaceFirst(sub, mapx, arr);
				}
				sb.append(sub);
			}
			return sb.toString();
		}
		flag[0] = 1;
		return content;
	}

	private static String replaceFirst(String content, Map<String, Object> markers, int[] flag) {
		int start = content.indexOf("${");
		if (start > -1) {
			String marker = content.substring(start);
			int end = marker.indexOf("}");
			if (end > -1) {
				marker = marker.substring(0, end + 1);
				if (marker.length() > 3) {
					String markerKey = marker.substring(2, marker.length() - 1);
					return content.replace(marker, StringUtil.toString(markers.get(markerKey)));
				}
			}
		}
		flag[0] = 1;
		return content;
	}

}
