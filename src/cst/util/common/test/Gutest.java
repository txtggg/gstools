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
	
	public static void main(String[] args) throws InterruptedException{
		TimedSoftRefCache<String, String> tsrf = new TimedSoftRefCache<String, String>();
		int s = 1000;
		for(int i = 100;i<s;i++){
			for(int x =0;x<s;x++){
				String ks = String.valueOf(x);
				tsrf.put(ks,ks);
			}
		}
		for(int i = 1;i<s;i++){
			Thread.sleep(i*1000*60*10);
			tsrf.setOverTime(i);
		}
		System.out.println("gutest执行完成,缓存后台trim中");
		
	}
	
	public static void testSoftRef(Map<String,Object> map){
		map = null;
	}

	 
}
