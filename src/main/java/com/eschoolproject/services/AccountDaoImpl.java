package com.eschoolproject.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eschoolproject.entities.AccountEntity;
import com.eschoolproject.entities.RoleEntity;
import com.eschoolproject.entities.UserEntity;
import com.eschoolproject.repositories.AccountRepository;
import com.eschoolproject.repositories.RoleRepository;
import com.eschoolproject.repositories.UserRepository;
import com.eschoolproject.util.Encryption;
import com.eschoolproject.util.customexceptions.EntityMissmatchException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

@Service
public class AccountDaoImpl implements AccountDao {
	private final AccountRepository accountRepository;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	// @Autowired
	public AccountDaoImpl(AccountRepository accountRepository, UserRepository userRepository,
			RoleRepository roleRepository) {
		this.accountRepository = accountRepository;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public List<AccountEntity> findAll() {
		return (List<AccountEntity>) accountRepository.findAll();
	}

	@Override
	public AccountEntity findById(Long id) throws EntityNotFoundException {
		AccountEntity accountEntity = accountRepository.findById(id).orElse(null);
		if (accountEntity == null) {
			throw new EntityNotFoundException("Account Entity with id = " + id + " does not exist.");
		}
		return accountEntity;
	}

	@Override
	public AccountEntity findFirstByAccountName(String accountName) throws EntityNotFoundException {
		AccountEntity accountEntity = accountRepository.findFirstByAccountName(accountName);
		if (accountEntity == null) {
			throw new EntityNotFoundException("Account Entity with accountName = " + accountName + " does not exist.");
		}
		return accountEntity;
	}

	@Override
	public List<AccountEntity> findByAccountName(String accountName) {
		return (List<AccountEntity>) accountRepository.findByAccountName(accountName);
	}

	@Override
	public List<AccountEntity> findByRole(RoleEntity roleEntity) {
		return (List<AccountEntity>) accountRepository.findByRole(roleEntity);
	}

	@Override
	public List<AccountEntity> findByUser(UserEntity userEntity) {
		return (List<AccountEntity>) accountRepository.findByUser(userEntity);
	}

	@Override
	public AccountEntity deleteById(Long id) throws EntityNotFoundException, EntityMissmatchException {
		AccountEntity accountEntity = findById(id);
		if(accountEntity.getUser() != null) {
			throw new EntityMissmatchException("Account belong to existing user!") ;
		}
		accountRepository.deleteById(id);
		return accountEntity;
	}

//	@Override
//	public AccountEntity save(	AccountEntity accountEntity,
//								Long userId,
//								Long roleId)
//			throws DataIntegrityViolationException, EntityNotFoundException {
//		RoleEntity roleEntity = roleRepository.findById(roleId).orElse(null);
//		if (roleEntity == null) {
//			throw new EntityNotFoundException("Role entity with id = " + roleId + " does not exist.");
//		}
//		UserEntity userEntity = userRepository.findById(userId).orElse(null);
//		if (userEntity == null) {
//			throw new EntityNotFoundException("User entity with id = " + userId + " does not exist.");
//		}
//		accountEntity.setRole(roleEntity);
//		accountEntity.setUser(userEntity);
//		return accountRepository.save(accountEntity);
//	}

	@Override
	public AccountEntity update(AccountEntity accountEntity) {
		return accountRepository.save(accountEntity);
	}

	@Override
	public void createUserAccount(	Long userId,
									String roleName,
									String accountName)
			throws EntityNotFoundException {
		RoleEntity roleEntity = roleRepository.findFirstByRoleName(roleName);
		if (roleEntity == null)
			throw new EntityNotFoundException("Role with name = " + roleName + " does not exist.");
		UserEntity userEntity = userRepository.findById(userId).orElse(null);
		if (userEntity == null)
			throw new EntityNotFoundException("User Entity with id = " + userId + " does not exist.");

		AccountEntity accountEntity = new AccountEntity();
		accountEntity.setAccountName(accountName);
		// setting up initial password
		accountEntity.setPassword(Encryption.getPassEncoded("12345"));
		accountEntity.setRole(roleEntity);
		accountEntity.setUser(userEntity);
		accountRepository.save(accountEntity);
	}

	@Override
	public void deleteUserAccount(String accountName) throws EntityNotFoundException {
		AccountEntity accountEntity = accountRepository.findFirstByAccountName(accountName);
		if (accountEntity == null) {
			throw new EntityNotFoundException("Account with name " + accountName + " doesn't exist.");
		}
		accountRepository.deleteById(accountEntity.getId());

	}

}
