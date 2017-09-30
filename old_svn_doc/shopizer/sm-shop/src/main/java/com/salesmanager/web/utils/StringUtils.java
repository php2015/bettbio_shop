package com.salesmanager.web.utils;

public class StringUtils {

	public static boolean isNull(String s) {
		if (s==null || s.trim().equals("")) {
			return true;
		} else return false;
	}
}
