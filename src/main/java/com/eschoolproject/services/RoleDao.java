package com.eschoolproject.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import com.eschoolproject.entities.RoleEntity;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

public interface RoleDao {
	public List<RoleEntity> findAll();

	public RoleEntity findById(Long id) throws EntityNotFoundException;

	public List<RoleEntity> findByRoleName(String roleName);

	public RoleEntity save(RoleEntity roleEntity) throws DataIntegrityViolationException;

	public RoleEntity deleteById(Long id) throws EntityNotFoundException, EntityIsReferencedException;

}
