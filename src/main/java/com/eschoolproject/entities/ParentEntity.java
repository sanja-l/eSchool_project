package com.eschoolproject.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = { "children" })
@Entity
@Table(name = "U_PARENT")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ParentEntity extends UserEntity {
	@Size(min = 1)
	@Column(name = "parent_sid", nullable = false, unique = true)
	@NotNull(message = "parentSID must be provided.") // SID = School ID
	private String parentSID; // Unique ID in the school, the student attends.

	@NotNull(message = "Email must be provided.")
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email is not valid.")
	private String email;

	@NotNull(message = "Phone number must be provided.")
	private String phoneNumber;

	@NotNull(message = "Address must be provided.")
	private String address;

	@JsonIgnore
	@JsonManagedReference
	@ManyToMany
	@JoinTable(name = "PARENT_STUDENT", joinColumns = { @JoinColumn(name = "parent_id") }, inverseJoinColumns = {
			@JoinColumn(name = "student_id") })
	private Set<StudentEntity> children = new HashSet<>();

}