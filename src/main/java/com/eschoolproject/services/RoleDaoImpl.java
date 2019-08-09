package com.eschoolproject.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.eschoolproject.entities.RoleEntity;
import com.eschoolproject.repositories.RoleRepository;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

@Service
public class RoleDaoImpl implements RoleDao {

	private final RoleRepository roleRepository;

	// @Autowired
	public RoleDaoImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public List<RoleEntity> findAll() {
		return (List<RoleEntity>) roleRepository.findAll();
	}

	@Override
	public RoleEntity findById(Long id) throws EntityNotFoundException {
		RoleEntity roleEntity = roleRepository.findById(id).orElse(null);
		if (roleEntity == null)
			throw new EntityNotFoundException("RoleEntity with id = " + id + " does not exist.");

		return roleEntity;
	}

	@Override
	public List<RoleEntity> findByRoleName(String roleName) {
		return (List<RoleEntity>) roleRepository.findByRoleName(roleName);
	}

	@Override
	public RoleEntity deleteById(Long id) throws EntityNotFoundException, EntityIsReferencedException {
		RoleEntity roleEntity = roleRepository.findById(id).orElse(null);
		if (roleEntity == null)
			throw new EntityNotFoundException("Role Entity with id = " + id + " does not exist.");
		if (roleEntity.getAccountEntities().size() > 0)
			throw new EntityIsReferencedException(
					"Cannot delete. Role Entity with id = " + id + " is referenced by account.");
		roleRepository.deleteById(id);
		return roleEntity;
	}

	@Override
	public RoleEntity save(RoleEntity roleEntity) throws DataIntegrityViolationException {

		return roleRepository.save(roleEntity);
	}

}
