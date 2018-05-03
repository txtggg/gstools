package cst.util.common.cache.softref;

/**
 * @author gwc
 * @version 18.4 因为使用的软引用,null对象不会被缓存
 * @param <V>
 * @param <K>
 */
public interface ISoftRefCache<K, V> {
	/**
	 * @param k
	 * @param v,当存入null时,移除对应的缓存
	 */
	void put(K k, V v);

	V get(K k);

	void clear();

	/**
	 * 清理多余的空间:从map中移除已失效的缓存,并释放map多次扩容收缩导致的多占用的空间
	 */
	void trim();
}
