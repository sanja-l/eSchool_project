package com.eschoolproject.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.eschoolproject.enumerations.ESemester;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString()
@Entity
@Table(name = "ASSESMENT")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class AssesmentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull(message = "Mark must be provided.")
	@Digits(integer = 1, fraction = 0)
	@Min(value = 1, message = "Minimal value of the mark is 1")
	@Max(value = 5, message = "Maximal value of the mark is 5")
	private Integer mark;

	private String description;

	@NotNull(message = "Day of assesment mark must be provided.")
	private Date markDate;

	@Column(columnDefinition = "boolean default false")
	private Boolean isFinalMark;

	private ESemester semester;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher")
	@JsonManagedReference
	private TeacherEntity teacher;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "student")
	@JsonManagedReference
	private StudentEntity student;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "courseGrade	")
	@JsonManagedReference
	private CourseGradeEntity courseGrade;

}
