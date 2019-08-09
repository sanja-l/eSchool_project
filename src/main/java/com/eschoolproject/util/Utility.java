package com.eschoolproject.util;


import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;


public class Utility {
	public static ConstraintViolationException getCVexeption(Throwable t) {
		while ((t != null) && !(t instanceof ConstraintViolationException)) {
			t = t.getCause();
		}
		if (t instanceof ConstraintViolationException) {
			return (ConstraintViolationException) t;
		}
		return null;
	}

	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}

	}
	public static String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}
	



}
