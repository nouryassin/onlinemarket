package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.StoreRequests;

public interface storeRequestRepository extends CrudRepository<StoreRequests,Integer> {
	
	public StoreRequests findById(int id);
}
