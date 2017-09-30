package com.salesmanager.web.admin.controller.products.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationsipList;
import com.salesmanager.core.business.catalog.product.service.relationship.ProductRelationshipService;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.web.admin.entity.products.ReadableRalationshipList;
import com.salesmanager.web.admin.entity.products.ReadableRelationship;

@Service("relationshipFacade")
public class RelationshipFacadeImpl implements RelationshipFacade{
	@Autowired
	ProductRelationshipService productRelationshipService;

	@Override
	public ReadableRalationshipList getByCriteria(Criteria criteria) throws Exception {
		// TODO Auto-generated method stub
		ProductRelationsipList relist = productRelationshipService.getByCriteria(criteria);
		if(relist != null && relist.getTotalCount()>0){
			ReadableRalationshipList returnList = new ReadableRalationshipList();
			returnList.setTotalCount(relist.getTotalCount());
			List<ReadableRelationship> ships = new ArrayList<ReadableRelationship>();
			for(ProductRelationship p:relist.getShips()){
				ReadableRelationship r = new ReadableRelationship();
				r.setId(p.getId());
				r.setName(p.getCode());
				r.setActive(p.isActive());
				ships.add(r);
			}
			returnList.setShips(ships);
			return returnList;
		}
		return null;
	}

}
