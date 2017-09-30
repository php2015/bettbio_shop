package com.bettbio.core.mongo.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bettbio.core.common.page.Page;
import com.bettbio.core.common.utils.ConvertHelper;
import com.bettbio.core.mongo.dao.ConsumableMaterialProductDao;
import com.bettbio.core.mongo.dao.ReagentProductDao;
import com.bettbio.core.mongo.dao.ServiceProductDao;
import com.bettbio.core.mongo.model.ConsumableMaterialProduct;
import com.bettbio.core.mongo.model.Product;
import com.bettbio.core.mongo.model.ProductBaseModel;
import com.bettbio.core.mongo.model.ReagentProduct;
import com.bettbio.core.mongo.model.ServiceProduct;
import com.bettbio.core.mongo.service.ProductService;
import com.mongodb.DBObject;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ReagentProductDao reagentProductDao;

	@Autowired
	ConsumableMaterialProductDao consumableMaterialProductDao;

	@Autowired
	ServiceProductDao serviceProductDao;

	String collectionName = "product";

	@Override
	public void insertProduct(ProductBaseModel product) {

		mongoTemplate.save(product, collectionName);
	}

	@Override
	public int updateOne(ProductBaseModel product) {
		Query query = Query.query(Criteria.where("_id").is(product.getId()));
		DBObject dbObject = ConvertHelper.convertNotNull(product);
		Update update = Update.fromDBObject(dbObject);
		return mongoTemplate.updateFirst(query, update, collectionName).getN();
	}
	
	@Override
	public Product selectProductById(Integer id) {
		return mongoTemplate.findById(id, Product.class, collectionName);
	}

	@Override
	public int updateOne(ReagentProduct reagentProduct, String collectionName) {

		return updateOne(reagentProduct);
	}

	@Override
	public int updateOne(ConsumableMaterialProduct consumableMaterialProduct, String collectionName) {

		return updateOne(consumableMaterialProduct);
	}

	@Override
	public int updateOne(ServiceProduct serviceProduct, String collectionName) {

		return updateOne(serviceProduct);
	}

	@Override
	public void insertProduct(ReagentProduct reagentProduct, String collectionName) {
		insertProduct(reagentProduct);
	}

	@Override
	public void insertProduct(ConsumableMaterialProduct consumableMaterialProduct, String collectionName) {
		insertProduct(consumableMaterialProduct);
	}

	@Override
	public void insertProduct(ServiceProduct serviceProduct, String collectionName) {
		insertProduct(serviceProduct);
	}

	@Override
	public Page<ProductBaseModel> selectProducs(Map<String, Object> map) throws Exception {
		Page<ProductBaseModel> page = new Page<ProductBaseModel>(map);
		Query query = new Query();

		if (!StringUtils.isEmpty(map.get("findName"))) {
			String findName = map.get("findName").toString();
			Pattern pattern = Pattern.compile("^.*" + findName + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("productNameCh").regex(pattern));
		}

		if (!StringUtils.isEmpty(map.get("classCode"))) {
			String classCode = map.get("classCode").toString();
			query.addCriteria(Criteria.where("productClass.code").is(classCode));
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String _startDate = map.get("startDate") + "";
		String _endDate = map.get("endDate") + "";
		if (!"null".equals(_startDate) && !"null".equals(_endDate)) {
			Date startDate = sdf.parse(_startDate);
			Date endDate = sdf.parse(_endDate);
			query.addCriteria(Criteria.where("createDate").gte(startDate).lte(endDate));
		} else if (!"null".equals(_startDate) && "null".equals(_endDate)) {
			Date startDate = sdf.parse(_startDate);
			query.addCriteria(Criteria.where("createDate").gte(startDate));
		} else if (!"null".equals(_endDate) && "null".equals(_startDate)) {
			Date endDate = sdf.parse(_endDate);
			query.addCriteria(Criteria.where("createDate").lte(endDate));
		}

		query.with(new Sort(Direction.DESC, "createDate"));

		page.setTotal(mongoTemplate.count(query, collectionName));
		query.skip(page.getStartRow());
		query.limit(page.getPageSize());
		page.setList(mongoTemplate.find(query, ProductBaseModel.class, collectionName));
		return page;
	}

	@Override
	public ReagentProduct selectOneByQuery(ReagentProduct reagentProduct) {
		Query query = new Query(Criteria.where("id").is(reagentProduct.getId()));
		reagentProduct = reagentProductDao.selectOneByQuery(query, collectionName);
		return reagentProduct;
	}

	@Override
	public ConsumableMaterialProduct selectOneByQuery(ConsumableMaterialProduct consumableMaterialProduct) {
		Query query = new Query(Criteria.where("id").is(consumableMaterialProduct.getId()));
		consumableMaterialProduct = consumableMaterialProductDao.selectOneByQuery(query, collectionName);
		return consumableMaterialProduct;
	}

	@Override
	public ServiceProduct selectOneByQuery(ServiceProduct serviceProduct) {
		Query query = new Query(Criteria.where("id").is(serviceProduct.getId()));
		serviceProduct = serviceProductDao.selectOneByQuery(query, collectionName);
		return serviceProduct;
	}

	@Override
	public void remove(int id, String collectionName) {
		serviceProductDao.remove(id, collectionName);
	}

	@Override
	public void removes(List<Integer> ids, String collectionName) {
		serviceProductDao.removes(ids, collectionName);
	}

	@Override
	public String selectClassifyById(int id) {
		Query query = new Query(Criteria.where("id").is(id));
		ProductBaseModel productBaseModel = reagentProductDao.selectOneByQuery(query,collectionName);
		return productBaseModel.getProductClass().getCode();
	}

	@Override
	public List<Product> getProductByIds(List<Integer> ids) {
		Query query = new Query(Criteria.where("_id").in(ids));
		return mongoTemplate.find(query, Product.class, "product");
	}

	@Override
	public Map<Integer, Product> getPrdouctPrice(List<Integer> ids) {
		Map<Integer, Product> map = new HashMap<Integer, Product>();
		List<Product> products = getProductByIds(ids);
		Product t = null;
		for (Product product : products) {
			t = new Product();
			t.setId(product.getId());
			t.setProductPrices(product.getProductPrices());
			t.setPrice(product.getPrice());
			map.put(product.getId(), t);
		}
		return map;
	}
}
