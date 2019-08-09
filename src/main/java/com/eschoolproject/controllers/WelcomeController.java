package com.eschoolproject.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class WelcomeController {
	@RequestMapping(method = RequestMethod.GET)
	public String Welcome() {
		return "Welcome!";
	}
	@RequestMapping(method = RequestMethod.GET, value = "/eschool")
	public String WelcomeESchool() {
		return "Welcome to eSchool!";
	}
	@Secured("ROLE_GUEST")
	@RequestMapping(method = RequestMethod.GET, value = "/eschool/guests")
	public String WelcomeESchoolGuest() {
		return "Welcome Guest,  to eSchool!";
	}

}
