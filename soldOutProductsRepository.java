package com.example.demo.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.Offer;
import com.example.demo.entities.SoldOutProduct;

public interface soldOutProductsRepository extends CrudRepository <SoldOutProduct,Integer>{
	public List<SoldOutProduct> findAllByStoreid(int storeid);
	
	
}
