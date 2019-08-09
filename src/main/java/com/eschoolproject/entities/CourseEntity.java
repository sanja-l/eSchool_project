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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "COURSE")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class CourseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull(message = "Course id must be provided.")
	@Size(min = 3, max = 20, message = "Course id must be between {min} and {max} characters long.")
	@Column(name = "course_sid", unique = true, updatable = false)
	private String courseSID;

	@NotNull(message = "Course name must be provided.")
	@Size(min = 3, max = 50, message = "Course name must be between {min} and {max} characters long.")
	@Column(name = "course_name")
	private String courseName;

	@OneToMany(mappedBy = "course", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonBackReference
	private List<CourseGradeEntity> courseGradeEntities = new ArrayList<CourseGradeEntity>();

	@Override
	public String toString() {
		return "CourseEntity [id=" + id + ", courseSID=" + courseSID + ", courseName=" + courseName + "]";
	}

}
