package com.eschoolproject.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;

import com.eschoolproject.entities.ClassEntity;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

public interface ClassDao {
	public List<ClassEntity> findAll();

	public ClassEntity findById(Long id) throws EntityNotFoundException;

	public List<ClassEntity> findByClassName(String className);

	public List<ClassEntity> findByGradeId(Long gradeId) throws EntityNotFoundException;

	public List<ClassEntity> findByClassNameAndGradeValue(	String className,
														Integer gradeValue);

	public ClassEntity save(ClassEntity classEntity, Long gradeId) throws DataIntegrityViolationException, EntityNotFoundException;

	public ClassEntity deleteById(Long id) throws EntityNotFoundException, EntityIsReferencedException;

	public List<ClassEntity> findByGradeValue(Integer gradeValue);
}
