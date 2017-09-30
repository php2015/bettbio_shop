package com.salesmanager.web.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.catalog.product.model.type.ProductType;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductAttributeService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductOptionService;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductOptionValueService;
import com.salesmanager.core.business.catalog.product.service.availability.ProductAvailabilityService;
import com.salesmanager.core.business.catalog.product.service.certificate.ProductCertificateService;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.catalog.product.service.price.ProductPriceService;
import com.salesmanager.core.business.catalog.product.service.proof.ProductProofService;
import com.salesmanager.core.business.catalog.product.service.relationship.ProductRelationshipService;
import com.salesmanager.core.business.catalog.product.service.thirdproof.ProductThirdproofService;
import com.salesmanager.core.business.catalog.product.service.type.ProductTypeService;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.common.service.BasedataTypeService;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.currency.service.CurrencyService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.web.admin.entity.products.UploadProductsResult;

/**
 * 导入excel文件中的商品信息
 * @class com.salesmanager.web.utils.TransExcelView.java
 * @author sam
 * @date 2015年8月23日
 */
@Component
public class TransExcelView extends AbstractExcelView {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransExcelView.class);
	@Autowired
	protected ProductService productService;
	
	@Autowired
	protected ProductPriceService productPriceService;
	
	@Autowired
	protected ProductAttributeService productAttributeService;
	
	@Autowired
	protected ProductOptionService productOptionService;
	
	@Autowired
	protected ProductOptionValueService productOptionValueService;
	
	@Autowired
	protected ProductAvailabilityService productAvailabilityService;
	
	@Autowired
	protected ProductImageService productImageService;
	
	@Autowired
	protected CategoryService categoryService;
	
	@Autowired
	protected MerchantStoreService merchantService;
	
	@Autowired
	protected ProductTypeService productTypeService;
	
	@Autowired
	protected LanguageService languageService;
	
	@Autowired
	protected CountryService countryService;
	
	@Autowired
	protected ZoneService zoneService;
	
	@Autowired
	protected CustomerService customerService;
	
	@Autowired
	protected ManufacturerService manufacturerService;

	@Autowired
	protected CurrencyService currencyService;
	
	@Autowired
	protected OrderService orderService;
	
	@Autowired
	protected GroupService   groupService;
	
	@Autowired
	private ProductRelationshipService productRelationshipService;
	
	@Autowired
	private ProductThirdproofService productThirdproofService; //第三方认证
	
	@Autowired
	private ProductProofService productProofService; //购买凭证
	
	@Autowired
	private ProductCertificateService productCertificateService; //参考文献
	
	@Autowired
	private BasedataTypeService basedataTypeService; //基本类型
	
	private boolean adminImp = true;
	private int impMaxLine = 90000;

	//private final static String[] productsColNames = new String[]{"序号", "产品名称", "产品有效期", "是否上架"};

	private int beginline = 5; //表示处理商品数据从第6行数据开始
	private int services_beginline = 5; //表示处理服务类商品数据从第6行数据开始
	//private String CERTIFICATE_INIT_WEIGHT = "0.2";
	public TransExcelView() {
	}

	public UploadProductsResult importProducts(File file) throws Exception {
		return importProducts(file, null, null,null);
	}
	/**
	 * 
	 * @param file
	 * @param store
	 * @param user 
	 * @return 返回Map处理结果集合
	 * 			
	 */
	public UploadProductsResult importProducts(File file, MerchantStore store, User user,HttpServletRequest request) {
		LOGGER.info("BEGIN IMPORTING PRODUCTS..." + file.getName());
		try {
			return importProducts(new FileInputStream(file), store, user,request);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 导入试剂、耗材、仪器类商品
	 * @param file
	 * @param store
	 * @param user 
	 * @return 返回Map处理结果集合
	 * 			
	 */
	public UploadProductsResult importProducts(InputStream inputStream, MerchantStore store, User user,HttpServletRequest request) {
		UploadProductsResult result = new UploadProductsResult();
		Workbook book = null;
        try {
            book = new XSSFWorkbook(inputStream);
        } catch (Exception ex) {
        	ex.printStackTrace();
            try {
				book = new HSSFWorkbook(inputStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage());
				result.setMessage(e.getMessage());
				result.setStatus(UploadProductsResult.StatusEnum.UNSUCCESS.getCode());
				return result;
			} catch (IOException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage());
				result.setMessage(e.getMessage());
				result.setStatus(UploadProductsResult.StatusEnum.UNSUCCESS.getCode());
				return result;
			}
        }
//		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
        ProductType generalType = null;
        Language zh = null;
        MerchantStore defaultStore = null;
        Map<String, Category> cateMap = new HashMap<String, Category>();
        Map<String, MerchantStore> storeMap = new HashMap<String, MerchantStore>();
        
        Date date = new Date(System.currentTimeMillis());
        AuditSection auditSection = new AuditSection();
        auditSection.setDateCreated(date);
        auditSection.setDateModified(date);
        auditSection.setModifiedBy(user.getAdminName());

       // auditSection.setAudit(-1);  //未提交审核资料

        //auditSection.setAudit(0);
        try {
			//2 languages by default
			zh = languageService.getByCode("zh");
			
			/*Country China = countryService.getByCode("CN");
			Zone zone = zoneService.getByCode("HU");*/
			defaultStore = merchantService.getMerchantStore(MerchantStore.DEFAULT_STORE);
			//create a merchant
			if(store==null) {
				store = defaultStore;
			}
			
			generalType = productTypeService.getProductType(ProductType.GENERAL_TYPE);
        } catch (Exception e) {
        	e.printStackTrace();
			LOGGER.error(e.getMessage());
			result.setMessage(e.getMessage());
			result.setStatus(UploadProductsResult.StatusEnum.UNSUCCESS.getCode());
			try {
				book.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return result;
		}
        // 循环工作表Sheet
        //book.getNumberOfSheets()，当前只处理第一个sheet内容
        for (int numSheet = 0; numSheet < 1; numSheet++) {
        	Sheet hssfSheet = book.getSheetAt(numSheet);
            if (hssfSheet == null || book.isSheetHidden(numSheet) == true) { //如果sheet不存在，或者隐藏，则不解析
                continue;
            }
            // 循环行Row，由于前面beginline为标题行等信息，所以不读取，row和cell数据都是从零开始读取
            /**
             * 产品名称0   英文名称1	品牌2	 产品编码3	生产批号4	CAS5	产品简述6	一级分类7	二级分类8	三级分类9	规格10	 价格11	货期12	存储条件13 
             * 合作模式（付费、免费）14  商铺编码15
             */
            Product lastProduct = new Product();
            int end = beginline + impMaxLine;
            //做限制，一次最多导入1000条数据
            for (int rowNum = beginline; (rowNum <= hssfSheet.getLastRowNum() && rowNum <= end) ; 
            		rowNum++) {
            	try{
            		Row hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow == null) {
                        continue;
                    }
                    MerchantStore populatestore = null;
                    //获取商铺编码

                    
                    if (adminImp&&!StringUtils.isNull(getValue(hssfRow.getCell(15)))) {
                    	if(storeMap.get(getValue(hssfRow.getCell(15)))!=null) {
                    		populatestore = storeMap.get(getValue(hssfRow.getCell(15)));
                    	} else {
                    		List<MerchantStore> tmpList = merchantService.findByProperties(new String[]{"code"}, new String[]{getValue(hssfRow.getCell(15))}, null);
                    		if (tmpList!=null&&tmpList.size()>0) {
                    			populatestore = tmpList.get(0);
                    			storeMap.put(getValue(hssfRow.getCell(15)), tmpList.get(0));
                    		} else {
                    			populatestore = store;
                    		}
                    	}
					} else {
						populatestore = store;
					}
                    //由于存在同一商品多种规格信息，因此有可能商品名称和编码为空，但是存在规格库存等信息
                    if(StringUtils.isNull(getValue(hssfRow.getCell(0)))&&StringUtils.isNull(getValue(hssfRow.getCell(10)))) continue;
                    //如果hssfRow.getCell(0)获取的商品名称为空，并且hssfRow.getCell(10)不为空，即有产品规格信息，则新增加一条产品规格信息
                   /*if((StringUtils.isNull(getValue(hssfRow.getCell(0)))||
                    		getValue(hssfRow.getCell(0)).equals(lastName))
                    		&&!StringUtils.isNull(getValue(hssfRow.getCell(10)))) {*/
                	if(StringUtils.isNull(getValue(hssfRow.getCell(0)))&&!StringUtils.isNull(getValue(hssfRow.getCell(10)))) {
                    	//新增规格产品信息，即将上一个产品的信息新增一条产品价格信息（即产品规格）
                    	if(lastProduct!=null){
                    		ProductAvailability availability = lastProduct.getAvailabilities().iterator().next();
                    		ProductPrice price = new ProductPrice();
                    		price.setDefaultPrice(false);
                    		price.setCode("base");
                    		if (StringUtils.isNull(getValue(hssfRow.getCell(11)))) {
                    			price.setProductPriceAmount(new BigDecimal(0)); //产品规格价格
     						} else {
     							price.setProductPriceAmount(new BigDecimal(getValue(hssfRow.getCell(11)))); //产品规格价格
     						}
//                    		price.setProductPriceAmount(new BigDecimal(getValue(hssfRow.getCell(11))));
//                    		price.setProductPriceStockAmount(Integer.valueOf(getValue(hssfRow.getCell(12))));
                    		price.setProductPricePeriod(getValue(hssfRow.getCell(12)));
                    		price.setProductAvailability(availability); //关联上产品供应信息

                		    ProductPriceDescription dpd = new ProductPriceDescription();
                		    dpd.setName(getValue(hssfRow.getCell(10)));
                		    
                		    dpd.setProductPrice(price);
                		    dpd.setLanguage(zh);
                		    price.getDescriptions().add(dpd); 
                		    productPriceService.create(price); //添加产品价格信息（即规格）
                		    result.addSuccesslines(String.valueOf(rowNum+1));
                    	}
                    } else { //新增商品信息
                    	
                    	Product product = new Product();
                        String sku = NumberUtils.getNumByTime(String.valueOf(rowNum));
            		    product.setSku(sku); //产品系统编号SKU，今后将由系统自动生成
            		    product.setCode(getValue(hssfRow.getCell(3))); //产品编码
            		    product.setBatchnum(getValue(hssfRow.getCell(4))); //批次号
            		    product.setCas(getValue(hssfRow.getCell(5))); //CAS
            		    product.setType(generalType);
            		    product.setMerchantStore(populatestore);
            		    //product.setProductShipeable(true);
            		    product.setAvailable(true);
            		    //获取品牌，根据品牌名称，如果没有则新建品牌，如果品牌名称为空，则不作处理
            		    String brand_name = getValue(hssfRow.getCell(2));
            		    if (!StringUtils.isNull(brand_name)) {
            		    	List<Manufacturer> manufacturers = manufacturerService.getListByName(brand_name);
            		    	Manufacturer manufacturer = null;
            		    	if (manufacturers!=null&&manufacturers.size()>0) {
            		    		manufacturer = manufacturers.get(0);
            		    	} else {
            		    		manufacturer = new Manufacturer();
            		    		ManufacturerDescription description = new ManufacturerDescription();
            		    		description.setAuditSection(auditSection);
            		    		description.setName(brand_name);
            		    		description.setLanguage(zh);
            		    		description.setManufacturer(manufacturer);
            		    		manufacturer.getDescriptions().add(description);
            		    		manufacturer.setAuditSection(auditSection);
            		    		manufacturer.setMerchantStore(populatestore); 
            		    		//manufacturer.setSrcMerchantStoreId(populatestore.getId()); //创建品牌的商铺
            		    		Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer> manMap = (Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer>)request.getSession().getServletContext().getAttribute("manMap");
            					manufacturerService.saveOrUpdate(manufacturer,manMap);
            		    		//manufacturerService.save(manufacturer);
            		    	}
            		    	product.setManufacturer(manufacturer);
						}

            		    //设定产品分类(试剂/耗材/仪器/服务)，三级分类编号(cell(9))对应,二级分类编号(cell(8))对应,一级分类编号(cell(7))对应
            		    String cateName3 = getValue(hssfRow.getCell(9));
            		    String cateName2 = getValue(hssfRow.getCell(8));
            		    String cateName1 = getValue(hssfRow.getCell(7));
            		   
            		    Category cate=null;
            		    //判断当前的Map是否存在该category对象
            		    if (!StringUtils.isNull(cateName3)) {
							//三级分类（只有试剂拥有三级分类）
            		    	cate = cateMap.get(cateName3);
						} else if(!StringUtils.isNull(cateName2)) {
							cate = cateMap.get(cateName2);
						} else {
							cate = cateMap.get(cateName1);
						}
            		    if (cate!=null) {
        					product.getCategories().add(cate);
        				} else {
        					//当前的Map不存在该category对象，需要查询数据库
        					List<Category> catelist = null;
        					String tmp_cateName = "";
        					if (!StringUtils.isNull(cateName3)) {
     							//三级分类（只有试剂拥有三级分类）
                 		    	catelist = categoryService.getByCateName(cateName3, 2, null);
                 		    	tmp_cateName = cateName3;
     						} else if(!StringUtils.isNull(cateName2)) {
     							//二级分类（耗材拥有二级分类）
     							catelist = categoryService.getByCateName(cateName2, 1, null);
     							tmp_cateName = cateName2;
     						} else {
     							//一级分类（仪器/服务拥有一级分类）
     							catelist = categoryService.getByCateName(cateName1, 0, null);
     							tmp_cateName = cateName1;
     						}
        					if (catelist != null && catelist.size()>0) {
        						cate = catelist.get(0);
        						cateMap.put(tmp_cateName, cate);
        						product.getCategories().add(cate);
        					}
        				}
            		    
            		    // Product description
            		    ProductDescription description = new ProductDescription();
            		    description.setName(getValue(hssfRow.getCell(0))); //中文名称
            		    description.setEnName(getValue(hssfRow.getCell(1))); //英文名
            		    description.setMetatagTitle(getValue(hssfRow.getCell(0))); //metatitle默认采用产品名称
            		    description.setMetatagDescription(getValue(hssfRow.getCell(6))); //metadescription默认采用产品简单描述
//            		    description.setProductHighlight(getValue(hssfRow.getCell(6))); //产品简称，用于搜索高亮效果
            		    description.setSimpleDescription(getValue(hssfRow.getCell(6))); //产品简单描述
            		    //description.setDescription(getValue(hssfRow.getCell(6))); //产品描述
            		    description.setStorecondDescription(getValue(hssfRow.getCell(13))); //存储条件
//            		    description.setMethodDescription(getValue(hssfRow.getCell(14))); //实验方法
//            		    description.setTestDescription(getValue(hssfRow.getCell(15))); //实验数据
            		    
            		    //设置友好链接，暂时采用sku
            		    description.setSeUrl(sku);
            		    
            		    description.setLanguage(zh);
            		    description.setProduct(product);

            		    product.getDescriptions().add(description);
            		    //判断收费产品是收费还是免费产品
            		    if ("免费产品".equalsIgnoreCase(getValue(hssfRow.getCell(14)))) {
							product.setProductIsFree(true);
						} else {
							product.setProductIsFree(false);
							//auditSection.setAudit(0);  //收费产品默认修改为待审核资料状态
						}
            		    product.setAuditSection(auditSection);
            		    productService.create(product);
            		    
            		    // Availability
            		    ProductAvailability availability = new ProductAvailability();
            		    //产品发布日期
            		    availability.setProductDateAvailable(date);
            		    
            		    availability.setRegion("*");
            		    availability.setProduct(product);// associate with product
            		    productAvailabilityService.create(availability);
            		    product.getAvailabilities().add(availability);

            		    ProductPrice dprice = new ProductPrice();
            		    dprice.setDefaultPrice(true);
            		    //产品价格
            		    
            		    dprice.setProductAvailability(availability); //关联上产品供应信息
            		    if (StringUtils.isNull(getValue(hssfRow.getCell(11)))) {
            		    	dprice.setProductPriceAmount(new BigDecimal(0)); //产品规格价格
						} else {
							dprice.setProductPriceAmount(new BigDecimal(getValue(hssfRow.getCell(11)))); //产品规格价格
						}
//            		    dprice.setProductPriceStockAmount(Integer.valueOf(getValue(hssfRow.getCell(12)))); //产品规格库存
            		    dprice.setProductPricePeriod(getValue(hssfRow.getCell(12))); //供货时间

            		    ProductPriceDescription dpd = new ProductPriceDescription();
            		    dpd.setName(getValue(hssfRow.getCell(10))); //产品规格名称
            		    dpd.setProductPrice(dprice);
            		    dpd.setLanguage(zh);

            		    dprice.getDescriptions().add(dpd); //添加产品价格信息

            		    productPriceService.create(dprice);
            		    
            		   
            		    lastProduct = product;
            		    result.addSuccesslines(String.valueOf(rowNum+1));
                    }
                    
            	} catch (Exception e) {
                	e.printStackTrace();
        			LOGGER.error(e.getMessage());
        			result.addUnsuccesslines(String.valueOf(rowNum+1), e.getMessage());
        		}
            }
        }
        if(result.getErrorlines().length()>0) {
        	result.setStatus(UploadProductsResult.StatusEnum.PARTIALSUCCESS.getCode());
        } else {
        	result.setStatus(UploadProductsResult.StatusEnum.SUCCESS.getCode());
        }
        try {
			book.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return result;
	}
	
	
	/**
	 * 导入服务类商品
	 * @param file
	 * @param store
	 * @param user 
	 * @return 返回Map处理结果集合
	 * 			
	 */
	public UploadProductsResult importServiceProducts(InputStream inputStream, MerchantStore store, User user,HttpServletRequest request) {
		UploadProductsResult result = new UploadProductsResult();
		Workbook book = null;
        try {
            book = new XSSFWorkbook(inputStream);
        } catch (Exception ex) {
        	ex.printStackTrace();
            try {
				book = new HSSFWorkbook(inputStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage());
				result.setMessage(e.getMessage());
				result.setStatus(UploadProductsResult.StatusEnum.UNSUCCESS.getCode());
				return result;
			} catch (IOException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage());
				result.setMessage(e.getMessage());
				result.setStatus(UploadProductsResult.StatusEnum.UNSUCCESS.getCode());
				return result;
			}
        }
//		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
        ProductType generalType = null;
        Language zh = null;
        MerchantStore defaultStore = null;
        Map<String, Category> cateMap = new HashMap<String, Category>();
        Map<String, MerchantStore> storeMap = new HashMap<String, MerchantStore>();
        
        Date date = new Date(System.currentTimeMillis());
        AuditSection auditSection = new AuditSection();
        auditSection.setDateCreated(date);
        auditSection.setDateModified(date);
        auditSection.setModifiedBy(user.getAdminName());

       // auditSection.setAudit(-1);  //未提交审核资料

        //auditSection.setAudit(0);
        try {
			//2 languages by default
			zh = languageService.getByCode("zh");
			
			/*Country China = countryService.getByCode("CN");
			Zone zone = zoneService.getByCode("HU");*/
			defaultStore = merchantService.getMerchantStore(MerchantStore.DEFAULT_STORE);
			//create a merchant
			if(store==null) {
				store = defaultStore;
			}
			
			generalType = productTypeService.getProductType(ProductType.GENERAL_TYPE);
        } catch (Exception e) {
        	e.printStackTrace();
			LOGGER.error(e.getMessage());
			result.setMessage(e.getMessage());
			result.setStatus(UploadProductsResult.StatusEnum.UNSUCCESS.getCode());
			try {
				book.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return result;
		}
        // 循环工作表Sheet
        //book.getNumberOfSheets()，当前只处理第一个sheet内容
        for (int numSheet = 0; numSheet < 1; numSheet++) {
        	Sheet hssfSheet = book.getSheetAt(numSheet);
            if (hssfSheet == null || book.isSheetHidden(numSheet) == true) { //如果sheet不存在，或者隐藏，则不解析
                continue;
            }
            // 循环行Row，由于前面beginline为标题行等信息，所以不读取，row和cell数据都是从零开始读取
            /**
             * 产品名称0	英文名1	品牌2	产品编码3	产品简述4	一级分类5	二级分类6	三级分类7	规格8	 价格9    合作方式10  商铺编码11
             */
            Product lastProduct = new Product();
            int end = services_beginline + impMaxLine;
            //做限制，一次最多导入1000条数据
            for (int rowNum = services_beginline; (rowNum <= hssfSheet.getLastRowNum() && rowNum <= end) ; 
            		rowNum++) {
            	try{
            		Row hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow == null) {
                        continue;
                    }
                    MerchantStore populatestore = null;
                    //获取商铺编码

                    if (adminImp&&!StringUtils.isNull(getValue(hssfRow.getCell(11)))) {
                    	if(storeMap.get(getValue(hssfRow.getCell(11)))!=null) {
                    		populatestore = storeMap.get(getValue(hssfRow.getCell(11)));
                    	} else {
                    		List<MerchantStore> tmpList = merchantService.findByProperties(new String[]{"code"}, new String[]{getValue(hssfRow.getCell(11))}, null);
                    		if (tmpList!=null&&tmpList.size()>0) {
                    			populatestore = tmpList.get(0);
                    			storeMap.put(getValue(hssfRow.getCell(11)), tmpList.get(0));
                    		} else {
                    			populatestore = store;
                    		}
                    	}
					} else {
						populatestore = store;
					}
                    //由于存在同一商品多种规格信息，因此有可能商品名称和编码为空，但是存在规格库存等信息
                    if(StringUtils.isNull(getValue(hssfRow.getCell(0)))&&StringUtils.isNull(getValue(hssfRow.getCell(8)))) continue;
                    //如果hssfRow.getCell(0)获取的商品名称为空，并且hssfRow.getCell(8)不为空，即有产品规格信息，则新增加一条产品规格信息
                   /*if((StringUtils.isNull(getValue(hssfRow.getCell(0)))||
                    		getValue(hssfRow.getCell(0)).equals(lastName))
                    		&&!StringUtils.isNull(getValue(hssfRow.getCell(8)))) {*/
                    
                	if(StringUtils.isNull(getValue(hssfRow.getCell(0)))&&!StringUtils.isNull(getValue(hssfRow.getCell(8)))) {
                    	//新增规格产品信息，即将上一个产品的信息新增一条产品价格信息（即产品规格）
                    	if(lastProduct!=null){
                    		ProductAvailability availability = lastProduct.getAvailabilities().iterator().next();
                    		ProductPrice price = new ProductPrice();
                    		price.setDefaultPrice(false);
                    		price.setCode("base");
                    		if (StringUtils.isNull(getValue(hssfRow.getCell(9)))) {
                    			price.setProductPriceAmount(new BigDecimal(0)); //产品规格价格
     						} else {
     							price.setProductPriceAmount(new BigDecimal(getValue(hssfRow.getCell(9)))); //产品规格价格
     						}
//                    		price.setProductPriceAmount(new BigDecimal(getValue(hssfRow.getCell(11))));
//                    		price.setProductPriceStockAmount(Integer.valueOf(getValue(hssfRow.getCell(12))));
                    		price.setProductAvailability(availability); //关联上产品供应信息

                		    ProductPriceDescription dpd = new ProductPriceDescription();
                		    dpd.setName(getValue(hssfRow.getCell(8)));
                		    
                		    dpd.setProductPrice(price);
                		    dpd.setLanguage(zh);
                		    price.getDescriptions().add(dpd); 
                		    productPriceService.create(price); //添加产品价格信息（即规格）
                		    result.addSuccesslines(String.valueOf(rowNum+1));
                    	}
                    } else { //新增商品信息
                    	Product product = new Product();
                        String sku = NumberUtils.getNumByTime(String.valueOf(rowNum));
            		    product.setSku(sku); //产品系统编号SKU，今后将由系统自动生成
            		    product.setCode(getValue(hssfRow.getCell(3))); //产品编码
            		    product.setType(generalType);
            		    product.setMerchantStore(populatestore);
            		    //product.setProductShipeable(true);
            		    product.setAvailable(true);
            		    //获取品牌，根据品牌名称，如果没有则新建品牌，如果品牌名称为空，则不作处理
            		    String brand_name = getValue(hssfRow.getCell(2));
            		    if (!StringUtils.isNull(brand_name)) {
            		    	List<Manufacturer> manufacturers = manufacturerService.getListByName(brand_name);
            		    	Manufacturer manufacturer = null;
            		    	if (manufacturers!=null&&manufacturers.size()>0) {
            		    		manufacturer = manufacturers.get(0);
            		    	} else {
            		    		manufacturer = new Manufacturer();
            		    		ManufacturerDescription description = new ManufacturerDescription();
            		    		description.setAuditSection(auditSection);
            		    		description.setName(brand_name);
            		    		description.setLanguage(zh);
            		    		description.setManufacturer(manufacturer);
            		    		manufacturer.getDescriptions().add(description);
            		    		manufacturer.setAuditSection(auditSection);
            		    		manufacturer.setMerchantStore(populatestore); 
            		    		//manufacturer.setSrcMerchantStoreId(populatestore.getId()); //创建品牌的商铺
            		    		Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer> manMap = (Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer>)request.getSession().getServletContext().getAttribute("manMap");
            					manufacturerService.saveOrUpdate(manufacturer,manMap);
            		    		//manufacturerService.save(manufacturer);
            		    	}
            		    	product.setManufacturer(manufacturer);
						}
            		    //设定产品分类(试剂/耗材/仪器/服务)，三级分类编号(cell(7))对应,二级分类编号(cell(6))对应,一级分类编号(cell(5))对应
            		    String cateName3 = getValue(hssfRow.getCell(7));
            		    String cateName2 = getValue(hssfRow.getCell(6));
            		    String cateName1 = getValue(hssfRow.getCell(5));
            		    int level = 0;
            		    String cateName = "";
            		    Category cate=null;
            		    //判断当前的Map是否存在该category对象
            		    
            		    if (!StringUtils.isNull(cateName3)) {
            		    	level = 2;
							cateName += cateName3 + "_" + level;
						} else if (!StringUtils.isNull(cateName2)) {
							level = 1;
							cateName += cateName2 + "_" + level;
						} else if (!StringUtils.isNull(cateName1)) {
							level = 0;
							cateName += cateName1 + "_" + level;
						} 
            		    cate = cateMap.get(cateName);
            		    
            		    if (cate!=null) {
        					product.getCategories().add(cate);
        				} else {
        					//当前的Map不存在该category对象，需要查询数据库
        					List<Category> catelist = null;
        					if (!StringUtils.isNull(cateName3)) {
                 		    	catelist = categoryService.getByCateNameForServices(cateName3, level, null);
     						} else if(!StringUtils.isNull(cateName2)) {
     							catelist = categoryService.getByCateNameForServices(cateName2, level, null);
     						} else {
     							catelist = categoryService.getByCateNameForServices(cateName1, level, null);
     						}
        					if (catelist != null && catelist.size()>0) {
        						cate = catelist.get(0);
        						cateMap.put(cateName, cate);
        						product.getCategories().add(cate);
        					}
        				}
            		    
            		    // Product description
            		    ProductDescription description = new ProductDescription();
            		    description.setName(getValue(hssfRow.getCell(0))); //中文名称
            		    description.setEnName(getValue(hssfRow.getCell(1))); //英文名
            		    description.setMetatagTitle(getValue(hssfRow.getCell(0))); //metatitle默认采用产品名称
            		    description.setMetatagDescription(getValue(hssfRow.getCell(4))); //metadescription默认采用产品简单描述
//            		    description.setProductHighlight(getValue(hssfRow.getCell(4))); //产品简称，用于搜索高亮效果
            		    description.setSimpleDescription(getValue(hssfRow.getCell(4))); //产品简单描述
            		    //description.setDescription(getValue(hssfRow.getCell(4))); //产品描述
            		    
            		    //设置友好链接，暂时采用sku
            		    description.setSeUrl(sku);
            		    
            		    description.setLanguage(zh);
            		    description.setProduct(product);

            		    product.getDescriptions().add(description);
            		    //判断收费产品是收费还是免费产品
            		    if ("免费产品".equalsIgnoreCase(getValue(hssfRow.getCell(10)))) {
							product.setProductIsFree(true);
						} else {
							product.setProductIsFree(false);
							//auditSection.setAudit(0);  //收费产品默认修改为待审核资料状态
						}
            		    product.setAuditSection(auditSection);
            		    productService.create(product);
            		    
            		    // Availability
            		    ProductAvailability availability = new ProductAvailability();
            		    //产品发布日期
            		    availability.setProductDateAvailable(date);
            		    
            		    availability.setRegion("*");
            		    availability.setProduct(product);// associate with product
            		    productAvailabilityService.create(availability);
            		    product.getAvailabilities().add(availability);

            		    ProductPrice dprice = new ProductPrice();
            		    dprice.setDefaultPrice(true);
            		    //产品价格
            		    
            		    dprice.setProductAvailability(availability); //关联上产品供应信息
            		    if (StringUtils.isNull(getValue(hssfRow.getCell(9)))) {
            		    	dprice.setProductPriceAmount(new BigDecimal(0)); //产品规格价格
						} else {
							dprice.setProductPriceAmount(new BigDecimal(getValue(hssfRow.getCell(9)))); //产品规格价格
						}
//            		    dprice.setProductPriceStockAmount(Integer.valueOf(getValue(hssfRow.getCell(12)))); //产品规格库存

            		    ProductPriceDescription dpd = new ProductPriceDescription();
            		    dpd.setName(getValue(hssfRow.getCell(8))); //产品规格名称
            		    dpd.setProductPrice(dprice);
            		    dpd.setLanguage(zh);

            		    dprice.getDescriptions().add(dpd); //添加产品价格信息

            		    productPriceService.create(dprice);
            		    
            		   
            		    lastProduct = product;
            		    result.addSuccesslines(String.valueOf(rowNum+1));
                    }
                    
            	} catch (Exception e) {
                	e.printStackTrace();
        			LOGGER.error(e.getMessage());
        			result.addUnsuccesslines(String.valueOf(rowNum+1), e.getMessage());
        		}
            }
        }
        if(result.getErrorlines().length()>0) {
        	result.setStatus(UploadProductsResult.StatusEnum.PARTIALSUCCESS.getCode());
        } else {
        	result.setStatus(UploadProductsResult.StatusEnum.SUCCESS.getCode());
        }
        try {
			book.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return result;
	}
	/**
     * 获取单元格数字，转换成字符串，如果是字符串，则会对前后空字符trim
     * @param hssfCell
     *            Excel中的每一个格子
     * @return Excel中每一个格子中的值
     */
	private String getValue(Cell hssfCell) {
    	return getValue(hssfCell, true);
    }
	
	/**
	 * 获取单元格数字，转换成字符串
	 * @param hssfCell
	 * @param istrim 是否需要将字符串的前后空字符trim
	 * @return
	 */
	private String getValue(Cell hssfCell, boolean istrim) {
    	if (hssfCell == null) return "";
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值，取消科学计数法的表达方式，
        	//如果是数值，先判断是否是日期格式，如果是日期格式，则先进行转换，否则按照字符串方式处理
//        	String.format("%.0f", row.getCell(0).getNumericCellValue())
        	if(HSSFDateUtil.isCellDateFormatted(hssfCell)){
        		return DateUtil.formatDate(hssfCell.getDateCellValue());
        	} else return String.format("%.0f", hssfCell.getNumericCellValue());
        } else {
            // 返回字符串类型的值
        	if (istrim) {
        		return String.valueOf(hssfCell.getStringCellValue().trim());
			} else {
				return String.valueOf(hssfCell.getStringCellValue());
			}
        }
    }

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
	}
    
}
