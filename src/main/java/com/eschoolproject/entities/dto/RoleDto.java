package com.eschoolproject.entities.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class RoleDto {
	
	@NotNull(message = "Role must be provided.") 
	@Pattern (regexp = "^ROLE_{1}[A-Z]+$",  message="Role name is not valid.")
	private String roleName;

}
