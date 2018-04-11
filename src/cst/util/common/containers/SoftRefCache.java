package cst.util.common.containers;

import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gwc
 * @version 18.4 null对象不会被缓存
 * 不应该直接使用,应该使用其他类来管理cache的存入,清理,获取真实值等
 * @param <V>
 * @param <K>
 */
public abstract class SoftRefCache<K, V> {
	private Map<K, SoftReference<V>> map = new ConcurrentHashMap<K, SoftReference<V>>();

	/**
	 * null对象不会被缓存
	 * 
	 * @param k
	 * @param v
	 */
	public void put(K k, V v) {
		if (v != null) {
			map.put(k, new SoftReference<V>(v));
		}
	}

	public V get(K k) {
		SoftReference<V> sv = map.get(k);
		if (sv == null) {
			return null;
		}
		V v = sv.get();
		return v;

	}

	public void remove(K k) {
		map.remove(k);
	}

	public void clearAll() {
		map.clear();
	}

	public void clearNull() {
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

}
