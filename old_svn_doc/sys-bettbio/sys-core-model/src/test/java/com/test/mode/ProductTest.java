package com.test.mode;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.model.Country;
import com.bettbio.core.mongo.model.ProductBaseModel;
import com.bettbio.core.mongo.model.ReagentProduct;
import com.bettbio.core.mongo.service.ProductService;
import com.test.mode.base.BaseT;

public class ProductTest extends BaseT {
	@Autowired
	ProductService reagentProductService;

	@Autowired
	ProductService productService;

	String collectionName = "product";
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Test
	public void test(){
		Query query = new Query();
		query.skip(10);
		query.limit(10);
		List<ReagentProduct> list =  mongoTemplate.find(query, ReagentProduct.class, collectionName);
		System.out.println(list.size());
	}
	
	@Test
	public void list(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page",1);
		map.put("row",10);
		map.put("findName", "人脑");
		map.put("startDate", "2016/05/01");
		//map.put("endDate", "2016/08/31");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Page<ProductBaseModel> pageInfo;
		try {
			pageInfo = productService.selectProducs(map);
			System.out.println(pageInfo);
			for (ProductBaseModel productBaseModel : pageInfo.getList()) {
				System.out.println(productBaseModel.getProductNameCh()+"+++++++++++++++"+productBaseModel.getCreateDate());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
