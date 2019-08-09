package com.eschoolproject.entities.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class GradeDto {
	@NotNull(message = "Grade must be provided.")
	@Digits(integer = 2, fraction = 0)
	@Min(value = 1, message = "Minimal value of grade is 1")
	private Integer gradeValue;

}
