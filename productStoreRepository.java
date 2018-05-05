
package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.ProductStore;

@Repository
@Transactional
public interface productStoreRepository extends CrudRepository<ProductStore ,Integer> {
	
	public ProductStore findById(int id);
}
