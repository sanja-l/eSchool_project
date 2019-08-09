package com.eschoolproject.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.eschoolproject.entities.AccountEntity;
import com.eschoolproject.entities.RoleEntity;
import com.eschoolproject.entities.UserEntity;

public interface AccountRepository extends CrudRepository<AccountEntity, Long> {

	public List<AccountEntity> findByRole(RoleEntity roleEntity);

	public List<AccountEntity> findByUser(UserEntity userEntity);

	public List<AccountEntity> findByAccountName(String accountName);

	public AccountEntity findFirstByAccountName(String accountName);

	
}
