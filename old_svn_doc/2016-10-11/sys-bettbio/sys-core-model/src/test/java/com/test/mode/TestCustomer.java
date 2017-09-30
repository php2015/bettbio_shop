package com.test.mode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.model.Country;
import com.bettbio.core.model.SStoreUser;
import com.bettbio.core.model.SUser;
import com.bettbio.core.service.ProductClassificationService;
import com.bettbio.core.service.SUserService;
import com.bettbio.core.service.StoreUserService;
import com.test.mode.base.BaseT;

public class TestCustomer  extends BaseT {

	@Autowired
	SUserService sUserService;
	
	@Autowired
	StoreUserService storeUserService;
	
	@Autowired
	ProductClassificationService productClassificationService;
	
	@Test
	public void selectAll(){
			/*SUser sUser = new SUser();
			List<SUser> sUserlist = new ArrayList<SUser>();
			sUserlist = sUserService.selectByPage(sUser, 1, 1);*/
			
			List<SStoreUser> sStoreUserlist = new ArrayList<SStoreUser>();
			SStoreUser sStoreUser = new SStoreUser();
			sStoreUserlist = storeUserService.selectByPage(sStoreUser, 1, 1);
	}
	
	@Test
	public void selectByPage(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page",1);
		map.put("row",10);
		Page<SStoreUser> page = storeUserService.selectByPage(map);
		
			
	}
}
