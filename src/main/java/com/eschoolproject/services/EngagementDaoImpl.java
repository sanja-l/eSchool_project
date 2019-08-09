package com.eschoolproject.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.eschoolproject.entities.ClassEntity;
import com.eschoolproject.entities.CourseGradeEntity;
import com.eschoolproject.entities.EngagementEntity;
import com.eschoolproject.entities.TeacherEntity;
import com.eschoolproject.repositories.ClassRepository;
import com.eschoolproject.repositories.CourseGradeRepository;
import com.eschoolproject.repositories.EngagementRepository;
import com.eschoolproject.repositories.TeacherRepository;
import com.eschoolproject.util.customexceptions.EntityMissmatchException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

@Service
public class EngagementDaoImpl implements EngagementDao {
	private final EngagementRepository engagementRepository;
	private final CourseGradeRepository courseGradeRepository;
	private final TeacherRepository teacherRepository;
	private final ClassRepository classRepository;

	
	
	
	public EngagementDaoImpl(EngagementRepository engagementRepository, CourseGradeRepository courseGradeRepository,
			TeacherRepository teacherRepository, ClassRepository classRepository) {
		this.engagementRepository = engagementRepository;
		this.courseGradeRepository = courseGradeRepository;
		this.teacherRepository = teacherRepository;
		this.classRepository = classRepository;
	}

	@Override
	public List<EngagementEntity> findAll() {
		return (List<EngagementEntity>) engagementRepository.findAll();
	}

	@Override
	public EngagementEntity findById(Long id) throws EntityNotFoundException {
		EngagementEntity engagementEntity = engagementRepository.findById(id).orElse(null);
		if (engagementEntity == null)
			throw new EntityNotFoundException("Engagement Entity with id: " + id + " does not exist.");
		return engagementEntity;
	}

	@Override
	public List<EngagementEntity> findByCourseGradeId(Long courseGradeId) throws EntityNotFoundException {
		CourseGradeEntity courseGradeEntity = courseGradeRepository.findById(courseGradeId).orElse(null);
		if (courseGradeEntity == null)
			throw new EntityNotFoundException("CourseGrade Entity with id: " + courseGradeId + " does not exist.");
		return (List<EngagementEntity>) engagementRepository.findByCourseGrade(courseGradeEntity);
	}

	@Override
	public List<EngagementEntity> findByTeacherId(Long teacherId) throws EntityNotFoundException {
		TeacherEntity teacherEntity = teacherRepository.findById(teacherId).orElse(null);
		if (teacherEntity == null)
			throw new EntityNotFoundException("Teacher Entity with id: " + teacherId + " does not exist.");
		return (List<EngagementEntity>) engagementRepository.findByTeacher(teacherEntity);
	}
	
	@Override
	public List<EngagementEntity> findByClassId(Long sclassId) throws EntityNotFoundException {
		ClassEntity classEntity = classRepository.findById(sclassId).orElse(null);
		if (classEntity == null)
			throw new EntityNotFoundException("ClassEntity with id: " + sclassId + " does not exist.");
		return (List<EngagementEntity>) engagementRepository.findBySclass(classEntity);
	}


	@Override
	public EngagementEntity save(	EngagementEntity engagemenEntity,
									Long courseGradeId,
									Long teacherId,
									Long sclassId)
			throws DataIntegrityViolationException, EntityNotFoundException, EntityMissmatchException {
		
		TeacherEntity teacherEntity = teacherRepository.findById(teacherId).orElse(null);
		if (teacherEntity == null)
			throw new EntityNotFoundException("Teacher Entity with id: " + teacherId + " does not exist.");
		
		CourseGradeEntity courseGradeEntity = courseGradeRepository.findById(courseGradeId).orElse(null);
		if (courseGradeEntity == null)
			throw new EntityNotFoundException("CourseGrade Entity with id: " + courseGradeId + " does not exist.");
		
		ClassEntity classEntity = classRepository.findById(sclassId).orElse(null);
		if (classEntity == null)
			throw new EntityNotFoundException("ClassEntity with id: " + sclassId + " does not exist.");
		
		if(!courseGradeEntity.getGrade().equals(classEntity.getGrade()))
			throw new EntityMissmatchException("Grades do not match for Class Entity and CourseGradeEntity");
		engagemenEntity.setCourseGrade(courseGradeEntity);
		engagemenEntity.setTeacher(teacherEntity);
		engagemenEntity.setSclass(classEntity);
		
		return engagementRepository.save(engagemenEntity);
	}

	@Override
	public EngagementEntity deleteById(Long id) throws EntityNotFoundException{
		EngagementEntity engagementEntity = findById(id);
		engagementRepository.deleteById(id);
		return engagementEntity;
	}
	
	

}
