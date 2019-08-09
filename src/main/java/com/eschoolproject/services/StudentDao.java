package com.eschoolproject.services;

import java.util.List;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;

import com.eschoolproject.entities.CourseEntity;
import com.eschoolproject.entities.ParentEntity;
import com.eschoolproject.entities.StudentEntity;
import com.eschoolproject.entities.TeacherEntity;
import com.eschoolproject.entities.UserEntity;
import com.eschoolproject.util.customexceptions.EntityIsReferencedException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

public interface StudentDao {
	public List<StudentEntity> findAll();

	public StudentEntity findById(Long id) throws EntityNotFoundException;
	
	public StudentEntity save(StudentEntity studentEntity, UserEntity userEntity) throws DataIntegrityViolationException, EntityNotFoundException;

	public StudentEntity update(StudentEntity studentEntity) throws DataIntegrityViolationException, EntityNotFoundException;

	public StudentEntity deleteById(Long Id) throws EntityNotFoundException, EntityIsReferencedException;
	
	public Set<ParentEntity> findParentsByStudentId(Long Id) throws EntityNotFoundException;

	public List<CourseEntity> findCoursesByStudentId(Long studentId) throws EntityNotFoundException;
	
	public List<TeacherEntity> findTeachersByStudentId(Long studentId) throws EntityNotFoundException;

	public List<String> findParentsEmailsByStudentId(Long sid);

}
