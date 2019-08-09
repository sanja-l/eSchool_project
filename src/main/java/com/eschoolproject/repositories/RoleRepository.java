package com.eschoolproject.repositories;

import org.springframework.data.repository.CrudRepository;

import com.eschoolproject.entities.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

	public Iterable<RoleEntity> findByRoleName(String roleName);

	public RoleEntity findFirstByRoleName(String roleName);

}
