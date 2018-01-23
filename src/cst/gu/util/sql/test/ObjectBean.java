package cst.gu.util.sql.test;

import java.sql.Blob;

import cst.gu.util.annotation.PrimaryKey;
import cst.gu.util.annotation.Table;
import cst.gu.util.sql.IBean;

@Table("t_objects")
public class ObjectBean implements IBean{

	@PrimaryKey
	private int object_id;
	
	private int project_id;
	private Blob obj_name;
	private String obj_type;
	private String obj_description;
	public int getObject_id() {
		return object_id;
	}
	public void setObject_id(int object_id) {
		this.object_id = object_id;
	}
	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	public Blob getObj_name() {
		return obj_name;
	}
	public void setObj_name(Blob obj_name) {
		this.obj_name = obj_name;
	}
	public String getObj_type() {
		return obj_type;
	}
	public void setObj_type(String obj_type) {
		this.obj_type = obj_type;
	}
	public String getObj_description() {
		return obj_description;
	}
	public void setObj_description(String obj_description) {
		this.obj_description = obj_description;
	}
	
	
}