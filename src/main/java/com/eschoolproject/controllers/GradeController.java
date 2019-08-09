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
import com.eschoolproject.entities.GradeEntity;
import com.eschoolproject.entities.dto.GradeDto;
import com.eschoolproject.services.GradeDao;
import com.eschoolproject.util.Utility;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/eschool/grades")
@Secured("ROLE_ADMIN")
public class GradeController {

	private final GradeDao gradeDao;

	public GradeController(GradeDao gradeDao) {
		this.gradeDao = gradeDao;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllGradeEntities() {
		log.debug("Access to all grades ");
		return new ResponseEntity<List<GradeEntity>>(gradeDao.findAll(), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/find-by-grade-value/{gradeValue}")
	public ResponseEntity<?> getAllGradeEntitiesByGradeValue(@PathVariable Integer gradeValue) {
		log.debug("Access to all grades by gradeValue");
		return new ResponseEntity<List<GradeEntity>>(gradeDao.findByGradeValue(gradeValue), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getGradeEntityById(@PathVariable Long id) {
		GradeEntity gradeEntity;
		try {
			gradeEntity = gradeDao.findById(id);
			log.debug("Access to grade with id = " + id);
			return new ResponseEntity<GradeEntity>(gradeEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteGradeEntityById(@PathVariable Long id) {
		try {
			GradeEntity gradeEntity = gradeDao.deleteById(id);
			log.debug("Grade deleted, id = " + id);
			return new ResponseEntity<GradeEntity>(gradeEntity, HttpStatus.OK);
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
	public ResponseEntity<?> createGradeEntity(	@Valid @RequestBody GradeDto gradeDto,
												BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		GradeEntity gradeEntity = new GradeEntity();
		gradeEntity.setGradeValue(gradeDto.getGradeValue());
		try {
			gradeEntity = gradeDao.save(gradeEntity);
			log.debug("Grade created: " + gradeEntity.toString());
			return new ResponseEntity<GradeEntity>(gradeEntity, HttpStatus.OK);
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

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateGradeEntityById(	@PathVariable Long id,
													@Valid @RequestBody GradeDto gradeDto,
													BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		GradeEntity gradeEntity;

		try {
			gradeEntity = gradeDao.findById(id);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

		gradeEntity.setGradeValue(gradeDto.getGradeValue());
		try {
			gradeEntity = gradeDao.save(gradeEntity);
			log.debug("Grade created: " + gradeEntity.toString());
			return new ResponseEntity<GradeEntity>(gradeEntity, HttpStatus.OK);
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
