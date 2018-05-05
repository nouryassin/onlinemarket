package com.example.demo.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
public class Product {
		
	 @Id
	    @GeneratedValue(strategy=GenerationType.AUTO)
	    private Integer id;
		private String product_name;
		private String category;
	 	private float low_price;
	 	private float high_price;
	 	

	 	
		public Integer getId() {
			return id;
		}


		public void setId(Integer id) {
			this.id = id;
		}


		public Product() {
			this.product_name = "";
			this.category = "";
			this.low_price = 0;
			this.high_price = 0;
		}
	 	
	 	
		public Product(String product_name, String category,
				float low_price, float high_price) {
			super();
			this.product_name = product_name;
			this.category = category;
			this.low_price = low_price;
			this.high_price = high_price;
		}
		public String getProduct_name() {
			return product_name;
		}
		public void setProduct_name(String product_name) {
			this.product_name = product_name;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public float getLow_price() {
			return low_price;
		}
		public void setLow_price(float low_price) {
			this.low_price = low_price;
		}
		public float getHigh_price() {
			return high_price;
		}
		public void setHigh_price(float high_price) {
			this.high_price = high_price;
		}
	 	
	 	

	 

}
