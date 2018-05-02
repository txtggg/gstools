package cst.util.common.containers;

import java.util.HashSet;
import java.util.Set;


/**
 * @author gwc
 * @version 18.3
 */
public class Sets {

	public static <T> HashSet<T> newHashSet() {
		return new HashSet<T>();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Set<T> trim(Set<T> set) {
		Set<T> rs = null;
		if(set instanceof HashSet){
			rs = newHashSet(set);
		}else{
			try {
				rs = set.getClass().newInstance();
				rs.addAll(set);
			} catch ( Exception e) {
				e.printStackTrace();
			}  
		}
		
		return rs;
	}
	
	/**
	 * 创建一个hashset并加入指定的数据
	 * @param t
	 * @return
	 */
	public static <T> HashSet<T> newHashSet(T...t) {
		HashSet<T> set = newHashSet();
		for(T ti :t){
			set.add(ti);
		}
		return set;
	}
	
	public static <T> HashSet<T> newHashSet(Set<T> set) {
		HashSet<T> rset = new HashSet<T>(set);
		return rset;
	}
	
	public static <T> HashSet<T> newHashSet(int size) {
		HashSet<T> set = new HashSet<T>(size);
		return set;
	}

	public static <T>  boolean isEmpty(Set<T> set){
		return set == null || set.isEmpty();
	}
}
