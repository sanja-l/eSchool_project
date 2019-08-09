package com.eschoolproject.util;



import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.eschoolproject.entities.dto.AccountDto;



// ovo  je validator na nivou klase
@Component // registracija komponente za koriscenje dependency injection
public class AccountCustomValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return AccountDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		AccountDto accountDto = (AccountDto) target;
		if (!accountDto.getNewPassword().equals(accountDto.getConfirmNewPassword())) {
			errors.reject("400", "Passwords must be the same");
		}
	}

}
