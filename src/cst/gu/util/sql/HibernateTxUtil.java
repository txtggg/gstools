package cst.gu.util.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.engine.SessionFactoryImplementor;

import cst.gu.util.sql.test.LoggerUtil;

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
	private static ThreadLocal<Session> thse = new ThreadLocal<Session>();
	private static ThreadLocal<Boolean> thtx = new ThreadLocal<Boolean>();

	public abstract Session getSession();

	@Override
	protected  Connection getConnection(){
		if (cp == null) {
			initCP();
		}
		 try {
			return cp.getConnection();
		} catch (SQLException e) {
			LoggerUtil.errorLog(e);
		}
		 return null;
	}

	/**************************************************** transaction ***************************************************/
	public synchronized HibernateTxUtil beginTx() {
		if (getTx()) {
			LoggerUtil.infoLog("事务已经开启",LoggerUtil.cutline);
		}
		thtx.set(true);
		super.beginTx();
		return this;
	}

	public synchronized HibernateTxUtil rollBack() {
		if (!getTx()) {
			throw new RuntimeException("事务尚未开启");
		}
		getTxSession().getTransaction().rollback();
		super.rollBack();
		return this;
	}

	public synchronized HibernateTxUtil commitChange() {
		if (!getTx()) {
			throw new RuntimeException("事务尚未开启");
		}
		getTxSession().getTransaction().commit();
		super.commitChange();
		return this;
	}

	/**
	 * 关闭事务
	 * 
	 * @return
	 */
	public synchronized HibernateTxUtil endTx() {
		if (!getTx()) {
			throw new RuntimeException("事务尚未开启");
		}
		Session se = getTxSession();
		thtx.remove();
		thse.remove();
		se.close();
		LoggerUtil.infoLog("关闭Session[事务] ...");
		super.endTx();
		return this;
	}

	 
	/*********************************** hibernate ******************************************/

	public int updateHql(String hql, Object... obj) {
		Session session = getTxSession();
		try {
			Query q = session.createQuery(hql);
			for (int i = 0; i < obj.length; i++) {
				q.setParameter(i, obj[i]);
			}
			return q.executeUpdate();

		} finally {
			closeSession(session); // 避免发生异常 session无法关闭
		}
	}

	public void saveOrUpdate(Object bean) {
		Session session = getTxSession();
		try {
			session.saveOrUpdate(bean);
		} finally {
			closeSession(session);
		}
	}

	public void savePO(Object o) {
		Session session = getTxSession();
		try {
			session.save(o);
		} finally {
			closeSession(session);
		}
	}

	public void updatePO(Object o) {
		Session session = getTxSession();
		try {
			session.update(o);
		} finally {
			closeSession(session);
		}
	}

	public void deletePO(Object o) {
		Session session = getTxSession();
		try {
			session.delete(o);
		} finally {
			closeSession(session);
		}
	}

	@SuppressWarnings("unchecked")
	public <S> List<S> loadPOList(String hql, Object... obj) {
		Session session = getTxSession();
		try {
			Query q = session.createQuery(hql);
			for (int i = 0; i < obj.length; i++) {
				q.setParameter(i, obj[i]);
			}
			return q.list();
		} finally {
			closeSession(session);
		}
	}

	@SuppressWarnings({ "unchecked" })
	public <T> T loadPO(long pk, Class<T> clazz) {
		Session session = getTxSession();
		try {
			return (T) session.get(clazz, pk);
		} finally {
			closeSession(session);
		}
	}


	private synchronized void initCP() {
		if (cp == null) {
			Session s = getSession();
			cp = ((SessionFactoryImplementor) s.getSessionFactory()).getConnectionProvider();
			s.close();
		}
	}


	private Session getTxSession() {
		if (getTx()) {
			Session se = thse.get();
			if(se == null){
				LoggerUtil.infoLog("获取Session[事务] ...");
				se = getSession();
				se.beginTransaction();
			}
			thse.set(se);
			return se;
		} else {
			LoggerUtil.infoLog("获取Session ...");
			return getSession();
		}
	}

	private void closeSession(Session session) {
		if (!getTx()) {
			if (session != null && session.isOpen()) {
				session.flush();
				session.close();
				LoggerUtil.infoLog("关闭Session ...");
			}
		} else {
			// session.flush();
		}
	}
	
	private boolean getTx(){
		Boolean tx = thtx.get();
		if(tx == null){
			return false;
		}
		return tx.booleanValue();
	}

}
