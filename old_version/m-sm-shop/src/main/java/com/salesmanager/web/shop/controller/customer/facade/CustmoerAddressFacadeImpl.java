package com.salesmanager.web.shop.controller.customer.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.customer.model.CustomerAddress;
import com.salesmanager.core.business.customer.model.CustomerInvoice;
import com.salesmanager.core.business.customer.service.CustomerAddressService;
import com.salesmanager.core.business.customer.service.CustomerInvoiceService;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.web.entity.customer.CustomerAdress;
import com.salesmanager.web.entity.customer.DefautAddressInvoice;
import com.salesmanager.core.business.generic.exception.ConversionException;

import org.apache.commons.lang.StringUtils;

import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;

@Service("customerAddressFacade")
public class CustmoerAddressFacadeImpl implements CustomerAddressFacade{
	
	@Autowired
    private CustomerAddressService customerAddressService;
	@Autowired
    private CountryService countryService;
	@Autowired
    private ZoneService zoneService;
	@Autowired
    private CustomerInvoiceService customerInvoiceService;
	@Autowired
	private CustomerService customerService;

	@Override
	public void saveOrUpdate(CustomerAdress address,Customer customerModel,Language language) throws Exception {
		// TODO Auto-generated method stub
		
		
	       Map<String, Country> countriesMap = countryService.getCountriesMap( language );
	       Country country = countriesMap.get( address.getCountry() );
		com.salesmanager.core.business.customer.model.CustomerAddress  caddress = new com.salesmanager.core.business.customer.model.CustomerAddress(); 
  	  caddress.setName(address.getName());
  	  caddress.setCompany(address.getCompany());
  	  caddress.setAddress(address.getStreetAdress());
  	  caddress.setCity(address.getCity());
  	  caddress.setPostalCode(address.getPostCode());
  	  caddress.setTelephone(address.getTelephone());
  	  caddress.setCountry(country);
  	  caddress.setCustomer(customerModel);
  	  if(StringUtils.isNotBlank( address.getZone() )){
            Zone zone = zoneService.getByCode(address.getZone());
            if(zone==null) {
               throw new ConversionException("Unsuported zone code " + address.getZone());
            }
            caddress.setZone( zone );
            caddress.setState(null);
            
        } else {
      	  caddress.setZone(null);
        }
  	  if(StringUtils.isNotBlank(address.getId())){
  		caddress.setId(Long.parseLong(address.getId()));
  		this.customerAddressService.update(caddress);
  	  }else{
  		this.customerAddressService.create(caddress);
  	  }
  	caddress.getId();
	}

