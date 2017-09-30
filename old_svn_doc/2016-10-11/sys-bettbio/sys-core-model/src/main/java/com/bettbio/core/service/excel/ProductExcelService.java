package com.bettbio.core.service.excel;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bettbio.core.common.utils.DateUtil;
import com.bettbio.core.common.web.utils.CodeUtils;
import com.bettbio.core.model.SProductBrand;
import com.bettbio.core.model.SProductClassification;
import com.bettbio.core.model.SStore;
import com.bettbio.core.mongo.model.Product;
import com.bettbio.core.mongo.model.ProductPrice;
import com.bettbio.core.mongo.service.ProductService;
import com.bettbio.core.service.ProductBrandService;
import com.bettbio.core.service.ProductClassificationService;
import com.google.common.collect.Lists;

@Service
public class ProductExcelService {

	private final Logger LOGGER = LoggerFactory.getLogger(ProductExcelService.class);

	@Autowired
	ProductService productService;
	
	@Autowired
	ProductClassificationService productClassificationService;
	
	@Autowired
	ProductBrandService productBrandService;

	private int batchSize = 1000; // 批处理大小
	private int beginline = 5; // 表示处理商品数据从第6行数据开始 跳过表头
	private int servicesBeginline = 5; // 表示处理服务类商品数据从第6行数据开始
	private int impMaxLine = 600000; //最大行数60W

	private String rVersion = "2.2";
	private String oVersion = "2.2";
	private String sVersion = "2.3";
	
	private Map<String, Object> paramMap = new HashMap<String,Object>(4);
	
	// 批量添加
	private void doBatchSave(final List<Product> dataList) {
		LOGGER.info("excel import product size : " + dataList.size());
		for (Product product : dataList) {
			productService.insertProduct(product);
		}
	}

	@Async
	public void importProduct(final SStore store, final InputStream is,int type) {

		Workbook book = null;
		try {
			LOGGER.info("excel import start ...");
			long beginTime = System.currentTimeMillis();

			try {
				book = WorkbookFactory.create(is);
			} catch (Exception e) {
				LOGGER.error("excel import create Workbook error", e);
				e.printStackTrace();
			}

			List<Product> productList = Lists.newArrayList();
            int totalSize = 0; //总大小
            
            ProductExcelImportRHandler excelImportRHandler = new ProductExcelImportRHandler(store,book, productList, totalSize);
            switch (type) {
			case 0:
				totalSize = excelImportRHandler.importR(); //试剂
				break;
			case 1:
				totalSize = excelImportRHandler.importO(); //耗材/仪器
				break;
			case 2:
				totalSize = excelImportRHandler.importS();//服务
				break;
			}
			
			long endTime = System.currentTimeMillis();

			// 用时
			LOGGER.info("excel import end");
			LOGGER.info("excel import seconds : " + (endTime - beginTime) / 1000);
		} catch (Exception e) {
			LOGGER.error("excel import error : ", e);
			e.printStackTrace();
		} finally {
			//IOUtils.closeQuietly(is);
		}
	}
	
	class ProductExcelImportRHandler {

		SStore store = null;
		Workbook book = null;
		List<Product> productList = null;
        int totalSize = 0; 
        
        ProductExcelImportRHandler(final SStore store, 
        		final Workbook book, 
        		final List<Product> productList, 
        		int totalSize){
			
        	this.store = store;
        	this.book = book;
			this.productList = productList;
			this.totalSize = totalSize;
		}
		
