package com.example.demo.repositories;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.User;

public interface userRepository extends CrudRepository<User,Integer> {

	public User findByUsernameAndPassword(String username,String password);
	public User findByUsername(String username);
}
