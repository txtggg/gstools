package test.cst.util.common.cache.softref;


import org.junit.Test;

import cst.util.common.cache.softref.TimedSoftRefCache;

/**
 * @author gwc
 * @version 18.5 带时间限制的软引用缓存:超过指定时间(单位:分)或软引用失效都会导致清理缓存
 * 
 * @param <V>
 * @param <K>
 */
public class TestTimedSoftRefCache<K, V> { 
	
	@Test
	public void testBackTrim() {
		TimedSoftRefCache<String, String> tsrf = new TimedSoftRefCache<String, String>();
		int s = 1000;
		for(int i = 100;i<s;i++){
			for(int x =0;x<s;x++){
				String ks = String.valueOf(x);
				tsrf.put(ks,ks);
			}
		}
		for(int i = 1;i<s;i++){
			try {
				System.out.println("tsrc的size:"+tsrf.size());
				tsrf.setOverTime(i);
				Thread.sleep(i*1000*60*10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("gutest执行完成,缓存后台trim中");
	}
}
