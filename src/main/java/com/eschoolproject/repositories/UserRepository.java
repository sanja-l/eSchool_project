package com.eschoolproject.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.eschoolproject.entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

	public List<UserEntity> findByFirstnameIgnoreCase(String firstname);

	public List<UserEntity> findByLastnameIgnoreCase(String lastname);

	public List<UserEntity> findByLastnameAndFirstnameIgnoreCase(	String lastname,
																	String firstname);

	public List<UserEntity> findByPin(String pin);

}
