package com.salesmanager.core.modules.sms;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.salesmanager.core.utils.WorkModeConfiguration;

@Service("textShortMessageService")
public class TextShortMessageServiceRongTongYunImpl implements TextShortMessageService {

	private static String SMS_URL_PDT = "app.cloopen.com";
	private static String SMS_PORT = "8883";
	private static String SMS_ACCOUNT = "8a48b55150b86ee80150c17acac21769";
	private static String SMS_ACCOUNT_TOKEN = "793aa20d7e2346da979f4fd77077e2bd";
	private static String SMS_APPID_PDT = "8a48b55151f5dfae0151f616b2790080";

	@Autowired
	private WorkModeConfiguration workMode;

	@Override
	public boolean sendNotifySms(String phone, String smsTemplate, String[] smsParams) {
		HashMap<String, Object> result = null;


		try {
			CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
			if (workMode.isProductEnv() || workMode.isSendSms()) {
				restAPI.init(SMS_URL_PDT, SMS_PORT);
				restAPI.setAccount(SMS_ACCOUNT, SMS_ACCOUNT_TOKEN);
				restAPI.setAppId(SMS_APPID_PDT);
				result = restAPI.sendTemplateSMS(phone, smsTemplate, smsParams);
			} else {
				result = new HashMap<String, Object>();
				result.put("statusCode", "000000");
				HashMap<String, Object> data = new HashMap<String, Object>();
				result.put("data", data);
				data.put("dateCreated", new Date().toString());
//				restAPI.init(SMS_URL_PDT, SMS_PORT);
//				restAPI.setAccount(SMS_ACCOUNT, SMS_ACCOUNT_TOKEN);
//				restAPI.setAppId(SMS_APPID_PDT);
//				result = restAPI.sendTemplateSMS(workMode.getMobileNumber(), smsTemplate, smsParams);
			}

			if ("000000".equals(result.get("statusCode"))) {
				return true;
			} else {
				System.out.println("\n\n[ERROR] Send SMS Failed: " + result + "\n\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}