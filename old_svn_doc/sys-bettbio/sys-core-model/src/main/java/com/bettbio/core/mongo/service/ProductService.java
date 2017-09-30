package com.bettbio.core.mongo.service;

import java.util.List;
import java.util.Map;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.mongo.model.ConsumableMaterialProduct;
import com.bettbio.core.mongo.model.Product;
import com.bettbio.core.mongo.model.ProductBaseModel;
import com.bettbio.core.mongo.model.ReagentProduct;
import com.bettbio.core.mongo.model.ServiceProduct;

public interface ProductService {

	// 添加产品 实际添加的是传进来的子类对象
	void insertProduct(ProductBaseModel product);

	// 更新产品 实际更新的是传进来的子类对象
	int updateOne(ProductBaseModel product);
	
	// 根据id查找产品
	Product selectProductById(Integer id);

	public int updateOne(ReagentProduct reagentProduct, String collectionName);

	public int updateOne(ConsumableMaterialProduct consumableMaterialProduct, String collectionName);

	public int updateOne(ServiceProduct serviceProduct, String collectionName);

	public void insertProduct(ReagentProduct reagentProduct, String collectionName);

	public void insertProduct(ConsumableMaterialProduct consumableMaterialProduct, String collectionName);

	public void insertProduct(ServiceProduct serviceProduct, String collectionName);

	public Page<ProductBaseModel> selectProducs(Map<String, Object> map) throws Exception;

	public ReagentProduct selectOneByQuery(ReagentProduct reagentProduct);

	public ConsumableMaterialProduct selectOneByQuery(ConsumableMaterialProduct consumableMaterialProduct);

	public ServiceProduct selectOneByQuery(ServiceProduct serviceProduct);

	public void remove(int id, String collectionName);

	public void removes(List<Integer> ids, String collectionName);

	public String selectClassifyById(int id);
	
	List<Product> getProductByIds(List<Integer> ids);
	
	Map<Integer, Product> getPrdouctPrice(List<Integer> ids);
}
