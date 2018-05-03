package test.cst.util.common.cache.softref;

import java.util.List;

import org.junit.Test;

import cst.gu.util.datetime.LocalDateUtil;
import cst.util.common.cache.softref.SoftRefCache;
import cst.util.common.containers.Lists;

/**
 * @author gwc
 * @version 18.5 带时间限制的软引用缓存:超过指定时间(单位:分)或软引用失效都会导致清理缓存
 * 
 * @param <V>
 * @param <K>
 */
public class TestSoftRefCache<K, V> { 
	@Test
	public void testTrim() {
		SoftRefCache<String,Object> src =  SoftRefCache.getInstance();
		int s = 5000;
		Runtime r = Runtime.getRuntime();
		List<byte[]> bytess = Lists.newArrayList();
		for(int x =0;x<s;x++){
			for(int y =0;y<3;y++){
				String kv = x+"_"+y;
				src.put(kv,new byte[1024*1024*2]);
			}
			try {
				System.out.println("time:" + LocalDateUtil.getNow());
				System.out.println("src.size:"+src.size());
				src.trim();
				System.out.println("src.size:"+src.size());
				System.out.println("memory/兆(m)");
				System.out.println("free:"+r.freeMemory()/1024/1024);
				System.out.println("total:"+r.totalMemory()/1024/1024);
				System.out.println("max:"+r.maxMemory()/1024/1024);
				System.out.println("------------------");
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(bytess.size());
		
	}
}
