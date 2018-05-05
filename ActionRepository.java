package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.Actions;

public interface ActionRepository extends CrudRepository<Actions,Integer> {

}
