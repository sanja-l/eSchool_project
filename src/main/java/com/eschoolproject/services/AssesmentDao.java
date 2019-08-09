package com.eschoolproject.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;

import com.eschoolproject.entities.AssesmentEntity;
import com.eschoolproject.util.customexceptions.EntityMissmatchException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

public interface AssesmentDao {
	public List<AssesmentEntity> findAll();

	public AssesmentEntity findById(Long id) throws EntityNotFoundException;

	public List<AssesmentEntity> findByCourseGradeId(Long courseGradeId) throws EntityNotFoundException;

	public List<AssesmentEntity> findByTeacherId(Long teacherId) throws EntityNotFoundException;

	public List<AssesmentEntity> findByStudentId(Long studentId) throws EntityNotFoundException;

	public List<AssesmentEntity> findByParentId(Long parentId);

	public AssesmentEntity save(AssesmentEntity assesmentEntity,
								Long courseGradeId,
								Long teacherId,
								Long studentId)
			throws DataIntegrityViolationException, EntityNotFoundException, EntityMissmatchException;

	public AssesmentEntity deleteById(Long id) throws EntityNotFoundException;

	public AssesmentEntity deleteByIdAndTeacherId(	Long id,
													Long teacherId)
			throws EntityNotFoundException, EntityMissmatchException;
	
	public Long FindTeachersByAssesmentId(Long id);

}
