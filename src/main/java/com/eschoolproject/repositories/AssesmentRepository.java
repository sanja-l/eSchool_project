package com.eschoolproject.repositories;

import org.springframework.data.repository.CrudRepository;

import com.eschoolproject.entities.AssesmentEntity;
import com.eschoolproject.entities.CourseGradeEntity;
import com.eschoolproject.entities.StudentEntity;
import com.eschoolproject.entities.TeacherEntity;

public interface AssesmentRepository extends CrudRepository<AssesmentEntity, Long> {
	
	public Iterable<AssesmentEntity> findByTeacher(TeacherEntity teacherEntity);

	public Iterable<AssesmentEntity> findByStudent(StudentEntity studentEntity);

	public Iterable<AssesmentEntity> findByCourseGrade(CourseGradeEntity courseGradeEntity);

}
