package cst.util.common.containers;

import java.util.HashMap;
import java.util.Map;


/**
 * @author gwc
 * @version 18.3
 */
public class Maps {

	public static <K, V> HashMap<K,V> newHashMap(){
		return new HashMap<K, V>();
	}
	
	public static <K, V> HashMap<K,V> newHashMapSized(int size){
		return new HashMap<K, V>(size);
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
	
	public static void map2List(){
		
	}
	 
}
