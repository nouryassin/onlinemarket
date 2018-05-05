package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.AdminStatsitics;

public interface AdminStatRepository extends CrudRepository<AdminStatsitics,Integer> {
	public List<AdminStatsitics> findByStoreidAndType(int storeid,String type);
}
