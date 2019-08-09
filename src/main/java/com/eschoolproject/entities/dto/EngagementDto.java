package com.eschoolproject.entities.dto;
import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EngagementDto {
	private Date engStart;
	private Date engEnd;

	@NotNull(message = "teacherId must be provided.")
	@Digits(integer = 19, fraction = 0, message = "teacherId must be the whole number")
	@Min(value = 1, message = "Minimal value of teachereId is 1")
	private Long teacherId;
	
	@NotNull(message = "classId must be provided.")
	@Digits(integer = 19, fraction = 0, message = "classtId must be the whole number")
	@Min(value = 1, message = "Minimal value of classId is 1")
	private Long classId;
	
	@NotNull(message = "courseGradeId must be provided.")
	@Digits(integer = 19, fraction = 0, message = "courseGradeId must be the whole number")
	@Min(value = 1, message = "Minimal value of courseGradeId is 1")
	private Long courseGradeId;


}
