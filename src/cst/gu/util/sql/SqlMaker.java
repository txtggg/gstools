package cst.gu.util.sql;


/**
 * @author guweichao 
 * 20171102
 * 
 */
public interface SqlMaker {
	void insert();
	void delete();
	void update();
	void select();
	String getSql();
	Object[] getParams();
}
