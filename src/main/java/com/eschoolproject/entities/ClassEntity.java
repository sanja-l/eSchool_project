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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
@ToString(exclude = { "studentEntities", "engagementEntities" })
@Entity
@Table(name = "CLASS", uniqueConstraints = @UniqueConstraint(columnNames = { "class_name", "grade" }))
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ClassEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotNull(message = "Class name must be provided.")
	@Size(min = 1, max = 15, message = "Class name must be between {min} and {max} characters long.")
	@Column(name = "class_name", nullable = false)
	private String className;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "grade")
	@JsonManagedReference
	private GradeEntity grade;

	@OneToMany(mappedBy = "sclass", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonBackReference
	private List<StudentEntity> studentEntities = new ArrayList<StudentEntity>();

	@OneToMany(mappedBy = "sclass", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonBackReference
	private List<EngagementEntity> engagementEntities = new ArrayList<EngagementEntity>();

}
