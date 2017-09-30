package com.salesmanager.web.init.data;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salesmanager.core.business.customer.model.UserSegment;
import com.salesmanager.core.business.customer.service.UserSegmentService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantRank;
import com.salesmanager.core.business.merchant.service.MerchantRankService;
import com.salesmanager.core.business.reference.init.service.InitializationDatabase;
import com.salesmanager.core.business.system.model.SystemConfiguration;
import com.salesmanager.core.business.system.service.SystemConfigurationService;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.GroupType;
import com.salesmanager.core.business.user.model.Permission;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.core.business.user.service.PermissionService;
import com.salesmanager.core.constants.GlobalConstants;
import com.salesmanager.core.constants.SystemConstants;
import com.salesmanager.core.utils.CoreConfiguration;
import com.salesmanager.web.admin.security.WebUserServices;
import com.salesmanager.web.constants.ApplicationConstants;
import com.salesmanager.web.utils.AppConfiguration;



@Component
public class InitializationLoader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InitializationLoader.class);
	
	@Autowired
	private AppConfiguration appConfiguration;

	
	@Autowired
	private InitializationDatabase initializationDatabase;
	
	@Autowired
	private com.salesmanager.web.init.data.InitData initData;
	
	@Autowired
	private SystemConfigurationService systemConfigurationService;
	
	@Autowired
	private WebUserServices userDetailsService;
	
	@Autowired
	protected PermissionService  permissionService;
	
	@Autowired
	protected GroupService   groupService;
	
	@Autowired
	private CoreConfiguration configuration;

	@Autowired
	private MerchantRankService merchantRankService;
	
	@Autowired
	private UserSegmentService userSegmentService;
	
	@PostConstruct
	public void init() {
		
		try {
			
			initUserSegment();
			
			if (initializationDatabase.isEmpty()) {
				LOGGER.info(String.format("%s : Shopizer database is empty, populate it....", "sm-shop"));
		
				 initializationDatabase.populate("sm-shop");
				
				
				
				 //security groups and permissions

				  Group gsuperadmin = new Group("SUPERADMIN");
				  gsuperadmin.setGroupType(GroupType.ADMIN);
				  Group gadmin = new Group("ADMIN");
				  gadmin.setGroupType(GroupType.ADMIN);
				  Group gcatalogue = new Group("ADMIN_CATALOGUE");
				  gcatalogue.setGroupType(GroupType.ADMIN);
				  Group gstore = new Group("ADMIN_STORE");
				  gstore.setGroupType(GroupType.ADMIN);
				  Group gorder = new Group("ADMIN_ORDER");
				  gorder.setGroupType(GroupType.ADMIN);
				  Group gdiamondproduct = new Group("ADMIN_DIAMOND_PRODUCT");
				  gorder.setGroupType(GroupType.ADMIN);
				  /*Group gcontent = new Group("ADMIN_CONTENT");
				  gcontent.setGroupType(GroupType.ADMIN);*/

				  groupService.create(gsuperadmin);
				  groupService.create(gadmin);
				  groupService.create(gcatalogue);
				  groupService.create(gstore);
				  groupService.create(gorder);
				  groupService.create(gdiamondproduct);
				  //groupService.create(gcontent);
				  
				  Permission storeadmin = new Permission("STORE_ADMIN");//Administrator of the store
				  storeadmin.getGroups().add(gsuperadmin);
				  storeadmin.getGroups().add(gadmin);
				  permissionService.create(storeadmin);
				  
				  Permission superadmin = new Permission("SUPERADMIN");
				  superadmin.getGroups().add(gsuperadmin);
				  permissionService.create(superadmin);
				  
				  Permission admin = new Permission("ADMIN");
				  admin.getGroups().add(gsuperadmin);
				  admin.getGroups().add(gadmin);
				  permissionService.create(admin);
				  
				  Permission auth = new Permission("AUTH");//Authenticated
				  auth.getGroups().add(gsuperadmin);
				  auth.getGroups().add(gadmin);
				  auth.getGroups().add(gcatalogue);
				  auth.getGroups().add(gstore);
				  auth.getGroups().add(gorder);
				  permissionService.create(auth);

				  
				  Permission products = new Permission("PRODUCTS");
				  products.getGroups().add(gsuperadmin);
				  products.getGroups().add(gadmin);
				  products.getGroups().add(gcatalogue);
				  permissionService.create(products);

				  Permission order = new Permission("ORDER");
				  order.getGroups().add(gsuperadmin);
				  order.getGroups().add(gorder);
				  order.getGroups().add(gadmin);
				  permissionService.create(order);
				  
				  Permission content = new Permission("CONTENT");
				  content.getGroups().add(gsuperadmin);
//				  content.getGroups().add(gadmin);
//				  content.getGroups().add(gcontent);
				  permissionService.create(content);
				  
				  
				  
				  Permission pstore = new Permission("STORE");
				  pstore.getGroups().add(gsuperadmin);
				  pstore.getGroups().add(gstore);
				  pstore.getGroups().add(gadmin);
				  permissionService.create(pstore);
				  
				 /* Permission tax = new Permission("TAX");
				  tax.getGroups().add(gsuperadmin);
				  tax.getGroups().add(gstore);
				  tax.getGroups().add(gadmin);
				  permissionService.create(tax);*/
				  
				  
				 /* Permission payment = new Permission("PAYMENT");
				  payment.getGroups().add(gsuperadmin);
				  payment.getGroups().add(gstore);
				  payment.getGroups().add(gadmin);
				  permissionService.create(payment);*/
				  
				  Permission customer = new Permission("CUSTOMER");
				  customer.getGroups().add(gsuperadmin);
				  //customer.getGroups().add(gstore);
				  customer.getGroups().add(gadmin);
				  permissionService.create(customer);
				  
				  
				  /*Permission shipping = new Permission("SHIPPING");
				  shipping.getGroups().add(gsuperadmin);
				  shipping.getGroups().add(gadmin);
				  shipping.getGroups().add(gstore);
				  
				  permissionService.create(shipping);*/
				
				
				
				  userDetailsService.createDefaultAdmin();
				  
				  
				  //load customer groups and permissions
				  Group gcustomer = new Group("CUSTOMER");
				  gcustomer.setGroupType(GroupType.CUSTOMER);
				  
				  groupService.create(gcustomer);
				  
				  Permission gcustomerpermission = new Permission("AUTH_CUSTOMER");
				  gcustomerpermission.getGroups().add(gcustomer);
				  permissionService.create(gcustomerpermission);

				  
				  Permission diaproducts = new Permission("DIAMOND_PRODUCT_CONTROLLER");
				  diaproducts.getGroups().add(gsuperadmin);
				  diaproducts.getGroups().add(gadmin);
				  diaproducts.getGroups().add(gdiamondproduct);
				  permissionService.create(diaproducts);
				  
				  // merchant rank
				  initMerchantRank();
				  
  
				  loadData();

			}
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error in the init method",e);
		}
		

		
	}

	protected void initUserSegment() throws ServiceException {
		UserSegment uSegment = new UserSegment();
		uSegment.setName(GlobalConstants.BuyerType_ResearchWorker);
		uSegment.setDiscriptionEn("Research Worker");
		uSegment.setDiscriptionZh("科研用户");
		uSegment.setForBuyer(true);
		uSegment.setForSeller(false);
		userSegmentService.ensure(uSegment);
		
		uSegment = new UserSegment();
		uSegment.setName(GlobalConstants.BuyerType_Dealer);
		uSegment.setDiscriptionEn("Dealer");
		uSegment.setDiscriptionZh("经销商");
		uSegment.setForBuyer(true);
		uSegment.setForSeller(false);
		userSegmentService.ensure(uSegment);
		
		uSegment = new UserSegment();
		uSegment.setName(GlobalConstants.BuyerType_IndustrialCustomer);
		uSegment.setDiscriptionEn("IndustrialCustomer");
		uSegment.setDiscriptionZh("工业用户");
		uSegment.setForBuyer(true);
		uSegment.setForSeller(false);
		userSegmentService.ensure(uSegment);
	}

	protected void initMerchantRank() throws ServiceException {
		MerchantRank mrank = new MerchantRank();
		  mrank.setId(1);
		  mrank.setRankName("JUNIOR");
		  mrank.setRankChineseName("基本");
		  mrank.setDiamondProductNumber(5);
		  merchantRankService.create(mrank);
		  
		  mrank = new MerchantRank();
		  mrank.setId(2);
		  mrank.setRankName("MIDDLE");
		  mrank.setRankChineseName("普通");
		  mrank.setDiamondProductNumber(10);
		  merchantRankService.create(mrank);
		  
		  mrank = new MerchantRank();
		  mrank.setId(1);
		  mrank.setRankName("SENIOR");
		  mrank.setRankChineseName("高级");
		  mrank.setDiamondProductNumber(20);
		  merchantRankService.create(mrank);
		  
		  mrank = new MerchantRank();
		  mrank.setId(1);
		  mrank.setRankName("VIP");
		  mrank.setRankChineseName("贵宾");
		  mrank.setDiamondProductNumber(100);
		  merchantRankService.create(mrank);
	}
	
	private void loadData() throws ServiceException {
		
		String loadTestData = configuration.getProperty(ApplicationConstants.POPULATE_TEST_DATA);
		boolean loadData =  !StringUtils.isBlank(loadTestData) && loadTestData.equals(SystemConstants.CONFIG_VALUE_TRUE);

		
		if(loadData) {
			
			SystemConfiguration configuration = systemConfigurationService.getByKey(ApplicationConstants.TEST_DATA_LOADED);
		
			if(configuration!=null) {
					if(configuration.getKey().equals(ApplicationConstants.TEST_DATA_LOADED)) {
						if(configuration.getValue().equals(SystemConstants.CONFIG_VALUE_TRUE)) {
							return;		
						}
					}		
			}
			
			initData.initInitialData();
			
			configuration = new SystemConfiguration();
			configuration.getAuditSection().setModifiedBy(SystemConstants.SYSTEM_USER);
			configuration.setKey(ApplicationConstants.TEST_DATA_LOADED);
			configuration.setValue(SystemConstants.CONFIG_VALUE_TRUE);
			systemConfigurationService.create(configuration);
			
			
		}
	}



}
