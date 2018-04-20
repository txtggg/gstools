package cst.gu.util.sql.test;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import cst.gu.util.container.Containers;
import cst.gu.util.datetime.LocalDateUtil;
import cst.gu.util.string.StringUtil;
import cst.util.common.containers.Lists;
import cst.util.common.file.Files;
import cst.util.common.poi.excel.ExcelWriter;
import cst.util.common.string.Strings;

/**
 * @author guweichao 20171102
 * 
 */
public class MainTest {
	private static final String markerReg1 = "\\$\\{.*..*\\}";
	private static final String markerReg2 = "\\$\\{.*\\}";
	public static void main(String[] args) { 
		System.out.println(Strings.getWindowsClassFilePath(MainTest.class));;
	}

	public static void ts() {
		Map<String, Object> map = new HashMap<String, Object>();
		byte[] abyDate = (byte[]) map.get("byte_abyData");
		byte[] abyHead = (byte[]) map.get("byte_abyHead");

		checkNull(abyDate);
		checkNull(abyHead);
		byte[] byte_allStruct = new byte[28 + abyDate.length + abyHead.length];
		System.out.println(new String(byte_allStruct));
	}

	public static void checkNull(Object o) {
		if (o == null) {
			throw new IllegalStateException("npe");
		}
	}
}

enum PathType {
	isemp, temp, file, tmp, birt, swf, pdf2swf, fonts, ckImages, templateHome, repositories, fileLib, macro, javascript
}
