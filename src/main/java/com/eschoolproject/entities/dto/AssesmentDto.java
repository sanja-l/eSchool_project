package com.eschoolproject.entities.dto;


import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.eschoolproject.enumerations.ESemester;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AssesmentDto {
	
	@NotNull(message = "Mark must be provided.") 
	@Digits(integer=1,fraction=0)
	@Min(value = 1, message ="Minimal value of the mark is 1")
	@Max(value = 5, message ="Maximal value of the mark is 5")
	private Integer mark;
	
	private String description;
	
	private Boolean isFinalMark;
	
	private ESemester semester;

	@NotNull(message = "teacherId must be provided.")
	@Digits(integer = 19, fraction = 0, message = "teacherId must be the whole number")
	@Min(value = 1, message = "Minimal value of teachereId is 1")
	private Long teacherId;
	
	@NotNull(message = "studentId must be provided.")
	@Digits(integer = 19, fraction = 0, message = "studentId must be the whole number")
	@Min(value = 1, message = "Minimal value of studentId is 1")
	private Long studentId;
	
	@NotNull(message = "courseGradeId must be provided.")
	@Digits(integer = 19, fraction = 0, message = "courseGradeId must be the whole number")
	@Min(value = 1, message = "Minimal value of courseGradeId is 1")
	private Long courseGradeId;


}
