package cst.gu.util.map;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cst.gu.util.container.Containers;

/**
 * @author guweichao 2017 1016
 * @deprecated
 * @see Containers
 */
public class MapUtil {

	
	
	public static <K, V> HashMap<K,V> newHashMap(){
		return new HashMap<K, V>();
	}
	
	public static <K, V> HashMap<K,V> newHashMapWithSize(int size){
		return new HashMap<K, V>(size);
	}
	
	public static <K, V> LinkedHashMap<K,V> newLinkedHashMap(){
		return new LinkedHashMap<K, V>();
	}
	
	public static <K, V> LinkedHashMap<K,V> newLinkedHashMapWith(int size){
		return new LinkedHashMap<K, V>(size);
	}
	
	public static <K, V> ConcurrentHashMap<K,V> newConcurrentHashMap(){
		return new ConcurrentHashMap<K, V>();
	}
	/**
	 * 
	 * @param map
	 * @return map == null || map.isEmpty()
	 */
	public static <K,V> boolean isEmpty(Map<K,V> map){
		return map == null || map.isEmpty();
	}
	
	/**
	 * 
	 * @param map
	 * @return !isEmpty(map)
	 */
	public static <K,V> boolean isNotEmpty(Map<K,V> map){
		return !isEmpty(map);
				
	}
	
	
	 
}
