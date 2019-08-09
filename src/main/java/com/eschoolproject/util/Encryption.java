package com.eschoolproject.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encryption {

	public static String getPassEncoded(String pass) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder.encode(pass);
	}

//	public static void main(String[] args) {
//		System.out.println(getPassEncoded("pass"));
////		 dobija se sledeca vrednost
//		// $2a$10$r2TGjHoYmx/Q/EJtktXgfu6wnAWDbnc7Tn5qgQ175fwd4ZdfAqjHO
//
//	}
	
	public static boolean isEqualToEncriptedPass(String rawPassword, String encriptedPassword) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder.matches(rawPassword, encriptedPassword);
		
	}

}
