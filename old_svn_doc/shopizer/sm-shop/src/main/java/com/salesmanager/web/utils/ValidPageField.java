package com.salesmanager.web.utils;

import java.util.Locale;

import org.springframework.validation.ObjectError;



public class ValidPageField {
	

	public static ObjectError validphone(String phone,Locale locale,LabelUtils messages){
		String isPhone = "1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}"; 
		if(!phone.matches(isPhone)){
			ObjectError error = new ObjectError("phone",messages.getMessage("messages.invalid.fax", locale));
			return error;
		}
		return null;
	}
	
	public static ObjectError validfax(String phone,Locale locale,LabelUtils messages){
		String isPhone = "1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}"; 
		if(!phone.matches(isPhone)){
			ObjectError error = new ObjectError("phone",messages.getMessage("messages.invalid.fax1", locale));
			return error;
		}
		return null;
	}
	
	public static ObjectError validmobile(String mobile,Locale locale,LabelUtils messages){
		String isMob="^1[34578]\\d{9}$";
		if(!mobile.matches(isMob)){
			ObjectError error = new ObjectError("fax",messages.getMessage("messages.invalid.phone", locale));
			return error;
		}
		return null;
	}
	
	public static ObjectError validEmail(String email,Locale locale,LabelUtils messages){
		String emailReg = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		if(!email.matches(emailReg)){
			ObjectError error = new ObjectError("email",messages.getMessage("messages.invalid.email", locale));
			return error;
		}
		return null;
	}

}
