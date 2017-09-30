package com.salesmanager.web.utils;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.UserGrade;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.model.ProductList;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.certificate.ProductCertificate;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.proof.ProductProof;
import com.salesmanager.core.business.catalog.product.model.thirdproof.ProductThirdproof;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.certificate.ProductCertificateService;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.catalog.product.service.price.ProductPriceService;
import com.salesmanager.core.business.catalog.product.service.proof.ProductProofService;
import com.salesmanager.core.business.catalog.product.service.thirdproof.ProductThirdproofService;
import com.salesmanager.core.business.common.model.BasedataType;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.common.service.BasedataTypeService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.search.service.SearchService;
import com.salesmanager.web.admin.entity.products.UploadProductsResult;
import com.salesmanager.web.constants.Constants;

@Component("loadDataThread")
@Scope("prototype")
public class LoadDataThread implements Runnable{
	
	private static Logger logger = Logger.getLogger(LoadDataThread.class);
	private static String impproduct = "impproduct";
	public static String updateindex = "updateindex";
	public static String relaod = "relaod";
	public static String man ="man";
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
	@Autowired
	private ProductPriceService productPriceService;
	@Autowired
	private ProductCertificateService productCertificateService;
	@Autowired
	private ProductThirdproofService productThirdproofService;
	@Autowired
	private ProductProofService productProofService;
	
