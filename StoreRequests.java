package com.example.demo.entities;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class StoreRequests  {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
	private String store_name;
	private String store_location;
	private String store_type;
	private int storeowner_id;
	
	public int getStoreowner_id() {
		return storeowner_id;
	}
	public void setStoreowner_id(int storeowner_id) {
		this.storeowner_id = storeowner_id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public String getStore_location() {
		return store_location;
	}
	public void setStore_location(String store_location) {
		this.store_location = store_location;
	}
	public String getStore_type() {
		return store_type;
	}
	public void setStore_type(String store_type) {
		this.store_type = store_type;
	}
	
	


}
