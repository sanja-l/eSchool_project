package com.eschoolproject.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.eschoolproject.entities.CourseEntity;
import com.eschoolproject.entities.CourseGradeEntity;
import com.eschoolproject.entities.GradeEntity;

public interface CourseGradeRepository extends CrudRepository<CourseGradeEntity, Long> {

	public List<CourseGradeEntity> findByCourse(CourseEntity courseEntity);

	public List<CourseGradeEntity> findByGrade(GradeEntity gradeEntity);

	public List<CourseGradeEntity> findByCourseAndGrade(CourseEntity courseEntity,
														GradeEntity gradeEntity);
}
