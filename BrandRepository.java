package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.Brand;

public interface BrandRepository extends CrudRepository<Brand,Integer>{

	public Brand findById(int id);
	
}
