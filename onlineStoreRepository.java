package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Admin;
import com.example.demo.entities.OnlineStore;

@Repository
@Transactional
public interface onlineStoreRepository extends CrudRepository<OnlineStore,Integer>{
	public OnlineStore findById(int id);
}