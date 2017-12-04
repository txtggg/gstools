package cst.gu.util.collection;

import java.util.HashSet;
import java.util.Set;

import cst.gu.util.container.Containers;


/**
 * @author guweichao 2017 1016
 * @deprecated
 * @see Containers
 */
public class SetUtil {

	public static <T> HashSet<T> newHashSet(T... ts) {
		HashSet<T> hs = new HashSet<T>();
		for (T t : ts) {
			hs.add(t);
		}
		return hs;
	}
	
	public static <T> HashSet<T> newHashSet() {
		HashSet<T> hs = new HashSet<T>();
		return hs;
	}
	
	public static <T> HashSet<T> newHashSetWithSize(int size) {
		return new HashSet<T>(size);
	}
	
	/**
	 * @author guweichao 20171016
	 * @param <T>
	 * @return set ==  null || set.isEmpyt();
	 */
	public static <T> boolean isEmpty(Set<T> set){
		return set == null || set.isEmpty();
	}
	
	/**
	 * @author guweichao 20171016
	 * @param <T>
	 * @return !isEmpyt(set);
	 */
	public static <T> boolean isNotEmpty(Set<T> set){
		return !isEmpty(set);
	}
}
