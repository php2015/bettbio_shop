package com.salesmanager.web.utils;


import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.UserGrade;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.model.ProductList;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.common.model.BasedataType;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.common.service.BasedataTypeService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.search.service.SearchService;
import com.salesmanager.web.constants.Constants;

@Component("loadDataThread")
@Scope("prototype")
public class LoadDataThread implements Runnable{
	
	private static Logger logger = Logger.getLogger(LoadDataThread.class);
	private static String impproduct = "impproduct";
	public static String updateindex = "updateindex";
	public static String relaod = "relaod";
	@Autowired
	private TransExcelView view;
	private String flag;
	private String filename;
	private Language language ;
	private Long storeId;
	private ServletContext context;
	@Autowired
	private ProductService productService;
	@Autowired
	SearchService searchService;
	@Autowired
	private BasedataTypeService basedataTypeService;
	@Autowired
	private LanguageService languageService;
	@Autowired
	private MerchantStoreService merchantStoreService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	protected ManufacturerService manufacturerService;
	
	public void run() {
		logger.info("it is begining loading product, flag=" + flag);
		logger.info("it is hashCode = " + this.getClass().hashCode());
		if (flag.equalsIgnoreCase(LoadDataThread.impproduct)) {
			URL url = Thread.currentThread().getContextClassLoader().getResource("");
			logger.info("file path = " + url.getFile());
			if(StringUtils.isNull(filename))filename = "impproduct.xlsx";
			File file = new File(url.getFile()+"/"+filename);
			try {
				view.importProducts(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
 		}else if(flag.equalsIgnoreCase(LoadDataThread.updateindex)){
 			ProductCriteria searchCriteria = new ProductCriteria();
 			if (storeId!=null) {
				searchCriteria.setStoreId(storeId);
			}
 			searchCriteria.setAvailableP(true);
 			ProductList productList = productService.listByCriteria(language, searchCriteria);
 			for(Product product : productList.getProducts()) {
 				//LOGGER.info("begin inserting or updating a document.");
 	 			try {
 	 				//product.setQualityScore(this.getQuality(product));
 	 				//productService.update(product);
 	 				/**
 	 				 * 未审核的数据不到ES
 	 				 */
 	 				boolean isFree= product.getProductIsFree();
 	 				if(isFree == false){
 	 					Long now = new Date().getTime();
 	 					isFree = true;
 	 					if(product.getDateChargeBegin()!=null && product.getDateChargeEnd()!=null){
 	 						if(product.getDateChargeBegin().getTime()<= now && now >=product.getDateChargeEnd().getTime() ){
 	 	 						isFree = false;
 	 	 					}
 	 					}
 	 				}
 	 				if( !isFree ||product.getAuditSection().getAudit()>0){
 	 					searchService.index(product.getMerchantStore(), product);
 	 				}
					System.out.println(product.getCode());
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					continue;
				}
 	 			//LOGGER.info("finish inserting or updating a document.");	
			}
 		}else if(flag.equalsIgnoreCase(LoadDataThread.relaod)){
 			Map <Long,Category> categoryMap= new HashMap<Long, Category>();
 			String lang = Constants.DEFAULT_LANGUAGE;
 			String store =Constants.DEFAULT_STORE;
 			Map<String, Language> langs = null;
 			try {
 				langs = languageService.getLanguagesMap();
 				//做数据的判断，如果没有初始化数据，则不加载application 数据
 				if (langs != null && langs.size()>0) {
 					Language l = langs.get(lang);
 					MerchantStore merchantStore = null;
 					merchantStore = merchantStoreService.getByCode(store);
 					Map<String, Category> objects = null;
 					objects = categoryService.listTwoDepthByLanguage(merchantStore,l);
 					if(objects!=null && objects.size()>0) {				
 						//create a Map<String,List<Content>
 						 
 						for(Object categoryKey : objects.keySet()){ 
 							Category category = objects.get(categoryKey);				
 							if(category.isVisible()) {
 								//put in id
 								categoryMap.put(category.getId(), category);
 							}
 						}
 						//增加顶级菜单
 						List<Category> topcatgory = categoryService.listByParent(null, l);
 						context.setAttribute("topCateGory", topcatgory);
 						context.setAttribute("categoryMap", categoryMap);	
 						TreeMap <String, Integer> cmap = this.getCustmorGradeMap(0);
 						if(cmap != null)context.setAttribute("custmoerGradeMap", cmap);
 						TreeMap <String, Integer> amap = this.getCustmorGradeMap(1);
 						if(cmap != null)context.setAttribute("adminGradeMap", amap);
 					}
 					Criteria criteria  = new Criteria();
 					List<com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer> manufacturers = manufacturerService.getListByCriteria(criteria);
 					if(manufacturers !=null && manufacturers.size()>0){
 						Map<Long,Manufacturer> manMap = new HashMap<Long, Manufacturer>();
 						for(Manufacturer man:manufacturers){
 							manMap.put(man.getId(),man);
 						}
 						context.setAttribute("manMap", manMap);
 					}
 					
 					//加载系统基础数据表 basedata_type
 					List<BasedataType> baseList = basedataTypeService.list();
 					if (baseList!=null && baseList.size()>0) {
 						Map<String, BasedataType> basedataMap = new HashMap<String, BasedataType>();
 						for (BasedataType basedataType : baseList) {
 							basedataMap.put(basedataType.getCode(), basedataType);
 						}
 						context.setAttribute(Constants.BASEDATATYPE_MAP, basedataMap);
 					}
 				}
 			} catch (ServiceException e1) {
 				e1.printStackTrace();
 			}
 		}
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}
	/**
	private int getQuality(Product product){
		int score =0;
		float tempScore =0;
		if(product.getCertificates() != null && product.getCertificates().size()>0){
			BasedataType baseType=null;
			Set<ProductCertificate> certificates = product.getCertificates();
			 for(ProductCertificate cerfiCertificate :certificates) {
				 BasedataType basedataType =basedataTypeService.getById(cerfiCertificate.getBasedataType().getId());
				 float temp =1;
				 if(basedataType.getValue()!=null){
					 tempScore += (this.getWeight(basedataType))*(Float.parseFloat(basedataType.getValue()));
				 }else{
					 tempScore +=(this.getWeight(basedataType))*temp;
				 }
				 baseType=basedataType;
			 }
			 tempScore +=(this.getWeight(baseType));
			 if (tempScore>Float.parseFloat(PropertiesUtils.getPropertiesValue(baseType.getType()))) tempScore=Float.parseFloat(PropertiesUtils.getPropertiesValue(baseType.getType()));
			 score += (int)(tempScore+0.5);
			
		}
		
		tempScore=0;
		if(product.getProofs() !=null && product.getProofs().size()>0){
			BasedataType baseType=null;
			Set<ProductProof> productProofs = product.getProofs();
			for(ProductProof productProof: productProofs){
				 BasedataType basedataType =basedataTypeService.getById(productProof.getBasedataType().getId());
				 float temp=1;
				 if(basedataType.getValue()!=null){
					 tempScore += this.getWeight(basedataType)*Float.parseFloat(basedataType.getValue());
				 }else{
					 tempScore +=(this.getWeight(basedataType))*temp;
				 }
				 baseType=basedataType;
			}
			tempScore +=(this.getWeight(baseType));
			 if (tempScore>Float.parseFloat(PropertiesUtils.getPropertiesValue(baseType.getType()))) tempScore=Float.parseFloat(PropertiesUtils.getPropertiesValue(baseType.getType()));
			 score += (int)tempScore;
		}
		
		tempScore=0;
		if(product.getThirdproofs() !=null && product.getThirdproofs().size()>0){
			BasedataType baseType=null;
			Set<ProductThirdproof> productProofs = product.getThirdproofs();
			for(ProductThirdproof productProof: productProofs){
				 BasedataType basedataType =basedataTypeService.getById(productProof.getBasedataType().getId());
				 float temp=1;
				 if(basedataType.getValue()!=null){
					 tempScore += this.getWeight(basedataType)*Float.parseFloat(basedataType.getValue());
				 }else{
					 tempScore +=(this.getWeight(basedataType))*temp;
				 }
				
				 baseType=basedataType;
			}
			tempScore +=(this.getWeight(baseType));
			 if (tempScore>Float.parseFloat(PropertiesUtils.getPropertiesValue(baseType.getType()))) tempScore=Float.parseFloat(PropertiesUtils.getPropertiesValue(baseType.getType()));
			 score += (int)tempScore;
		}
		
		tempScore=0;
		if(product.getSelfProofs() !=null && product.getSelfProofs().size()>0){
			tempScore = Float.parseFloat(PropertiesUtils.getPropertiesValue(BasedataTypeEnum.BTYPE_SELFPROOF.name()));			
			 score += (int)tempScore;
		}
		
		return score;
	}
	
	private float getWeight(BasedataType b){
		float weight =Float.parseFloat(PropertiesUtils.getPropertiesValue(b.getType()));
		return (weight/2);
	}
	*/
	private TreeMap <String, Integer> getCustmorGradeMap(int type){
		TreeMap <String, Integer> cGradeMap = new TreeMap <String, Integer>();
		List<UserGrade> userGrades = categoryService.getGradeByType(type);
		for(UserGrade uGrade: userGrades){
			cGradeMap.put(uGrade.getCode(), uGrade.getValue());
		}
		return cGradeMap;
	}
	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	
	public ServletContext getContext() {
		return context;
	}

	public void setContext(ServletContext context) {
		this.context = context;
	}
	
}
