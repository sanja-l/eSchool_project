package com.eschoolproject.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = { "assesmentEntities", "engagementEntities" })
@Entity
@Table(name = "U_TEACHER")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class TeacherEntity extends UserEntity {
	@Size(min = 1)
	@Column(name = "teacher_sid", nullable = false, unique = true, updatable = false)
	@NotNull(message = "TeacherSID must be provided.") // SID = School ID
	private String teacherSID;// Unique ID in the school, where teacher is employed.

	@NotNull(message = "Field must be provided.")
	private String eduField;

	@NotNull(message = "Vocation must be provided.")
	private String vocation;

	@OneToMany(mappedBy = "teacher", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonBackReference
	private List<EngagementEntity> engagementEntities = new ArrayList<EngagementEntity>();

	@OneToMany(mappedBy = "teacher", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonBackReference
	private List<AssesmentEntity> assesmentEntities = new ArrayList<AssesmentEntity>();

}