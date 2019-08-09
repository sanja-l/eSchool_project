package com.eschoolproject.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eschoolproject.controllers.utils.RESTError;
import com.eschoolproject.entities.RoleEntity;
import com.eschoolproject.entities.dto.RoleDto;
import com.eschoolproject.services.RoleDao;
import com.eschoolproject.util.Utility;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/eschool/roles")
@Secured("ROLE_ADMIN")
public class RoleController {

	private final RoleDao roleDao;

	public RoleController(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllRoleEntities() {
		log.debug("Access to all roles ");
		return new ResponseEntity<List<RoleEntity>>(roleDao.findAll(), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/find-by-role-name/{roleName}")
	public ResponseEntity<?> getAllRoleEntitiesByRoleName(@PathVariable String roleName) {
		log.debug("Access to all roles by roleName");
		return new ResponseEntity<List<RoleEntity>>(roleDao.findByRoleName(roleName), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getRoleEntityById(@PathVariable Long id) {
		RoleEntity roleEntity;
		try {
			roleEntity = roleDao.findById(id);
			log.debug("Access to role with id = " + id);
			return new ResponseEntity<RoleEntity>(roleEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteRoleEntityById(@PathVariable Long id) {
		try {
			RoleEntity roleEntity = roleDao.deleteById(id);
			log.debug("Role deleted, id = " + id);
			return new ResponseEntity<RoleEntity>(roleEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		} catch (EntityIsReferencedException e) {
			log.error("EntityIsReferencedException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(6, "Cannot delete. Role is referenced."),
					HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createRoleEntity(	@Valid @RequestBody RoleDto roleDto,
												BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		RoleEntity roleEntity = new RoleEntity();
		roleEntity.setRoleName(roleDto.getRoleName().trim());
		try {
			roleEntity = roleDao.save(roleEntity);
			log.debug("Role created: " + roleEntity.toString());
			return new ResponseEntity<RoleEntity>(roleEntity, HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			log.error("DataIntegrityViolationException occured: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(7, "DataIntegrityViolation occurred: " + e.getMessage()),
					HttpStatus.CONFLICT);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateRoleEntityById(	@PathVariable Long id,
													@Valid @RequestBody RoleDto roleDto,
													BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		RoleEntity roleEntity;

		try {
			roleEntity = roleDao.findById(id);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}
		roleEntity.setRoleName(roleDto.getRoleName().trim());
		try {
			roleEntity = roleDao.save(roleEntity);
			log.debug("Role updated: ", roleEntity.toString());
			return new ResponseEntity<RoleEntity>(roleEntity, HttpStatus.OK);

		} catch (DataIntegrityViolationException e) {
			log.error("DataIntegrityViolationException occured: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(7, "DataIntegrityViolation occurred: " + e.getMessage()),
					HttpStatus.CONFLICT);
		}
	}

}