		private int importR() {
			//只看第一个sheet
			for (int numSheet = 0; numSheet < 1; numSheet++) {
				Sheet hssfSheet = book.getSheetAt(numSheet);
				if (hssfSheet == null || book.isSheetHidden(numSheet) == true) {
					// 如果sheet不存在，或者隐藏，则不解析
					continue;
				}

				// 模板校验
				Row row1 = hssfSheet.getRow(0);
				Cell celli = row1.getCell(0);
				String cellValue = celli.getStringCellValue().trim();
				int rindex = cellValue.indexOf("RVersion:"); //版本信息
				if (rindex == -1) {
					// 模板文件不正确
					break;
				}

				String trimVersion = cellValue.substring(rindex);
				if (!StringUtils.isEmpty(trimVersion)) {
					if (trimVersion.indexOf(rVersion) == -1) {//版本号
						//不是最新模板
						break;
					}
				}
				
				/**
				 * 产品名称0   英文名称1  品牌2	 产品编码3   生产批号4   CAS 5   产品简述6    一级分类7    二级分类8   三级分类9 
				 * 规格10  价格11   货期12   存储条件13   详细描述14   合作模式（付费、免费）15
				 * 0 3 7 10 11 12 必填
				 */

				//可导入最大行数
				int totalRowNum = hssfSheet.getLastRowNum();
				if(totalRowNum > impMaxLine){
					totalRowNum = impMaxLine;
				}
				
				Product product = null;//产品
				List<ProductPrice> priceList = null;//价格集
				boolean merge = false;//合并标识
				boolean single = false;//单个标识
				
			    for (int rowNum = beginline; (rowNum <= totalRowNum && rowNum <= (totalRowNum+5)) ; 
			    		rowNum++) {

					Row hssfRow = hssfSheet.getRow(rowNum);
			        if (hssfRow == null) {//空行
			            continue;
			        }
			        
			        //必填参数检验 0 3 7
					if (!StringUtils.isEmpty(getValue(hssfRow.getCell(0)))&&
							!StringUtils.isEmpty(getValue(hssfRow.getCell(3)))&&
							!StringUtils.isEmpty(getValue(hssfRow.getCell(7)))){
						
						if(merge){//对合并的先进行新增
							product.setProductPrices(priceList);
							productList.add(product);
							merge = false;
							single = true;

							totalSize++;
						}else{//此时新增的是上一个产品
							if(single){
								productList.add(product);

								totalSize++;
							}
							single = true;
						}
						if(single){
							//只有一个价格集时只负责创建产品不执行新增
			            	product = new Product();
			            	priceList = Lists.newArrayList();
			            	
			            	//基本信息
			            	product.setProductNameCh(getValue(hssfRow.getCell(0)));//中文名
			            	product.setProductNameEn(getValue(hssfRow.getCell(1)));//英文名
			    		    product.setProductNo(getValue(hssfRow.getCell(3))); //产品编码
			    		    product.setBatchCode(getValue(hssfRow.getCell(4))); //批次号
			    		    product.setCasCode(getValue(hssfRow.getCell(5))); //CAS
			    		    product.setSimpleDescription(getValue(hssfRow.getCell(6)));//简单描述
			    		    product.setStorageCondition(getValue(hssfRow.getCell(13)));//存储条件
			    		    product.setDetailedDescription(getValue(hssfRow.getCell(14)));//详细描述
			    		    //getValue(hssfRow.getCell(15)) 收费方式
			            	
			    		    //商家
			    			product.setStore(store);
			    			
			    			//自动生成信息
			    			product.setId(CodeUtils.getProductId());
			    			product.setCode(CodeUtils.getCode());
			    			product.setCreateDate(new Date());
			    		   
			    		    //品牌
			    		    String brandName = getValue(hssfRow.getCell(2));
			    		    SProductBrand productBrand = productBrandService.selectByBrandName(brandName);
			    		    if(productBrand!=null){
								product.setProductBrand(productBrand);
			    		    }
			    		    
			            	//分类
			    		    String cateName3 = getValue(hssfRow.getCell(9));
			    		    String cateName2 = getValue(hssfRow.getCell(8));
			    		    String cateName1 = getValue(hssfRow.getCell(7));
			    		    
							SProductClassification productClass = null;
			    		    String prefixCode = "__";
							
							if (!StringUtils.isEmpty(cateName3)){
								prefixCode = prefixCode + "______";
								paramMap.put("cateName", cateName3);
							}else if(!StringUtils.isEmpty(cateName2)){
								prefixCode = prefixCode + "___";
								paramMap.put("cateName", cateName2);
							}else if(!StringUtils.isEmpty(cateName1)){
								paramMap.put("cateName", cateName1);
							}
							
			    		    paramMap.put("prefixCode", prefixCode);
							productClass = productClassificationService.selectByCateNameAndPCode(paramMap);
							
							if(productClass!=null){
								product.setProductClass(productClass);
							}
			    		    
			    		    //10 11 12 不为空 价格信息
							if (!StringUtils.isEmpty(getValue(hssfRow.getCell(10)))&&
									!StringUtils.isEmpty(getValue(hssfRow.getCell(11)))&&
									!StringUtils.isEmpty(getValue(hssfRow.getCell(12)))) {

								ProductPrice price = new ProductPrice();
								price.setSpecifications(getValue(hssfRow.getCell(10)));
								price.setPrice(Double.valueOf(getValue(hssfRow.getCell(11))));
								price.setSupplyCycle(getValue(hssfRow.getCell(12)));
								priceList.add(price);
							}
							
							product.setProductPrices(priceList);
						}
						
					}else{
						//10 11 12 为空跳出当前循环
						if (!StringUtils.isEmpty(getValue(hssfRow.getCell(10)))&&
								!StringUtils.isEmpty(getValue(hssfRow.getCell(11)))&&
								!StringUtils.isEmpty(getValue(hssfRow.getCell(12)))) {

							//当不存在0、3、7  存在10、11、12时开启合并 
							merge = true;
							single = false;
							
							//同一个产品多个价格集
							ProductPrice price = new ProductPrice();
							price.setSpecifications(getValue(hssfRow.getCell(10)));
							price.setPrice(Double.valueOf(getValue(hssfRow.getCell(11))));
							price.setSupplyCycle(getValue(hssfRow.getCell(12)));
							priceList.add(price);
						}else{
							if(product!=null){
								productList.add(product);
								product = null;

								totalSize++;
							}
							continue;
						}
					}

			        //一次最多导入1000条数据
					if (totalSize > 0 && totalSize % batchSize == 0) {
						try {
							doBatchSave(productList);
						} catch (Exception e) {
							LOGGER.error("excel import doBatchSave error : ", e);
						}
						productList.clear();
					}
			    }
			    
				// 把最后剩下的不足1000大小
				if (productList.size() > 0) {
					try {
						doBatchSave(productList);
					} catch (Exception e) {
						LOGGER.error("excel import doBatchSave error : ", e);
					}
					productList.clear();
				}
			}
			return totalSize;
		}

