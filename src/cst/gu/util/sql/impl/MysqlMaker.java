package cst.gu.util.sql.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import cst.gu.util.annotation.AnnoBeanUtil;
import cst.gu.util.annotation.AnnoUtil;
import cst.gu.util.container.Containers;
import cst.gu.util.sql.SqlMaker;

/**
 * @author guweichao 
 * 20171106
 * 根据bean生成sql(此bean至少应该含有2个字段,一个主键一个数据列,否则可能发生异常)
 * 如果有注解,使用注解
 * 默认传入的bean含有字段(field),如果没有,则会发生异常
 * 如果传入的类不含PrimaryKey注解,则默认使用第一个字段
 * 为兼容hibernate的bean,字段映射会去掉Set类型的
 * 
 */
public class MysqlMaker implements SqlMaker {
	private static Map<String,String> sqlMap = Containers.newHashMap();
	private static Map<String,Field[]> fieldsMap = Containers.newHashMap();
	private static Map<String,Field> pkMap = Containers.newHashMap();
	private Object bean;
	private Class<?> clz;
	private String clzName;
	private String key;
	private String sql;
	private Object[] params;
	private Field[] fields;
	private Field pkf;
	private boolean insertPK ;
	

	/**
	 * 
	 * @param bean 含有数据库表结构注解(@Table @Column @PrimaryKey)和数据信息的bean对象
	 * @param insertPK 执行插入语句时是否带有主键(如果主键自增,应设置false,数据库自动使用自增主键)
	 */
	public MysqlMaker(Object bean,boolean insertPK){
		this.bean = bean;
		this.insertPK = insertPK;
		initFields();
	}
	
	/**
	 * 
	 * @param bean 含有数据库表结构注解(@Table @Column @PrimaryKey)和数据信息的bean对象
	 * 默认insertPK = false(mysql有主键自增,目前平台也是用的主键自增,因此默认值为false)
	 */
	public MysqlMaker(Object bean){
		this(bean,false);
	}
	

	@Override
	public void insert() {
		key = clzName + ":insert:" + insertPK;
		sql = sqlMap.get(key);
		if(sql == null){
			String table = AnnoBeanUtil.getTable(clz);
			StringBuilder sb1 = new StringBuilder();
			StringBuilder sb2 = new StringBuilder();
			sb1.append(AnnoBeanUtil.getColumn(fields[0]));
			sb2.append('?');
			for(int x = 1 ;x < fields.length ;x++){
				sb1.append(",").append(AnnoBeanUtil.getColumn(fields[x]));
				sb2.append(",?");
			}
			if(insertPK){
				sb1.append(',').append(AnnoBeanUtil.getColumn(pkf));
				sb2.append(",?");
			}
			StringBuilder sb = new StringBuilder("insert into ")
			.append(table).append(" (").append(sb1).append(") values (").append(sb2).append(")");
			sql = sb.toString();
			sqlMap.put(key, sql);
		}
		if (insertPK) {
			int flen = fields.length;
			params = new Object[flen + 1];
			for (int x = 0; x < flen ; x++) {
				params[x] = getFieldValue(fields[x]);
			}
			params[flen] = getFieldValue(pkf);
		}else{
			int flen = fields.length;
			params = new Object[flen];
			for (int x = 0; x < flen ; x++) {
				params[x] = getFieldValue(fields[x]);
			}
		}
	 
	}

	@Override
	public void delete() {
		key = clzName + ":delete";
		sql = sqlMap.get(key);
		if(sql == null){
			String table = AnnoBeanUtil.getTable(bean.getClass());
			String pkName = AnnoBeanUtil.getColumn(pkf);
			StringBuilder sb = new StringBuilder("delete from ").append(table).append(" where ").append(pkName).append(" = ?");
			sql = sb.toString();
			sqlMap.put(key, sql);
		}
		params = new Object[1];
		params[0] = getFieldValue(pkf);
	}

	@Override
	public void update() {
		key = clzName + ":update";
		sql = sqlMap.get(key);
		if(sql == null){
			String table = AnnoBeanUtil.getTable(bean.getClass());
			StringBuilder sb = new StringBuilder("update ").append(table).append(" set ").append(AnnoBeanUtil.getColumn(fields[0])).append(" = ?");
			for(int x = 1; x < fields.length;x ++){
				sb.append(",").append(AnnoBeanUtil.getColumn(fields[x])).append(" = ?");
			}
			sb.append(" where ").append(AnnoBeanUtil.getColumn(pkf)).append(" = ?");
			sql = sb.toString();
			sqlMap.put(key,sql);
		}
		int len = fields.length;
		params = new Object[len + 1];
		for(int x = 0; x < len;x++){
			params[x] = getFieldValue(fields[x]);
		}
		params[len] = getFieldValue(pkf);
	
	}
	
	@Override
	public void select() {
		key = clzName + ":select";
		sql = sqlMap.get(key);
		if (sql == null) {
			String table = AnnoBeanUtil.getTable(bean.getClass());
			StringBuilder sb = new StringBuilder("select * from ").append(table).append(" where ")
					.append(AnnoBeanUtil.getColumn(pkf)).append(" = ?");
			sql = sb.toString();
			sqlMap.put(key, sql);
		}
		params = new Object[1];
		params[0] = getFieldValue(pkf);
	}
	
	private Object getFieldValue(Field f){
		Object o = null;
		try {
			Class<?> fclz = f.getType();
			if(AnnoUtil.getTableValue(fclz) != null){ // 如果能获取到@Table 则是bean对象,值使用bean的主键id
				Field fpk = pkMap.get(fclz.getName()); // 从缓存中获取bean的主键
				Object fbean = f.get(bean);
				if(fbean != null){
					if(fpk == null){
						new MysqlMaker(fbean); // 使用fbean 进行一次实例化,就会缓存进去
					}
					fpk = pkMap.get(fclz.getName());
					o = fpk.get(fbean);
				}
			}else{
				o = f.get(bean);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return o;
	}

	@Override
	public String getSql() {
		return sql;
	}

	@Override
	public Object[] getParams() {
		return params;
	}
	
	/**
	 * 初始化Field
	 * 将主键的field放到第一个位置
	 */
	private void initFields(){
		clz = bean.getClass();
		clzName = clz.getName();
		fields = fieldsMap.get(clzName);
		pkf = pkMap.get(clzName);
		if(fields == null){
			Field[] fs = clz.getDeclaredFields();
			List<Field> flist = Containers.newArrayList();
			for(int x =0;x<fs.length;x++){
				Field f = fs[x];
				if(!f.getType().getName().equals("java.util.Set")){
					if(AnnoUtil.isPrimaryKey(f)){
						pkf = f;
					}else{
						f.setAccessible(true);
						flist.add(f);
					}
				}
			}
			if(pkf == null){// 没有@PrimaryKey 使用第一个字段做主键
				pkf = fs[0];
				flist.remove(0);
			}
			pkf.setAccessible(true);
			fields = flist.toArray(new Field[flist.size()]);
			pkMap.put(clzName, pkf);
			fieldsMap.put(clzName, fields);
		}
	}
	
	 
}
