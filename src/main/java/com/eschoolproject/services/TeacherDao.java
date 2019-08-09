package com.eschoolproject.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;

import com.eschoolproject.entities.TeacherEntity;
import com.eschoolproject.entities.UserEntity;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

public interface TeacherDao {
	public List<TeacherEntity> findAll();

	public TeacherEntity findById(Long id) throws EntityNotFoundException;
	
	public TeacherEntity save(TeacherEntity teacherEntity, UserEntity userEntity) throws DataIntegrityViolationException, EntityNotFoundException;

	public TeacherEntity update(TeacherEntity teacherEntity) throws DataIntegrityViolationException, EntityNotFoundException;

	public TeacherEntity deleteById(Long Id) throws EntityNotFoundException, EntityIsReferencedException;

}
