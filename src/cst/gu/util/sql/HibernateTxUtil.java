package cst.gu.util.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.engine.SessionFactoryImplementor;

import cst.gu.util.container.Containers;

/**
 * @author guweichao 20171019 hibernate 的事务增强工具 解决hibernate和jdbc事务同时开启的冲突
 *         所有异常已重新包装为runtimeException ,如果需要异常信息 使用try catch 或者throws即可处理异常
 * 
 * 
 * 
 * @author guweichao 20171030
 * 
 * 修复一个bug(在事务中创建包含HibernateTxUtil的对象,事务无法传递此对象)
 * 
 */
public abstract class HibernateTxUtil extends SqlTxUtil{
	private static ConnectionProvider cp = null;
	private Long thid = null;
	private static Map<Long, Session> txSessions = Containers.newHashMap();

	private boolean tx = false;
	private Session session;

	public abstract Session getSession();
	public HibernateTxUtil() {
		thid = Thread.currentThread().getId();
		if (txSessions.containsKey(thid) ) {
			tx = true;
		}
	}

	@Override
	protected  Connection getConnection(){
		if (cp == null) {
			initCP();
		}
		 try {
			return cp.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 return null;
	}

	/**************************************************** transaction ***************************************************/
	public synchronized HibernateTxUtil beginTx() {
		if (tx) {
			throw new RuntimeException("事务已经开启");
		}
		session = getSession();
		session.beginTransaction();
		super.beginTx();
		tx = true;

		return this;
	}

	public HibernateTxUtil rollBack() {
		if (!tx) {
			throw new RuntimeException("事务尚未开启");
		}
		session.getTransaction().rollback();
		super.rollBack();
		return this;
	}

	public HibernateTxUtil commitChange() {
		if (!tx) {
			throw new RuntimeException("事务尚未开启");
		}
		session.getTransaction().commit();
		super.commitChange();
		return this;
	}

	/**
	 * 关闭事务
	 * 
	 * @return
	 */
	public HibernateTxUtil endTx() {
		if (!tx) {
			throw new RuntimeException("事务尚未开启");
		}
		session.close();
		txSessions.remove(thid);
		super.endTx();
		tx = false;
		return this;
	}

	 
	/*********************************** hibernate ******************************************/

	public int updateHql(String hql, Object... obj) {
		try {
			getTxSession();
			Query q = session.createQuery(hql);
			for (int i = 0; i < obj.length; i++) {
				q.setParameter(i, obj[i]);
			}
			return q.executeUpdate();

		} finally {
			closeSession(); // 避免发生异常 session无法关闭
		}
	}

	public void saveOrUpdate(Object bean) {
		try {
			getTxSession();
			session.saveOrUpdate(bean);
		} finally {
			closeSession();
		}
	}

	public void savePO(Object o) {
		try {
			getTxSession();
			session.save(o);
		} finally {
			closeSession();
		}
	}

	public void updatePO(Object o) {
		try {
			getTxSession();
			session.update(o);
		} finally {
			closeSession();
		}
	}

	public void deletePO(Object o) {
		try {
			getTxSession();
			session.delete(o);
		} finally {
			closeSession();
		}
	}

	@SuppressWarnings("unchecked")
	public <S> List<S> loadPOList(String hql, Object... obj) {
		try {
			getTxSession();
			Query q = session.createQuery(hql);
			for (int i = 0; i < obj.length; i++) {
				q.setParameter(i, obj[i]);
			}
			return q.list();
		} finally {
			closeSession();
		}
	}

	@SuppressWarnings({ "unchecked" })
	public <T> T loadPO(long pk, Class<T> clazz) {
		try {
			getTxSession();
			return (T) session.get(clazz, pk);
		} finally {
			closeSession();
		}
	}


	private synchronized void initCP() {
		if (cp == null) {
			Session s = getSession();
			cp = ((SessionFactoryImplementor) s.getSessionFactory()).getConnectionProvider();
			s.close();
		}
	}


	private void getTxSession() {
		if (tx) {
			session = txSessions.get(thid);
		} else {
			session = getSession();
		}
	}

	private void closeSession() {
		if (!tx) {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
			}
		} else {
			// session.flush();
		}
	}

}
