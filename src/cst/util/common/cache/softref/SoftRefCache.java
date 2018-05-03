package cst.util.common.cache.softref;

/**
 * @author gwc
 * @version 18.4 因为使用的软引用,null对象不会被缓存
 * @param <V>
 * @param <K>
 */
public interface SoftRefCache<K, V> {
	/**
	 * null对象不会被缓存
	 * 
	 * @param k
	 * @param v
	 */
	void put(K k, V v);

	V get(K k);

	void remove(K k);

	void clearAll();

	/**
	 * 清理多余的空间:移除已失效的缓存,并释放已被map移除,但仍然存在于原始数组中的数据
	 */
	void trim();
}
