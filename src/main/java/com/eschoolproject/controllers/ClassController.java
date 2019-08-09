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
import com.eschoolproject.entities.ClassEntity;
import com.eschoolproject.entities.dto.ClassDto;
import com.eschoolproject.services.ClassDao;
import com.eschoolproject.util.Utility;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/eschool/classes")
@Secured("ROLE_ADMIN")
public class ClassController {

	private final ClassDao classDao;

	public ClassController(ClassDao classDao) {
		this.classDao = classDao;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllClassEntities() {
		log.debug("Access to all classes ");
		return new ResponseEntity<List<ClassEntity>>(classDao.findAll(), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/find-by-class-name/{className}")
	public ResponseEntity<?> getAllClassEntitiesByClassName(@PathVariable String className) {
		log.debug("Access to all classes by className");
		return new ResponseEntity<List<ClassEntity>>(classDao.findByClassName(className), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/find-by-gradeId/{gradeId}")
	public ResponseEntity<?> getAllClassEntitiesByGradeId(@PathVariable Long gradeId) {
		List<ClassEntity> classEntities;
		try {
			classEntities = classDao.findByGradeId(gradeId);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}
		log.debug("Access to all classes by Grade");
		return new ResponseEntity<List<ClassEntity>>(classEntities, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/find-by-gradeVal/{gradeValue}")
	public ResponseEntity<?> getAllClassEntitiesByGradeVal(@PathVariable Integer gradeValue) {
		List<ClassEntity> classEntities = classDao.findByGradeValue(gradeValue);
		log.debug("Access to all classes by GradeValue");
		return new ResponseEntity<List<ClassEntity>>(classEntities, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/find-by-className/{className}/gradeVal/{gradeValue}")
	public ResponseEntity<?> getAllClassEntitiesByClassNameGradeVal(@PathVariable String className,
																	@PathVariable Integer gradeValue) {
		List<ClassEntity> classEntities = classDao.findByClassNameAndGradeValue(className, gradeValue);
		log.debug("Access to all classes by GradeValue");
		;
		return new ResponseEntity<List<ClassEntity>>(classEntities, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getClassEntityById(@PathVariable Long id) {
		ClassEntity classEntity;
		try {
			classEntity = classDao.findById(id);
			log.debug("Access to class entity with id = " + id);
			return new ResponseEntity<ClassEntity>(classEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteClassEntityById(@PathVariable Long id) {
		try {
			ClassEntity classEntity = classDao.deleteById(id);
			log.debug("Class deleted, id = " + id);
			return new ResponseEntity<ClassEntity>(classEntity, HttpStatus.OK);
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

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createClassEntity(	@Valid @RequestBody ClassDto classDto,
												BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		ClassEntity classEntity = new ClassEntity();
		classEntity.setClassName(classDto.getClassName().trim());
		try {
			classEntity = classDao.save(classEntity, classDto.getGradeId());
			log.debug("Class created:" + classEntity.toString());
			return new ResponseEntity<ClassEntity>(classEntity, HttpStatus.OK);
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
	public ResponseEntity<?> updateClassEntityByClassName(	@PathVariable Long id,
															@Valid @RequestBody ClassDto classDto,
															BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		ClassEntity classEntity;

		try {
			classEntity = classDao.findById(id);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

		classEntity.setClassName(classDto.getClassName().trim());

		try {
			classEntity = classDao.save(classEntity, classDto.getGradeId());
			log.debug("Class updated: " + classEntity.toString());
			return new ResponseEntity<ClassEntity>(classEntity, HttpStatus.OK);
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

}
