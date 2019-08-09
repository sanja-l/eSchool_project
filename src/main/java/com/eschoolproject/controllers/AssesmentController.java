package com.eschoolproject.controllers;

import java.security.Principal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
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
import com.eschoolproject.entities.AccountEntity;
import com.eschoolproject.entities.AssesmentEntity;
import com.eschoolproject.entities.dto.AssesmentDto;
import com.eschoolproject.entities.dto.AssesmentReportDto;
import com.eschoolproject.entities.email.EmailObject;
import com.eschoolproject.services.AccountDao;
import com.eschoolproject.services.AssesmentDao;
import com.eschoolproject.services.StudentDao;
import com.eschoolproject.services.email.EmailService;
import com.eschoolproject.util.Utility;
import com.eschoolproject.util.customexceptions.EntityMissmatchException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/eschool/assesments")
public class AssesmentController {

	@Value(value = "${dateFormat}")
	private String dateFormat;

	private final AssesmentDao assesmentDao;
	private final AccountDao accountDao;
	private final EmailService emailService;
	private final StudentDao studentDao;

	
	
	

	public AssesmentController(AssesmentDao assesmentDao, AccountDao accountDao, EmailService emailService,
			StudentDao studentDao) {
		super();
		this.assesmentDao = assesmentDao;
		this.accountDao = accountDao;
		this.emailService = emailService;
		this.studentDao = studentDao;
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllAssesmentEntities() {
		log.debug("Access to all engagement entities ");
		return new ResponseEntity<List<AssesmentEntity>>(assesmentDao.findAll(), HttpStatus.OK);

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getAssesmentEntityById(@PathVariable Long id) {
		AssesmentEntity assesmentEntity;
		try {
			assesmentEntity = assesmentDao.findById(id);
			log.debug("Access to engagement with id = " + id);
			return new ResponseEntity<AssesmentEntity>(assesmentEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, value = "/find-by-courseGrade/{courseGradeId}")
	public ResponseEntity<?> getAllAssesmentEntitiesByCourseGrade(@PathVariable Long courseGradeId) {
		List<AssesmentEntity> assesmentEntities;
		try {
			assesmentEntities = assesmentDao.findByCourseGradeId(courseGradeId);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}
		log.debug("Access to all classes by CourseGrade");
		return new ResponseEntity<List<AssesmentEntity>>(assesmentEntities, HttpStatus.OK);

	}

	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER" })
	@RequestMapping(method = RequestMethod.GET, value = "/reports/teacher/{teacherId}")
	public ResponseEntity<?> getAllAssesmentEntitiesByTeacher(Principal principal, @PathVariable Long teacherId) {
		if (!allowAccount(principal, teacherId)) {
			log.error("Teacher can only access to his/her own assesments.");
			return new ResponseEntity<RESTError>(new RESTError(3, "Teacher can only access to his/her own assesments."),
					HttpStatus.FORBIDDEN);
		}
		List<AssesmentEntity> assesmentEntities;
		try {
			assesmentEntities = assesmentDao.findByTeacherId(teacherId);
			log.debug("Access to all classes by Teacher");
			// return new ResponseEntity<List<AssesmentEntity>>(assesmentEntities,
			// HttpStatus.OK);
			return new ResponseEntity<List<AssesmentReportDto>>(aes2ards(assesmentEntities), HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

	}

	@Secured({ "ROLE_ADMIN", "ROLE_STUDENT" })
	@RequestMapping(method = RequestMethod.GET, value = "/reports/student/{studentId}")
	public ResponseEntity<?> getAllAssesmentEntitiesByStudent(Principal principal, @PathVariable Long studentId) {
		if (!allowAccount(principal, studentId)) {
			log.error("Student can only access to his/her own assesments.");
			return new ResponseEntity<RESTError>(new RESTError(4, "Student can only access to his/her own assesments."),
					HttpStatus.FORBIDDEN);
		}
		List<AssesmentEntity> assesmentEntities;
		try {
			assesmentEntities = assesmentDao.findByStudentId(studentId);
			log.debug("Access to all assesments by Student");
			// return new ResponseEntity<List<AssesmentEntity>>(assesmentEntities,
			// HttpStatus.OK);
			return new ResponseEntity<List<AssesmentReportDto>>(aes2ards(assesmentEntities), HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

	}

	@Secured({ "ROLE_ADMIN", "ROLE_PARENT" })
	@RequestMapping(method = RequestMethod.GET, value = "/reports/parent/{parentId}")
	public ResponseEntity<?> getAllAssesmentEntitiesByParent2(Principal principal, @PathVariable Long parentId) {
		if (!allowAccount(principal, parentId)) {
			log.error("Parent can only access to his/her own childern's assesments.");
			return new ResponseEntity<RESTError>(
					new RESTError(5, "Parent can only access to his/her own childern's assesments."),
					HttpStatus.FORBIDDEN);
		}

		List<AssesmentEntity> assesmentEntities;

		assesmentEntities = assesmentDao.findByParentId(parentId);
		log.debug("Access to all assesments by Parent" + parentId);
		// return new ResponseEntity<List<AssesmentEntity>>(assesmentEntities,
		// HttpStatus.OK);
		return new ResponseEntity<List<AssesmentReportDto>>(aes2ards(assesmentEntities), HttpStatus.OK);

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteAssesmentEntityById(Principal principal, @PathVariable Long id) {

		try {
			AssesmentEntity assesmentEntity = assesmentDao.deleteById(id);
			log.debug("Assesment deleted, id = " + id);
			return new ResponseEntity<AssesmentEntity>(assesmentEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

	}

	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER" })
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}/teacher/{teacherId}")
	public ResponseEntity<?> deleteAssesmentEntityByIdAndTeacherId(Principal principal, @PathVariable Long id,
			@PathVariable Long teacherId) {
		if (!allowAccount(principal, teacherId)) {
			log.error("Teacher can only access to his/her own assesments.");
			return new ResponseEntity<RESTError>(new RESTError(3, "Teacher can only access to his/her own assesments."),
					HttpStatus.FORBIDDEN);
		}

		try {
			AssesmentEntity assesmentEntity = assesmentDao.deleteByIdAndTeacherId(id, teacherId);
			log.debug("Assesment deleted, id = " + id);
			return new ResponseEntity<AssesmentEntity>(assesmentEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		} catch (EntityMissmatchException e) {
			log.error("EntityMissmatchException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(6, "Entity missmatch  occurred: " + e.getMessage()),
					HttpStatus.FORBIDDEN);
		}

	}

	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER" })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createAssesmentEntity(Principal principal, @Valid @RequestBody AssesmentDto assesmentDto,
			BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (!allowAccount(principal, assesmentDto.getTeacherId())) {
			log.error("Teacher can access to his/her own students.");
			return new ResponseEntity<RESTError>(new RESTError(3, "Teacher can only access to his/her own assesments."),
					HttpStatus.FORBIDDEN);
		}

		AssesmentEntity assesmentEntity = new AssesmentEntity();
		assesmentEntity.setMark(assesmentDto.getMark());
		assesmentEntity.setMarkDate(new Date());
		assesmentEntity.setDescription(assesmentDto.getDescription());
		if (assesmentDto.getIsFinalMark() == null)
			assesmentDto.setIsFinalMark(false);
		assesmentEntity.setIsFinalMark(assesmentDto.getIsFinalMark());
		assesmentEntity.setSemester(assesmentDto.getSemester());

		try {
			assesmentEntity = assesmentDao.save(assesmentEntity, assesmentDto.getCourseGradeId(),
					assesmentDto.getTeacherId(), assesmentDto.getStudentId());
			log.debug("Assesment created.");
			for (EmailObject emailObject : prepareEmail(assesmentEntity)) {
				emailService.sendSimpleMessage(emailObject);
				log.debug("Sent mail");
			}

			return new ResponseEntity<AssesmentEntity>(assesmentEntity, HttpStatus.OK);
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
					HttpStatus.BAD_REQUEST);
		} catch (EntityMissmatchException e) {
			log.error("EntityMissmatchException  occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(6, "Entity missmatch  occurred: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER" })
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateAssesmentEntityByAssesmentName(Principal principal, @PathVariable Long id,
			@Valid @RequestBody AssesmentDto assesmentDto, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (!allowAccount(principal, assesmentDto.getTeacherId())) {
			log.error("Teacher can access to his/her own students.");
			return new ResponseEntity<RESTError>(new RESTError(3, "Teacher can only access to his/her own assesments."),
					HttpStatus.FORBIDDEN);
		}
		AssesmentEntity assesmentEntity;

		try {
			assesmentEntity = assesmentDao.findById(id);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

		assesmentEntity.setMark(assesmentDto.getMark());
		assesmentEntity.setMarkDate(new Date());
		assesmentEntity.setDescription(assesmentDto.getDescription());
		assesmentEntity.setIsFinalMark(assesmentDto.getIsFinalMark());
		assesmentEntity.setSemester(assesmentDto.getSemester());

		try {
			assesmentEntity = assesmentDao.save(assesmentEntity, assesmentDto.getCourseGradeId(),
					assesmentDto.getTeacherId(), assesmentDto.getStudentId());
			log.debug("Assesment updated:" + assesmentEntity.toString());
			return new ResponseEntity<AssesmentEntity>(assesmentEntity, HttpStatus.OK);
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
					HttpStatus.BAD_REQUEST);
		} catch (EntityMissmatchException e) {
			log.error("EntityMissmatchException  occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(6, "Entity missmatch  occurred: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	private boolean allowAccount(Principal principal, Long Id) {
		AccountEntity accountEntity;
		try {
			accountEntity = accountDao.findFirstByAccountName(principal.getName());
			return accountEntity.getRole().getRoleName().equals("ROLE_ADMIN")
					|| accountEntity.getUser().getId().equals(Id);
		} catch (EntityNotFoundException e) {
			return false;
		}

	}

	private List<EmailObject> prepareEmail(AssesmentEntity assesmentEntity) {
		List<EmailObject> emailObjects = new ArrayList<EmailObject>();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String strDate = sdf.format(assesmentEntity.getMarkDate());

		String text = "Student: " + assesmentEntity.getStudent().getFirstname() + " "
				+ assesmentEntity.getStudent().getLastname() + "\n Mark: " + assesmentEntity.getMark() + "\n Course: "
				+ assesmentEntity.getCourseGrade().getCourse().getCourseName() + "\n Teacher: "
				+ assesmentEntity.getTeacher().getFirstname() + " " + assesmentEntity.getTeacher().getLastname()
				+ "\n Date: " + strDate;

		for (String emailTo : studentDao.findParentsEmailsByStudentId(assesmentEntity.getStudent().getId())) {
			EmailObject emailObject = new EmailObject();
			emailObject.setSubject("Assesment");
			emailObject.setText(text);
			emailObject.setTo(emailTo);
			emailObjects.add(emailObject);
		}
		return emailObjects;

	}


	private AssesmentReportDto ae2ard(AssesmentEntity ae) {
		AssesmentReportDto ard = new AssesmentReportDto();
		ard.setId(ae.getId());
		ard.setStudentName(ae.getStudent().getFirstname() + " " + ae.getStudent().getLastname());
		ard.setClassName(ae.getStudent().getSclass().getGrade().getGradeValue() + "-"
				+ ae.getStudent().getSclass().getClassName());
		ard.setCourseName(ae.getCourseGrade().getCourse().getCourseName());
    	ard.setTeacherName(ae.getTeacher().getFirstname()+" "+ae.getTeacher().getLastname());
		ard.setTeacherName("");
		ard.setMark(ae.getMark());
		ard.setIsFinalMark(ae.getIsFinalMark());
		ard.setMarkDate(ae.getMarkDate());
		ard.setSemester(ae.getSemester());
		return ard;
	}

	private List<AssesmentReportDto> aes2ards(List<AssesmentEntity> aes) {
		List<AssesmentReportDto> ards = new ArrayList<AssesmentReportDto>(aes.size());
		for (AssesmentEntity ae : aes) {
			ards.add(ae2ard(ae));
		}
		return ards;

	}

}
