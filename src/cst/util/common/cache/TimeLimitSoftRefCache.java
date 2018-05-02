package cst.util.common.cache;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cst.util.common.containers.Maps;
import cst.util.common.containers.Sets;

/**
 * @author gwc
 * @version 18.5 因为使用软引用,不能缓存null对象
 * 
 * @param <V>
 * @param <K>
 */
public class TimeLimitSoftRefCache<K, V> implements SoftRefCache<K, V> {
	private Map<K, SoftReference<V>> map = Maps.newConcurrentHashMap();
	private Map<K, Long> keyTimes = Maps.newConcurrentHashMap();// 缓存的时间

	private int overTime = 0;
	private int countMod = 0;

	@Override
	public void put(K k, V v) {
		if (v != null) {
			map.put(k, new SoftReference<V>(v));
			keyTimes.put(k, System.currentTimeMillis() / 1000);
		}
	}

	@Override
	public V get(K k) {
		 SoftReference<V> sv = map.get(k);
		 V v = sv.get();
		return null;
	}

	@Override
	public void remove(K k) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void clearAll() {
		map.clear();
		keyTimes.clear();
	}

	@Override
	public void trim() {
		clearllegal();
		keyTimes = Maps.trim(keyTimes);
		map = Maps.trim(map);
	}

	private void clearllegal() {
		long over = System.currentTimeMillis() - overTime * 1000;
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
						if (keyTimes.get(k) > over) {
							nullKey.add(k);
						}
					}
				}
			}
		}
		for (K k : nullKey) {
			map.remove(k);
			keyTimes.remove(k);
		}
	}

	private void removeOverTime(int countMod) {
		while (overTime > 0) {
			System.out.println(countMod);
			while (countMod == this.countMod) {
				System.out.println("第" + countMod + "次设置,清理缓存运行中");
				trim();
			}
		}
	}

	/**
	 * 设置失效时间
	 * 
	 * @param overTime:秒
	 * @return
	 */
	public void setOverTime(int overTime) {
		this.overTime = overTime;
		removeOverTime(++countMod);
	}

	/**
	 * 获取当前设置的失效时间
	 * 
	 * @return
	 */
	public int getOverTime() {
		return overTime;
	}
}
