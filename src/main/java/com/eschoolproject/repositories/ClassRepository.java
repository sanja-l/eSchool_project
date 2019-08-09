package com.eschoolproject.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.eschoolproject.entities.ClassEntity;
import com.eschoolproject.entities.GradeEntity;

public interface ClassRepository extends CrudRepository<ClassEntity, Long> {

	public List<ClassEntity> findByGrade(GradeEntity gradeEntity);

	public List<ClassEntity> findByClassName(String className);

	@Query("SELECT DISTINCT c FROM ClassEntity c, GradeEntity g "
			+ " WHERE c.grade  = g "
			+ " AND g.gradeValue =:gv AND c.className =:cn" )
	public List<ClassEntity> qfindByClassNameAndGradeValue(	@Param("cn") String className,
	                                                      	@Param("gv") Integer gradeValue);
	
	@Query("SELECT DISTINCT c FROM ClassEntity c, GradeEntity g "
			+ " WHERE c.grade  = g "
			+ " AND g.gradeValue =:gv")
	public List<ClassEntity> qfindByGradeValue(@Param("gv") Integer gradeValue);
}
