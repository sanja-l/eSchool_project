package com.eschoolproject.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.eschoolproject.entities.UserEntity;
import com.eschoolproject.repositories.RoleRepository;
import com.eschoolproject.repositories.UserRepository;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

@Service
public class UserDaoImpl implements UserDao {
	@PersistenceContext
	private EntityManager em;

	private final UserRepository userRepository;
	private final AccountDao accountDao;

	// @Autowired
	public UserDaoImpl(UserRepository userRepository, AccountDao accountDao, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.accountDao = accountDao;
	}

	@Override
	public List<UserEntity> findAll() {

		return (List<UserEntity>) userRepository.findAll();
	}

	@Override
	public UserEntity findById(Long id) throws EntityNotFoundException {
		UserEntity userEntity = userRepository.findById(id).orElse(null);
		if (userEntity == null)
			throw new EntityNotFoundException("UserEntity with id = " + id + " does not exist.");

		return userEntity;
	}

	@Override
	public List<UserEntity> findByFirstname(String firstname) {
		return (List<UserEntity>) userRepository.findByFirstnameIgnoreCase(firstname);
	}

	@Override
	public List<UserEntity> findByLastname(String lastname) {
		return (List<UserEntity>) userRepository.findByLastnameIgnoreCase(lastname);
	}

	@Override
	public List<UserEntity> findByLastnameAndFirstname(String lastname, String firstname) {
		return (List<UserEntity>) userRepository.findByLastnameAndFirstnameIgnoreCase(lastname, firstname);
	}

	@Override
	public UserEntity deleteById(Long id) throws EntityNotFoundException, EntityIsReferencedException {
		UserEntity userEntity = userRepository.findById(id).orElse(null);
		if (userEntity == null)
			throw new EntityNotFoundException("User Entity with id = " + id + " does not exist.");
		if (userEntity.getAccountEntities().size() == 1
				&& !userEntity.getAccountEntities().get(0).getRole().getRoleName().matches("ROLE_ADMIN|ROLE_GUEST"))
			throw new EntityIsReferencedException(
					"Cannot delete. User Entity with id = " + id + " is referenced(inhereted) from user.");
		if (userEntity.getAccountEntities().size() > 1)
			throw new EntityIsReferencedException(
					"Cannot delete. User Entity with id = " + id + " is referenced by account.");
		userRepository.deleteById(id);
		// when deleting a user, account is automatically deleted
		accountDao.deleteUserAccount(userEntity.getPin());
		return userEntity;
	}

	@Override
	public UserEntity save(UserEntity userEntity) throws DataIntegrityViolationException, EntityNotFoundException {
		userEntity = userRepository.save(userEntity);
		// TODO user that has no role: parent, teacher, is initially set to guest
		// account, that has no rights
		accountDao.createUserAccount(userEntity.getId(), "ROLE_GUEST", userEntity.getPin());
		return userEntity;
	}

	@Override
	public UserEntity update(UserEntity userEntity) throws DataIntegrityViolationException {
		return userRepository.save(userEntity);
	}

	@Override
	public UserEntity findByPin(String pin) {
		List<UserEntity> userEntities = userRepository.findByPin(pin);
		if (userEntities.isEmpty())
			return null;
		return userEntities.get(0);

	}

}
