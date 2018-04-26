package cst.util.common.db;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * @author gwc
 * @version v201804 实例事务DAO 同一实例,使用同一事务
 */
public abstract class AbstractInstanceTxDAO extends AbstractDAO {
	private Connection conn;
	public AbstractInstanceTxDAO(){
		conn = getConnection();
	}
	
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