	@Override
	public List<CustomerAdress> getCustomerAddress(Customer customerModel,Language language)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String, Zone> zonesMap = zoneService.getZones(language);
		List<CustomerAdress> listReturn = new ArrayList<CustomerAdress>();
		Set<CustomerAddress> addressModel= customerModel.getAddresss();
		for(CustomerAddress address:addressModel){
			CustomerAdress caddress = new CustomerAdress();
			caddress.setId(address.getId().toString());
			 caddress.setName(address.getName());
		  	  caddress.setCompany(address.getCompany());
		  	  caddress.setStreetAdress(address.getAddress());
		  	  caddress.setCity(address.getCity());
		  	  caddress.setPostCode(address.getPostalCode());
		  	  caddress.setTelephone(address.getTelephone());
		  	  caddress.setCountry(address.getCountry().getIsoCode());
		  	caddress.setMemo(address.getMemo());
		  	  if(zonesMap!=null) {
				Zone z = zonesMap.get(address.getZone().getCode());
				if(z!=null) {					
					 caddress.setZone(z.getName());
				}
		  	  }
		  	 
			listReturn.add(caddress);
		}
		return listReturn;
	}

	@Override
	public CustomerAdress getById(long id, Customer customerModel,Language language)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String, Zone> zonesMap = zoneService.getZones(language);		
		Set<CustomerAddress> addressModel= customerModel.getAddresss();
		for(CustomerAddress address:addressModel){
			if (address.getId()==id){
			CustomerAdress caddress = new CustomerAdress();
			caddress.setId(address.getId().toString());
			 caddress.setName(address.getName());
		  	  caddress.setCompany(address.getCompany());
		  	  caddress.setStreetAdress(address.getAddress());
		  	  caddress.setCity(address.getCity());
		  	  caddress.setPostCode(address.getPostalCode());
		  	  caddress.setTelephone(address.getTelephone());
		  	  caddress.setCountry(address.getCountry().getIsoCode());
		  	caddress.setMemo(address.getMemo());
		  	  caddress.setId(String.valueOf(id));		  	
		  	  if(zonesMap!=null) {
				Zone z = zonesMap.get(address.getZone().getCode());
				if(z!=null) {					
					 caddress.setZone(z.getName());
					 caddress.setZoneCode(z.getCode());
				}
		  	  }
		  	  return caddress;
			}
		  	 
			
		}
		return null;
	}

	@Override
	public void remove(long id) throws Exception {
		// TODO Auto-generated method stub
			
		//this.customerAddressService.delete(address);
		this.customerAddressService.delete(id);
	}

	@Override
	public CustomerAdress getDefaultAdress(Customer customerModel,
			Language language) throws Exception {
		
		//get default address
		Set<CustomerAddress> addressModel= customerModel.getAddresss();
		
		if (null != addressModel && addressModel.size()>0){
			Map<String, Zone> zonesMap = zoneService.getZones(language);
			for(CustomerAddress address:addressModel){
				
				  if(address.getId() == customerModel.getAddressDefault()){
					  CustomerAdress caddress = new CustomerAdress();
						caddress.setId(address.getId().toString());
						 caddress.setName(address.getName());
						  caddress.setCompany(address.getCompany());
						  caddress.setStreetAdress(address.getAddress());
						  caddress.setCity(address.getCity());
						  caddress.setPostCode(address.getPostalCode());
						  caddress.setTelephone(address.getTelephone());
						  caddress.setCountry(address.getCountry().getIsoCode());
						  if(zonesMap!=null) {
							Zone z = zonesMap.get(address.getZone().getCode());
							if(z!=null) {					
								 caddress.setZone(z.getName());
							}
						  }
					 return caddress;
				  }
			}
			//fisrt one as defaut
			for(CustomerAddress address:addressModel){
					  CustomerAdress caddress = new CustomerAdress();
						caddress.setId(address.getId().toString());
						 caddress.setName(address.getName());
						  caddress.setCompany(address.getCompany());
						  caddress.setStreetAdress(address.getAddress());
						  caddress.setCity(address.getCity());
						  caddress.setPostCode(address.getPostalCode());
						  caddress.setTelephone(address.getTelephone());
						  caddress.setCountry(address.getCountry().getIsoCode());
						  if(zonesMap!=null) {
							Zone z = zonesMap.get(address.getZone().getCode());
							if(z!=null) {					
								 caddress.setZone(z.getName());
							}
						  }
						  customerModel.setAddressDefault(address.getId());
						  customerService.update(customerModel);
						  
					 return caddress;				
			}
		}
		return null;
	}

	@Override
	public DefautAddressInvoice initDefaultData(Customer customerModel,
			Language language) throws Exception {
		// TODO Auto-generated method stub
		DefautAddressInvoice defautAddressInvoice = new DefautAddressInvoice();
		CustomerAdress address = this.getDefaultAdress(customerModel, language);
		if(null != address) defautAddressInvoice.setDefaultAddress(address);
		CustomerInvoice customerInvoice =customerInvoiceService.getDefaultInvoice(customerModel);
		if(null != customerInvoice) defautAddressInvoice.setDefaultInvoice(customerInvoice);
		/**
		List<CustomerAdress> otherAddress = this.getCustomerAddress(customerModel, language);
		if(null != otherAddress) defautAddressInvoice.setOthersAddresss(otherAddress);
		Set<CustomerInvoice> othersInvoice = customerModel.getInvoices();
		if(null != othersInvoice) defautAddressInvoice.setOthersInvoices(othersInvoice);
		*/
		return defautAddressInvoice;
	}

	@Override
	public long saveOrUpdateAddress(CustomerAdress address,Customer customerModel,Language language) throws Exception {

		
	       Map<String, Country> countriesMap = countryService.getCountriesMap( language );
	       Country country = countriesMap.get( address.getCountry() );
		com.salesmanager.core.business.customer.model.CustomerAddress  caddress = new com.salesmanager.core.business.customer.model.CustomerAddress(); 
	  caddress.setName(address.getName());
	  caddress.setCompany(address.getCompany());
	  caddress.setAddress(address.getStreetAdress());
	  caddress.setCity(address.getCity());
	  caddress.setPostalCode(address.getPostCode());
	  caddress.setTelephone(address.getTelephone());
	  caddress.setCountry(country);
	  caddress.setMemo(address.getMemo());
	  caddress.setCustomer(customerModel);
	  if(StringUtils.isNotBlank( address.getZone() )){
         Zone zone = zoneService.getByCode(address.getZone());
         if(zone==null) {
            throw new ConversionException("Unsuported zone code " + address.getZone());
         }
         caddress.setZone( zone );
         caddress.setState(null);
         
     } else {
   	  caddress.setZone(null);
     }
	  if(StringUtils.isNotBlank(address.getId())){
		caddress.setId(Long.parseLong(address.getId()));
		this.customerAddressService.update(caddress);
	  }else{
		this.customerAddressService.create(caddress);
	  }
	
		return caddress.getId();
	}

}
