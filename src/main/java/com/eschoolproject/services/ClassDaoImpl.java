package com.eschoolproject.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.eschoolproject.entities.ClassEntity;
import com.eschoolproject.entities.GradeEntity;
import com.eschoolproject.repositories.ClassRepository;
import com.eschoolproject.repositories.GradeRepository;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

@Service
public class ClassDaoImpl implements ClassDao {
	private final ClassRepository classRepository;
	private final GradeRepository gradeRepository;

	// @Autowired
	public ClassDaoImpl(ClassRepository classRepository, GradeRepository gradeRepository) {
		this.classRepository = classRepository;
		this.gradeRepository = gradeRepository;
	}

	@Override
	public List<ClassEntity> findAll() {
		return (List<ClassEntity>) classRepository.findAll();
	}

	@Override
	public List<ClassEntity> findByClassName(String className) {
		return (List<ClassEntity>) classRepository.findByClassName(className);
	}

	@Override
	public List<ClassEntity> findByClassNameAndGradeValue(	String className,
															Integer gradeValue) {
		return (List<ClassEntity>) classRepository.qfindByClassNameAndGradeValue(className, gradeValue);
	}

	@Override
	public List<ClassEntity> findByGradeValue(Integer gradeValue) {
		return (List<ClassEntity>) classRepository.qfindByGradeValue(gradeValue);
	}

	@Override
	public List<ClassEntity> findByGradeId(Long gradeId) throws EntityNotFoundException {
		GradeEntity gradeEntity = gradeRepository.findById(gradeId).orElse(null);
		if (gradeEntity == null) {
			throw new EntityNotFoundException("Grade Entity with id = " + gradeId + " does not exist.");
		}
		return (List<ClassEntity>) classRepository.findByGrade(gradeEntity);
	}

	@Override
	public ClassEntity findById(Long id) throws EntityNotFoundException {
		ClassEntity classEntity = classRepository.findById(id).orElse(null);
		if (classEntity == null)
			throw new EntityNotFoundException("Class Entity with id: " + id + " does not exist.");

		return classEntity;
	}

	@Override
	public ClassEntity deleteById(Long id) throws EntityNotFoundException, EntityIsReferencedException {
		ClassEntity classEntity = classRepository.findById(id).orElse(null);
		if (classEntity == null)
			throw new EntityNotFoundException("Class Entity with id: " + id + " does not exist.");
		if (classEntity.getStudentEntities().size() > 0)
			throw new EntityIsReferencedException(
					"Cannot delete. Class Entity with id: " + id + " is referenced by Student entities.");
		if (classEntity.getEngagementEntities().size() > 0)
			throw new EntityIsReferencedException(
					"Cannot delete. Class Entity with id: " + id + " is referenced by Engagement entities.");

		classRepository.deleteById(id);
		return classEntity;
	}

	@Override
	public ClassEntity save(ClassEntity classEntity,
							Long gradeId)
			throws DataIntegrityViolationException, EntityNotFoundException {
		GradeEntity gradeEntity = gradeRepository.findById(gradeId).orElse(null);
		if (gradeEntity == null) {
			throw new EntityNotFoundException("GradeEntity with id = " + gradeId + " does not exist.");
		}
		classEntity.setGrade(gradeEntity);
		return classRepository.save(classEntity);
	}

}
