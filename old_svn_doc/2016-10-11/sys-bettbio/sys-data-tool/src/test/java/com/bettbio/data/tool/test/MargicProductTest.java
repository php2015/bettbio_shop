package com.bettbio.data.tool.test;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

public class MargicProductTest extends AbstractBaseTest{


	String collectionName = "product";
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Test
	public void search(){
		long l = mongoTemplate.count(null, collectionName);
		System.out.println(l);
		
		
		String sql = "SELECT COUNT(0) FROM USERGRADE";
		int cnt = jdbcTemplate.queryForInt(sql);
		System.out.println(cnt);
	}
	
	@Test
	public void sql(){
		String sql = "SELECT p.PRODUCT_ID id, "
				+ "pd.NAME productNameCh, "
				+ "pd.EN_NAME productNameEn, "
				+ "pd.SIMPLE_DESCRIPTION simpleDescription , "
				+ "pd.STORECOND_DESCRIPTION storageCondition "
				+ "FROM PRODUCT p "+
			"LEFT JOIN PRODUCT_DESCRIPTION pd ON pd.PRODUCT_ID = p.PRODUCT_ID "+
			"WHERE p.DATE_CREATED < '2016-10-01' "+
			"ORDER BY p.DATE_CREATED DESC "+
			"LIMIT 10";
		
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		
		for (Map<String, Object> map : list) {
			System.out.println(map);
		}
	}
}
