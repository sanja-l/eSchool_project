package com.eschoolproject.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;

import com.eschoolproject.entities.ParentEntity;
import com.eschoolproject.entities.UserEntity;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

public interface ParentDao {
	public List<ParentEntity> findAll();

	public ParentEntity findById(Long id) throws EntityNotFoundException;
	
	public ParentEntity save(ParentEntity parentEntity, UserEntity userEntity) throws DataIntegrityViolationException, EntityNotFoundException;

	public ParentEntity update(ParentEntity parentEntity) throws DataIntegrityViolationException, EntityNotFoundException;

	public ParentEntity deleteById(Long Id) throws EntityNotFoundException;
	
	public ParentEntity addStudentToParent(Long parentId,
	   									Long studentId) throws DataIntegrityViolationException, EntityNotFoundException;

	ParentEntity removeStudentFromParent(	Long parentId,
											Long studentId)
			throws EntityNotFoundException;

}
