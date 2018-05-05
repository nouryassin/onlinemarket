package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.apache.catalina.Store;

import java.util.*;

import javax.persistence.*;

@Entity
public class ProductStore {
	
		@Id
	    @GeneratedValue(strategy=GenerationType.AUTO)
	    private int id;
	 	private String product_name;
		private String brand_name;
	 	private String store_name;
	 	private int Quantity;
	 	private float price;
	 	private int views=0;
	 	private int buyers=0;
	 	
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

		public float getPrice() {
			return price;
		}

		public void setPrice(float price) {
			this.price = price;
		}

		public String getBrand_name() {
			return brand_name;
		}

		public void setBrand_name(String brand_name) {
			this.brand_name = brand_name;
		}

	 	
	 	public int getQuantity() {
			return Quantity;
		}

		public void setQuantity(int quantity) {
			Quantity = quantity;
		}

		@ManyToOne(fetch=FetchType.LAZY,targetEntity=OnlineStore.class)
	 	@JoinColumn(name="store_id")
	 	private OnlineStore stores;
		
		@ManyToOne(fetch=FetchType.LAZY,targetEntity=Brand.class)
	 	@JoinColumn(name="brand_id")
	 	private Brand brands;

	 	
		public Brand getBrands() {
			return brands;
		}

		public void setBrands(Brand brands) {
			this.brands = brands;
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

		public OnlineStore getStores() {
			return stores;
		}

		public void setStores(OnlineStore store) {
			this.stores = store;
		}
	 	
}
