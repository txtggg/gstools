package cst.util.common.cache.softref;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cst.util.common.containers.Maps;
import cst.util.common.containers.Sets;

/**
 * @author gwc
 * 使用linkedhashmap来保存时间,当缓存map的实际占用容量较大,而当前size较小时,执行trim清理空间
 * @version 18.5 带时间限制的软引用缓存:超过指定时间(单位:分)或软引用失效都会导致清理缓存
 * 以最近一次的get或put操作定义超时时间
 * @see TimedSoftRefCache
 * @param <V>
 * @param <K>
 */
public class TimedSoftRefCache2<K, V> implements ISoftRefCache<K, V> {
	// map.size * trimRate < maxSize 并且maxSize >trimSize则执行trim
	// (因为map扩容后再删除,他占用的空间不会收缩)
	private static final int trimRate = 2;
	private static final int trimSize = 1024;
	private static final int timeNumber = 60 * 1000;// 时间转换系数,从系统的ms转换为设置的时间minite
	private int maxSize = 0;
	private Map<K, SoftReference<V>> map = Maps.newConcurrentHashMap();
	private Map<K, Long> keyTimes = initKeyTimeMap();

	private int overTime = 0;

	private LinkedHashMap<K, Long> initKeyTimeMap() {
		return new LinkedHashMap<K, Long>(16, 0.75F, true);
	}

	private TimedSoftRefCache2() {

	}

	/**
	 * 创建实例
	 * 
	 * @return
	 */
	public static <K, V> TimedSoftRefCache2<K, V> newInstance() {
		return new TimedSoftRefCache2<K, V>();
	}

	public int size() {
		return map.size();
	}

	/**
	 * 使用put(k,null)可以移除缓存
	 */
	@Override
	public void put(K k, V v) {
		if (v == null) {
			remove(k);
		} else {
			map.put(k, new SoftReference<V>(v));
			keyTimes.put(k, System.currentTimeMillis() / 1000);
		}
		int s = map.size();
		if (maxSize < s) {
			maxSize = s;
		} else if (maxSize > trimSize && s * trimRate < maxSize) {
			trim();
		}
	}

	@Override
	public V get(K k) {
		SoftReference<V> sv = map.get(k);
		if (sv == null) {
			return null;
		}
		V v = sv.get();
		if (v != null) {
			keyTimes.put(k, System.currentTimeMillis() / 1000);
		}
		return v;
	}

	private void remove(K k) {
		map.remove(k);
		keyTimes.remove(k);
	}

	@Override
	public void clear() {
		map.clear();
		keyTimes.clear();
	}

	@Override
	public void trim() {
		clearllegal();
		keyTimes = Maps.trim(keyTimes);
		map = Maps.trim(map);
	}

	private synchronized void clearllegal() {
		long over = System.currentTimeMillis() - overTime; // overTime从minite转为ms
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
				} else {
					if (overTime > 0) {
						Long kt = keyTimes.get(k);
						if (kt != null && kt > over) {
							nullKey.add(k);
						}
					}
				}
			}
		}
		for (K k : nullKey) {
			remove(k);
		}
	}


	/**
	 * 设置失效时间 当设置为<=0时,不超时
	 * 
	 * @param overTime:分
	 * @return
	 */
	public void setOverTime(int overTime) {
		this.overTime = overTime * timeNumber;
	}

	/**
	 * 获取当前设置的失效时间
	 * 
	 * @param overTime:分
	 * @return
	 */
	public int getOverTime() {
		return overTime / timeNumber;
	}
}
