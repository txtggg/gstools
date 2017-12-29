package cst.gu.util.sql.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import cst.gu.util.sql.SqlTxUtil;



/**
 * @author guweichao 20171102
 * 
 */
public class MainTest {
	private static SqlTxUtil db = new SqlTxUtil() {
		
		@Override
		protected Connection getConnection() {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				return DriverManager.getConnection("jdbc:mysql://localhost/gutest?autoReconnect=true&amp;zeroDateTimeBehavior=convertToNull&amp;characterEncoding=utf8&amp;useSSL=true","root","123456");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			return null;
			
		}
	};
	public static void main(String[] args) {
		System.out.println(db.update("delete t1.*,t2.* from t_bug t1,t_bugfj t2 where t1.bug_id = t2.bug_id and t1.bug_id = 11"));
	}
}
