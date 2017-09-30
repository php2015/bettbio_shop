package com.test.mode;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bettbio.core.model.PhoneCode;
import com.bettbio.core.service.PhoneCodeService;
import com.test.mode.base.BaseT;

public class PhoneCodeTest extends BaseT {

	@Autowired
	PhoneCodeService phoneCodeService;

	@Test
	public void test() {
		String phone = "13122223333";
		String code = "123123";

		PhoneCode phoneCode = new PhoneCode();
		phoneCode.setPhone(phone);
		phoneCode.setCode(code);
		
		DateTime createTime = new DateTime();
		DateTime invalidTime = createTime.plusMinutes(15);
		phoneCode.setCreateTime(createTime.toDate());
		phoneCode.setInvalidTime(invalidTime.toDate());

		phoneCodeService.save(phoneCode);
	}

	@Test
	public void search() {
		String phone = "13122223333";
		String code = "123123";
		PhoneCode phoneCode = new PhoneCode();
		phoneCode.setPhone(phone);
		phoneCode.setCode(code);

		phoneCode = phoneCodeService.invalidPhoneCode(phoneCode);

		Assert.assertNotNull(phoneCode);
		Assert.assertTrue(phoneCode.getIsInvalid() == 1);
	}
}
