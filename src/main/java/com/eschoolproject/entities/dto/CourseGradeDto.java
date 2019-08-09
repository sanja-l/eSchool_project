package com.eschoolproject.entities.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CourseGradeDto {
	
	@NotNull(message = "Weekly number of classes must be provided.") 
	@Digits(integer=2,fraction=0)
	@Min(value = 0, message ="Weekly number of classes minimal value is 0")
	@Max(value = 25, message ="Weekly number of classes minimal value is 25")
	private Integer classesWeekly;
	
	@NotNull(message = "courseId must be provided.")
	@Digits(integer = 19, fraction = 0, message = "courseId must be the whole number")
	@Min(value = 1, message = "Minimal value of courseId is 1")
	private Long courseId;
	
	@NotNull(message = "gradeId must be provided.")
	@Digits(integer = 19, fraction = 0, message = "gradeId must be the whole number")
	@Min(value = 1, message = "Minimal value of gradeId is 1")
	private Long gradeId;


}
