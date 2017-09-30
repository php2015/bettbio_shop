package com.salesmanager.web.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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

import com.salesmanager.core.business.catalog.category.service.CategoryService;
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
import com.salesmanager.core.business.common.service.BasedataTypeService;
import com.salesmanager.core.business.customer.service.CustomerService;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.service.MerchantStoreService;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.country.service.CountryService;
import com.salesmanager.core.business.reference.currency.model.Currency;
import com.salesmanager.core.business.reference.currency.service.CurrencyService;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.reference.language.service.LanguageService;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.business.reference.zone.service.ZoneService;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.GroupService;
import com.salesmanager.web.admin.entity.products.UploadProductsResult;
import com.salesmanager.web.constants.Constants;

/**
 * 导入excel的商家信息
 * @class com.salesmanager.web.utils.StoresExcelView.java
 * @author sam
 * @date 2015年12月31日
 */
@Component
public class StoresExcelView extends AbstractExcelView {

	private static final Logger LOGGER = LoggerFactory.getLogger(StoresExcelView.class);
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
	
		
	private Language zh;
	private Country cn;
	private Currency currency;
	
	private boolean adminImp = true;
	private int impMaxLine = 90000;
	private String default_domainName = "www.bettbio.com";
	private String default_template = "bootstrap3";

	private int beginline = 1; //表示处理商品数据从第2行数据开始
	public StoresExcelView() {
	}

	/**
	 * 
	 * @param file
	 * @param store
	 * @param user 
	 * @return 返回Map处理结果集合
	 * 			
	 */
	public UploadProductsResult importStores(InputStream inputStream, User user) {
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
        try {
			zh = languageService.getByCode("zh");
			cn = countryService.getByCode("CN");
			currency = currencyService.getByCode(Constants.CURRENCY_CHINA_YUAN);
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		
       
        // 循环工作表Sheet
        //book.getNumberOfSheets()，当前只处理第一个sheet内容
        for (int numSheet = 0; numSheet < 1; numSheet++) {
        	Sheet hssfSheet = book.getSheetAt(numSheet);
            if (hssfSheet == null || book.isSheetHidden(numSheet) == true) { //如果sheet不存在，或者隐藏，则不解析
                continue;
            }
            // 循环行Row，由于前面beginline为标题行等信息，所以不读取，row和cell数据都是从零开始读取
           
            int end = beginline + impMaxLine;
            for (int rowNum = beginline; (rowNum <= hssfSheet.getLastRowNum() && rowNum <= end) ; 
            		rowNum++) {
            	try{
            		Row hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow == null) {
                        continue;
                    } else {
                    	createMerchant(hssfRow);
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
	/**
	 * 商家唯一代码0	商家名称1	联系人2	座机电话3	手机4	 传真5	 商家邮箱地址6	商家地址7
	 * 商家所在城市8	商家所在国家9	商家所在省/直辖市10	商家邮编11	商家中文简介12	商家英文简介13
	 * @param hssfRow
	 * @throws ServiceException
	 */
	private void createMerchant(Row hssfRow) throws ServiceException {
		if (StringUtils.isNotBlank(getValue(hssfRow.getCell(0)))) {
			Date date = new Date(System.currentTimeMillis());
			List<Language> supportedLanguages = new ArrayList<Language>();
			supportedLanguages.add(zh);
			
			//create a merchant
			MerchantStore store = new MerchantStore();
			store.setCurrency(currency);
			store.setDefaultLanguage(zh);
			store.setInBusinessSince(date);
			
			store.setCode(getValue(hssfRow.getCell(0)));
			store.setStorename(getValue(hssfRow.getCell(1)));
			store.setStorecontacts(getValue(hssfRow.getCell(2)));
			store.setStorephone(getValue(hssfRow.getCell(3)));
			store.setStoremobile(getValue(hssfRow.getCell(4)));
			store.setStorefax(getValue(hssfRow.getCell(5)));
			store.setStoreEmailAddress(getValue(hssfRow.getCell(6)));
			store.setStoreaddress(getValue(hssfRow.getCell(7)));
			store.setStorecity(getValue(hssfRow.getCell(8)));
			if (StringUtils.isNotBlank(getValue(hssfRow.getCell(9)))) {
				List<Country> countries = countryService.listByName(getValue(hssfRow.getCell(9)));
				if (countries!=null && countries.size()>0) {
					store.setCountry(countries.get(0));
				}
			}
			if (StringUtils.isNotBlank(getValue(hssfRow.getCell(10)))) {
				List<Zone> zones = zoneService.listByName(getValue(hssfRow.getCell(10)));
				if (zones!=null&&zones.size()>0) {
					store.setZone(zones.get(0));
				}
			}
			
			store.setStorepostalcode(getValue(hssfRow.getCell(11)));
			store.setIntroduce(getValue(hssfRow.getCell(12)));
			store.setEnintroduce(getValue(hssfRow.getCell(13)));
			
			store.setDomainName(default_domainName);
			store.setStoreTemplate(default_template);
			
			store.setLanguages(supportedLanguages);
			
			merchantService.create(store);
			
			
			
		}
	}
    
}
