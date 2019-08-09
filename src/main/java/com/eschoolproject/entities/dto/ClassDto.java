package com.eschoolproject.entities.dto;

import javax.persistence.Column;
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
public class ClassDto {
	@NotNull(message = " Class name  must be provided.")
	@Size(min = 1, max = 15, message = "Class name must be between {min} and {max} characters long.")
	@Column(name = "class_name", nullable = false)
	private String className;

	@NotNull(message = "gradeId must be provided.")
	@Digits(integer = 19, fraction = 0, message = "gradeId must be the whole number")
	@Min(value = 1, message = "Minimal value of gradeId is 1")
	private Long gradeId;

}
