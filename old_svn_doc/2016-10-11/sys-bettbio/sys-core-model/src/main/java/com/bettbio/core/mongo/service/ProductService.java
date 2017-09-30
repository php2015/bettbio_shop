package com.bettbio.core.mongo.service;

import java.util.List;
import java.util.Map;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.mongo.model.Product;

public interface ProductService {

	// 添加产品 实际添加的是传进来的子类对象
	void insertProduct(Product product);

	// 更新产品 实际更新的是传进来的子类对象
	int updateOne(Product product);
	
	// 根据id查找产品
	Product selectProductById(Integer id);
	
	// 根据id查找产品
	Product selectProductByCode(String code);

	public Page<Product> selectProducs(Map<String, Object> map) throws Exception;

	public void remove(int id);

	public void removes(List<Integer> ids);

	public void removeAll(String storeCode);
	
	public String selectClassifyById(int id);
	
	List<Product> getProductByIds(List<Integer> ids);
	
	Map<Integer, Product> getPrdouctPrice(List<Integer> ids);
}
