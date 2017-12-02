package cst.gu.util.sql.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import cst.gu.util.collection.ListUtil;
import cst.gu.util.map.MapUtil;
import cst.gu.util.sql.HibernateTxUtil;
import cst.gu.util.sql.SqlMaker;
import cst.gu.util.sql.SqlTxUtil;
import cst.gu.util.sql.impl.MysqlMaker;

/**
 * @author guweichao 20171102
 * 
 */
public class MainTest extends HibernateTxUtil{
	static String charset = "utf-8";
	public static void main(String[] args) {
//		String sql = "SELECT tr.review_id, tr.rev_name, tr.rev_type, tr.rev_date, tr.seq_flow, tr.flow_astep, tr.rev_allworkload / 60 rev_allworkload, tr.rev_verdict, sum( CASE trp.revpbl_status WHEN 004400040 THEN 1 ELSE 0 END ) closenum, count(trp.reviewproblem_id) totalnum, att.attribute_name rev_typename, tfs.status_name flow_statusname FROM t_review tr LEFT JOIN t_reviewproblem trp ON tr.review_id = trp.review_id AND trp.revpbl_status IN ( 004400010, 004400020, 004400040, 004400000 ) LEFT JOIN t_attribute att ON tr.rev_type = att.attribute_value LEFT JOIN t_flow_status tfs ON tr.seq_flow = tfs.seq_flow AND (tr.flow_astep + 1) = tfs.ordernumber WHERE tr.project_id = '17' AND tr.rev_date > '2017-07-23' AND tr.rev_date < '2017-09-09' GROUP BY tr.review_id";
		String sql = "select * from t_review";
		System.out.println(stu.queryListWithBlob(charset, sql));
	}

	static	SqlTxUtil stu = new SqlTxUtil() {
			String url = "jdbc:mysql://localhost:3306/gutest?useUnicode=true&characterEncoding=UTF8";

			@Override
			public Connection getConnection() {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection conn= DriverManager.getConnection(url, "root", "123456");
					return conn;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		};
	@Override
	public Session getSession() {
		// TODO 自动生成的方法存根
		return null;
	}
		
//		stu.beginTx();
//		User u = new User();
//		u.setName("gu");
//		u.setId(4);
//		User u2 = new User();
//		u2.setName("zhang");
//		u2.setBf(u);
//		u2.setDescription("not desc");
//		u2.setId(88);
//		u2.setAge(18);
//		SqlMaker maker = new MysqlMaker(u2);
//		maker.insert();
//		System.out.println(maker.getSql());
//		for (Object o : maker.getParams()) {
//			System.out.println(o);
//		}
//		System.out.println("-----------------");
//		
//		User nu = new User();
//		nu.setId(7);
//		System.out.println(nu);
//		stu.getBean(nu);
//		nu.setName("nu7gggdg");
//		stu.updateBean(nu);
//		stu.commitChange().endTx();
//		
//	}

}