		//耗材/仪器
		private int importO() {
			//只看第一个sheet
			for (int numSheet = 0; numSheet < 1; numSheet++) {
				Sheet hssfSheet = book.getSheetAt(numSheet);
				if (hssfSheet == null || book.isSheetHidden(numSheet) == true) {
					// 如果sheet不存在，或者隐藏，则不解析
					continue;
				}

				// 模板校验
				Row row1 = hssfSheet.getRow(0);
				Cell celli = row1.getCell(0);
				String cellValue = celli.getStringCellValue().trim();
				int rindex = cellValue.indexOf("OVersion:"); //版本信息
				if (rindex == -1) {
					// 模板文件不正确
					break;
				}

				String trimVersion = cellValue.substring(rindex);
				if (!StringUtils.isEmpty(trimVersion)) {
					if (trimVersion.indexOf(oVersion) == -1) {//版本号
						//不是最新模板
						break;
					}
				}
				
				/**
				 * 产品名称0   英文名称1  品牌2	 产品编码3    产品简述4    一级分类5    二级分类6   三级分类7 
				 * 规格8  价格9   合作模式（付费、免费）10
				 * 0 3 5 8 9 必填
				 */

				//可导入最大行数
				int totalRowNum = hssfSheet.getLastRowNum();
				if(totalRowNum > impMaxLine){
					totalRowNum = impMaxLine;
				}
				
				Product product = null;//产品
				List<ProductPrice> priceList = null;//价格集
				boolean merge = false;//合并标识
				boolean single = false;//单个标识
				
			    for (int rowNum = beginline; (rowNum <= totalRowNum && rowNum <= (totalRowNum+5)) ; 
			    		rowNum++) {

					Row hssfRow = hssfSheet.getRow(rowNum);
			        if (hssfRow == null) {//空行
			            continue;
			        }
			        
			        //必填参数检验 0 3 5
					if (!StringUtils.isEmpty(getValue(hssfRow.getCell(0)))&&
							!StringUtils.isEmpty(getValue(hssfRow.getCell(3)))&&
							!StringUtils.isEmpty(getValue(hssfRow.getCell(5)))){
						
						if(merge){//对合并的先进行新增
							product.setProductPrices(priceList);
							productList.add(product);
							merge = false;
							single = true;

							totalSize++;
						}else{//此时新增的是上一个产品
							if(single){
								productList.add(product);

								totalSize++;
							}
							single = true;
						}
						if(single){
							//只有一个价格集时只负责创建产品不执行新增
			            	product = new Product();
			            	priceList = Lists.newArrayList();
			            	
			            	//基本信息
			            	product.setProductNameCh(getValue(hssfRow.getCell(0)));//中文名
			            	product.setProductNameEn(getValue(hssfRow.getCell(1)));//英文名
			    		    product.setProductNo(getValue(hssfRow.getCell(3))); //产品编码
			    		    product.setSimpleDescription(getValue(hssfRow.getCell(4)));//简单描述
			    		    //getValue(hssfRow.getCell(10)) 收费方式
			            	
			    		    //商家
			    			product.setStore(store);
			    			
			    			//自动生成信息
			    			product.setId(CodeUtils.getProductId());
			    			product.setCode(CodeUtils.getCode());
			    			product.setCreateDate(new Date());
			    		   
			    		    //品牌
			    		    String brandName = getValue(hssfRow.getCell(2));
			    		    SProductBrand productBrand = productBrandService.selectByBrandName(brandName);
			    		    if(productBrand!=null){
								product.setProductBrand(productBrand);
			    		    }
			    		    
			            	//分类
			    		    String cateName3 = getValue(hssfRow.getCell(7));
			    		    String cateName2 = getValue(hssfRow.getCell(6));
			    		    String cateName1 = getValue(hssfRow.getCell(5));
			    		    
							SProductClassification productClass = null;
			    		    String prefixCode = "__";
							
							if (!StringUtils.isEmpty(cateName3)){
								prefixCode = prefixCode + "______";
								paramMap.put("cateName", cateName3);
							}else if(!StringUtils.isEmpty(cateName2)){
								prefixCode = prefixCode + "___";
								paramMap.put("cateName", cateName2);
							}else if(!StringUtils.isEmpty(cateName1)){
								paramMap.put("cateName", cateName1);
							}
							
			    		    paramMap.put("prefixCode", prefixCode);
							productClass = productClassificationService.selectByCateNameAndPCode(paramMap);
							
							if(productClass!=null){
								product.setProductClass(productClass);
							}
			    		    
			    		    //8  9   不为空 价格信息
							if (!StringUtils.isEmpty(getValue(hssfRow.getCell(8)))&&
									!StringUtils.isEmpty(getValue(hssfRow.getCell(9)))) {

								ProductPrice price = new ProductPrice();
								price.setSpecifications(getValue(hssfRow.getCell(8)));
								price.setPrice(Double.valueOf(getValue(hssfRow.getCell(9))));
								priceList.add(price);
							}
							
							product.setProductPrices(priceList);
						}
						
					}else{
		    		    //8  9   不为空 价格信息
						if (!StringUtils.isEmpty(getValue(hssfRow.getCell(8)))&&
								!StringUtils.isEmpty(getValue(hssfRow.getCell(9)))) {

							//当不存在0、3、5  存在8、9 时开启合并 
							merge = true;
							single = false;
							
							//同一个产品多个价格集
							ProductPrice price = new ProductPrice();
							price.setSpecifications(getValue(hssfRow.getCell(8)));
							price.setPrice(Double.valueOf(getValue(hssfRow.getCell(9))));
							priceList.add(price);
						}else{
							if(product!=null){
								productList.add(product);
								product = null;

								totalSize++;
							}
							continue;
						}
					}

			        //一次最多导入1000条数据
					if (totalSize > 0 && totalSize % batchSize == 0) {
						try {
							doBatchSave(productList);
						} catch (Exception e) {
							LOGGER.error("excel import doBatchSave error : ", e);
						}
						productList.clear();
					}
			    }
			    
				// 把最后剩下的不足1000大小
				if (productList.size() > 0) {
					try {
						doBatchSave(productList);
					} catch (Exception e) {
						LOGGER.error("excel import doBatchSave error : ", e);
					}
					productList.clear();
				}
			}
			return totalSize;
		}
		
