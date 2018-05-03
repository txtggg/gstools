package cst.util.common.cache.softref;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;

import cst.util.common.containers.Maps;

/**
 * @author gwc
 * @version 18.4 null对象不会被缓存
 * 不应该直接使用,应该使用其他类来管理cache的存入,清理,获取真实值等
 * @param <V>
 * @param <K>
 */
public class NumberedSoftRefCache<K,V> implements ISoftRefCache<K,V>{
	private int maxSize = 0;
	private Map<K, SoftReference<V>> map = initMap();
	public static <K, V> NumberedSoftRefCache<K, V> getInstance(){
		return new NumberedSoftRefCache<K, V>();
	}

	@Override
	public void put(K k, V v) {
		// TODO 自动生成的方法存根
	}

	@Override
	public V get(K k) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void clear() {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void trim() {
		Map<K, SoftReference<V>> map1 = initMap();
		map1.putAll(map);
		map = map1;
	}

	private Map<K, SoftReference<V>> initMap(){
		Map<K, SoftReference<V>> map1 = new LinkedHashMap<K, SoftReference<V>>(){
			private static final long serialVersionUID = 1L;

			protected boolean removeEldestEntry(Map.Entry<K, SoftReference<V>> eldest) {
				if(maxSize > 0 && this.size() > maxSize){
					return true;
				}
				return false;
			};
		};
		return map1;
	} }
