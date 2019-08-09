package com.eschoolproject.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;

import com.eschoolproject.entities.CourseEntity;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

public interface CourseDao {
	public List<CourseEntity> findAll();

	public CourseEntity findById(Long id) throws EntityNotFoundException;

	public List<CourseEntity> findByCourseName(String courseName);

	public List<CourseEntity> findByCourseNameContaining(String courseName);

	public List<CourseEntity> findByCourseSID(String courseSID);

	public CourseEntity deleteById(Long id) throws EntityNotFoundException, EntityIsReferencedException;
	
	public CourseEntity save(CourseEntity courseEntity) throws DataIntegrityViolationException;

}
