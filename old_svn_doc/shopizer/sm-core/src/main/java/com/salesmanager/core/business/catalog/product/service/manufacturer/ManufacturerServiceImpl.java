package com.salesmanager.core.business.catalog.product.service.manufacturer;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.dao.manufacturer.ManufacturerDao;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufactureList;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.search.service.SearchService;



@Service("manufacturerService")
public class ManufacturerServiceImpl extends
		SalesManagerEntityServiceImpl<Long, Manufacturer> implements ManufacturerService {

	@Autowired
	SearchService searchService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ManufacturerServiceImpl.class);
	private ManufacturerDao manufacturerDao;
	
	@Autowired
	public ManufacturerServiceImpl(
		ManufacturerDao manufacturerDao) {
		super(manufacturerDao);
		this.manufacturerDao = manufacturerDao;		
	}
	
	@Override 
	public void delete(Manufacturer manufacturer,Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer> manMap) throws ServiceException{
		manufacturer =  this.getById(manufacturer.getId() );
		manMap.remove(manufacturer.getId());
		super.delete( manufacturer );
	}
	
	@Override
	public int getCountManufAttachedProducts( Manufacturer manufacturer ) throws ServiceException {
		return manufacturerDao.getCountManufAttachedProducts( manufacturer );
	}
	
	
	@Override
	public List<Manufacturer> listByStore(MerchantStore store, Language language) throws ServiceException {
		return manufacturerDao.listByStore(store, language);
	}
	
	@Override
	public List<Manufacturer> listByStore(MerchantStore store) throws ServiceException {
		return manufacturerDao.listByStore(store);
	}
	
	@Override
	public List<Manufacturer> listByProductsByCategoriesId(List<Long> ids, Language language) throws ServiceException {
		return manufacturerDao.listByProductsByCategoriesId(ids, language);
	}

	@Override
	public void addManufacturerDescription(Manufacturer manufacturer, ManufacturerDescription description)
			throws ServiceException {
		
		
		if(manufacturer.getDescriptions()==null) {
			manufacturer.setDescriptions(new HashSet<ManufacturerDescription>());
		}
		
		manufacturer.getDescriptions().add(description);
		description.setManufacturer(manufacturer);
		update(manufacturer);
	}
	
	@Override	
	public void saveOrUpdate(Manufacturer manufacturer, Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer> manMap) throws ServiceException {

		LOGGER.debug("Creating Manufacturer");
		
		try {
			if(manufacturer.getId()!=null && manufacturer.getId()>0) {
				manMap.remove(manufacturer.getId());			
			   super.update(manufacturer);  
				
			} else {						
			   super.create(manufacturer);

			}
			manMap.put(manufacturer.getId(),manufacturer);
			System.out.println(manMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public ManufactureList getByCriteria(Criteria criteria, Language language)
			throws ServiceException {
		// TODO Auto-generated method stub
		return manufacturerDao.getByCriteria(criteria, language);
	}
	
	@Override
	public List<Manufacturer> getListByCriteria(Criteria criteria){
		return manufacturerDao.getListByCriteria(criteria);
	}
	
	@Override
	public List<Manufacturer> getListByName(String name){
		return manufacturerDao.getListByName(name);
	}

	@Override
	public List<Manufacturer> listByProductsByCriteria(
			ProductCriteria criteria, Language language) {
		// TODO Auto-generated method stub
		return manufacturerDao.listByProductsByCriteria(criteria, language);
	}

	@Override
	public List<Object[]> getStoreName() {
		// TODO Auto-generated method stub
		return manufacturerDao.getStoreName();
	}
	//查询全部的品牌
	@Override
	public ManufactureList queryByManufacturer(){
		return this.manufacturerDao.queryByManufacturer();
	}
	//获取产品的品牌mid
	@Override
	public List<ManufacturerDescription> queryByManufacturerId(Long id){
		return this.manufacturerDao.queryByManufacturerId(id);
	}
	//判定该产品是否已经被授权了
	@Override
	public Integer queryByAuthorization(Long mid){
		return this.manufacturerDao.queryByAuthorization(mid);
	}
}
