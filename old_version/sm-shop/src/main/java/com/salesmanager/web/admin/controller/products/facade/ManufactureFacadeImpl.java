package com.salesmanager.web.admin.controller.products.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.fabric.xmlrpc.base.Array;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufactureList;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.admin.entity.products.ReadableManufacture;
import com.salesmanager.web.admin.entity.products.ReadableManufactureList;
@Service("manufactureFacade")
public class ManufactureFacadeImpl implements ManufactureFacade{

	@Autowired
	ManufacturerService manufacturerService;
	
	@Override
	public ReadableManufactureList getByCriteria(Criteria criteria,
			Language language) throws Exception {
		// TODO Auto-generated method stub
		ManufactureList mList = manufacturerService.getByCriteria(criteria, language);
		if(mList != null && mList.getTotalCount()>0){
			ReadableManufactureList returnList = new ReadableManufactureList();
			returnList.setTotalCount(mList.getTotalCount());
			List<ReadableManufacture> ms = new ArrayList<ReadableManufacture>();
			for(Manufacturer m: mList.getManus()){
				ReadableManufacture manu = new ReadableManufacture();
				manu.setId(m.getId());
				if(m.getDescriptions().size()>0){
					ManufacturerDescription mdes = m.getDescriptions().iterator().next();
					manu.setName(mdes.getName());
					manu.setUrl(mdes.getUrl());
				}
				ms.add(manu);
			}
			returnList.setManus(ms);
			return returnList;
		}
		return null;
	}
	
	@Override
	public ReadableManufactureList getByManufacturer() throws Exception {
		// TODO Auto-generated method stub
		ManufactureList mList = manufacturerService.queryByManufacturer();
		if(mList != null && mList.getTotalCount()>0){
			ReadableManufactureList returnList = new ReadableManufactureList();
			returnList.setTotalCount(mList.getTotalCount());
			List<ReadableManufacture> ms = new ArrayList<ReadableManufacture>();
			for(Manufacturer m: mList.getManus()){
				ReadableManufacture manu = new ReadableManufacture();
				manu.setId(m.getId());
				if(m.getDescriptions().size()>0){
					ManufacturerDescription mdes = m.getDescriptions().iterator().next();
					manu.setName(mdes.getName());
					manu.setUrl(mdes.getUrl());
				}
				ms.add(manu);
			}
			returnList.setManus(ms);
			return returnList;
		}
		return null;
	}

}
