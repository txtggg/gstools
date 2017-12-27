package cst.gu.util.sql.test;

import java.util.Set;

import cst.gu.util.annotation.Column;
import cst.gu.util.annotation.PrimaryKey;
import cst.gu.util.annotation.Table;

/**
 * @author guweichao 20171102
 * 
 */
@Table("user")
public class Ser1 {
	
	public static Ser1 getSer1(){
		TestServiceProxy proxy = new TestServiceProxy();
		return proxy.getProxy(Ser1.class);
		
	}
	public void say(String ss) {
		say1();
		say(66);
		System.out.println(ss);
		say2();
		say2();
	}
	public void say(int i) {
		System.out.println(i);
	}

	public void say1() {
		System.out.println("say1");
	}

	public void say2() {
		System.out.println("say2");
	}
}