		private int importS() {

			//只看第一个sheet
			for (int numSheet = 0; numSheet < 1; numSheet++) {
				Sheet hssfSheet = book.getSheetAt(numSheet);
				if (hssfSheet == null || book.isSheetHidden(numSheet) == true) {
					// 如果sheet不存在，或者隐藏，则不解析
					continue;
				}
				
				// 模板校验
				Row row1 = hssfSheet.getRow(0);
				Cell celli = row1.getCell(0);
				String cellValue = celli.getStringCellValue().trim();
				int rindex = cellValue.indexOf("SVersion:"); //版本信息
				if (rindex == -1) {
					// 模板文件不正确
					break;
				}

				String trimVersion = cellValue.substring(rindex);
				if (!StringUtils.isEmpty(trimVersion)) {
					if (trimVersion.indexOf(sVersion) == -1) {//版本号
						//不是最新模板
						break;
					}
				}
				
				/**
				 * 产品名称0   英文名称1  品牌2	 产品编码3    产品简述4    一级分类5    二级分类6   三级分类7 
				 * 价格8   合作模式（付费、免费）9
				 * 0 3 5 8 必填
				 */

				//可导入最大行数
				int totalRowNum = hssfSheet.getLastRowNum();
				if(totalRowNum > impMaxLine){
					totalRowNum = impMaxLine;
				}
				
				Product product = null;//产品
				
				for (int rowNum = servicesBeginline; (rowNum <= totalRowNum && rowNum <= (totalRowNum+5)) ; 
			    		rowNum++) {

					Row hssfRow = hssfSheet.getRow(rowNum);
			        if (hssfRow == null) {//空行
			            continue;
			        }

			        
			        //必填参数检验 0 3 5
					if (!StringUtils.isEmpty(getValue(hssfRow.getCell(0)))&&
							!StringUtils.isEmpty(getValue(hssfRow.getCell(3)))&&
							!StringUtils.isEmpty(getValue(hssfRow.getCell(5)))){
						product = new Product();
		            	
		            	//基本信息
		            	product.setProductNameCh(getValue(hssfRow.getCell(0)));//中文名
		            	product.setProductNameEn(getValue(hssfRow.getCell(1)));//英文名
		    		    product.setProductNo(getValue(hssfRow.getCell(3))); //产品编码
		    		    product.setSimpleDescription(getValue(hssfRow.getCell(4)));//简单描述
		    		    //getValue(hssfRow.getCell(9)) 收费方式
		            	
		    		    //商家
		    			product.setStore(store);
		    			
		    			//自动生成信息
		    			product.setId(CodeUtils.getProductId());
		    			product.setCode(CodeUtils.getCode());
		    			product.setCreateDate(new Date());
		    		   
		    		    //品牌
		    		    String brandName = getValue(hssfRow.getCell(2));
		    		    SProductBrand productBrand = productBrandService.selectByBrandName(brandName);
		    		    if(productBrand!=null){
							product.setProductBrand(productBrand);
		    		    }
		    		    
		            	//分类
		    		    String cateName3 = getValue(hssfRow.getCell(7));
		    		    String cateName2 = getValue(hssfRow.getCell(6));
		    		    String cateName1 = getValue(hssfRow.getCell(5));
		    		    
						SProductClassification productClass = null;
		    		    String prefixCode = "__";
						
						if (!StringUtils.isEmpty(cateName3)){
							prefixCode = prefixCode + "______";
							paramMap.put("cateName", cateName3);
						}else if(!StringUtils.isEmpty(cateName2)){
							prefixCode = prefixCode + "___";
							paramMap.put("cateName", cateName2);
						}else if(!StringUtils.isEmpty(cateName1)){
							paramMap.put("cateName", cateName1);
						}
						
		    		    paramMap.put("prefixCode", prefixCode);
						productClass = productClassificationService.selectByCateNameAndPCode(paramMap);
						
						if(productClass!=null){
							product.setProductClass(productClass);
						}
		    		    
		    		    //8  价格信息
						if(!StringUtils.isEmpty(getValue(hssfRow.getCell(8)))){
							product.setPrice(getValue(hssfRow.getCell(8)));
						}
						
						productList.add(product);
						totalSize++;
					}
					if (totalSize > 0 && totalSize % batchSize == 0) {
						try {
							doBatchSave(productList);
						} catch (Exception e) {
							LOGGER.error("excel import doBatchSave error : ", e);
						}
						productList.clear();
					}
				}
				if (productList.size() > 0) {
					try {
						doBatchSave(productList);
					} catch (Exception e) {
						LOGGER.error("excel import doBatchSave error : ", e);
					}
					productList.clear();
				}
			}
			return totalSize;
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
	
}
