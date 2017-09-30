package com.salesmanager.core.utils;

import java.util.regex.Pattern;

public class VerificationUtils {
	private VerificationUtils() {}
	
	public static final Pattern ptnMobileNumber = Pattern.compile("^1[3-9]\\d{9}$");
	public static boolean isMobileNumber(String input){
		if (input == null){
			return false;
		}
		return ptnMobileNumber.matcher(input.trim()).matches();
	}
	
	public static final Pattern ptnEmail = Pattern.compile("^[^@]+@[^@]+$");
	public static boolean isEmail(String input){
		if (input == null){
			return false;
		}
		return ptnEmail.matcher(input.trim()).matches();
	}
}
