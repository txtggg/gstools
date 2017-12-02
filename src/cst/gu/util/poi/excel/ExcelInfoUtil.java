package cst.gu.util.poi.excel;

import java.util.List;

import cst.gu.util.string.StringUtil;

/**
 * 
 */
public class ExcelInfoUtil {
	private ExcelInfoUtil() {
	}
	
	/**
	 * @author guweichao
	 * 检查list中内容是否全为空(excel的内容一行都是空)
	 * @param list
	 * @return
	 */
	public static boolean isRowEmpty(List<String> list){
		if(list == null || list.isEmpty()){
			return true;
		}
		for(String s : list){
			if(StringUtil.isNotTrimBlank(s)){
				return false;
			}
		}
		return true;
	}
	
}
