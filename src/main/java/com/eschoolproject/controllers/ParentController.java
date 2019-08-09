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
import com.eschoolproject.entities.ParentEntity;
import com.eschoolproject.entities.UserEntity;
import com.eschoolproject.entities.dto.ParentDto;
import com.eschoolproject.services.ParentDao;
import com.eschoolproject.services.UserDao;
import com.eschoolproject.util.Utility;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/eschool/parents")
@Secured("ROLE_ADMIN")
public class ParentController {

	private final ParentDao parentDao;
	private final UserDao userDao;

	public ParentController(ParentDao parentDao, UserDao userDao) {
		this.parentDao = parentDao;
		this.userDao = userDao;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllCourseEntities() {
		log.debug("Access to all courses ");
		return new ResponseEntity<List<ParentEntity>>(parentDao.findAll(), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getParentEntityById(@PathVariable Long id) {
		ParentEntity parentEntity;
		try {
			parentEntity = parentDao.findById(id);
			log.debug("Access to parent with id = " + id);
			return new ResponseEntity<ParentEntity>(parentEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createParentEntity(@Valid @RequestBody ParentDto parentDto,
												BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		UserEntity userEntity = userDao.findByPin(parentDto.getPin());
		if (userEntity != null && !(parentDto.getFirstname().equalsIgnoreCase(userEntity.getFirstname())
				&& parentDto.getLastname().equals(userEntity.getLastname()))) {
			log.error("User with pin" + parentDto.getPin() + "already exist, first and last name does not mach: ");
			return new ResponseEntity<RESTError>(
					new RESTError(15, "User already exist, first and last name does not mach "), HttpStatus.CONFLICT);

		}

		ParentEntity parentEntity = new ParentEntity();
		parentEntity.setPin(parentDto.getPin());
		parentEntity.setFirstname(parentDto.getFirstname());
		parentEntity.setLastname(parentDto.getLastname());
		parentEntity.setParentSID(parentDto.getParentSID());
		parentEntity.setAddress(parentDto.getAddress());
		parentEntity.setEmail(parentDto.getEmail());
		parentEntity.setPhoneNumber(parentDto.getPhoneNumber());

		try {
			parentEntity = parentDao.save(parentEntity, userEntity);
			log.debug("Parent created:" + parentEntity.toString());
			return new ResponseEntity<ParentEntity>(parentEntity, HttpStatus.OK);

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
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> createParentEntity(@PathVariable Long id,
												@Valid @RequestBody ParentDto parentDto,
												BindingResult result)
			throws EntityNotFoundException {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		ParentEntity parentEntity;

		try {
			parentEntity = parentDao.findById(id);
			log.debug("Access to parent with id = " + id);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}
		if (!parentDto.getParentSID().equalsIgnoreCase(parentEntity.getParentSID()))
			return new ResponseEntity<RESTError>(new RESTError(12, "Cannot change ParentSID: "), HttpStatus.CONFLICT);
		
		if (!parentDto.getPin().equalsIgnoreCase(parentEntity.getPin()))
			return new ResponseEntity<RESTError>(new RESTError(14, "Cannot change pin: "), HttpStatus.CONFLICT);

		parentEntity.setFirstname(parentDto.getFirstname());
		parentEntity.setLastname(parentDto.getLastname());
		parentEntity.setAddress(parentDto.getAddress());
		parentEntity.setEmail(parentDto.getEmail());
		parentEntity.setPhoneNumber(parentDto.getPhoneNumber());

		try {
			parentEntity = parentDao.update(parentEntity);
			log.debug("Parent updated:" + parentEntity.toString());
			return new ResponseEntity<ParentEntity>(parentEntity, HttpStatus.OK);

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

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteParentEntityById(@PathVariable Long id) {

		try {
			ParentEntity parentEntity = parentDao.deleteById(id);
			log.debug("Parent deleted, id = " + id);
			return new ResponseEntity<ParentEntity>(parentEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{parentId}/addChild/{studentId}")
	public ResponseEntity<?> addStudentToParent(@PathVariable Long parentId,
												@PathVariable Long studentId) {
		try {
			ParentEntity parentEntity = parentDao.addStudentToParent(parentId, studentId);
			log.debug("Child id = " + studentId + " is added to parent id = " + parentId);
			return new ResponseEntity<ParentEntity>(parentEntity, HttpStatus.OK);
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
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{parentId}/removeChild/{studentId}")
	public ResponseEntity<?> removeStudentFromParent(@PathVariable Long parentId,
												@PathVariable Long studentId) {
		try {
			ParentEntity parentEntity = parentDao.removeStudentFromParent(parentId, studentId);
			log.debug("Child id = " + studentId + " is removed from parent id = " + parentId);
			return new ResponseEntity<ParentEntity>(parentEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

	}

}
