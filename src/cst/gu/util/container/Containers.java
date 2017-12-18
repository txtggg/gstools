package cst.gu.util.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cst.guweichao 2017 1129 容器工具类
 * set map list
 * 代替listUtil setutil maputil
 */
public class Containers {

	public static <V, K> boolean isEmpty(Map<K, V> map) {
		return map == null || map.isEmpty();
	}

	public static <E> boolean isEmpty(List<E> list) {
		return list == null || list.isEmpty();
	}

	public static <T> boolean isEmpty(Set<T> set) {
		return set == null || set.isEmpty();
	}
	
	public static <V, K> boolean isNotEmpty(Map<K, V> map) {
		return !isEmpty(map);
	}

	public static <E> boolean isNotEmpty(List<E> list) {
		return !isEmpty(list);
	}

	public static <T> boolean isNotEmpty(Set<T> set) {
		return !isEmpty(set);
	}

	/**
	 * 指定初始大小,创建arraylist
	 * @param initSize
	 * @return
	 */
	public static <E> ArrayList<E> newArrayListSize(int initSize) {
		return new ArrayList<E>(initSize);
	}

	/**
	 * 创建arraylist并放入指定初始数据
	 * @param es
	 * @return
	 */
	public static <E> ArrayList<E> newArrayList(E... es) {
		ArrayList<E> list = new ArrayList<E>(es.length);
		for (E e : es) {
			list.add(e);
		}
		return list;
	}
	
	public static <E> ArrayList<E> newArrayList( ) {
		ArrayList<E> list = new ArrayList<E>();
		return list;
	}

	/**
	 * 根据index获取list中的数据,如果list为null或者index越界,则返回null,不会抛出异常
	 * @param list
	 * @param index
	 * @return
	 */
	public static <T> T tryGet(List<T> list, int index) {
		if (list != null && list.size() > index) {
			return list.get(index);
		}
		return null;
	}

	public static <T> HashSet<T> newHashSet(T... ts) {
		HashSet<T> hs = new HashSet<T>(ts.length);
		for (T t : ts) {
			hs.add(t);
		}
		return hs;
	}
	
	public static <T> HashSet<T> newHashSet() {
		HashSet<T> hs = new HashSet<T>();
		return hs;
	}

	public static <T> HashSet<T> newHashSetSize(int size) {
		return new HashSet<T>(size);
	}

	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}

	public static <K, V> HashMap<K, V> newHashMap(int initSize) {
		return new HashMap<K, V>(initSize);
	}

	public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
		return new LinkedHashMap<K, V>();
	}

	public static <K, V> LinkedHashMap<K, V> newLinkedHashMapSize(int size) {
		return new LinkedHashMap<K, V>(size);
	}

	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
		return new ConcurrentHashMap<K, V>();
	}

}
