package cst.gu.util.sql;

import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cst.gu.util.bean.BeanUtil;
import cst.gu.util.container.Containers;
import cst.gu.util.sql.impl.MysqlMaker;
import cst.gu.util.string.StringUtil;

/**
 * @author guweichao 20171019 hibernate 的事务增强工具 解决hibernate和jdbc事务同时开启的冲突
 *         所有异常已重新包装为runtimeException ,如果需要异常信息 使用try catch 或者throws即可处理异常
 */
public abstract class SqlTxUtil {
	private Long thid = null;
	private static Map<Long, Connection> txConns = Containers.newHashMap();
	private boolean tx = false;
	private Connection conn;

	protected abstract Connection getConnection();

	public SqlTxUtil() {
		thid = Thread.currentThread().getId();
		if (txConns.containsKey(thid)) {
			tx = true;
		}
	}

	/**************************************************** transaction ***************************************************/
	public synchronized SqlTxUtil beginTx() {

		if (tx) {
			throw new RuntimeException("事务已经开启");
		}
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			thid = Thread.currentThread().getId();
			txConns.put(thid, conn); // 将连接放入线程id为key的map中待用
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			throw new RuntimeException(e);
		}
		tx = true;
		return this;
	}

	public SqlTxUtil rollBack() {
		if (!tx) {
			throw new RuntimeException("事务尚未开启");
		}
		try {
			conn.rollback();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public SqlTxUtil commitChange() {
		if (!tx) {
			throw new RuntimeException("事务尚未开启");
		}
		try {
			conn.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	/**
	 * 关闭事务
	 * 
	 * @return
	 */
	public SqlTxUtil endTx() {

		if (!tx) {
			throw new RuntimeException("事务尚未开启");
		}
		try {
			conn.setAutoCommit(false);
			conn.close();
			txConns.remove(thid);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		tx = false;
		return this;
	}

	/**************************************************** jdbc ***************************************************/

	/**
	 * 
	 * @param sql
	 * @param objects
	 *            参数占位符？
	 * @return 执行结果影响的行数 返回 -1 表示执行有问题(发生异常会抛出运行时异常)
	 */
	public int update(String sql, Object... obj) {
		getTxConn();
		PreparedStatement pstmt = null;
		int count = -1;
		try {
			pstmt = conn.prepareStatement(sql);
			setParams(pstmt, obj);
			count = pstmt.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			closeAll(pstmt, null);
		}
		return count;
	}

	/**
	 * 根据bean执行单表的更新 
	 * 生成sql,通过jdbc进行执行
	 * @param o
	 * @return
	 */
	public int updateBean(Object o) {
		SqlMaker mk = new MysqlMaker(o);
		mk.update();
		return update(mk.getSql(), mk.getParams());
	}

	/**
	 * 根据bean执行单表的删除 
	 * 生成sql,通过jdbc进行执行
	 * @param o
	 * @return
	 */
	public int deleteBean(Object o) {
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
		try {
			getTxConn();
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
			closeAll(pst, rs);
		}
	}

	/**
	 * 根据bean执行单表的删除  生成sql,通过jdbc进行执行
	 * 执行后并不会自动赋值bean对象,需要手动处理
	 * @param o
	 * @return
	 */
	public int insertBean(Object o) {
		SqlMaker mk = new MysqlMaker(o);
		mk.insert();
		return insert(mk.getSql(), mk.getParams());
	}

	/**
	 * 根据传入的bean,查询sql,并将数据插入对象中,生成sql,通过jdbc进行执行
	 * 
	 * @param t
	 */
	public void getBean(Object bean) {
		SqlMaker mk = new MysqlMaker(bean);
		mk.select();
		Map<String, Object> m = query(mk.getSql(), mk.getParams());
		BeanUtil.fillValueWithAnnotation(bean, query(mk.getSql(), m));
	}

	public List<Map<String, Object>> queryList(String sql, Object... objs) {
		getTxConn();
		List<Map<String, Object>> rowList = Containers.newArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
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
			closeAll(pstmt, rs);
		}
		return rowList;
	}

	public List<Map<String, Object>> queryListWithBlob(String charset, String sql, Object... obj) {
		getTxConn();
		List<Map<String,Object>> rowList = Containers.newArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			setParams(pstmt, obj);
			rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				Map<String, Object> row = Containers.newHashMap();
				for (int i = 1; i <= columnCount; i++) {
					Object v = rs.getObject(i);
					v = binary2String(v, charset);
					row.put(rsmd.getColumnName(i), v);
				}
				rowList.add(row);
			}
		} catch (SQLException sqlex) {
			throw new RuntimeException(sqlex);
		} finally {
			closeAll(pstmt, rs);
		}
		return rowList;
	}

	/**
	 * @author guweichao 20170511 将查询结果全部使用String封装(blob字段使用utf-8解析为string) null
	 *         会转为""
	 * @param sql
	 * @param obj
	 * @return
	 */
	public List<Map<String, String>> queryStringList(String charset, String sql, Object... obj) {
		getTxConn();
		List<Map<String, String>> list = Containers.newArrayList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			setParams(pstmt, obj);
			rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				Map<String, String> rsMap = Containers.newHashMap();
				for (int i = 1; i <= columnCount; ++i) {
					Object v = rs.getObject(i);
					v = binary2String(v, charset);
					rsMap.put(rsmd.getColumnName(i), StringUtil.toString(v));
				}
				list.add(rsMap);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			closeAll(pstmt, rs);
		}
		return list;
	}

	public Map<String, Object> query(String sql, Object... obj) {
		getTxConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, Object> rsMap = Containers.newHashMap();
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
			closeAll(pstmt, rs);
		}
		return rsMap;
	}

	/**
	 * @param sql
	 *            查询两个字段分别作为作为key和value成为map 如果查询字段数不为2 则返回空map
	 * @return
	 */
	public Map<String, String> mapQuery(String sql, Object... obj) {
		getTxConn();
		Map<String, String> rsMap = Containers.newHashMap();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			setParams(pstmt, obj);
			rs = pstmt.executeQuery();
			if (rs != null) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				if (columnCount == 2) {
					while (rs.next()) {
						rsMap.put(StringUtil.toString(rs.getObject(1)), StringUtil.toString(rs.getObject(2)));
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			closeAll(pstmt, rs);
		}
		return rsMap;
	}

	/**
	 * 查询单字段,直接装入list
	 * 
	 * @param sql
	 * @param objects
	 * @return
	 */
	public Collection<Object> getOneColumn(Collection<Object> coll, String sql, Object... obj) {
		getTxConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			setParams(pstmt, obj);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					coll.add(rs.getObject(1));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			closeAll(pstmt, rs);
		}
		return coll;
	}

	/**
	 * 查询单字段,直接装入Set
	 * 
	 * @param sql
	 * @param objects
	 * @return
	 */
	public Set<String> getStringSet(String sql, Object... obj) {
		Set<String> set = Containers.newHashSet();
		getTxConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			setParams(pstmt, obj);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					set.add(StringUtil.toString(binary2String(rs.getObject(1), "utf-8")));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			closeAll(pstmt, rs);
		}
		return set;
	}

	/**************************************************** private ***************************************************/

	private Object binary2String(Object o, String charset) {
		if (o != null) {
			if (o instanceof byte[]) {
				byte[] bs = (byte[]) o;
				try {
					o = new String(bs, charset);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else if (o instanceof Blob) {
				o = StringUtil.blob2String((Blob) o, charset);
			}
		}
		return o;

	}

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

	private void closeAll(Statement st, ResultSet rs) {
		if (!tx && conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}

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
	}

	private void getTxConn() {
		if (tx) {
			// 如果已开启事务,则从当前线程获取Connection
			conn = txConns.get(thid);
		} else {
			conn = getConnection();
		}
	}

}
