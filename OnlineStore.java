package com.example.demo.entities;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class OnlineStore {
	
		
	 @Id
	 @GeneratedValue(strategy=GenerationType.AUTO)
	 private Integer id;
	 private String store_name;
	 private String store_location;
	 private String store_type;


	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	@OneToMany(cascade=CascadeType.ALL,mappedBy="stores", fetch=FetchType.EAGER,targetEntity=ProductStore.class)
	 private List<ProductStore> storeproducts;
	
	
	@ManyToOne(fetch=FetchType.LAZY,targetEntity=User.class)
 	@JoinColumn(name="storeowner_id")
 	private User storeowner;
	public User getStoreowner() {
		return storeowner;
	}
	public void setStoreowner(User storeowner) {
		this.storeowner = storeowner;
	}
	@ManyToOne(fetch=FetchType.LAZY,targetEntity=collaborator.class)
 	@JoinColumn(name="collaborator_id")
 	private collaborator collaborator;
	public collaborator getCollaborator() {
		return collaborator;
	}

	public void setCollaborator(collaborator collaborator) {
		this.collaborator = collaborator;
	}
	

	public List<ProductStore> getStoreproducts() {
		return storeproducts;
	}

	public void setStoreproducts(List<ProductStore> storeproducts) {
		this.storeproducts = storeproducts;
	}
	

	

	public OnlineStore() {
			this.store_name ="";
			this.store_location ="";
			this.store_type ="";
		}
	 
	public OnlineStore(String store_name, String store_location, String store_type) {
		super();
		this.store_name = store_name;
		this.store_location = store_location;
		this.store_type = store_type;
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
