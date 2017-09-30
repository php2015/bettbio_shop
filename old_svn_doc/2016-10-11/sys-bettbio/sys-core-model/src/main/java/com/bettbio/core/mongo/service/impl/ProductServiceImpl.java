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
import com.bettbio.core.model.BasedataType;
import com.bettbio.core.model.SStore;
import com.bettbio.core.mongo.dao.ProductDao;
import com.bettbio.core.mongo.model.AuthorityCertification;
import com.bettbio.core.mongo.model.ExperimentReport;
import com.bettbio.core.mongo.model.Literature;
import com.bettbio.core.mongo.model.Product;
import com.bettbio.core.mongo.model.ProductPrice;
import com.bettbio.core.mongo.model.PurchaseVoucher;
import com.bettbio.core.mongo.service.ProductService;
import com.bettbio.core.service.search.SearchService;
import com.bettbio.core.service.util.impl.BaseDataUtils;
import com.mongodb.DBObject;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	ProductDao productDao;
	
	@Autowired
	SearchService searchService;

	String collectionName = "product";

	@Override
	public void insertProduct(Product product) {
		product.setQualityScore(this.getScore(product));
		try {
			productDao.save(product, collectionName);
			searchService.addIndex(product);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int updateOne(Product product) {
		product.setQualityScore(this.getScore(product));
		Query query = Query.query(Criteria.where("_id").is(product.getId()));
		DBObject dbObject = ConvertHelper.convertNotNull(product);
		Update update = Update.fromDBObject(dbObject);
		int n = mongoTemplate.updateFirst(query, update, collectionName).getN();
		if(n>0){
			searchService.addIndex(product);
		}
		return n;
	}
	
	@Override
	public Product selectProductById(Integer id) {
		return mongoTemplate.findById(id, Product.class, collectionName);
	}

	@Override
	public Product selectProductByCode(String code) {
		Query query = Query.query(Criteria.where("code").is(code));
		return mongoTemplate.findOne(query, Product.class, collectionName);
	}
	
	@Override
	public Page<Product> selectProducs(Map<String, Object> map) throws Exception {
		Page<Product> page = new Page<Product>(map);
		Query query = new Query();

		if (!StringUtils.isEmpty(map.get("storeCode"))) {
			String storeCode = map.get("storeCode").toString();
			query.addCriteria(Criteria.where("store.code").is(storeCode));
		}
		
		if (!StringUtils.isEmpty(map.get("findName"))) {
			String findName = map.get("findName").toString();
			Pattern pattern = Pattern.compile("^.*" + findName + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("productNameCh").regex(pattern));
		}

		if (!StringUtils.isEmpty(map.get("classCode"))) {
			String classCode = map.get("classCode").toString();
			Pattern pattern = Pattern.compile(classCode + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("productClass.code").regex(pattern));
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

		page.setTotal(mongoTemplate.count(query,Product.class));
		query.skip(page.getStartRow());
		query.limit(page.getPageSize());
		page.setList(mongoTemplate.find(query, Product.class, collectionName));
		return page;
	}

	@Override
	public void remove(int id) {
		try {
			mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), collectionName);
			searchService.deleteIndex(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removes(List<Integer> ids) {
		try {

			mongoTemplate.remove(Query.query(Criteria.where("_id").in(ids)), collectionName);
			searchService.deleteIndex(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void removeAll(String storeCode) {
		try {
			Query query = new Query(Criteria.where("store.code").is(storeCode));
			List<Product> products = mongoTemplate.find(query,Product.class,collectionName);
			for (Product product : products) {
				searchService.deleteIndex(product.getId());
			}
			mongoTemplate.remove(query, collectionName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String selectClassifyById(int id) {
		Product product = mongoTemplate.findById(id, Product.class, collectionName);
		return product.getProductClass().getCode();
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
			t.setCode(product.getCode());
			t.setStore(product.getStore());
			t.setId(product.getId());
			t.setProductPrices(product.getProductPrices());
			t.setPrice(product.getPrice());
			map.put(product.getId(), t);
		}
		return map;
	}

	private int getScore(Product product){
		
		int score = 0;
		
		BasedataType basedataType=null;
		//文献
		List<Literature> literatures = product.getLiteratures();
		boolean flag = false;
		if(literatures != null  && literatures.size() > 0){					
			int docQuality = 0;
			for(Literature literature : literatures){
				basedataType = literature.getBasedataType();
				docQuality = BaseDataUtils.getProductQuality(docQuality, basedataType);						
			}
			docQuality = BaseDataUtils.addFirstProductQuality(docQuality,basedataType);
			score += docQuality;
			flag = true;
		}
		//购买凭证
		List<PurchaseVoucher> purchaseVouchers = product.getPurchaseVouchers();
		if (purchaseVouchers != null && purchaseVouchers.size() > 0) {			
			int proofQuality=0;
			for(PurchaseVoucher productProof : purchaseVouchers){
				basedataType = productProof.getBasedataType();
				proofQuality = BaseDataUtils.getProductQuality(proofQuality, basedataType);
			}
			proofQuality = BaseDataUtils.addFirstProductQuality(proofQuality, basedataType);
			score += proofQuality;			
		}
		//第三方认证
		List<AuthorityCertification> authorityCertifications = product.getAuthorityCertifications();
		if (authorityCertifications != null && authorityCertifications.size() > 0) {
			int proofQuality=0;
			for(AuthorityCertification productThirdproof : authorityCertifications){
				basedataType = productThirdproof.getBasedataType();
				proofQuality = BaseDataUtils.getProductQuality(proofQuality, basedataType);
			}
			proofQuality = BaseDataUtils.addFirstProductQuality(proofQuality, basedataType);
			score += proofQuality;	
			flag = true;
		}

		//实验报告
		List<ExperimentReport> experimentReports = product.getExperimentReports();
		
		/**
		 * 如果以上三个质量分没有，图片价格描述加20分,如果只有图片价格加10分
		 */
		
		if(score <=0 ){
			//判断是否有图片
			List<String> images = product.getImgUrls();
			if(images!=null && images.size()>0){
				score += 10;
				//判断是否有描述
				if(product.getSimpleDescription()!=null && !product.getSimpleDescription().equalsIgnoreCase("")){
					score += 10;
				}
			}
		}else{
			//只有4个凭证的加加20分
			if(flag==false){
				//实验报告也没有
				if (experimentReports != null && experimentReports.size() > 0) {
					if(experimentReports.size() >= 4){
						score += 40;
					}
				}
				
			}
		}
		

		if (experimentReports != null && experimentReports.size() > 0) {
			for(ExperimentReport report : experimentReports){
				int proofQuality = Integer.parseInt(BaseDataUtils.baseDataMap.get("BTYPE_SELFPROOF").toString());
				score += proofQuality;
				break;
			}
			
		}
		if(score >= 100) score=98;
		return score;
		
	}

	private int getPriceScore(Product product){
		int score = 0;
		List<ProductPrice> pr = product.getProductPrices();
		for(ProductPrice p : pr){
			if(p.getDiscountPrice() > 0){
				score = 10;
				break;
			}
		}
			
		return score;
	}

	private int getDescScore(Product product){
		int score = 0;
		if(product.getSimpleDescription()!=null && !product.getSimpleDescription().equalsIgnoreCase("")){
			score = 10;
		}
		return score;
	}

}
