package com.eschoolproject.entities.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeacherDto {
	@NotNull(message = "PIN must be provided") 
	@Size(min=13, max=13, message = "Pin must be between {min} characters long.")
	private String pin; // EX YU personal identification number i.e. JMBG
	
	@NotNull(message = "First name must be provided.")
	@Size(min = 2, max = 30, message = "First name must be between {min} and {max} characters long.")
	protected String firstname;
	
	@NotNull(message = "Last name must be provided.")
	@Size(min = 2, max = 50, message = "Last name must be between {min} and {max} characters long.")
	protected String lastname;
	
	@Size(min = 1)
	@NotNull(message = "TeacherSID must be provided.") //SID = School ID
	private String teacherSID;// Unique ID in the school, where teacher is employed.
	
	@NotNull(message = "Field must be provided.")
	private String eduField;
	
	@NotNull(message = "Vocation must be provided.")
	private String vocation;
}
