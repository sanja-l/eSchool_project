package com.eschoolproject.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import com.eschoolproject.entities.UserEntity;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

public interface UserDao {
	public List<UserEntity> findAll();

	public UserEntity findById(Long id) throws EntityNotFoundException;
	
	public UserEntity findByPin(String pin);

	public List<UserEntity> findByFirstname(String firstname);

	public List<UserEntity> findByLastname(String lastname);

	public List<UserEntity> findByLastnameAndFirstname(	String lastname,
														String firstname);

	public UserEntity save(UserEntity userEntity) throws DataIntegrityViolationException, EntityNotFoundException;

	public UserEntity deleteById(Long id) throws EntityNotFoundException, EntityIsReferencedException;

	UserEntity update(UserEntity userEntity) throws DataIntegrityViolationException;
}
