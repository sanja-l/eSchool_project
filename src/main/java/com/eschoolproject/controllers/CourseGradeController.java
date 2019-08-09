package com.eschoolproject.controllers;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.validation.Valid;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
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
import com.eschoolproject.entities.CourseGradeEntity;
import com.eschoolproject.entities.dto.CourseGradeDto;
import com.eschoolproject.services.CourseGradeDao;
import com.eschoolproject.util.Utility;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/eschool/course-grade")
@Secured("ROLE_ADMIN")
public class CourseGradeController {

	private final CourseGradeDao courseGradeDao;

	// @Autowired
	public CourseGradeController(CourseGradeDao courseGradeDao) {
		this.courseGradeDao = courseGradeDao;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllCourseGradeEntities() {
		log.debug("Access to all course-grade entities ");
		return new ResponseEntity<List<CourseGradeEntity>>(courseGradeDao.findAll(), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/find-by-gradeId/{gradeId}")
	public ResponseEntity<?> getAllCourseGradeEntitiesByGrade(@PathVariable Long gradeId) {
		List<CourseGradeEntity> courseGradeEntities;
		try {
			courseGradeEntities = courseGradeDao.findByGradeId(gradeId);
		} catch (EntityNotFoundException e) {
			log.error("Grade with id = " + gradeId + " does not exist: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "CourseGrade not found"), HttpStatus.NOT_FOUND);
		}
		log.debug("Access to all classes by Grade");
		return new ResponseEntity<List<CourseGradeEntity>>(courseGradeEntities, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/find-by-course/{courseId}")
	public ResponseEntity<?> getAllCourseGradeEntitiesByCourse(@PathVariable Long courseId) {
		List<CourseGradeEntity> courseGradeEntities;
		try {
			courseGradeEntities = courseGradeDao.findByCourseId(courseId);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}
		log.debug("Access to all courseGrade entities by Course");
		return new ResponseEntity<List<CourseGradeEntity>>(courseGradeEntities, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/find-by-course/{courseId}/grade/{gradeId}")
	public ResponseEntity<?> getAllCourseGradeEntitiesByCourseIdAndGradeId(	@PathVariable Long courseId,
																			@PathVariable Long gradeId) {
		List<CourseGradeEntity> courseGradeEntities;
		try {
			courseGradeEntities = courseGradeDao.findByCourseIdAndGradeId(courseId, gradeId);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}
		log.debug("Access to all courseGrade entities by Course");
		return new ResponseEntity<List<CourseGradeEntity>>(courseGradeEntities, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getCourseGradeEntityById(@PathVariable Long id) {
		CourseGradeEntity courseGradeEntity;
		try {
			courseGradeEntity = courseGradeDao.findById(id);
			log.debug("Access to class with id = " + id);
			return new ResponseEntity<CourseGradeEntity>(courseGradeEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteCourseGradeEntityById(@PathVariable Long id) {
		try {
			CourseGradeEntity courseGradeEntity = courseGradeDao.deleteById(id);
			log.debug("CourseGrade deleted, id = " + id);
			return new ResponseEntity<CourseGradeEntity>(courseGradeEntity, HttpStatus.OK);
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
	public ResponseEntity<?> createCourseGradeEntity(	@Valid @RequestBody CourseGradeDto courseGradeDto,
														BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		CourseGradeEntity courseGradeEntity = new CourseGradeEntity();
		courseGradeEntity.setClassesWeekly(courseGradeDto.getClassesWeekly());
		try {
			courseGradeEntity = courseGradeDao.save(courseGradeEntity, courseGradeDto.getCourseId(),
					courseGradeDto.getGradeId());
			log.debug("CourseGrade created:" + courseGradeEntity.toString());
			return new ResponseEntity<CourseGradeEntity>(courseGradeEntity, HttpStatus.OK);
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
	public ResponseEntity<?> updateCourseGradeEntityByCourseGradeName(	@PathVariable Long id,
																		@Valid @RequestBody CourseGradeDto courseGradeDto,
																		BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		CourseGradeEntity courseGradeEntity;

		try {
			courseGradeEntity = courseGradeDao.findById(id);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "CourseGrade entity not found "),
					HttpStatus.NOT_FOUND);
		}

		courseGradeEntity.setClassesWeekly(courseGradeDto.getClassesWeekly());

		try {
			courseGradeEntity = courseGradeDao.save(courseGradeEntity, courseGradeDto.getCourseId(),
					courseGradeDto.getGradeId());
			log.debug("CourseGrade updated: " + courseGradeEntity.toString());
			return new ResponseEntity<CourseGradeEntity>(courseGradeEntity, HttpStatus.OK);
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
