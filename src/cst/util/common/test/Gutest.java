package cst.util.common.test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cst.util.common.cache.softref.TimedSoftRefCache2;
import cst.util.common.containers.Lists;
import cst.util.common.containers.Maps;
import cst.util.common.object.Objects;

/**
 * @author gwc
 * @version 18.4 null对象不会被缓存 不应该直接使用,应该使用其他类来管理cache的存入,清理,获取真实值等
 * @param <V>
 * @param <K>
 */
public class Gutest<K, V> {

	public static void main(String[] args) {
		int x = 1;
		testSoftRef(x);
	}

	public static String testSoftRef(Object o) {
		System.out.println(o.getClass());
		return null;
	}

}
