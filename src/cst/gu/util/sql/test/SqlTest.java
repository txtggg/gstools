	package cst.gu.util.sql.test;

import java.io.File;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

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
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}  
				
				return null;
			}
		};
		String cs = "utf-8";
		ObjectBean ob = new ObjectBean();
//		ob.setProject_id(24);
//		ob.setObj_name(StringUtil.string2Blob("我是谷魏朝1", cs));
//		ob.setObj_type("ocode");
//		ob.setObj_description("desc1");
//		sql.insertBean(ob);
		
		
		ob.setObject_id(8);
		sql.getBean(ob);
		
		Map m = sql.query("select * from t_objects where object_id = ?", 6);
		System.out.println(StringUtil.blob2String((Blob) m.get("obj_name"), cs));
		
		
		
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