package com.eschoolproject.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;

import com.eschoolproject.entities.CourseGradeEntity;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

public interface CourseGradeDao {
	public List<CourseGradeEntity> findAll();

	public CourseGradeEntity findById(Long id) throws EntityNotFoundException;

	public List<CourseGradeEntity> findByCourseId(Long courseId)throws EntityNotFoundException;

	public List<CourseGradeEntity> findByGradeId(Long gradeId) throws EntityNotFoundException;

	public List<CourseGradeEntity> findByCourseIdAndGradeId(	Long courseId,
	                                                    	Long gradeId) throws EntityNotFoundException;

	public CourseGradeEntity save(CourseGradeEntity courseGradeEntity, Long courseId, Long gradeId) throws DataIntegrityViolationException, EntityNotFoundException;

	public CourseGradeEntity deleteById(Long id) throws EntityNotFoundException, EntityIsReferencedException;

}
