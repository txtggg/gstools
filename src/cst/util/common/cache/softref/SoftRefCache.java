package cst.util.common.cache.softref;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cst.util.common.containers.Maps;
import cst.util.common.containers.Sets;

/**
 * @author gwc
 * @version 18.5 带时间限制的软引用缓存:超过指定时间(单位:分)或软引用失效都会导致清理缓存
 * 
 * @param <V>
 * @param <K>
 */
public class SoftRefCache<K, V> implements ISoftRefCache<K, V> {
	private Map<K, SoftReference<V>> map = Maps.newConcurrentHashMap();

	/**
	 * 使用put(k,null)可以移除缓存
	 */
	@Override
	public void put(K k, V v) {
		if (v == null) {
			remove(k);
		} else {
			map.put(k, new SoftReference<V>(v));
		}
	}

	@Override
	public V get(K k) {
		SoftReference<V> sv = map.get(k);
		if (sv == null) {
			remove(k);
			return null;
		}
		return sv.get();
	}

	private void remove(K k) {
		map.remove(k);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public void trim() {
		clearllegal();
		map = Maps.trim(map);
	}

	private synchronized void clearllegal() {
		Set<K> nullKey = Sets.newHashSet();
		Set<Entry<K, SoftReference<V>>> entrys = map.entrySet();
		for (Entry<K, SoftReference<V>> e : entrys) {
			SoftReference<V> sv = e.getValue();
			K k = e.getKey();
			if (sv == null) {
				nullKey.add(k);
			} else {
				V v = sv.get();
				if (v == null) {
					nullKey.add(k);
				}
			}
		}
		for (K k : nullKey) {
			remove(k);
		}

	}

}
