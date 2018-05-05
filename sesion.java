package com.example.demo.entities;

import javax.persistence.*;

@Entity
public class sesion {
	@Id
	private int id;
	private int userid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

}
