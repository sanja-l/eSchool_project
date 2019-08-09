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
import com.eschoolproject.entities.EngagementEntity;
import com.eschoolproject.entities.dto.EngagementDto;
import com.eschoolproject.services.EngagementDao;
import com.eschoolproject.util.Utility;
import com.eschoolproject.util.customexceptions.EntityMissmatchException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/eschool/engagements")
@Secured("ROLE_ADMIN")
public class EngagementController {

	private final EngagementDao engagementDao;

	public EngagementController(EngagementDao engagementDao) {
		this.engagementDao = engagementDao;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllEngagementEntities() {
		log.debug("Access to all engagement entities ");
		return new ResponseEntity<List<EngagementEntity>>(engagementDao.findAll(), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/find-by-teacher/{teacherId}")
	public ResponseEntity<?> getAllEngagementEntitiesByTeacher(@PathVariable Long teacherId) {
		List<EngagementEntity> engagementEntities;
		try {
			engagementEntities = engagementDao.findByTeacherId(teacherId);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}
		log.debug("Access to all classes by Teacher");
		return new ResponseEntity<List<EngagementEntity>>(engagementEntities, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/find-by-courseGrade/{courseGradeId}")
	public ResponseEntity<?> getAllEngagementEntitiesByCourseGrade(@PathVariable Long courseGradeId) {
		List<EngagementEntity> engagementEntities;
		try {
			engagementEntities = engagementDao.findByCourseGradeId(courseGradeId);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}
		log.debug("Access to all classes by CourseGrade");
		return new ResponseEntity<List<EngagementEntity>>(engagementEntities, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/find-by-class/{sclassId}")
	public ResponseEntity<?> getAllEngagementEntitiesByClass(@PathVariable Long sclassId) {
		List<EngagementEntity> engagementEntities;
		try {
			engagementEntities = engagementDao.findByClassId(sclassId);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}
		log.debug("Access to all engagement entities by Class");
		return new ResponseEntity<List<EngagementEntity>>(engagementEntities, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getEngagementEntityById(@PathVariable Long id) {
		EngagementEntity engagementEntity;
		try {
			engagementEntity = engagementDao.findById(id);
			log.debug("Access to engagement with id = " + id);
			return new ResponseEntity<EngagementEntity>(engagementEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteEngagementEntityById(@PathVariable Long id) {
		try {
			EngagementEntity engagementEntity = engagementDao.deleteById(id);
			log.debug("Engagement deleted, id = " + id);
			return new ResponseEntity<EngagementEntity>(engagementEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Engagement not found"), HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createEngagementEntity(@Valid @RequestBody EngagementDto engagementDto,
													BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		EngagementEntity engagementEntity = new EngagementEntity();
		engagementEntity.setEngEnd(engagementDto.getEngEnd());
		engagementEntity.setEngStart(engagementDto.getEngStart());
		try {
			engagementEntity = engagementDao.save(engagementEntity, engagementDto.getCourseGradeId(),
					engagementDto.getTeacherId(), engagementDto.getClassId());
			log.debug("Engagement created:" + engagementEntity.toString());
			return new ResponseEntity<EngagementEntity>(engagementEntity, HttpStatus.OK);
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
		} catch (EntityMissmatchException e) {
			log.error("EntityMissmatchException  occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(10, "Entity missmatch: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateEngagementEntityByEngagementName(@PathVariable Long id,
																	@Valid @RequestBody EngagementDto engagementDto,
																	BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		EngagementEntity engagementEntity;

		try {
			engagementEntity = engagementDao.findById(id);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

		engagementEntity.setEngEnd(engagementDto.getEngEnd());
		engagementEntity.setEngStart(engagementDto.getEngStart());
		try {
			engagementEntity = engagementDao.save(engagementEntity, engagementDto.getCourseGradeId(),
					engagementDto.getTeacherId(), engagementDto.getClassId());
			log.debug("Engagement updated:" + engagementEntity.toString());
			return new ResponseEntity<EngagementEntity>(engagementEntity, HttpStatus.OK);
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
		} catch (EntityMissmatchException e) {
			log.error("EntityMissmatchException  occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(10, "Entity missmatch: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

}
