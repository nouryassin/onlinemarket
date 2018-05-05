package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.sesion;

public interface sesionRepository extends CrudRepository<sesion,Integer>{

	public sesion findById(int id);
}
