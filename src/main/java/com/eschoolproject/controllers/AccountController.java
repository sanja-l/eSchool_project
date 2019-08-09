package com.eschoolproject.controllers;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eschoolproject.controllers.utils.RESTError;
import com.eschoolproject.entities.AccountEntity;
import com.eschoolproject.entities.dto.AccountDto;
import com.eschoolproject.services.AccountDao;
import com.eschoolproject.util.AccountCustomValidator;
import com.eschoolproject.util.Encryption;
import com.eschoolproject.util.Utility;
import com.eschoolproject.util.customexceptions.EntityMissmatchException;
import com.eschoolproject.util.customexceptions.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/eschool/accounts")
public class AccountController {

	private final AccountDao accountDao;
	@Autowired
	AccountCustomValidator accountValidator;

	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.addValidators(accountValidator);
	}

	public AccountController(AccountDao accountDao, AccountCustomValidator accountValidator) {
		this.accountDao = accountDao;
		this.accountValidator = accountValidator;
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public Iterable<AccountEntity> getAllAccounts() {
		log.info("Access to all accounts");
		return accountDao.findAll();
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getAccountEntityById(@PathVariable Long id) {
		AccountEntity accountEntity;
		try {
			accountEntity = accountDao.findById(id);
			log.debug("Access to account with id = " + id);
			return new ResponseEntity<AccountEntity>(accountEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occured: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, value = "/find-by-accountName/{accountName}")
	public ResponseEntity<?> getAccountEntityByName(@PathVariable String accountName) {
		AccountEntity accountEntity;
		try {
			accountEntity = accountDao.findFirstByAccountName(accountName);
			log.debug("Access to account with account name  = " + accountName);
			return new ResponseEntity<AccountEntity>(accountEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occured: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteAccountEntityById(@PathVariable Long id) {
		try {
			AccountEntity accountEntity = accountDao.deleteById(id);
			log.debug("Account deleted, id = " + id);
			return new ResponseEntity<AccountEntity>(accountEntity, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found: " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		} catch (EntityMissmatchException e) {
			log.error("EntityMissmatchException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(6, "Entity missmatch  occurred: " + e.getMessage()),
					HttpStatus.FORBIDDEN);
		}

	}

	@Secured({ "ROLE_ADMIN", "ROLE_TEACHER", "ROLE_PEARENT", "ROLE_STUDENT", "ROLE_GUEST" })
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<?> changePassword(Principal principal,
											@Valid @RequestBody AccountDto accountDto,
											BindingResult result) {

		if (result.hasErrors()) {
			log.info("Input not validated.");
			return new ResponseEntity<>(Utility.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		
		AccountEntity loggedinAccount;
		try {
			loggedinAccount =  accountDao.findFirstByAccountName(principal.getName());
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}
		
		if (!accountDto.getAccountName().equals(principal.getName()) && !loggedinAccount.getRole().getRoleName().equals("ROLE_ADMIN")) {
			log.error("Account name  and logged user name do not match.");
			return new ResponseEntity<RESTError>(
					new RESTError(2, "User can only change his/her own account passsword."), HttpStatus.FORBIDDEN);
		}


		AccountEntity accountEntity;
		try {
			accountEntity = accountDao.findFirstByAccountName(accountDto.getAccountName());
		} catch (EntityNotFoundException e) {
			log.error("EntityNotFoundException occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Entity not found " + e.getMessage()),
					HttpStatus.NOT_FOUND);
		}
		
		

		if (!Encryption.isEqualToEncriptedPass(accountDto.getCurrentPassword(), accountEntity.getPassword())) {
			log.error("Supplied current password do not match.");
			return new ResponseEntity<>(new RESTError(12, "Supplied current password and your password do not match"),
					HttpStatus.BAD_REQUEST);
		}
		accountEntity.setPassword(Encryption.getPassEncoded(accountDto.getNewPassword()));
		accountEntity = accountDao.update(accountEntity);
		log.debug("Password succesfuly changed for account " + accountEntity.getId());
		return new ResponseEntity<AccountEntity>(accountEntity, HttpStatus.OK);

	}

}
