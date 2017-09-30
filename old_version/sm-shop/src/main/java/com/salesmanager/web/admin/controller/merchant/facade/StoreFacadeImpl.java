package com.salesmanager.web.admin.controller.merchant.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.model.StoreList;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.web.entity.shop.ReadableStore;
import com.salesmanager.web.entity.shop.Store;


@Service("storeFacade")
public class StoreFacadeImpl implements StoreFacade {

	//private static final Logger LOGGER = LoggerFactory
		//	.getLogger(StoreFacadeImpl.class);
	

	@Autowired
	MerchantStoreService merchantStoreService;


	@Override
	public ReadableStore getByCriteria(Criteria store) throws Exception {
		// TODO Auto-generated method stub
		ReadableStore  rs= new ReadableStore();
		List<Store> stores = new ArrayList<Store>();
		StoreList mstores = merchantStoreService.getByCriteria(store);
		if(mstores != null && mstores.getStores()!=null  && mstores.getTotalCount()>0){
			for(MerchantStore  m : mstores.getStores()){
				Store s = new Store();
				s.setId(m.getId());
				s.setCode(m.getCode());
				s.setStoreEmailAddress(m.getStoreEmailAddress());
				s.setStorename(m.getStorename());
				s.setSeeded(m.getSeeded());
				stores.add(s);
				
			}
			rs.setStores(stores);
		}
		rs.setTotal(mstores.getTotalCount());
		return rs;
	}
}
