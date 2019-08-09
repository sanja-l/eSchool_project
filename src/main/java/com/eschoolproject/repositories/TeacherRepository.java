package com.eschoolproject.repositories;


import org.springframework.data.repository.CrudRepository;

import com.eschoolproject.entities.TeacherEntity;

public interface TeacherRepository extends CrudRepository<TeacherEntity, Long> {
	
	//List<TeacherRepository> findByLastnameAndFirstNameIgnoreCase(String lastname, String firstname);

}
