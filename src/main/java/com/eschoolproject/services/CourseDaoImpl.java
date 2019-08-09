package com.eschoolproject.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.eschoolproject.entities.CourseEntity;
import com.eschoolproject.repositories.CourseRepository;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

@Service
public class CourseDaoImpl implements CourseDao {
	
	private final CourseRepository courseRepository;

	// @Autowired
	public CourseDaoImpl(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	@Override
	public List<CourseEntity> findAll() {
		return (List<CourseEntity>) courseRepository.findAll();
	}

	@Override
	public CourseEntity findById(Long id) throws EntityNotFoundException {
		CourseEntity courseEntity = courseRepository.findById(id).orElse(null);
		if (courseEntity == null)
			throw new EntityNotFoundException("Course Entity with id = " + id + " does not exist.");

		return courseEntity;
	}

	@Override
	public List<CourseEntity> findByCourseName(String courseName) {
		return (List<CourseEntity>) courseRepository.findByCourseName(courseName);
	}

	@Override
	public List<CourseEntity> findByCourseNameContaining(String courseName) {
		return (List<CourseEntity>) courseRepository.findByCourseNameContainingIgnoreCase(courseName);
	}

	@Override
	public List<CourseEntity> findByCourseSID(String courseSID) {
		return (List<CourseEntity>) courseRepository.findByCourseSID(courseSID);
	}

	@Override
	public CourseEntity deleteById(Long id) throws EntityNotFoundException, EntityIsReferencedException {
		CourseEntity courseEntity = courseRepository.findById(id).orElse(null);
		if (courseEntity == null)
			throw new EntityNotFoundException("CourseEntity with id = " + id + " does not exist.");
		if ( courseEntity.getCourseGradeEntities().size() > 0)
			throw new EntityIsReferencedException(
					"Cannot delete. Course Entity with id = " + id + " is referenced by courseGrade.");
		courseRepository.deleteById(id);
		return courseEntity;
	}

	@Override
	public CourseEntity save(CourseEntity courseEntity) throws DataIntegrityViolationException {
		return courseRepository.save(courseEntity);
	}

}
