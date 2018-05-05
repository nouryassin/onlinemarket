package com.example.demo.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.*;

@Entity
public class SoldOutProduct {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	private int storeid;
	private String product_name;
	private String store_name;
	private int views;
	private int buyers;
	
	public int getViews() {
		return views;
	}
	public void setViews(int views) {
		this.views = views;
	}
	public int getBuyers() {
		return buyers;
	}
	public void setBuyers(int buyers) {
		this.buyers = buyers;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getStoreid() {
		return storeid;
	}
	public void setStoreid(int storeid) {
		this.storeid = storeid;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	
	
}
