package cst.gu.util.sql.test;

import java.util.Set;

import cst.gu.util.annotation.Column;
import cst.gu.util.annotation.PrimaryKey;
import cst.gu.util.annotation.Table;

/**
 * @author guweichao 
 * 20171102
 * 
 */
@Table("user")
public class User {
	@PrimaryKey
	@Column("uid")
	private int id;
	@Column("uname")
	private String name;
	@Column("uage")
	private int age;
	@Column("udesc")
	private String description;
	@Column("ubf")
	private User bf;
	private Set<User> friends;
	public Set<User> getFriends() {
		return friends;
	}
	public void setFriends(Set<User> friends) {
		this.friends = friends;
	}
	public User getBf() {
		return bf;
	}
	public User setBf(User bf) {
		this.bf = bf;
		return this;
	}
	public int getId() {
		return id;
	}
	public User setId(int id) {
		this.id = id;
		return this;
	}
	public String getName() {
		return name;
	}
	public User setName(String name) {
		this.name = name;
		return this;
	}
	public int getAge() {
		return age;
	}
	public User setAge(int age) {
		this.age = age;
		return this;
	}
	public String getDescription() {
		return description;
	}
	public User setDescription(String description) {
		this.description = description;
		return this;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", age=" + age + ", description=" + description + ", bf=" + bf
				+ ", friends=" + friends + "]";
	}
	
}
