package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.Product;

public interface productRepository extends CrudRepository<Product,Integer> {
	
	public Product findById(int id);
}
