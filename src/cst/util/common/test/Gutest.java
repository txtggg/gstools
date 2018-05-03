package cst.util.common.test;

import java.util.List;
import java.util.Map;

import cst.util.common.cache.softref.TimedSoftRefCache;
import cst.util.common.containers.Lists;
import cst.util.common.containers.Maps;

/**
 * @author gwc
 * @version 18.4 null对象不会被缓存 不应该直接使用,应该使用其他类来管理cache的存入,清理,获取真实值等
 * @param <V>
 * @param <K>
 */
public class Gutest<K, V> {
	
	public static void main(String[] args) throws InterruptedException{ }
	
	public static void testSoftRef(Map<String,Object> map){
		map = null;
	}

	 
}
