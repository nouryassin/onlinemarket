package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.Offer;

public interface OfferRepository extends CrudRepository<Offer,Integer> {

	public List<Offer> findAllByStoreid(int storeid);
	public Offer findById(int id);
}
