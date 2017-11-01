package cn.itcast.jdbc.domain;

import java.util.Date;

/**
 * 
 * 2008-12-6
 * 
 * @author <a href="mailto:liyongibm@gmail.com">liyong</a>
 * 
 */
public class User {
	private int id;
	private String name;
	private Date birthday;
	private float money;
	private Date registDate;

	public User() {

	}

	public User(String name) {
		this.name = name;
	}

	public User(float money) {
		this.money = money;
	}

	public void showName() {
		System.out.println(this.name);
	}

	@Override
	public String toString() {
		return "id=" + this.id + " name=" + this.name + " birthday="
				+ this.birthday + " money=" + this.money;
	}

	private void test() {

	}

	public int getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setBirthday(java.sql.Date birthday) {
		this.birthday = birthday;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
	}

	public Date getRegistDate() {
		return registDate;
	}

	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMoney(float money) {
		this.money = money;
	}
}
