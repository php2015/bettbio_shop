package com.salesmanager.web.admin.controller.products.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.salesmanager.core.business.catalog.product.model.attribute.OptionValueList;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductOptionValue;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductOptionValueService;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.admin.entity.products.ReadableOptionValue;
import com.salesmanager.web.admin.entity.products.ReadableOptionValueList;

@Service("optionValueFacade")
public class OptionValueFacadeImpl implements OptionValueFacade{
	@Autowired
	ProductOptionValueService productOptionValueService;

	@Override
	public ReadableOptionValueList getByCriteria(Criteria criteria,
			Language language) throws Exception {
		// TODO Auto-generated method stub
		OptionValueList oList = productOptionValueService.getByCriteria(criteria, language);
		if(oList != null && oList.getTotalCount()>0){
			ReadableOptionValueList returnList = new ReadableOptionValueList();
			returnList.setTotalCount(oList.getTotalCount());
			List<ReadableOptionValue> options = new ArrayList<ReadableOptionValue>();
			for(ProductOptionValue p :oList.getValues()){
				ReadableOptionValue option = new ReadableOptionValue();
				option.setId(p.getId());
				option.setImg(p.getImage());
				if(p.getDescriptions().size()>0)
					option.setName(p.getDescriptions().iterator().next().getName());
				options.add(option);
			}
			returnList.setValues(options);
			return returnList;
		}
		return null;
	}

}
