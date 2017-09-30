package com.test.mode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.bettbio.core.mongo.model.Product;
import com.bettbio.core.mongo.service.ProductService;
import com.test.mode.base.BaseT;

public class ProductPriceTest extends BaseT {

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ProductService productService;

	@Test
	public void price() {

		String idstr = "85113,85151,85156,85101,85055,85202,85106,85005,85050,85240,85207,85245,1527080,1527092,1527085,1528088,1528108,1528090,1528076,1528103";
		String[] idArr = idstr.split(",");
		List<Integer> ids = new ArrayList<Integer>();
		for (String id : idArr) {
			ids.add(Integer.parseInt(id));
		}

		List<Product> products = productService.getProductByIds(ids);
		Map<Integer, Product> maps = productService.getPrdouctPrice(ids);
		
		for (Product product : products) {
			System.out.println("List( pid = " + product.getId() + " prices = " + product.getProductPrices() + " )");
			System.out.println("Map( pid = " + product.getId() + " prices = " + maps.get(product.getId()).getProductPrices() + " )");
		}
		
	}
}
