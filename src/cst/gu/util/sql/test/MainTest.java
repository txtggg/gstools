package cst.gu.util.sql.test;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Map;

import cst.gu.util.file.FileUtil;
import cst.gu.util.sql.SqlMaker;
import cst.gu.util.sql.impl.MysqlMaker;
import sun.security.util.BigInt;



/**
 * @author guweichao 20171102
 * 
 */
public class MainTest {
	static String charset = "utf-8";
	public static void main(String[] args) throws UnsupportedEncodingException {
		String ddd = FileUtil.getFileContent("D:\\gu\\Desktop\\cb\\m1.txt");
		String m2 = new String(ddd.getBytes(),"gbk");
		System.out.println(m2);
	}
	 
}
