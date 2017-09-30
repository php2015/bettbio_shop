package com.salesmanager.web.init.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.UserGrade;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.common.model.BasedataType;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.common.service.BasedataTypeService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.web.constants.Constants;

@Component
public class InitApplication implements InitializingBean,ServletContextAware{

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private LanguageService languageService;
	@Autowired
	private MerchantStoreService merchantStoreService;
	@Autowired
	private ManufacturerService manufacturerService;
	@Autowired
	private BasedataTypeService basedataTypeService;
	
	@Override
	public void setServletContext(ServletContext arg0) {
		// TODO Auto-generated method stub
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
					arg0.setAttribute("topCateGory", topcatgory);
					arg0.setAttribute("categoryMap", categoryMap);	
					TreeMap <String, Integer> cmap = this.getCustmorGradeMap(0);
					if(cmap != null)arg0.setAttribute("custmoerGradeMap", cmap);
					TreeMap <String, Integer> amap = this.getCustmorGradeMap(1);
					if(cmap != null)arg0.setAttribute("adminGradeMap", amap);
				}
				Criteria criteria  = new Criteria();
				List<com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer> manufacturers = manufacturerService.getListByCriteria(criteria);
				if(manufacturers !=null && manufacturers.size()>0){
					Map<Long,Manufacturer> manMap = new HashMap<Long, Manufacturer>();
					for(Manufacturer man:manufacturers){
						manMap.put(man.getId(),man);
					}
					arg0.setAttribute("manMap", manMap);
				}
				
				//加载系统基础数据表 basedata_type
				List<BasedataType> baseList = basedataTypeService.list();
				if (baseList!=null && baseList.size()>0) {
					Map<String, BasedataType> basedataMap = new HashMap<String, BasedataType>();
					for (BasedataType basedataType : baseList) {
						basedataMap.put(basedataType.getCode(), basedataType);
					}
					arg0.setAttribute(Constants.BASEDATATYPE_MAP, basedataMap);
				}
			}
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
	}

	private TreeMap <String, Integer> getCustmorGradeMap(int type){
		TreeMap <String, Integer> cGradeMap = new TreeMap <String, Integer>();
		List<UserGrade> userGrades = categoryService.getGradeByType(type);
		for(UserGrade uGrade: userGrades){
			cGradeMap.put(uGrade.getCode(), uGrade.getValue());
		}
		return cGradeMap;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
