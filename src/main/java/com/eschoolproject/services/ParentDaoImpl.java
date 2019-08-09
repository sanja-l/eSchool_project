package com.eschoolproject.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.eschoolproject.entities.ParentEntity;
import com.eschoolproject.entities.StudentEntity;
import com.eschoolproject.entities.UserEntity;
import com.eschoolproject.repositories.ParentRepository;
import com.eschoolproject.repositories.StudentRepository;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

@Service
@Transactional
public class ParentDaoImpl implements ParentDao {
	@PersistenceContext
	private EntityManager em;

	private final ParentRepository parentRepository;
	private final AccountDao accountDao;
	private final StudentRepository studentRepository;

	// @Autowired
	public ParentDaoImpl(ParentRepository parentRepository, AccountDao accountDao,
			StudentRepository studentRepository) {
		this.parentRepository = parentRepository;
		this.accountDao = accountDao;
		this.studentRepository = studentRepository;
	}

	@Override
	public List<ParentEntity> findAll() {
		return (List<ParentEntity>) parentRepository.findAll();
	}

	@Override
	public ParentEntity findById(Long id) throws EntityNotFoundException {
		ParentEntity parentEntity = parentRepository.findById(id).orElse(null);
		if (parentEntity == null) {
			throw new EntityNotFoundException("Parent Entity with id = " + id + "does not exist.");
		}
		return parentEntity;
	}

	@Override
	public ParentEntity deleteById(Long id) throws EntityNotFoundException {
		ParentEntity parentEntity = parentRepository.findById(id).orElse(null);
		if (parentEntity == null) {
			throw new EntityNotFoundException("Parent Entity with id = " + id + "does not exist.");
		}
		// first remove all children
		parentEntity.setChildren(new HashSet<StudentEntity>());
		parentEntity = parentRepository.save(parentEntity);
		if (parentEntity.getAccountEntities().size() > 1) {
			// there are additional non-parent accounts, delete only from parent table
			deleteFromParent(parentEntity);
		} else {
			parentRepository.deleteById(id);
		}
		accountDao.deleteUserAccount(parentEntity.getParentSID());
		return parentEntity;
	}

	private int insertIntoTeacher(ParentEntity parentEntity) {
		String sqlString = "INSERT INTO u_parent (email, phone_number, address, parent_sid, id) VALUES (?,?,?,?,?,?)";
		// @formatter:off
		int result = em
				.createNativeQuery(sqlString)
				.setParameter(1, parentEntity.getEmail())
				.setParameter(2, parentEntity.getPhoneNumber())
				.setParameter(3, parentEntity.getAddress())
				.setParameter(4, parentEntity.getParentSID())
				.setParameter(5, parentEntity.getId())
				.executeUpdate();
		em.clear();
		// @formatter:on
		return result;

	}

	private int deleteFromParent(ParentEntity parentEntity) {
		String sqlString = "DELETE FROM u_parent WHERE id = ?";

		int result = em.createNativeQuery(sqlString).setParameter(1, parentEntity.getId()).executeUpdate();
		em.clear();
		return result;

	}

	@Override
	public ParentEntity save(	ParentEntity parentEntity,
								UserEntity userEntity)
			throws DataIntegrityViolationException, EntityNotFoundException {
		Long id;
		if (userEntity != null) {
			id = parentEntity.getId();
			int numUpdated = insertIntoTeacher(parentEntity);
			System.out.println(numUpdated);
			parentEntity = parentRepository.findById(id).orElse(null);
		} else {
			parentEntity = parentRepository.save(parentEntity);
			id = parentEntity.getId();
		}
		if (parentEntity == null) {
			throw new EntityNotFoundException("Parent Entity with id = " + id + " does not exist.");
		}
		// Automatically creating parents's account
		accountDao.createUserAccount(parentEntity.getId(), "ROLE_PARENT", parentEntity.getParentSID());
		return parentEntity;
	}

	@Override
	public ParentEntity update(ParentEntity parentEntity)
			throws DataIntegrityViolationException, EntityNotFoundException {
		return parentRepository.save(parentEntity);
	}

	@Override
	public ParentEntity addStudentToParent(	Long parentId,
											Long studentId)
			throws DataIntegrityViolationException, EntityNotFoundException {
		ParentEntity parentEntity = parentRepository.findById(parentId).orElse(null);
		if (parentEntity == null) {
			throw new EntityNotFoundException("Parent Entity with id = " + parentId + " does not exist." );
		}
		StudentEntity studentEntity = studentRepository.findById(studentId).orElse(null);
		if (studentEntity == null) {
			throw new EntityNotFoundException("Student Entity with id = " + studentId + " does not exist.");
		}
		Set<StudentEntity> studentEntities = parentEntity.getChildren();
		studentEntities.add(studentEntity);
		parentEntity.setChildren(studentEntities);
		return parentRepository.save(parentEntity);

	}
	
	@Override
	public ParentEntity removeStudentFromParent(	Long parentId,
											Long studentId)
			throws EntityNotFoundException {
		ParentEntity parentEntity = parentRepository.findById(parentId).orElse(null);
		if (parentEntity == null) {
			throw new EntityNotFoundException("Parent Entity with id = " + parentId + " does not exist." );
		}
		StudentEntity studentEntity = studentRepository.findById(studentId).orElse(null);
		if (studentEntity == null) {
			throw new EntityNotFoundException("Student Entity with id = " + studentId + " does not exist.");
		}
		Set<StudentEntity> studentEntities = parentEntity.getChildren();
		studentEntities.remove(studentEntity);
		parentEntity.setChildren(studentEntities);
		return parentRepository.save(parentEntity);

	}

}