	public void run() {
		logger.info("it is begining loading product, flag=" + flag);
		logger.info("it is hashCode = " + this.getClass().hashCode());
		if (flag.equalsIgnoreCase(LoadDataThread.impproduct)) {
			URL url = Thread.currentThread().getContextClassLoader()
			        .getResource("");
			logger.info("file path = " + url.getFile());
			//01 试剂
			String urlpath = url.getPath().replace("%20"," ").toString().trim();
			File reagent = new File(urlpath+"/upload/reagent");
			logger.info(".excl= " + reagent);
			File[] files = reagent.listFiles();
			if (files != null) {
			    for(File file:files){     
			    	if(file.isFile()){
			    		try {
			    			//view.setBeginline(2);
			    			view.setImpMaxLine(45000);
			    			UploadProductsResult importResesult =view.importProducts(file,context);
			    			writeTxt(importResesult,file.getAbsolutePath()+".txt");
						} catch (Exception e) {
							e.printStackTrace();
						}
			    	}
			    }
			}
		    //02 其他
		    File other = new File(urlpath+"/upload/other");
			File[] others = other.listFiles();
			if (others != null) {
			    for(File file:others){     
			    	if(file.isFile()){
			    		try {
			    			UploadProductsResult importResesult = view.importOthers(file,context);
			    			writeTxt(importResesult,file.getAbsolutePath()+".txt");
						} catch (Exception e) {
							e.printStackTrace();
						}
			    	}
			    }
			}
		  //03 服务
		    File server = new File(urlpath+"/upload/server");
			File[] servers = server.listFiles();
			if (servers != null) {
			    for(File file:servers){     
			    	if(file.isFile()){
			    		try {
			    			UploadProductsResult importResesult = view.importServers(file,context);
			    			writeTxt(importResesult,file.getAbsolutePath()+".txt");
						} catch (Exception e) {
							e.printStackTrace();
						}
			    	}
			    }
			}
 		}else if(flag.equalsIgnoreCase(LoadDataThread.updateindex)){
 			searchService.initService();
 			List<Object> ids = new ArrayList<Object>();
			if (storeId != null && storeId > 0) {
				ids = productService.getAllProductIDByStoreID(storeId);
 			}else
 				ids = productService.getAllProductID();
 			int idSize = ids.size();
 			logger.info("Wait For Processing  Product Size " + idSize);
 			if(idSize > 100000){
 				int n = 4;
 				int splitN = idSize / n;
 				int remainderN = idSize % n;
 				int fromIndex = 0;
 				int toIndex = splitN;
 				for (int i = 0; i < n; i++) {
	 				AddIndexDataRunnable runnable = new AddIndexDataRunnable(ids.subList(fromIndex, toIndex));
	 				Thread thread = new Thread(runnable);
	 				thread.start();
	 				
					fromIndex = toIndex;
					toIndex = toIndex + splitN;
					if (i == (n - 2) && remainderN > 0) toIndex = idSize;
				}
 			}else{
 				AddIndexDataRunnable runnable = new AddIndexDataRunnable(ids);
 				Thread thread = new Thread(runnable);
 				thread.start();
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
 		}else if(flag.equalsIgnoreCase(LoadDataThread.man)){
 			renameMan();
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
	
	public void renameMan(){
		
		/**
		Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer> manMap = (Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer>)context.getAttribute("manMap");
		URL url = Thread.currentThread().getContextClassLoader().getResource("");
		File server = new File(url.getFile()+"/man.xlsx");
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(server);
			Workbook book = null;
			try {
	        	book = WorkbookFactory.create(inputStream);
	        	//book = new XSSFWorkbook(inputStream);
	        } catch (Exception ex) {
	        	ex.printStackTrace();
	        	System.out.println(ex.getMessage());
	            try {
	            	book = new XSSFWorkbook(inputStream);
					//book = new HSSFWorkbook(inputStream);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					//LOGGER.error(e.getMessage());
					//result.setMessage(e.getMessage());
					//result.setStatus(UploadProductsResult.StatusEnum.UNSUCCESS.getCode());
					//return result;
					System.out.println(e.getMessage());
				} catch (IOException e) {
					//e.printStackTrace();
					//LOGGER.error(e.getMessage());
					System.out.println(e.getMessage());
					//result.setMessage(e.getMessage());
					//result.setStatus(UploadProductsResult.StatusEnum.UNSUCCESS.getCode());
					//return result;
				}
	        }
			logger.info("it is begining rename man, flag=" + flag);
			 for (int numSheet = 0; numSheet < 1; numSheet++) {
				 Sheet hssfSheet = book.getSheetAt(numSheet);
				 List<Long> mids = new ArrayList<Long>();
				 for (int rowNum = 0; rowNum <= 786  ; rowNum++){
					 	Row hssfRow = hssfSheet.getRow(rowNum);
					 	if(hssfRow !=null){
					 		if(!StringUtils.isNull(getValue(hssfRow.getCell(0)))){
						 		List <Manufacturer> mans = manufacturerService.getListByName(getValue(hssfRow.getCell(0)));
						 		if(mans !=null && mans.size()>0){
						 			String delteMan="Delete";
							 		if(!StringUtils.isNull(getValue(hssfRow.getCell(3)))){
							 			delteMan = getValue(hssfRow.getCell(3));
							 			if(!delteMan.equalsIgnoreCase(mans.get(0).getDescriptions().iterator().next().getName())){
							 				mans.get(0).getDescriptions().iterator().next().setName(delteMan);
								 			manufacturerService.saveOrUpdate(mans.get(0), manMap);
							 			}							 			
							 		}else{
							 			mids.add(mans.get(0).getId());							 			
							 		}							 		
							 		System.out.println(mans.get(0).getDescriptions().iterator().next().getName());
						 		}						 		
						 	}
					 	}
					 
				 }
				 if(mids !=null && mids.size()>0){
					 ProductCriteria criteria = new ProductCriteria();
					 criteria.setManufacturers(mids);
					 Map<String, Language> langs =languageService.getLanguagesMap();;
					 String lang = Constants.DEFAULT_LANGUAGE;
					 Language l = langs.get(lang);
					 ProductList ps =  productService.listByCriteria(l,criteria);
					 if(ps.getProducts() !=null && ps.getProducts().size()>0){
						 for(Product p:ps.getProducts()){
							 p.setManufacturer(null);
							 productService.saveOrUpdate(p);
						 }						 
					 }
					 for(Long m:mids){
						 Manufacturer mm = manufacturerService.getById(m);
						 manufacturerService.delete(mm,manMap);
					 }
				 }
			 }
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println(e1.getMessage());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}*/
		
		List<BasedataType> baseList = basedataTypeService.list();
		if (baseList!=null && baseList.size()>0) {
			ArrayList<Long> ids =new ArrayList<Long>();
			for (BasedataType basedataType : baseList) {				
				List<BasedataType> bases = basedataTypeService.getByName(basedataType.getName());
				//有重复的
				if(bases !=null && bases.size()>1){
					for(int i=1;i<bases.size();i++){
						//yimoyiy
						if(basedataType.getType().equalsIgnoreCase(bases.get(i).getType())){
							//wenxian
							if(basedataType.getType().startsWith("BTYPE_CERTIFICATE")){
								List<ProductCertificate> cs= productCertificateService.findByPropertiesHQL(new String[]{"basedataType.id"}, new Long[]{basedataType.getId()}, null);
								//duankailianj
								if(cs!=null && cs.size()>0){
									for (ProductCertificate c:cs){
										c.setBasedataType(bases.get(0));
										productCertificateService.saveOrUpdate(c);
									}
								}								
							}else if(basedataType.getType().startsWith("BTYPE_THIRDPROOF")){
								List<ProductThirdproof> ps = productThirdproofService.findByPropertiesHQL(new String[]{"basedataType.id"}, new Long[]{basedataType.getId()}, null);
								if(ps!=null && ps.size()>0){
									for (ProductThirdproof c:ps){
										c.setBasedataType(bases.get(0));
										productThirdproofService.saveOrUpdate(c);
									}
								}								
							}else if(basedataType.getType().startsWith("BTYPE_PROOF")){
								List<ProductProof> ps = productProofService.findByPropertiesHQL(new String[]{"basedataType.id"}, new Long[]{basedataType.getId()}, null);
								if(ps!=null && ps.size()>0){
									for (ProductProof c:ps){
										c.setBasedataType(bases.get(0));
										productProofService.saveOrUpdate(c);
									}
								}								
							}
							ids.add(bases.get(i).getId());							//remove
							
						}
					}
					if(ids.size()>0){
						Long[] idss = new Long[ids.size()];
						for (int k=0;k<ids.size();k++){
							idss[k]=ids.get(k);
						}
						try {
							basedataTypeService.deleteByProperties(new String[]{"id"}, idss);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						};
					}
					
				}
			}
		}
	}
	
	private void writeTxt(UploadProductsResult result,String fileName){
	
		try {
			
			 BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileName)));
			 if(result !=null){
				 if (result.getStatus().equals(UploadProductsResult.StatusEnum.SUCCESS.getCode())) {						
					 writer.write("successlines     ");
					 writer.write(result.getSuccesslines());
				} else if (result.getStatus().equals(UploadProductsResult.StatusEnum.PARTIALSUCCESS.getCode())) {						
					 writer.write("successlines     ");
					 writer.write(result.getSuccesslines());
					 writer.write("errorlines     ");
					 writer.write(result.getErrorlines());
				} else {
					writer.write("errorlines     ");
					 writer.write(result.getErrorlines());
				}
			 }else{
				 writer.write("fail     ");
			 }				 
			
			 writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	
	class AddIndexDataRunnable implements Runnable{

		private List<Object> ids = new ArrayList<Object>();
		
		public AddIndexDataRunnable(List<Object> ids){
			this.ids = ids;
		}
		
		@Override
		public void run() {
			String threadName = Thread.currentThread().getName();
			long startTime = System.currentTimeMillis();
			logger.info("Current Processing... Thread Name "+threadName+ " Begin Time " + DateUtil.generateTimeStamp() + " Process Size " + ids.size());
			
			for(Object id : ids) {
 	 			try {
 	 				
 	 				BigInteger bi = new BigInteger(id.toString());
 	 				Product product = productService.getById(bi.longValue());
 	 		
 	 				//只做非试剂类的算分
 	 				try{
	 						Category c = product.getCategories().iterator().next();
	 	 					if(!c.getCode().startsWith("01")){
	 	 						//服务类删除价格
	 	 						if(c.getCode().startsWith("04")){
	 	 							product = productService.getById(product.getId());
	 	 							ProductAvailability av= product.getAvailabilities().iterator().next();
	 	 							for(ProductPrice price : av.getPrices()){
	 	 								productPriceService.delete(price);
	 	 							}
	 	 						}
	 	 						
	 	 					}
	 	 					productService.saveOrUpdate(product);
	 					}catch (Exception e){
	 						//
	 					}
 	 				
 	 				//没有审核通过的删除
 	 				try{
 	 					if(product.getAuditSection().getAudit()<=0){
 	 						searchService.deleteIndex(product.getMerchantStore(), product);
 	 					} 	 					
 	 				}catch (Exception e){
 	 					
 	 				}
				} catch (Exception e) {
					continue;
				}
			}

			long endTime = System.currentTimeMillis();
			long minute = (endTime - startTime) / (1000 * 60);
			logger.info("Current Procesed Thread Name "+threadName+" " + " End Time "+DateUtil.generateTimeStamp() +" Procese Minute " + minute);
		}
	}
}
