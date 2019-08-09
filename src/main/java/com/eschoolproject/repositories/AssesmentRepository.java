package com.eschoolproject.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.eschoolproject.entities.AssesmentEntity;
import com.eschoolproject.entities.CourseGradeEntity;
import com.eschoolproject.entities.StudentEntity;
import com.eschoolproject.entities.TeacherEntity;

public interface AssesmentRepository extends CrudRepository<AssesmentEntity, Long> {
	
	public Iterable<AssesmentEntity> findByTeacher(TeacherEntity teacherEntity);

	public Iterable<AssesmentEntity> findByStudent(StudentEntity studentEntity);

	public Iterable<AssesmentEntity> findByCourseGrade(CourseGradeEntity courseGradeEntity);
	
	@Query("SELECT DISTINCT t.id FROM TeacherEntity t, AssesmentEntity a"
			+ " WHERE a.teacher  = t "
			+ " AND a.id =:aid")
	List<Long> qFindTeachersByAssesmentId(@Param("aid") Long aid);
	

}
