package cst.gu.litedao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import cst.gu.litedao.config.LitedaoConfig;
import cst.gu.util.bean.BeanUtil;
import cst.gu.util.container.Containers;
import cst.gu.util.sql.SqlMaker;
import cst.gu.util.sql.impl.mysql.MysqlMaker;

/**
 * @author gwc
 * @version v2018.05
 */
public abstract class AbstractDAO {
	Connection conn;
	private static final String charset = LitedaoConfig.charset();

	protected abstract Connection getConnection();

	protected abstract void CloseConnection(Connection conn);

	/**************************************************** jdbc ***************************************************/

	/**
	 * 
	 * @param sql
	 * @param objects
	 *            参数占位符？
	 * @return 执行结果影响的行数 返回 -1 表示执行有问题(发生异常会抛出运行时异常)
	 */
	public int update(String sql, Object... obj) {
		PreparedStatement pstmt = null;
		int count = -1;
		Connection conn = getConnection();
		try {
			pstmt = conn.prepareStatement(sql);
			setParams(pstmt, obj);
			count = pstmt.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			closeAll(conn, pstmt, null);
		}
		return count;
	}

	/**
	 * 根据bean执行单表的更新 生成sql,通过jdbc进行执行
	 * 
	 * @param o
	 * @return
	 */
	public void update(Object o) {
		SqlMaker mk = new MysqlMaker(o);
		mk.update();
		update(mk.getSql(), mk.getParams());
	}

	/**
	 * 根据bean执行单表的删除 生成sql,通过jdbc进行执行
	 * 
	 * @param o
	 * @return
	 */
	public int delete(Object o) {
		SqlMaker mk = new MysqlMaker(o);
		mk.delete();
		return update(mk.getSql(), mk.getParams());
	}

	/**
	 * 执行insert
	 * 
	 * @param sql
	 *            带有占位符?的sql
	 * @param obj
	 *            占位符实际参数
	 * @throws RuntimeException
	 * @return 返回主键(如果不需要主键,则使用update也可以)
	 */
	public int insert(String sql, Object... obj) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection conn = getConnection();
		try {
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			setParams(pst, obj);
			pst.executeUpdate();
			int id = 0;
			rs = pst.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
			return id;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			closeAll(conn, pst, rs);
		}
	}

	/**
	 * 根据bean执行单表的删除 生成sql,通过jdbc进行执行 执行后并不会自动赋值bean对象,需要手动处理
	 * 
	 * @param o
	 * @return 新插入语句的主键id
	 */
	public int insert(Object o) {
		SqlMaker mk = new MysqlMaker(o);
		mk.insert();
		return insert(mk.getSql(), mk.getParams());
	}

	/**
	 * 根据传入的bean,查询sql,并将数据插入对象中,生成sql,通过jdbc进行执行 bean对象需要id的值来获取数据
	 * 
	 * @param t
	 */
	public void select(Object bean) {
		SqlMaker mk = new MysqlMaker(bean);
		mk.select();
		Map<String, Object> m = select(mk.getSql(), mk.getParams());
		BeanUtil.fillValueWithAnnotation(bean, m);
	}

	public Map<String, Object> select(String sql, Object... obj) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Object> rsMap = Containers.newHashMap();
		Connection conn = getConnection();
		try {
			pstmt = conn.prepareStatement(sql);
			setParams(pstmt, obj);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String filedname = rsmd.getColumnName(i).toLowerCase();
					rsMap.put(filedname, rs.getObject(i));
				}
			}
		} catch (SQLException e) {
			new RuntimeException(e);
		} finally {
			closeAll(conn, pstmt, rs);
		}
		return rsMap;
	}

	public List<Map<String, Object>> selectList(String sql, Object... objs) {

		List<Map<String, Object>> rowList = Containers.newArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = getConnection();
		try {
			pstmt = conn.prepareStatement(sql);
			setParams(pstmt, objs);
			rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				Map<String, Object> row = Containers.newHashMap();
				for (int i = 1; i <= columnCount; i++) {
					row.put(rsmd.getColumnName(i), rs.getObject(i));
				}
				rowList.add(row);
			}
		} catch (SQLException sqlex) {
			throw new RuntimeException(sqlex);
		} finally {
			closeAll(conn, pstmt, rs);
		}
		return rowList;
	}

	/**************************************************** private ***************************************************/

	private void setParams(PreparedStatement pst, Object... objs) {
		for (int i = 0; i < objs.length; i++) {
			Object o = objs[i];
			try {
				if (o == null) {
					pst.setNull(i + 1, java.sql.Types.NUMERIC);
				} else {
					pst.setObject(i + 1, o);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	private void closeAll(Connection conn, Statement st, ResultSet rs) {

		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
			}
		}

		if (st != null) {
			try {
				st.close();
			} catch (Exception e) {
			}
		}
		CloseConnection(conn);
	}

}
