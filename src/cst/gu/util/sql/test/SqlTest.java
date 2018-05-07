	package cst.gu.util.sql.test;

import java.io.File;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import cst.gu.util.container.Containers;
import cst.gu.util.file.FileUtil;
import cst.gu.util.sql.SqlTxUtil;
import cst.gu.util.string.StringUtil;

public class SqlTest {
	public static void main(String[] args)  throws ClassNotFoundException {
		SqlTxUtil sql = new SqlTxUtil() {
			
			@Override
			protected Connection getConnection(){
				try {
					Class.forName("com.mysql.jdbc.Driver");
					 Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/isemptest?characterEncoding=utf8", "root","123456");  
					 return conn;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}  
				
				return null;
			}
		};
		long t1 = System.currentTimeMillis();
		Map<String,Object> m1 = Containers.newHashMap();
		for(int x =0;x<1000;x++){
			m1 = sql.query("select * from t_user where user_id = ?", 1);
			System.out.println(m1);
		}
		long t2 = System.currentTimeMillis();
		System.out.println("zongshijian:"+(t2 - t1));
		// 10000次20536ms  ;; 1000次 2750ms
		
		
	}
	
/*	public static void main(String[] args) { 
		File file = new File("D:\\gu\\Desktop\\cb\\ControllerAspect.java");
		System.out.println(removeCNEmpty(file));
	}*/
	
	public static String removeCNEmpty(File file){
		String str = FileUtil.getFileContent(file);
		return removeCNEmpty(str);
	}

	public static String removeCNEmpty(String str){
		return str.replace("　", " ");
	}
} 