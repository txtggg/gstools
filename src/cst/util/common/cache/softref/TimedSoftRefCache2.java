package cst.util.common.cache.softref;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cst.util.common.containers.Maps;
import cst.util.common.containers.Sets;

/**
 * @author gwc
 * 使用后台线程进行trim操作,获取实例时可选择根据put或get时间来定义超时时间
 * @version 18.5 带时间限制的软引用缓存:超过指定时间(单位:分)或软引用失效都会导致清理缓存
 * 
 * @see TimedSoftRefCache2
 * @param <V>
 * @param <K>
 */
public class TimedSoftRefCache2<K, V> implements ISoftRefCache<K, V> {

	private Map<K, SoftReference<V>> map = Maps.newConcurrentHashMap();
	private Map<K, Long> keyTimes = Maps.newConcurrentHashMap();// 缓存的时间

	private int overTime = 0;
	private int countMod = 0;
	private boolean putTimed ;

	private TimedSoftRefCache2(boolean putTimed) {
		this.putTimed = putTimed;
	}

	/**
	 * 超时限制类型为put时间
	 * @return
	 */
	public static <K, V> TimedSoftRefCache2<K, V> newPutTimedInstance() {
		return new TimedSoftRefCache2<K, V>(true);
	}

	/**
	 * 超时限制类型为get时间(上一次访问)
	 * @return
	 */
	public static <K, V> TimedSoftRefCache2<K, V> newGetTimedInstance() {
		return new TimedSoftRefCache2<K, V>(false);
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
	}

	@Override
	public V get(K k) {
		SoftReference<V> sv = map.get(k);
		if (sv == null) {
			return null;
		}
		V v = sv.get();
		if (!putTimed && v != null) {
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

	/**
	 * 执行trim操作,移除无效的缓存,并缩减map的容量以减少内存占用
	 */
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
	 * 多线程后台开始trim
	 * 
	 * @param countMod
	 */
	private void backTrim(int ctmod) {
		final int cm = ctmod;
		class ThRunner implements Runnable {
			@Override
			public void run() {
				while (cm == countMod) {
					try {
						if (overTime > 0) {
							Thread.sleep(overTime / 2);
							if (cm == countMod) {
								trim();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		ThRunner tr = new ThRunner();
		Thread th = new Thread(tr);
		th.start();
	}

	/**
	 * 设置失效时间 当设置为<=0时,不超时
	 * 
	 * @param overTime:分
	 * @return
	 */
	public void setOverTime(int overTime) {
		this.overTime = overTime * 60 * 1000;
		backTrim(++countMod);
	}

	/**
	 * 获取当前设置的失效时间
	 * 
	 * @param overTime:分
	 * @return
	 */
	public int getOverTime() {
		return overTime / 60 / 1000;
	}
}
