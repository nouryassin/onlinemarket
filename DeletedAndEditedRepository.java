package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.DeletedAndEdited;

public interface DeletedAndEditedRepository extends CrudRepository<DeletedAndEdited,Integer>{

	public DeletedAndEdited findById(int id);
	public DeletedAndEdited findByProductId(int productId);
	public DeletedAndEdited findByOfferid(int offerid);
	public List<DeletedAndEdited> findAllByTypeAndActiontypeAndStoreid(String type,String actiontype,int storeid);
	
}
