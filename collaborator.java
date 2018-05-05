package com.example.demo.entities;

import java.util.List;
import java.util.Set;

import javax.persistence.*;

@Entity
public class collaborator {

	 @Id
	    private Integer id;
		protected String username;
		protected String email;
	 	protected String password;
	 	protected String name;
	 	
	 	@OneToMany(cascade=CascadeType.ALL,mappedBy="collaborator", fetch=FetchType.EAGER,targetEntity=OnlineStore.class)
		private List<OnlineStore> store;
		public List<OnlineStore> getStore() {
			return store;
		}
		public void setStore(List<OnlineStore> store) {
			this.store = store;
		}
		
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
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
