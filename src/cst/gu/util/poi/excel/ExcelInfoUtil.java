package cst.gu.util.poi.excel;

import java.util.List;

import cst.gu.util.container.Containers;
import cst.gu.util.string.StringUtil;

/**
 * 
 */
public class ExcelInfoUtil {
	private ExcelInfoUtil() {
	}

	/**
	 * @author guweichao 检查list中内容是否全为空(excel的内容一行都是空)
	 * @param list
	 * @return
	 */
	public static boolean isRowEmpty(List<String> list) {
		if (list == null || list.isEmpty()) {
			return true;
		}
		for (String s : list) {
			if (StringUtil.isNotTrimBlank(s)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查excel文件的标题(导入excel时,要检查头一行或者几行是不是指定的内容,避免导入非法文件)
	 * 本方法默认expectTitle不为空,且title.size==expectTitle.size
	 * (调用者根据expectTitle,获取对应的行数,进行比较,如果title为null或行数少于expect,
	 * 则应该直接认为不合法,不需要调用此方法)
	 * 
	 * @return
	 */
	public static boolean checkTitle(List<List<String>> title, List<List<String>> expectTitle) {
		if (!Containers.isEmpty(title) && !Containers.isEmpty(expectTitle) && title.size() == expectTitle.size()) {
			for (int x = 0, s = title.size(); x < s; x++) {
				List<String> row = title.get(x);
				List<String> expectRow = expectTitle.get(x);
				boolean re = isRowEmpty(row);
				boolean ere = isRowEmpty(expectRow);
				if(re != ere){
					return false;
				}else{
					if(!re){
						for (int y = 0; y < expectRow.size(); y++) {
							String cell = StringUtil.toString(row.get(y)).trim();
							String expectCell = StringUtil.toString(expectRow.get(y)).trim();
							if(!cell.equals(expectCell)){
								return false;
							}
						}
					}
				}
			}
			return true;
		}
		return false;
	}

}
