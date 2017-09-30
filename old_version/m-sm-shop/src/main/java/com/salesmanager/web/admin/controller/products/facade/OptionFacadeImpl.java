package com.salesmanager.web.admin.controller.products.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.model.attribute.OptionList;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOption;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductOptionService;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.admin.entity.products.ReadableOption;
import com.salesmanager.web.admin.entity.products.ReadableOptionList;

@Service("productOptionFacade")
public class OptionFacadeImpl implements OptionFacade{

	@Autowired
	ProductOptionService productOptionService;
	
	@Override
	public ReadableOptionList getByCriteria(Criteria criteria,
			Language language) throws Exception {
		// TODO Auto-generated method stub
		OptionList oList = productOptionService.getByCriteria(criteria, language);
		if(oList != null && oList.getTotalCount()>0){
			ReadableOptionList readList = new ReadableOptionList();
			List<ReadableOption> op = new ArrayList<ReadableOption>();
			readList.setTotalCount(oList.getTotalCount());
			for(ProductOption p :oList.getOptions()){
				ReadableOption read = new ReadableOption();
				read.setId(p.getId());
				if(p.getDescriptions().size()>0)
				read.setName(p.getDescriptions().iterator().next().getName());
				read.setType(p.getProductOptionType());
				op.add(read);				
			}
			readList.setOptions(op);
			return readList;
		}
		return null;
	}

}
