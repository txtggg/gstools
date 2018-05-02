package cst.util.common.cache;

import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cst.util.common.containers.Maps;

/**
 * @author gwc
 * @version 18.4 null对象不会被缓存
 * 不应该直接使用,应该使用其他类来管理cache的存入,清理,获取真实值等
 * @param <V>
 * @param <K>
 */
public class NumberLimitSoftRefCache  implements SoftRefCache<K,V>{ }
