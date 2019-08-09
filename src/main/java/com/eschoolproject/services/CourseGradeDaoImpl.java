package com.eschoolproject.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.eschoolproject.entities.CourseEntity;
import com.eschoolproject.entities.CourseGradeEntity;
import com.eschoolproject.entities.GradeEntity;
import com.eschoolproject.repositories.CourseGradeRepository;
import com.eschoolproject.repositories.CourseRepository;
import com.eschoolproject.repositories.GradeRepository;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;
@Service
public class CourseGradeDaoImpl implements CourseGradeDao {
	private final CourseGradeRepository courseGradeRepository;
	private final CourseRepository courseRepository;
	private final GradeRepository gradeRepository;
	
	
//@Autowired
	public CourseGradeDaoImpl(CourseGradeRepository courseGradeRepository, CourseRepository courseRepository,
			GradeRepository gradeRepository) {
		this.courseGradeRepository = courseGradeRepository;
		this.courseRepository = courseRepository;
		this.gradeRepository = gradeRepository;
	}

	@Override
	public List<CourseGradeEntity> findAll() {
		return (List<CourseGradeEntity>) courseGradeRepository.findAll();
	}

	@Override
	public CourseGradeEntity findById(Long id) throws EntityNotFoundException {
		CourseGradeEntity courseGradeEntity = courseGradeRepository.findById(id).orElse(null);
		if (courseGradeEntity == null)
			throw new EntityNotFoundException("CourseGrade Entity with id: " + id + " does not exist.");

		return courseGradeEntity;
	}

	@Override
	public List<CourseGradeEntity> findByCourseId(Long courseId) throws EntityNotFoundException {
		CourseEntity courseEntity = courseRepository.findById(courseId).orElse(null);
		if (courseEntity == null)
			throw new EntityNotFoundException("Course Entity with id: " + courseId + " does not exist.");
		return  (List<CourseGradeEntity>) courseGradeRepository.findByCourse(courseEntity);
	}

	@Override
	public List<CourseGradeEntity> findByGradeId(Long gradeId) throws EntityNotFoundException {
		GradeEntity gradeEntity = gradeRepository.findById(gradeId).orElse(null);
		if (gradeEntity == null)
			throw new EntityNotFoundException("Grade Entity with id: " + gradeId + " does not exist.");
		return  (List<CourseGradeEntity>) courseGradeRepository.findByGrade(gradeEntity);
	}

	@Override
	public List<CourseGradeEntity> findByCourseIdAndGradeId(Long courseId,
														Long gradeId) throws EntityNotFoundException {
		CourseEntity courseEntity = courseRepository.findById(courseId).orElse(null);
		if (courseEntity == null)
			throw new EntityNotFoundException("Course Entity with id: " + courseId + " does not exist.");
		GradeEntity gradeEntity = gradeRepository.findById(gradeId).orElse(null);
		if (gradeEntity == null)
			throw new EntityNotFoundException("Grade Entity with id: " + gradeId + " does not exist.");
		return  (List<CourseGradeEntity>) courseGradeRepository.findByCourseAndGrade(courseEntity,gradeEntity);
	
	}

	@Override
	public CourseGradeEntity save(CourseGradeEntity courseGradeEntity,Long courseId, Long gradeId) throws DataIntegrityViolationException, EntityNotFoundException {
		CourseEntity courseEntity = courseRepository.findById(courseId).orElse(null);
		if (courseEntity == null)
			throw new EntityNotFoundException("Course Entity with id: " + courseId + " does not exist.");
		GradeEntity gradeEntity = gradeRepository.findById(gradeId).orElse(null);
		if (gradeEntity == null) {
			throw new EntityNotFoundException("Grade Entity with id = " + gradeId + "does not exist.");
		}
		courseGradeEntity.setCourse(courseEntity);
		courseGradeEntity.setGrade(gradeEntity);
		return courseGradeRepository.save(courseGradeEntity);
	}
	
	@Override
	public CourseGradeEntity deleteById(Long id) throws EntityNotFoundException, EntityIsReferencedException {
		CourseGradeEntity courseGradeEntity = courseGradeRepository.findById(id).orElse(null);
		if (courseGradeEntity == null)
			throw new EntityNotFoundException("CourseGrade Entity with id: " + id + " does not exist.");
		if(courseGradeEntity.getEngagementEntities().size()> 0)
			throw new EntityIsReferencedException(
					"Cannot delete. CourseGrade Entity with id: " + id + " is referenced by Engagement Entities.");
		if(courseGradeEntity.getAssesmentEntities().size()> 0)
			throw new EntityIsReferencedException(
					"Cannot delete. CourseGrade Entity with id: " + id + " is referenced by Assesment Entities.");
		courseGradeRepository.deleteById(id);
		return courseGradeEntity;
	}




	



}
