package com.example.demo.entities;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.context.annotation.Scope;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class User {
	
 @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	protected String username;
	protected String email;
 	protected String password;
 	protected String name;
 	protected String type;
 	private boolean first=true;
 
 	public boolean isFirst() {
		return first;
	}
	public void setFirst(boolean first) {
		this.first = first;
	}

	@OneToMany(cascade=CascadeType.ALL,mappedBy="storeowner", fetch=FetchType.EAGER,targetEntity=OnlineStore.class)
	private List<OnlineStore> stores;
	public List<OnlineStore> getStores() {
		return stores;
	}
 	public void setStores(List<OnlineStore> stores) {
		this.stores = stores;
	}
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

}
