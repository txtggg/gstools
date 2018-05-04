package cst.util.common.cache.softref;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cst.gu.util.datetime.LocalDateUtil;
import cst.util.common.containers.Maps;
import cst.util.common.containers.Sets;

/**
 * @author gwc
 * @version 18.5 带时间限制的软引用缓存:超过指定时间(单位:分)或软引用失效都会导致清理缓存
 * 
 * @param <V>
 * @param <K>
 */
public class TimedSoftRefCache2<K, V> implements ISoftRefCache<K, V> {
	// map.size * trimRate < maxSize 并且maxSize >trimSize则执行trim
	// (因为map扩容后再删除,他占用的空间不会收缩)
	private static final int trimRate = 2;
	private static final int trimSize = 1024;
	private int maxSize = 0;
	private Map<K, SoftReference<V>> map = Maps.newConcurrentHashMap();
	private Map<K, Long> keyTimes = initKeyTimeMap();

	private int overTime = 0;
	private int countMod = 0;

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
	 * 多线程后台开始trim
	 * 
	 * @param countMod
	 */
	private void backTrim(int ctmod) {
		final int cm = ctmod;
		final int ovt = getOverTime();
		class ThRunner implements Runnable {
			@Override
			public void run() {
				while (cm == countMod) {
					try {
						System.out.println("countMod:" + countMod);
						if (overTime > 0) {
							Thread.sleep(overTime / 2);
							if (cm == countMod) {
								System.out.println("执行trim操作,countMod:" + cm + ",时间:" + LocalDateUtil.getNow());
								trim();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println(LocalDateUtil.getNow());
				System.out.println("cm != countMod,结束本设置的trim,对应的countMod为:" + cm + ",对应的overTime为:" + ovt);
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
		System.out.println("设置超时时间为:" + overTime + "min");
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
