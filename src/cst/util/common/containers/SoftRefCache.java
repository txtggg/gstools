package cst.util.common.containers;

import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author gwc
 * @version 18.4 null对象不会被缓存
 * 不应该直接使用,应该使用其他类来管理cache的存入,清理,获取真实值等
 * @param <V>
 * @param <K>
 */
public abstract class SoftRefCache<K, V> {
	private Map<K, SoftReference<V>> map = Maps.newConcurrentHashMap();

	/**
	 * null对象不会被缓存
	 * @param k
	 * @param v
	 */
	 void put(K k, V v) {
		if (v != null) {
			map.put(k, new SoftReference<V>(v));
		}
	}

	 V get(K k) {
		SoftReference<V> srv = map.get(k);
		if (srv == null) {
			return null;
		}
		V v = srv.get();
		return v;

	}

	 void remove(K k) {
		map.remove(k);
	}

	 void clearAll() {
		map.clear();
	}

	 void clearNull() {
		Set<K> nullKey = new HashSet<K>();
		Set<Entry<K, SoftReference<V>>> entrys = map.entrySet();
		for(Entry<K, SoftReference<V>> e :entrys){
			SoftReference<V> sv = e.getValue();
			if(sv == null){
				nullKey.add(e.getKey());
			}else{
				V v = sv.get();
				if(v == null){
					nullKey.add(e.getKey());
				}
			}
		}
		for(K k : nullKey){
			map.remove(k);
		}
	}
	 
	 /**
	  * 清理多余的空间
	  * 释放已被map移除,但仍然存在于原始数组中的数据
	  */
	 void trim(){
		 Map<K, SoftReference<V>> map1 = Maps.newConcurrentHashMap();
		 map1.putAll(map);
		 map = map1;
	 }
	
	
}
