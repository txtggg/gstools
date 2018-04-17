package cst.util.common.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * @author gwc
 * @version v201804 线程事务DAO 同一线程,使用同一事务
 */
public abstract class AbstractInstanceTxDAO extends AbstractDAO {
	private static DataSource datasource;
	private Connection conn;
	private ThreadLocal<Connection> conns = new ThreadLocal<Connection>() {
		protected Connection initialValue() {
			 conn = getConnection();
			try {
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return conn;
		};
	};
	
	public void rollback(){
		try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void commit() {
		try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void CloseConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void beginTx(){
		
	}
}
