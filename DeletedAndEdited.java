package com.example.demo.entities;

import javax.persistence.*;

@Entity
public class DeletedAndEdited {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	private int offerid;
	private int discount;

	
	private String actiontype;
	private String type;
	
	private int productId;
	private String product_name;
	private String brand_name;
 	private String store_name;
 	private int Quantity;
 	private float price;
 	private float originalprice;
 	public float getOriginalprice() {
		return originalprice;
	}
	public void setOriginalprice(float originalprice) {
		this.originalprice = originalprice;
	}
	private int views=0;
 	private int buyers=0;
 	private int brandid;
 	private int storeid;
 	
	public int getBrandid() {
		return brandid;
	}
	public void setBrandid(int brandid) {
		this.brandid = brandid;
	}
	public int getStoreid() {
		return storeid;
	}
	public void setStoreid(int storeid) {
		this.storeid = storeid;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	public int getOfferid() {
		return offerid;
	}
	public void setOfferid(int offerid) {
		this.offerid = offerid;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getActiontype() {
		return actiontype;
	}
	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}
	
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getBrand_name() {
		return brand_name;
	}
	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public int getQuantity() {
		return Quantity;
	}
	public void setQuantity(int quantity) {
		Quantity = quantity;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
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
 	
}
