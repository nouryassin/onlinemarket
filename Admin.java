package com.example.demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="Administrator")
public class Admin {
	
	 @Id
	    @GeneratedValue(strategy=GenerationType.AUTO)
	    private Integer id;
	 
		private String username;
		private String email;
	 	
	 	private String password;
	 	private String name;
	 	
		public Admin() {
			username="";
			password="";
			email="";
			name="";
			
		}
		public Admin( String username, String password, String name) {
			super();
			this.username = username;
			this.password = password;
			this.name = name;
		}
		
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
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
