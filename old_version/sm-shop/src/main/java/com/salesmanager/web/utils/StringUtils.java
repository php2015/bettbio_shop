package com.salesmanager.web.utils;

import java.util.regex.Pattern;

public class StringUtils {

	public static boolean isNull(String s) {
		if (s==null || s.trim().equals("")) {
			return true;
		} else return false;
	}
	
	public static final Pattern ptnEmail = Pattern.compile("^[^@]+@[^@]+(\\.[^@]+)+$");
	public static boolean isEmail(String str){
		if (isNull(str)){
			return false;
		}
		return ptnEmail.matcher(str).matches();
		
	}
	
	public static final Pattern ptnMobile = Pattern.compile("^1[3-9]\\d{9}$");
	public static boolean isMobile(String str){
		if (isNull(str)){
			return false;
		}
		String mobile = str.replaceAll("[^\\d]", "");
		return ptnMobile.matcher(mobile).matches();
		
	}
}
