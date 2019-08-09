package com.eschoolproject.entities.dto;

import java.util.Date;

import com.eschoolproject.enumerations.ESemester;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AssesmentReportDto {
	private Long id;
	private String studentName;
	private String className;
	private String courseName;
	private String teacherName;
	private Integer mark;
	private Date markDate;
	private Boolean isFinalMark;
	private ESemester semester;

}
