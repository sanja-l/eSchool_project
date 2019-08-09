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
public class UserDto {
	@NotNull(message = "PIN must be provided") 
	@Pattern (regexp = "^[0-9]{13}",  message="pin is not valid.")
	private String pin; // EX YU personal identification number i.e. JMBG
	
	@NotNull(message = "First name must be provided.")
	@Size(min = 2, max = 30, message = "First name must be between {min} and {max} characters long.")
	protected String firstname;
	
	@NotNull(message = "Last name must be provided.")
	@Size(min = 2, max = 50, message = "Last name must be between {min} and {max} characters long.")
	protected String lastname;

}
