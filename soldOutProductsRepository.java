package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.SoldOutProduct;

public interface soldOutProductsRepository extends CrudRepository <SoldOutProduct,Integer>{
	
	
}
