package com.eschoolproject.repositories;

import org.springframework.data.repository.CrudRepository;

import com.eschoolproject.entities.ParentEntity;

public interface ParentRepository extends CrudRepository<ParentEntity, Long> {
	
}
