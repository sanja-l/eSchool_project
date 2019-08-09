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
import com.eschoolproject.entities.CourseEntity;
import com.eschoolproject.entities.dto.CourseDto;
import com.eschoolproject.services.CourseDao;
import com.eschoolproject.util.Utility;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/eschool/courses")
@Secured("ROLE_ADMIN")
public class CourseController {

	private final CourseDao courseDao;

	public CourseController(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllCourseEntities() {
		log.debug("Access to all courses ");
		return new ResponseEntity<List<CourseEntity>>(courseDao.findAll(), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/find-by-course-name/{courseName}")
	public ResponseEntity<?> getAllCourseEntitiesByCourseName(@PathVariable String courseName) {
		log.debug("Access to all courses by courseName");
		return new ResponseEntity<List<CourseEntity>>(courseDao.findByCourseName(courseName), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/find-contain-course-name/{courseName}")
	public ResponseEntity<?> getAllCourseEntitiesByCourseNameConatining(@PathVariable String courseName) {
		log.debug("Access to all courses which contain courseName");
		return new ResponseEntity<List<CourseEntity>>(courseDao.findByCourseNameContaining(courseName), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/find-by-course-sid/{courseSID}")
	public ResponseEntity<?> getAllCourseEntitiesByCourseSID(@PathVariable String courseSID) {
		log.debug("Access to all courses by courseSID");
		return new ResponseEntity<List<CourseEntity>>(courseDao.findByCourseSID(courseSID), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getCourseEntityById(@PathVariable Long id) {
		CourseEntity courseEntity;
		try {
			courseEntity = courseDao.findById(id);
			log.debug("Access to course with id = " + id);
			return new ResponseEntity<CourseEntity>(courseEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createCourseEntity(@Valid @RequestBody CourseDto courseDto,
												BindingResult result) {

		if (result.hasErrors()) {
			log.info("Input not validated.");
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		CourseEntity courseEntity = new CourseEntity();
		courseEntity.setCourseSID(courseDto.getCourseSID().trim());
		courseEntity.setCourseName(courseDto.getCourseName().trim());
		try {
			courseEntity = courseDao.save(courseEntity);
			log.debug("Course created:" + courseEntity.toString());
			return new ResponseEntity<CourseEntity>(courseEntity, HttpStatus.OK);

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
	public ResponseEntity<?> createCourseEntity(@PathVariable Long id,
												@Valid @RequestBody CourseDto courseDto,
												BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		CourseEntity courseEntity;

		try {
			courseEntity = courseDao.findById(id);
			log.debug("Access to course with id = " + id);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

		courseEntity.setCourseSID(courseDto.getCourseSID().trim());
		courseEntity.setCourseName(courseDto.getCourseName().trim());
		try {
			courseEntity = courseDao.save(courseEntity);
			log.debug("Course created:" + courseEntity.toString());
			return new ResponseEntity<CourseEntity>(courseEntity, HttpStatus.OK);

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
	public ResponseEntity<?> deleteCourseEntityById(@PathVariable Long id) {
		try {
			CourseEntity courseEntity = courseDao.deleteById(id);
			log.debug("Course deleted, id = " + id);
			return new ResponseEntity<CourseEntity>(courseEntity, HttpStatus.OK);
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
