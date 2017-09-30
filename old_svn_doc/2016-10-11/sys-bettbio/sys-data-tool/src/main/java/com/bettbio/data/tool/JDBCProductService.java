package com.bettbio.data.tool;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowCountCallbackHandler;
import org.springframework.stereotype.Service;

import com.bettbio.core.mongo.model.Product;

@Service
public class JDBCProductService {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	MongoTemplate mongoTemplate;

	String collectionName = "product";
	
	public void searchProduct(){
		
		//1.查询产品基础数据
		String sql = "SELECT p.PRODUCT_ID id,"
							+ "pd.NAME productNameCh,"
							+ "pd.EN_NAME productNameEn,"
							+ "pd.META_DESCRIPTION detailedDescription"
							+ "FROM PRODUCT p"+
						"LEFT JOIN PRODUCT_DESCRIPTION pd ON pd.PRODUCT_ID = p.PRODUCT_ID"+
						"WHERE p.DATE_CREATED < '2016-10-01'"+
						"ORDER BY p.DATE_CREATED DESC"+
						"LIMIT 1000";
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		
		//2.产品相关关联数据
		
	}
}
