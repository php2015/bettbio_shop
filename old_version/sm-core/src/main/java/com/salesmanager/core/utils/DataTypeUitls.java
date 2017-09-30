package com.salesmanager.core.utils;

public class DataTypeUitls {
	public static Long toLong(Object input){
		if (input == null){
			return null;
		}
		if (input instanceof Number){
			return ((Number) input).longValue();
		}
		if (input instanceof String){
			try{
				return Long.parseLong((String) input);
			}catch (Exception e){
				return null;
			}
		}
		return null;
	}
}
