package cst.util.common.containers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gwc
 * @version 18.3 
 * java.util.list的工具集
 */
public final class Lists {
	private Lists(){}
	
	/**
	 * 返回一个arraylist
	 * @return
	 */
	public static <T> List<T> newArrayList(){
		return new ArrayList<T>();
	}
	
	/**
	 * 返回一个指定大小的arraylist
	 * @param size
	 * @return
	 */
	public static <T> List<T> newArrayListSized(int size){
		return new ArrayList<T>(size);
	}
	
	public static <T> T tryGet(List<T> list,int index){
		if(list == null || list.size() <= index){
			return null;
		}
		return list.get(index);
	}
	
}
