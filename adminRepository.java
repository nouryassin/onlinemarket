package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.Admin;

public interface adminRepository extends CrudRepository<Admin,Integer> {
	public Admin findByUsernameAndPassword(String username,String password);
	
}
