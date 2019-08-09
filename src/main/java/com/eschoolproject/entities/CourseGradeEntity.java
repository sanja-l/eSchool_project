package com.eschoolproject.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = { "engagementEntities", "assesmentEntities" })
@Entity
@Table(name = "COURSE_GRADE", uniqueConstraints = @UniqueConstraint(columnNames = { "course", "grade" }))
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class CourseGradeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotNull(message = "Weekly number of classes must be provided.")
	@Digits(integer = 2, fraction = 0)
	@Min(value = 0, message = "Weekly number of classes minimal value is 0")
	@Max(value = 25, message = "Weekly number of classes minimal value is 25")
	private Integer classesWeekly;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "course")
	@JsonManagedReference
	private CourseEntity course;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "grade")
	@JsonManagedReference
	private GradeEntity grade;

	@OneToMany(mappedBy = "courseGrade", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonBackReference
	private List<EngagementEntity> engagementEntities = new ArrayList<EngagementEntity>();

	@OneToMany(mappedBy = "courseGrade", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonBackReference
	private List<AssesmentEntity> assesmentEntities = new ArrayList<AssesmentEntity>();

}
