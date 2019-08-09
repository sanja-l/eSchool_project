package com.eschoolproject.services;

import java.util.List;

import com.eschoolproject.entities.AccountEntity;
import com.eschoolproject.entities.RoleEntity;
import com.eschoolproject.entities.UserEntity;
import com.eschoolproject.util.customexceptions.EntityMissmatchException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

public interface AccountDao {
	public List<AccountEntity> findAll();

	public AccountEntity findById(Long id) throws EntityNotFoundException;

	public List<AccountEntity> findByAccountName(String accountName);

	public AccountEntity findFirstByAccountName(String accountName) throws EntityNotFoundException;

	public List<AccountEntity> findByRole(RoleEntity roleEntity);

	public List<AccountEntity> findByUser(UserEntity userEntity);

	public AccountEntity deleteById(Long id) throws EntityNotFoundException, EntityMissmatchException;

//	public AccountEntity save(	AccountEntity accountEntity,
//								Long userId,
//								Long roleId)
//			throws DataIntegrityViolationException, EntityNotFoundException;

	void createUserAccount(	Long userId,
							String roleName,
							String accountName)
			throws EntityNotFoundException;

	void deleteUserAccount(String accountName) throws EntityNotFoundException;

	AccountEntity update(AccountEntity accountEntity);

}
