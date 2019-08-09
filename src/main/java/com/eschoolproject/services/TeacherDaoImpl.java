package com.eschoolproject.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.eschoolproject.entities.TeacherEntity;
import com.eschoolproject.entities.UserEntity;
import com.eschoolproject.repositories.TeacherRepository;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

@Service
@Transactional
public class TeacherDaoImpl implements TeacherDao {
	@PersistenceContext
	private EntityManager em;

	private final TeacherRepository teacherRepository;
	private final AccountDao accountDao;

	// @Autowired
	public TeacherDaoImpl(TeacherRepository teacherRepository, AccountDao accountDao) {
		this.teacherRepository = teacherRepository;
		this.accountDao = accountDao;
	}

	@Override
	public List<TeacherEntity> findAll() {
		return (List<TeacherEntity>) teacherRepository.findAll();
	}

	@Override
	public TeacherEntity findById(Long id) throws EntityNotFoundException {
		TeacherEntity teacherEntity = teacherRepository.findById(id).orElse(null);
		if (teacherEntity == null) {
			throw new EntityNotFoundException("Teacher Entity with id = " + id + " does not exist.");
		}
		return teacherEntity;
	}

	@Override
	public TeacherEntity deleteById(Long id) throws EntityNotFoundException, EntityIsReferencedException {
		TeacherEntity teacherEntity = teacherRepository.findById(id).orElse(null);
		if (teacherEntity == null)
			throw new EntityNotFoundException("Teacher Entity with id = " + id + " does not exist.");
		if (teacherEntity.getAssesmentEntities().size() > 0)
			throw new EntityIsReferencedException("Teacher Entity with id = " + id + " is referenced by assesments");
		if (teacherEntity.getEngagementEntities().size() > 0)
			throw new EntityIsReferencedException("Teacher Entity with id = " + id + " is referenced by assesments");
		if (teacherEntity.getAccountEntities().size() > 1) {
			// there are additional non-teacher accounts, delete only from teacher table
			deleteFromTeacher(teacherEntity);

		} else {
			teacherRepository.deleteById(id);
		}
		accountDao.deleteUserAccount(teacherEntity.getTeacherSID());
		return teacherEntity;
	}

	private int insertIntoTeacher(TeacherEntity teacherEntity) {
		String sqlString = "INSERT INTO u_teacher (vocation, edu_field, teacher_sid, id) VALUES (?,?,?,?)";

		int result = em.createNativeQuery(sqlString).setParameter(1, teacherEntity.getVocation())
				.setParameter(2, teacherEntity.getEduField()).setParameter(3, teacherEntity.getTeacherSID())
				.setParameter(4, teacherEntity.getId()).executeUpdate();
		em.clear();
		return result;

	}

	private int deleteFromTeacher(TeacherEntity teacherEntity) {
		String sqlString = "DELETE FROM u_teacher WHERE id = ?";

		int result = em.createNativeQuery(sqlString).setParameter(1, teacherEntity.getId()).executeUpdate();
		em.clear();
		return result;

	}

	@Override
	public TeacherEntity save(	TeacherEntity teacherEntity,
								UserEntity userEntity)
			throws DataIntegrityViolationException, EntityNotFoundException {
		Long id;
		if (userEntity != null) {
			id = teacherEntity.getId();
			int numUpdated = insertIntoTeacher(teacherEntity);
			System.out.println(numUpdated);
			teacherEntity = teacherRepository.findById(id).orElse(null);
		} else {
			teacherEntity = teacherRepository.save(teacherEntity);
			id = teacherEntity.getId();
		}
		if (teacherEntity == null) {
			throw new EntityNotFoundException("Teacher Entity with id = " + id + " does not exist.");
		}
		// Automatically creating teacher's account
		accountDao.createUserAccount(teacherEntity.getId(), "ROLE_TEACHER", teacherEntity.getTeacherSID());

		return teacherEntity;
	}

	@Override
	public TeacherEntity update(TeacherEntity teacherEntity)
			throws DataIntegrityViolationException, EntityNotFoundException {
		return teacherRepository.save(teacherEntity);
	}

}
