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
import com.eschoolproject.entities.TeacherEntity;
import com.eschoolproject.entities.UserEntity;
import com.eschoolproject.entities.dto.TeacherDto;
import com.eschoolproject.services.TeacherDao;
import com.eschoolproject.services.UserDao;
import com.eschoolproject.util.Utility;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/eschool/teachers")
@Secured("ROLE_ADMIN")
public class TeacherController {

	private final TeacherDao teacherDao;
	private final UserDao userDao;

	public TeacherController(TeacherDao teacherDao, UserDao userDao) {
		this.teacherDao = teacherDao;
		this.userDao = userDao;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllCourseEntities() {
		log.debug("Access to all courses ");
		return new ResponseEntity<List<TeacherEntity>>(teacherDao.findAll(), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getTeacherEntityById(@PathVariable Long id) {
		TeacherEntity teacherEntity;
		try {
			teacherEntity = teacherDao.findById(id);
			log.debug("Access to teacher with id = " + id);
			return new ResponseEntity<TeacherEntity>(teacherEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createTeacherEntity(	@Valid @RequestBody TeacherDto teacherDto,
													BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		UserEntity userEntity = userDao.findByPin(teacherDto.getPin());
		if (userEntity != null && !(teacherDto.getFirstname().equalsIgnoreCase(userEntity.getFirstname())
				&& teacherDto.getLastname().equals(userEntity.getLastname()))) {
			log.error("User with pin" + teacherDto.getPin() + "already exist, first and last name does not mach: ");
			return new ResponseEntity<RESTError>(
					new RESTError(15, "User already exist, first and last name does not mach "), HttpStatus.CONFLICT);

		}

		TeacherEntity teacherEntity = new TeacherEntity();
		teacherEntity.setPin(teacherDto.getPin());
		teacherEntity.setFirstname(teacherDto.getFirstname());
		teacherEntity.setLastname(teacherDto.getLastname());
		teacherEntity.setTeacherSID(teacherDto.getTeacherSID());
		teacherEntity.setEduField(teacherDto.getEduField());
		teacherEntity.setVocation(teacherDto.getVocation());

		try {
			teacherEntity = teacherDao.save(teacherEntity, userEntity);
			log.debug("Teacher created:" + teacherEntity.toString());
			return new ResponseEntity<TeacherEntity>(teacherEntity, HttpStatus.OK);

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
	public ResponseEntity<?> createTeacherEntity(	@PathVariable Long id,
													@Valid @RequestBody TeacherDto teacherDto,
													BindingResult result)
			throws EntityNotFoundException {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		TeacherEntity teacherEntity;

		try {
			teacherEntity = teacherDao.findById(id);
			log.debug("Access to teacher with id = " + id);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}
		if (!teacherDto.getTeacherSID().equalsIgnoreCase(teacherEntity.getTeacherSID()))
			return new ResponseEntity<RESTError>(new RESTError(17, "Cannot change TeacherSID: "), HttpStatus.CONFLICT);
		
		if (!teacherDto.getPin().equalsIgnoreCase(teacherEntity.getPin()))
			return new ResponseEntity<RESTError>(new RESTError(14, "Cannot change pin: "), HttpStatus.CONFLICT);
	

		teacherEntity.setFirstname(teacherDto.getFirstname());
		teacherEntity.setLastname(teacherDto.getLastname());;
		teacherEntity.setEduField(teacherDto.getEduField());
		teacherEntity.setVocation(teacherDto.getVocation());

		try {
			teacherEntity = teacherDao.update(teacherEntity);
			log.debug("Teacher updated:" + teacherEntity.toString());
			return new ResponseEntity<TeacherEntity>(teacherEntity, HttpStatus.OK);

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
	public ResponseEntity<?> deleteTeacherEntityById(@PathVariable Long id) {

		try {
			TeacherEntity teacherEntity = teacherDao.deleteById(id);
			log.debug("Teacher deleted, id = " + id);
			return new ResponseEntity<TeacherEntity>(teacherEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		} catch (EntityIsReferencedException e) {
			log.error("EntityIsReferencedException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(9, "Entity is referenced: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

}
