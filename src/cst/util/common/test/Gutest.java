package cst.util.common.test;

import java.util.List;
import java.util.Map;

import cst.util.common.cache.softref.TimeLimitSoftRefCache;
import cst.util.common.containers.Lists;
import cst.util.common.containers.Maps;

/**
 * @author gwc
 * @version 18.4 null对象不会被缓存 不应该直接使用,应该使用其他类来管理cache的存入,清理,获取真实值等
 * @param <V>
 * @param <K>
 */
public class Gutest<K, V> {
	
	public static void main(String[] args) throws InterruptedException{
		TimeLimitSoftRefCache<String, String> tsrf = new TimeLimitSoftRefCache<String, String>();
		int s = 1000;
		tsrf.setOverTime(30);
		for(int i = 100;i<s;i++){
			for(int x =0;x<s;x++){
				String ks = String.valueOf(x);
				tsrf.put(ks,ks);
			}
		}
		System.out.println("gutest执行完成,缓存后台trim中");
		
	}
	
	public static void testRef(Map<String,Object> map){
		map = null;
	}

	public static void testTrim() throws InterruptedException {
		Runtime r = Runtime.getRuntime();
		List<Object> l = Lists.newArrayList();
		int s = 1000000;
		long last = 0;
		while (true) {
			Map<Integer, Object> mi = Maps.newConcurrentHashMap();
			for (int i = 0; i < s; i++) {
				mi.put(i, i);
			}
			mi.clear();
//			mi = Maps.trim(mi);
			l.add(mi);
			r.gc();
			long thiz = r.freeMemory() / 1024;
			System.out.println("上次剩余内存:" + last);
			System.out.println("本次剩余内存:" + thiz);
			System.out.println("本次使用内存:" + (last - thiz));
			last = thiz;
			System.out.println("总共内存:" + r.totalMemory() / 1024);
			System.out.println("最大内存:" + r.maxMemory() / 1024);
			System.out.println("------------------------------");
			Thread.sleep(4000);
		}
	}
	public static void testNoTrim() throws InterruptedException {
		Runtime r = Runtime.getRuntime();
		List<Object> l = Lists.newArrayList();
		int s = 100000;
		long last = 0;
		while (true) {
			Map<Integer, Object> mi = Maps.newConcurrentHashMap();
			for (int i = 0; i < s; i++) {
				mi.put(i, i);
			}
			mi.clear();
			mi = Maps.trim(mi);
			l.add(mi);
			long thiz = r.freeMemory() / 1024;
			r.gc();
			System.out.println("剩余内存:" + thiz);
			System.out.println("本次使用内存:" + (last - thiz));
			last = thiz;
			System.out.println("总共内存:" + r.totalMemory() / 1024);
			System.out.println("最大内存:" + r.maxMemory() / 1024);
			System.out.println("------------------------------");
			Thread.sleep(4000);
		}
	}
}
