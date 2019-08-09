package com.eschoolproject.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString(exclude = { "courseGradeEntities", "classEntities" })
@Table(name = "GRADE")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class GradeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull(message = "Grade must be provided.")
	@Digits(integer = 2, fraction = 0)
	@Min(value = 1, message = "Minimal value of grade is 1")
	@Column(name = "grade_value", unique = true)
	private Integer gradeValue;

	@OneToMany(mappedBy = "grade", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonBackReference
	private List<CourseGradeEntity> courseGradeEntities = new ArrayList<CourseGradeEntity>();

	@OneToMany(mappedBy = "grade", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonBackReference
	private List<ClassEntity> classEntities = new ArrayList<ClassEntity>();

}
