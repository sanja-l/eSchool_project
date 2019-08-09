package com.eschoolproject.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.eschoolproject.entities.GradeEntity;

public interface GradeRepository extends CrudRepository<GradeEntity, Long> {
	
	public List<GradeEntity>findByGradeValue(Integer gradeValue);

}
