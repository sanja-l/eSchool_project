package com.eschoolproject.services;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.eschoolproject.entities.CourseEntity;
import com.eschoolproject.entities.ParentEntity;
import com.eschoolproject.entities.StudentEntity;
import com.eschoolproject.entities.TeacherEntity;
import com.eschoolproject.entities.UserEntity;
import com.eschoolproject.repositories.AccountRepository;
import com.eschoolproject.repositories.StudentRepository;
import com.eschoolproject.repositories.UserRepository;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

@Service
@Transactional
public class StudentDaoImpl implements StudentDao {

	@PersistenceContext
	private EntityManager em;

	private final StudentRepository studentRepository;
	private final AccountDao accountDao;

	// @Autowired
	public StudentDaoImpl(StudentRepository studentRepository, UserRepository userRepository,
			AccountRepository accountRepository, AccountDao accountDao) {
		this.studentRepository = studentRepository;
		this.accountDao = accountDao;
	}

	@Override
	public List<StudentEntity> findAll() {
		return (List<StudentEntity>) studentRepository.findAll();
	}

	@Override
	public StudentEntity findById(Long id) throws EntityNotFoundException {
		StudentEntity studentEntity = studentRepository.findById(id).orElse(null);
		if (studentEntity == null) {
			throw new EntityNotFoundException("Student Entity with id = " + id + " does not exist.");
		}
		return studentEntity;
	}

	@Override
	public StudentEntity deleteById(Long id) throws EntityNotFoundException, EntityIsReferencedException {
		StudentEntity studentEntity = studentRepository.findById(id).orElse(null);
		if (studentEntity == null) {
			throw new EntityNotFoundException("Student Entity with id = " + id + " does not exist.");
		}
		if (studentEntity.getAssesmentEntities().size() > 0)
			throw new EntityIsReferencedException("Student Entity with id = " + id + " is referenced by assesments");
		
		if (studentEntity.getParents().size() > 0)
			throw new EntityIsReferencedException("Student Entity with id = " + id + " is referenced by parents");

		if (studentEntity.getAccountEntities().size() > 1) {
			// there are additional non-student accounts, delete only from teacher table
			deleteFromStudent(studentEntity);
		} else {
			studentRepository.deleteById(id);
		}
		accountDao.deleteUserAccount(studentEntity.getStudentSID());
		return studentEntity;
	}

	private int insertIntoStudent(StudentEntity studentEntity) {
		String sqlString = "INSERT INTO u_student (sclass,birth_date,birth_city, birth_country, student_sid, id) VALUES (?,?,?,?,?,?)";
		// @formatter:off
		int result = em.createNativeQuery(sqlString)
				.setParameter(1, studentEntity.getSclass())
				.setParameter(2, studentEntity.getBirthDate())
				.setParameter(3, studentEntity.getBirthCity())
				.setParameter(4, studentEntity.getBirthCountry())
				.setParameter(5, studentEntity.getStudentSID())
				.setParameter(6, studentEntity.getId())
				.executeUpdate();
		em.clear();
		// @formatter:on

		return result;

	}

	private int deleteFromStudent(StudentEntity studentEntity) {
		String sqlString = "DELETE FROM u_student WHERE id = ?";

		int result = em.createNativeQuery(sqlString).setParameter(1, studentEntity.getId()).executeUpdate();
		em.clear();
		return result;

	}

	@Override
	public StudentEntity save(	StudentEntity studentEntity,
								UserEntity userEntity)
			throws DataIntegrityViolationException, EntityNotFoundException {
		Long id;
		if (userEntity != null) {
			id = studentEntity.getId();
			int numUpdated = insertIntoStudent(studentEntity);
			System.out.println(numUpdated);
			studentEntity = studentRepository.findById(id).orElse(null);
		} else {
			studentEntity = studentRepository.save(studentEntity);
			id = studentEntity.getId();
		}
		if (studentEntity == null) {
			throw new EntityNotFoundException("Student Entity with id = " + id + " does not exist.");
		}
		// Automatically creating student's account
		accountDao.createUserAccount(studentEntity.getId(), "ROLE_STUDENT", studentEntity.getStudentSID());
		return studentEntity;
	}

	@Override
	public StudentEntity update(StudentEntity studentEntity)
			throws DataIntegrityViolationException, EntityNotFoundException {
		return studentRepository.save(studentEntity);
	}

	@Override
	public Set<ParentEntity> findParentsByStudentId(Long studentId) throws EntityNotFoundException {
		StudentEntity studentEntity = studentRepository.findById(studentId).orElse(null);
		if (studentEntity == null) {
			throw new EntityNotFoundException("Student Entity with id = " + studentId + " does not exist.");
		}
		Set<ParentEntity> parentEntities = studentEntity.getParents();
		return parentEntities;
	}

	@Override
	public List<CourseEntity> findCoursesByStudentId(Long studentId) throws EntityNotFoundException {
		StudentEntity studentEntity = studentRepository.findById(studentId).orElse(null);
		if (studentEntity == null) {
			throw new EntityNotFoundException("Student Entity with id = " + studentId + " does not exist.");
		}
		return studentRepository.qFindCoursesByStudentId(studentId);

	}

	@Override
	public List<TeacherEntity> findTeachersByStudentId(Long studentId) throws EntityNotFoundException {
		StudentEntity studentEntity = studentRepository.findById(studentId).orElse(null);
		if (studentEntity == null) {
			throw new EntityNotFoundException("Student Entity with id = " + studentId + " does not exist.");
		}
		return studentRepository.qFindTeachersByStudentId(studentId);
	}
	
	@Override
	public List<String> findParentsEmailsByStudentId(Long sid){
		return studentRepository.qFindParentsEmailsByStudentId(sid);
	}

}
