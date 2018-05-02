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
	public static <T> ArrayList<T> newArrayList(){
		return new ArrayList<T>();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> trim(List<T> list){
		List<T> rl = null;
		if(list instanceof ArrayList ){
			rl = newArrayListSized(list.size());
		}else{
			try {
				rl = list.getClass().newInstance();
			} catch ( Exception e) {
				e.printStackTrace();
			}  
		}
		rl.addAll(list);
		return rl;
	}
	
	/**
	 * 返回一个arraylist
	 * 将指定初始值放入
	 * @return
	 */
	public static <T> List<T> newArrayList(T...t){
		ArrayList<T> rlist = newArrayList();
		for(T ti : t){
			rlist.add(ti);
		}
		return rlist;
	}
	
	/**
	 * 返回一个指定大小的arraylist
	 * @param size
	 * @return
	 */
	public static <T> ArrayList<T> newArrayListSized(int size){
		return new ArrayList<T>(size);
	}
	
	public static <T> T tryGet(List<T> list,int index){
		if(list == null || list.size() <= index){
			return null;
		}
		return list.get(index);
	}
	
	public static <T> boolean isEmpty(List<T> list){
		return list ==null || list.isEmpty();
	}
	
}
