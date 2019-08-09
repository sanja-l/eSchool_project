package com.eschoolproject.repositories;

import org.springframework.data.repository.CrudRepository;

import com.eschoolproject.entities.CourseEntity;

public interface CourseRepository extends CrudRepository<CourseEntity, Long> {

	public Iterable<CourseEntity> findByCourseName(String courseName);

	public Iterable<CourseEntity> findByCourseNameContainingIgnoreCase(String courseName);

	public Iterable<CourseEntity> findByCourseSID(String courseSID);
}
