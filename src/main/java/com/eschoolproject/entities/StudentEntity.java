package com.eschoolproject.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@ToString(exclude = { "assesmentEntities", "parents" })
@Entity
@Table(name = "U_STUDENT")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class StudentEntity extends UserEntity {
	@Size(min = 1)
	@Column(name = "student_sid", nullable = false, unique = true)
	@NotNull(message = "studentSID must be provided.") // SID = School ID
	private String studentSID; // Unique ID in the school, the student attends.

	private Date birthDate;

	private String birthCity;

	private String birthCountry;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "sclass")
	@JsonManagedReference
	private ClassEntity sclass;

	@OneToMany(mappedBy = "student", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonBackReference
	private List<AssesmentEntity> assesmentEntities = new ArrayList<AssesmentEntity>();

	@JsonBackReference
	@ManyToMany
	@JoinTable(name = "PARENT_STUDENT", joinColumns = { @JoinColumn(name = "student_id") }, inverseJoinColumns = {
			@JoinColumn(name = "parent_id") })
	private Set<ParentEntity> parents = new HashSet<>();

}