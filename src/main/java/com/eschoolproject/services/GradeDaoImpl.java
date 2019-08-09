package com.eschoolproject.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.eschoolproject.entities.GradeEntity;
import com.eschoolproject.repositories.GradeRepository;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

@Service
public class GradeDaoImpl implements GradeDao {

	private final GradeRepository gradeRepository;


	//@Autowired
	public GradeDaoImpl(GradeRepository gradeRepository) {
		this.gradeRepository = gradeRepository;
	}

	@Override
	public List<GradeEntity> findAll() {
		return (List<GradeEntity>) gradeRepository.findAll();
	}

	@Override
	public GradeEntity findById(Long id) throws EntityNotFoundException {
		GradeEntity gradeEntity = gradeRepository.findById(id).orElse(null);
		if (gradeEntity == null)
			throw new EntityNotFoundException("Grade Entity with id: " + id + " does not exist.");

		return gradeEntity;
	}


	@Override
	public List<GradeEntity> findByGradeValue(Integer gradeValue) {
		return (List<GradeEntity>) gradeRepository.findByGradeValue(gradeValue);
	}

	@Override
	public GradeEntity deleteById(Long id) throws EntityNotFoundException, EntityIsReferencedException {
		GradeEntity gradeEntity = gradeRepository.findById(id).orElse(null);
		if (gradeEntity == null)
			throw new EntityNotFoundException("Grade Entity with id: " + id + " does not exist.");
		if (gradeEntity.getClassEntities().size()>0)
			throw new EntityIsReferencedException(
					"Cannot delete. Grade Entity with id: " + id + " is referenced by Class Entity .");
		if (gradeEntity.getCourseGradeEntities().size()>0)
			throw new EntityIsReferencedException(
					"Cannot delete. Grade Entity with id: " + id + " is referenced by CourseGrade Entity .");
		gradeRepository.deleteById(id);
		return gradeEntity;
	}

	@Override
	public GradeEntity save(GradeEntity gradeEntity) throws DataIntegrityViolationException {
		return gradeRepository.save(gradeEntity);
	}

}
