package com.eschoolproject.entities.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CourseDto {
	@NotNull(message = "Course sid must be provided.")
	@Size(min = 3, max = 20, message = "Course id must be between {min} and {max} characters long.")
	private String courseSID;

	@NotNull(message = "Course name must be provided.")
	@Size(min = 3, max = 50, message = "Course name must be between {min} and {max} characters long.")
	private String courseName;
	
}
