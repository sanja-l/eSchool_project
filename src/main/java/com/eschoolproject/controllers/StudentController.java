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
import com.eschoolproject.entities.CourseEntity;
import com.eschoolproject.entities.StudentEntity;
import com.eschoolproject.entities.TeacherEntity;
import com.eschoolproject.entities.UserEntity;
import com.eschoolproject.entities.dto.StudentDto;
import com.eschoolproject.services.ClassDao;
import com.eschoolproject.services.StudentDao;
import com.eschoolproject.services.UserDao;
import com.eschoolproject.util.Utility;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/eschool/students")
@Secured("ROLE_ADMIN")
public class StudentController {

	private final StudentDao studentDao;
	private final UserDao userDao;
	private final ClassDao classDao;

	public StudentController(StudentDao studentDao, UserDao userDao, ClassDao classDao) {
		this.studentDao = studentDao;
		this.userDao = userDao;
		this.classDao = classDao;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllCourseEntities() {
		log.debug("Access to all Students ");
		return new ResponseEntity<List<StudentEntity>>(studentDao.findAll(), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getStudentEntityById(@PathVariable Long id) {
		StudentEntity studentEntity;
		try {
			studentEntity = studentDao.findById(id);
			log.debug("Access to student with id = " + id);
			return new ResponseEntity<StudentEntity>(studentEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createStudentEntity(	@Valid @RequestBody StudentDto studentDto,
													BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		UserEntity userEntity = userDao.findByPin(studentDto.getPin());
		if (userEntity != null && !(studentDto.getFirstname().equalsIgnoreCase(userEntity.getFirstname())
				&& studentDto.getLastname().equals(userEntity.getLastname()))) {
			log.error("User with pin" + studentDto.getPin() + "already exist, first and last name do not mach: ");
			return new ResponseEntity<RESTError>(
					new RESTError(15, "User already exist, first and last name does not mach "), HttpStatus.CONFLICT);

		}
		ClassEntity classEntity;
		try {
			classEntity = classDao.findById(studentDto.getSclassId());
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

		StudentEntity studentEntity = new StudentEntity();
		studentEntity.setPin(studentDto.getPin());
		studentEntity.setFirstname(studentDto.getFirstname());
		studentEntity.setLastname(studentDto.getLastname());
		studentEntity.setStudentSID(studentDto.getStudentSID());
		studentEntity.setBirthDate(studentDto.getBirthDate());
		studentEntity.setBirthCity(studentDto.getBirthCity());
		studentEntity.setBirthCountry(studentDto.getBirthCountry());
		studentEntity.setSclass(classEntity);

		try {
			studentEntity = studentDao.save(studentEntity, userEntity);
			log.debug("Student created:" + studentEntity.toString());
			return new ResponseEntity<StudentEntity>(studentEntity, HttpStatus.OK);

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
	public ResponseEntity<?> createStudentEntity(	@PathVariable Long id,
													@Valid @RequestBody StudentDto studentDto,
													BindingResult result)
			throws EntityNotFoundException {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		StudentEntity studentEntity;

		try {
			studentEntity = studentDao.findById(id);
			log.debug("Access to student with id = " + id);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}
		if (!studentDto.getStudentSID().equalsIgnoreCase(studentEntity.getStudentSID()))
			return new ResponseEntity<RESTError>(new RESTError(35, "Cannot change StudentSID: "), HttpStatus.CONFLICT);
		
		if (!studentDto.getPin().equalsIgnoreCase(studentEntity.getPin()))
			return new ResponseEntity<RESTError>(new RESTError(14, "Cannot change pin: "), HttpStatus.CONFLICT);

		ClassEntity classEntity;
		try {
			classEntity = classDao.findById(studentDto.getSclassId());
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}


		studentEntity.setFirstname(studentDto.getFirstname());
		studentEntity.setLastname(studentDto.getLastname());
		studentEntity.setBirthDate(studentDto.getBirthDate());
		studentEntity.setBirthCity(studentDto.getBirthCity());
		studentEntity.setBirthCountry(studentDto.getBirthCountry());
		studentEntity.setSclass(classEntity);

		try {
			studentEntity = studentDao.update(studentEntity);
			log.debug("Student updated:" + studentEntity.toString());
			return new ResponseEntity<StudentEntity>(studentEntity, HttpStatus.OK);

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
	public ResponseEntity<?> deleteStudentEntityById(@PathVariable Long id) {

		try {
			StudentEntity studentEntity = studentDao.deleteById(id);
			log.debug("Student deleted, id = " + id);
			return new ResponseEntity<StudentEntity>(studentEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		} catch (EntityIsReferencedException e) {
			log.error("EntityIsReferencedException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(10, "EntityIsReferencedException: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/courses")
	public ResponseEntity<?> getStudentCoursesById(@PathVariable Long id) {
		try {
			List<CourseEntity> courseEntities = studentDao.findCoursesByStudentId(id);
			log.debug("Access to coureses for student with id = " + id);
			return new ResponseEntity<List<CourseEntity>>(courseEntities, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/teachers")
	public ResponseEntity<?> getStudentTeachersById(@PathVariable Long id) {
		try {
			List<TeacherEntity> teacherEntities = studentDao.findTeachersByStudentId(id);
			log.debug("Access to teachers for student with id = " + id);
			return new ResponseEntity<List<TeacherEntity>>(teacherEntities, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

	}

}
