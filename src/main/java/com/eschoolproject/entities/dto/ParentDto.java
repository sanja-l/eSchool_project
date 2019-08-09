package com.eschoolproject.entities.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ParentDto {
	
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
	@NotNull(message = "parentSID must be provided.") // SID = School ID
	private String parentSID; // Unique ID in the school, the student attends.

	@NotNull(message = "Email must be provided.")
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email is not valid.")
	private String email;

	@NotNull(message = "Phone number must be provided.")
	private String phoneNumber;

	@NotNull(message = "Address must be provided.")
	private String address;

}
