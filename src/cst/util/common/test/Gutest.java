package cst.util.common.test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cst.util.common.cache.softref.TimedSoftRefCache;
import cst.util.common.containers.Lists;
import cst.util.common.containers.Maps;

/**
 * @author gwc
 * @version 18.4 null对象不会被缓存 不应该直接使用,应该使用其他类来管理cache的存入,清理,获取真实值等
 * @param <V>
 * @param <K>
 */
public class Gutest<K, V> {
	
	public static void main(String[] args) {
		Map<String,Object> lm = new LinkedHashMap<String, Object>(16,0.75F,true){
			@Override
			protected boolean removeEldestEntry(java.util.Map.Entry<String, Object> eldest) {
				// TODO 自动生成的方法存根
				return false;
			}
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
		};
		
		lm.put("1", 1);
		lm.put("2", 2);
		lm.put("3", 3);
		lm.put("4", 4);
		lm.put("2", 4);
		lm.get("3");
//		lm.get("1");
		for(Entry<String, Object> e : lm.entrySet()){
			System.out.println(e.getKey());
		}
		
	}
	
	public static void testSoftRef(Map<String,Object> map){
		map = null;
	}

	 
}
