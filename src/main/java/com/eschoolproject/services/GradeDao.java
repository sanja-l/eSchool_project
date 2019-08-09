package com.eschoolproject.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import com.eschoolproject.entities.GradeEntity;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

public interface GradeDao {
	public List<GradeEntity> findAll();

	public GradeEntity findById(Long id) throws EntityNotFoundException;

	public List<GradeEntity> findByGradeValue(Integer gradeValue);;

	public GradeEntity save(GradeEntity gradeEntity) throws DataIntegrityViolationException;

	public GradeEntity deleteById(Long id) throws EntityNotFoundException, EntityIsReferencedException;

}
