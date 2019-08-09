package com.eschoolproject.entities.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountDto {
	
	@NotNull(message = "Account name must be provided.")
	@Size(min = 2, max = 20, message = "Account name must be between {min} and {max} characters long.")
	private String accountName;
	
	@NotNull(message = "Current password must be provided.")
	private String currentPassword;
	
	@NotNull(message = "New password must be provided.")
	@Size(min=6, max=20, message = "New password must be between {min} and {max} characters long.") 
	private String newPassword;
	
	@NotNull(message = "Confirm password must be provided.")
	@Size(min=6, max=20, message = "Confirm password must be between {min} and {max} characters long.") 
	private String confirmNewPassword;

}
