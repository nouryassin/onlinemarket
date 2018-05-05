package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.collaborator;

public interface collaboratorRepository extends CrudRepository<collaborator,Integer> {
	public collaborator findById(int id);
}
