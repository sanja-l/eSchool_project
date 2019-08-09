package com.eschoolproject.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.eschoolproject.entities.AssesmentEntity;
import com.eschoolproject.entities.CourseGradeEntity;
import com.eschoolproject.entities.EngagementEntity;
import com.eschoolproject.entities.StudentEntity;
import com.eschoolproject.entities.TeacherEntity;
import com.eschoolproject.repositories.AssesmentRepository;
import com.eschoolproject.repositories.CourseGradeRepository;
import com.eschoolproject.repositories.EngagementRepository;
import com.eschoolproject.repositories.StudentRepository;
import com.eschoolproject.repositories.TeacherRepository;
import com.eschoolproject.util.customexceptions.EntityMissmatchException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

@Service
public class AssesmentDaoImpl implements AssesmentDao {

	private final AssesmentRepository assesmentRepository;
	private final EngagementRepository engagementRepository;
	private final CourseGradeRepository courseGradeRepository;
	private final TeacherRepository teacherRepository;
	private final StudentRepository studentRepository;


	// @Autowired
	public AssesmentDaoImpl(AssesmentRepository assesmentRepository, EngagementRepository engagementRepository,
			CourseGradeRepository courseGradeRepository, TeacherRepository teacherRepository,
			StudentRepository studentRepository) {
		this.assesmentRepository = assesmentRepository;
		this.engagementRepository = engagementRepository;
		this.courseGradeRepository = courseGradeRepository;
		this.teacherRepository = teacherRepository;
		this.studentRepository = studentRepository;

	}

	@Override
	public List<AssesmentEntity> findAll() {
		return (List<AssesmentEntity>) assesmentRepository.findAll();
	}

	@Override
	public AssesmentEntity findById(Long id) throws EntityNotFoundException {
		AssesmentEntity assesmentEntity = assesmentRepository.findById(id).orElse(null);
		if (assesmentEntity == null)
			throw new EntityNotFoundException("Assesment Entity with id = " + id + " does not exist.");
		return assesmentEntity;
	}

	@Override
	public List<AssesmentEntity> findByCourseGradeId(Long courseGradeId) throws EntityNotFoundException {
		CourseGradeEntity courseGradeEntity = courseGradeRepository.findById(courseGradeId).orElse(null);
		if (courseGradeEntity == null)
			throw new EntityNotFoundException("CourseGrade Entity with id = " + courseGradeId + " does not exist.");
		return (List<AssesmentEntity>) assesmentRepository.findByCourseGrade(courseGradeEntity);
	}

	@Override
	public List<AssesmentEntity> findByTeacherId(Long teacherId) throws EntityNotFoundException {
		TeacherEntity teacherEntity = teacherRepository.findById(teacherId).orElse(null);
		if (teacherEntity == null)
			throw new EntityNotFoundException("Teacher Entity with id = " + teacherId + " does not exist.");
		return (List<AssesmentEntity>) assesmentRepository.findByTeacher(teacherEntity);
	}

	@Override
	public List<AssesmentEntity> findByStudentId(Long studentId) throws EntityNotFoundException {
		StudentEntity studentEntity = studentRepository.findById(studentId).orElse(null);
		if (studentEntity == null)
			throw new EntityNotFoundException("Student Entity with id = " + studentId + " does not exist.");
		return (List<AssesmentEntity>) assesmentRepository.findByStudent(studentEntity);
	}

	@Override
	public AssesmentEntity save(AssesmentEntity assesmentEntity, Long courseGradeId, Long teacherId, Long studentId)
			throws DataIntegrityViolationException, EntityNotFoundException, EntityMissmatchException {

		TeacherEntity teacherEntity = teacherRepository.findById(teacherId).orElse(null);
		if (teacherEntity == null)
			throw new EntityNotFoundException("Teacher Entity with id = " + teacherId + " does not exist.");

		CourseGradeEntity courseGradeEntity = courseGradeRepository.findById(courseGradeId).orElse(null);
		if (courseGradeEntity == null)
			throw new EntityNotFoundException("CourseGrade Entity with id = " + courseGradeId + " does not exist.");

		StudentEntity studentEntity = studentRepository.findById(studentId).orElse(null);
		if (studentEntity == null)
			throw new EntityNotFoundException("Student Entity with id = " + studentId + " does not exist.");

		if (!studentEntity.getSclass().getGrade().equals(courseGradeEntity.getGrade()))
			throw new EntityMissmatchException("Grades do not match for student Entity and courseGrade Entity");

		List<EngagementEntity> engagementEntities = (List<EngagementEntity>) engagementRepository
				.findByTeacherAndSclassAndCourseGrade(teacherEntity, studentEntity.getSclass(), courseGradeEntity);
		if (engagementEntities.isEmpty())
			throw new EntityMissmatchException("Teacher-Class-CourseGrade do not match for student Entity");

		assesmentEntity.setCourseGrade(courseGradeEntity);
		assesmentEntity.setTeacher(teacherEntity);
		assesmentEntity.setStudent(studentEntity);

		return assesmentRepository.save(assesmentEntity);
	}

	@Override
	public AssesmentEntity deleteById(Long id) throws EntityNotFoundException {
		AssesmentEntity assesmentEntity = findById(id);
		assesmentRepository.deleteById(id);
		return assesmentEntity;
	}

	@Override
	public List<AssesmentEntity> findByParentId(Long parentId){
		List<StudentEntity> studentEntities = studentRepository.qFindStudentsByParentId(parentId);

		List<AssesmentEntity> studentAssesments = new ArrayList<AssesmentEntity>();
		for (StudentEntity studentEntity : studentEntities) {
			studentAssesments.addAll(studentEntity.getAssesmentEntities());
			studentEntity.getAssesmentEntities();
		}
		return studentAssesments;
	}

	@Override
	public AssesmentEntity deleteByIdAndTeacherId(Long id, Long teacherId)
			throws EntityNotFoundException, EntityMissmatchException {
		AssesmentEntity assesmentEntity = findById(id);
		if (!assesmentEntity.getTeacher().getId().equals(teacherId))
			throw new EntityMissmatchException(
					"Assesment with id = " + id + " do not belog to the teacher with id = " + teacherId);
		assesmentRepository.deleteById(id);

		return assesmentEntity;
	}

	@Override
	public Long FindTeachersByAssesmentId(Long id) {
		List<Long> teacherIds = assesmentRepository.qFindTeachersByAssesmentId(id);
		return teacherIds.get(0);
	}

}
