package com.example.demo.entities;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class Brand {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	String brand_name;
	String brand_category;
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="brands", targetEntity=ProductStore.class)
	@Fetch(value = FetchMode.SUBSELECT)
	private Set<ProductStore> products;
	 
	
	
	
	public Set<ProductStore> getProducts() {
		return products;
	}

	public void setProducts(Set<ProductStore> products) {
		this.products = products;
	}

	

	
	public Brand() {
		this.brand_name = "";
		this.brand_category ="";
	}
	
	public Brand(String brand_name, String brand_category) {
		super();
		this.brand_name = brand_name;
		this.brand_category = brand_category;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBrand_name() {
		return brand_name;
	}
	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}
	public String getBrand_category() {
		return brand_category;
	}
	public void setBrand_category(String brand_category) {
		this.brand_category = brand_category;
	}
	

}
