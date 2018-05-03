package cst.util.common.cache.softref;


/**
 * @author gwc
 * @version 18.4 null对象不会被缓存
 * 不应该直接使用,应该使用其他类来管理cache的存入,清理,获取真实值等
 * @param <V>
 * @param <K>
 */
public class NumberedSoftRefCache<K,V> implements ISoftRefCache<K,V>{

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
		// TODO 自动生成的方法存根
		
	} }
