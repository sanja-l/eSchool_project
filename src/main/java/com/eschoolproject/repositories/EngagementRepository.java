package com.eschoolproject.repositories;

import org.springframework.data.repository.CrudRepository;

import com.eschoolproject.entities.ClassEntity;
import com.eschoolproject.entities.CourseGradeEntity;
import com.eschoolproject.entities.EngagementEntity;
import com.eschoolproject.entities.TeacherEntity;

public interface EngagementRepository extends CrudRepository<EngagementEntity, Long> {

	public Iterable<EngagementEntity> findByTeacher(TeacherEntity teacherEntity);

	public Iterable<EngagementEntity> findBySclass(ClassEntity classEntity);

	public Iterable<EngagementEntity> findByCourseGrade(CourseGradeEntity courseGradeEntity);

	public Iterable<EngagementEntity> findByTeacherAndSclassAndCourseGrade(	TeacherEntity teacherEntity,
																			ClassEntity classEntity,
																			CourseGradeEntity courseGradeEntity);

}
