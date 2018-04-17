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
	
	public static <T> HashSet<T> newHashSet(T...t) {
		HashSet<T> set = newHashSet();
		for(T ti :t){
			set.add(ti);
		}
		return set;
	}

	public static <T>  boolean isEmpty(Set<T> set){
		return set == null || set.isEmpty();
	}
}
