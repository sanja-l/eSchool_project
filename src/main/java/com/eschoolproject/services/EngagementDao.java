package com.eschoolproject.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;

import com.eschoolproject.entities.EngagementEntity;
import com.eschoolproject.util.customexceptions.EntityMissmatchException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

public interface EngagementDao {
	public List<EngagementEntity> findAll();

	public EngagementEntity findById(Long id) throws EntityNotFoundException;

	public List<EngagementEntity> findByCourseGradeId(Long courseGradeId)throws EntityNotFoundException;
	public List<EngagementEntity> findByTeacherId(Long teacherId)throws EntityNotFoundException;
	public List<EngagementEntity> findByClassId(Long sclassId)throws EntityNotFoundException;

	public EngagementEntity save(EngagementEntity engagementEntity, Long courseGradeId, Long teacherId, Long sclassId) throws DataIntegrityViolationException, EntityNotFoundException, EntityMissmatchException;

	public EngagementEntity deleteById(Long id) throws EntityNotFoundException;
}
