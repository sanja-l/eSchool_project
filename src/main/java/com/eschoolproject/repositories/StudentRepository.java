package com.eschoolproject.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.eschoolproject.entities.ClassEntity;
import com.eschoolproject.entities.CourseEntity;
import com.eschoolproject.entities.StudentEntity;
import com.eschoolproject.entities.TeacherEntity;

public interface StudentRepository extends CrudRepository<StudentEntity, Long> {
	public List<StudentEntity> findBySclass(ClassEntity classEntity);
	
	@Query("SELECT DISTINCT c FROM CourseEntity c, EngagementEntity e, StudentEntity s "
			+ " WHERE e.courseGrade.course = c "
			+ " AND  e.sclass = s.sclass AND s.id =:sid")
	List<CourseEntity> qFindCoursesByStudentId(@Param("sid") Long sid);
	
	@Query("SELECT DISTINCT t FROM TeacherEntity t, EngagementEntity e, StudentEntity s "
			+ " WHERE e.teacher  = t "
			+ " AND  e.sclass = s.sclass AND s.id =:sid")
	List<TeacherEntity> qFindTeachersByStudentId(@Param("sid") Long sid);
}
