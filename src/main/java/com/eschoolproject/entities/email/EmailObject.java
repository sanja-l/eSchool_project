package com.eschoolproject.entities.email;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailObject {
	private String to;
	private String subject;
	private String text;
}
