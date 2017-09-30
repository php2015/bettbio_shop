package com.test.mode;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bettbio.core.service.SUserService;
import com.bettbio.core.service.manager.CustomerService;
import com.test.mode.base.BaseT;

public class CustomerManagerTest extends BaseT {

	@Autowired
	CustomerService customerService;
	
	@Autowired
	SUserService sUserService;
	
	@Test
	public void selectUserEmails(){

		sUserService.selectUserEmails(new Integer[]{1,2,3,4});
	}
	@Test
	public void resetPwd(){
		
		customerService.resetPwd(new Integer[]{1});
	}
}
