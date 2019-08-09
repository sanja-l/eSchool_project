package com.eschoolproject.controllers;

import java.sql.SQLIntegrityConstraintViolationException;
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
import com.eschoolproject.entities.UserEntity;
import com.eschoolproject.entities.dto.UserDto;
import com.eschoolproject.services.UserDao;
import com.eschoolproject.util.Utility;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/eschool/users")
@Secured("ROLE_ADMIN")

public class UserController {

	private final UserDao userDao;

	public UserController(UserDao userDao) {
		this.userDao = userDao;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllRoleEntities() {
		log.debug("Access to all users ");
		return new ResponseEntity<List<UserEntity>>(userDao.findAll(), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/find-by-first-name/{firstname}")
	public ResponseEntity<?> getAllRoleEntitiesByFirstname(@PathVariable String firstname) {
		log.debug("Access to all users by firstname");
		return new ResponseEntity<List<UserEntity>>(userDao.findByFirstname(firstname), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/find-by-last-name/{lastname}")
	public ResponseEntity<?> getAllRoleEntitiesByLastname(@PathVariable String lastname) {
		log.debug("Access to all users by firstname");
		return new ResponseEntity<List<UserEntity>>(userDao.findByLastname(lastname), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/find-by-first/{firstname}/last-name/{lastname}")
	public ResponseEntity<?> getAllRoleEntitiesByLastname(	@PathVariable String firstname,
															@PathVariable String lastname) {
		log.debug("Access to all users by firstname and lastname");
		return new ResponseEntity<List<UserEntity>>(userDao.findByLastnameAndFirstname(lastname, firstname), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getUserEntityById(@PathVariable Long id) {
		UserEntity userEntity;
		try {
			userEntity = userDao.findById(id);
			log.debug("Access to user with id = " + id);
			return new ResponseEntity<UserEntity>(userEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()), HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteUserEntityById(@PathVariable Long id) {
		try {
			UserEntity userEntity = userDao.deleteById(id);
			log.debug("User deleted: " + userEntity.toString());
			return new ResponseEntity<UserEntity>(userEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Role not found"), HttpStatus.NOT_FOUND);
		} catch (EntityIsReferencedException e) {
			log.error("EntityIsReferencedException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(9, "Entity is referenced: " +e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createUserEntity(	@Valid @RequestBody UserDto userDto,
												BindingResult result)
			throws EntityNotFoundException {
		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		UserEntity userEntity = new UserEntity();
		userEntity.setPin(userDto.getPin());
		userEntity.setFirstname(userDto.getFirstname().trim());
		userEntity.setLastname(userDto.getLastname().trim());
		try {
			userEntity = userDao.save(userEntity);
			log.debug("User created: " + userEntity.toString());
			return new ResponseEntity<UserEntity>(userEntity, HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
				log.error("SQLIntegrityConstraintViolationException entry occured: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(8, "Duplicate entry occurred: " + e.getMessage()),
						HttpStatus.CONFLICT);
			}
			log.error("DataIntegrityViolationException occured: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(7, "DataIntegrityViolation occurred: " + e.getMessage()),
					HttpStatus.CONFLICT);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()), HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateUserEntityById(	@PathVariable Long id,
													@Valid @RequestBody UserDto userDto,
													BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		UserEntity userEntity;

		try {
			userEntity = userDao.findById(id);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()), HttpStatus.NOT_FOUND);
		}
		if(!userEntity.getPin().equals(userDto.getPin())) {
			log.error("PIN does not match");
			return new ResponseEntity<RESTError>(new RESTError(11, "PIN does not match"), HttpStatus.BAD_REQUEST);		
		}
		userEntity.setFirstname(userDto.getFirstname().trim());
		userEntity.setLastname(userDto.getLastname().trim());
		try {
			userEntity = userDao.update(userEntity);
			log.debug("User updated: " + userEntity.toString());
			return new ResponseEntity<UserEntity>(userEntity, HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
				log.error("SQLIntegrityConstraintViolationException entry occured: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(8, "Duplicate entry occurred: " + e.getMessage()),
						HttpStatus.CONFLICT);
			}
			log.error("DataIntegrityViolationException occured: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(7, "DataIntegrityViolation occurred: " + e.getMessage()),
					HttpStatus.CONFLICT);
		}
	}


}
