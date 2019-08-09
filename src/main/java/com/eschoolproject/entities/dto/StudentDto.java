package com.eschoolproject.entities.dto;

import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentDto {
	@NotNull(message = "PIN must be provided")
	@Size(min = 13, max = 13, message = "Pin must be between {min} characters long.")
	private String pin; // EX YU personal identification number i.e. JMBG

	@NotNull(message = "First name must be provided.")
	@Size(min = 2, max = 30, message = "First name must be between {min} and {max} characters long.")
	protected String firstname;

	@NotNull(message = "Last name must be provided.")
	@Size(min = 2, max = 50, message = "Last name must be between {min} and {max} characters long.")
	protected String lastname;

	@Size(min = 1)
	@NotNull(message = "studentSID must be provided.") // SID = School ID
	private String studentSID; // Unique ID in the school, the student attends.

	private Date birthDate;

	private String birthCity;

	private String birthCountry;

	@NotNull(message = " sclassId must be provided.")
	@Digits(integer = 19, fraction = 0, message = "sclassId must be the whole number")
	@Min(value = 1, message = "Minimal value of sclassId is 1")
	private Long sclassId;
}
