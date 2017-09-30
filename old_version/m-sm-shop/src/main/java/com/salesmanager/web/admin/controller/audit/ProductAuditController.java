package com.salesmanager.web.admin.controller.audit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.model.ProductList;
import com.salesmanager.core.business.catalog.product.model.certificate.ProductCertificate;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.proof.ProductProof;
import com.salesmanager.core.business.catalog.product.model.selfproof.ProductSelfproof;
import com.salesmanager.core.business.catalog.product.model.thirdproof.ProductThirdproof;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.common.model.BasedataType;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.content.model.BasedataTypeEnum;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.system.service.EmailService;
import com.salesmanager.core.modules.email.Email;
import com.salesmanager.core.utils.BaseDataUtils;
import com.salesmanager.core.utils.PropertiesUtils;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.constants.EmailConstants;
import com.salesmanager.web.entity.catalog.product.ProductsList;
import com.salesmanager.web.entity.catalog.product.Quality;
import com.salesmanager.web.entity.catalog.product.ReadableProduct;
import com.salesmanager.web.populator.catalog.ReadableProductPopulator;
import com.salesmanager.web.shop.model.paging.PaginationData;
import com.salesmanager.web.utils.DateUtil;
import com.salesmanager.web.utils.EmailUtils;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.LocaleUtils;
import com.salesmanager.web.utils.PageBuilderUtils;

@Controller
public class ProductAuditController {
	@Autowired
	ProductService productService;
	
	@Autowired
	private PricingService pricingService;
	
	@Autowired
	private LabelUtils messages;
	
	@Autowired
	private EmailService emailService;
	
	private int productShowPage=15;
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductAuditController.class);
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/admin/audit/auditManagement.html", method=RequestMethod.GET)
	public String displayProductEdit(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//return displayProduct(productId,model,request,response);
		setMenu(model,request,"audit");
		return "admin-audit";
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/admin/audit/auditcharge.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String auditcharge(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		Language language = (Language)request.getAttribute("LANGUAGE");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		String pid = request.getParameter("pid");
		if(pid != null && !StringUtils.isBlank(pid)){
			try{
				Locale customerLocale = LocaleUtils.getLocale(language);
				Product product = productService.getById(Long.parseLong(request.getParameter("pid")));
				
				Date start = DateUtil.getDate(request.getParameter("starDate"));
				Date end = DateUtil.getDate(request.getParameter("endDate"));
				product.setDateChargeBegin(start);
				product.setDateChargeEnd(end);
				AuditSection auditSection =	product.getAuditSection();
				auditSection.setAudit(com.salesmanager.core.constants.Constants.AUDIT_AUDITED);
				if(request.getParameter("score") !=null && !StringUtils.isBlank(request.getParameter("score"))){
					int score = Integer.parseInt(request.getParameter("score"));
					product.setQualityScore(score);
				}
				product.setAuditSection(auditSection);
				productService.saveOrUpdate(product);
				StringBuffer result= new StringBuffer();
				
				result.append(messages.getMessage("label.bettbio.product.charge",customerLocale)).append(",").append(messages.getMessage("label.bettbio.product.audit",customerLocale));
				result.append(messages.getMessage("label.bettbio.product.charge.perior",customerLocale)).append(request.getParameter("starDate")).append("-").append(request.getParameter("endDate"));
				this.sendOrderDownloadEmail(product, store, customerLocale, request.getContextPath(), result.toString(), null);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			}catch (Exception e){
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}else{
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		return resp.toJSONString();
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/admin/audit/auditquality.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String auditQuality(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		Language language = (Language)request.getAttribute("LANGUAGE");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		String pid = request.getParameter("pid");
		if(pid != null && !StringUtils.isBlank(pid)){
			try{
				Locale customerLocale = LocaleUtils.getLocale(language);
				Product product = productService.getById(Long.parseLong(request.getParameter("pid")));
				
				int score = Integer.parseInt(request.getParameter("score"));
				product.setQualityScore(score);
				int weight = Integer.parseInt(PropertiesUtils.getPropertiesValue("AUDIT"));				
				
				AuditSection auditSection =	product.getAuditSection();
				StringBuffer result= new StringBuffer();
				
				boolean isFree = product.getProductIsFree();
				if(isFree == true){
					if(score>=weight){
						auditSection.setAudit(com.salesmanager.core.constants.Constants.AUDIT_AUDITED);
						result.append(messages.getMessage("label.bettbio.product.after.audit",customerLocale));
						//审核通过
						resp.setStatus(1);
					}else{
						auditSection.setAudit(com.salesmanager.core.constants.Constants.AUDIT_FAILED);
						result.append(messages.getMessage("label.bettbio.product.failed.audit",customerLocale));
						resp.setStatus(0);
					}
				}else{
					auditSection.setAudit(com.salesmanager.core.constants.Constants.AUDIT_AUDITED);
					result.append(messages.getMessage("label.generic.post",customerLocale)).append(messages.getMessage("label.bettbio.product.audit",customerLocale));
					//收费产品的审核
					resp.setStatus(2);
				}
				
				String better = request.getParameter("less");
				if(better !=null && !StringUtils.isBlank(better)){
					StringBuffer modif = new StringBuffer();
					String [] bet = better.split(",");
					for(int i=0; i<bet.length;i++)modif.append(this.getQuality(bet[i], customerLocale)).append("   ");
					this.sendOrderDownloadEmail(product, store, customerLocale, request.getContextPath(), result.toString(), modif.toString());
				}else{
					this.sendOrderDownloadEmail(product, store, customerLocale, request.getContextPath(), result.toString(), null);
				}
				product.setAuditSection(auditSection);
				productService.saveOrUpdate(product);
				
			}catch (Exception e){
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}else{
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		return resp.toJSONString();
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/admin/audit/getquality.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody Quality getQuality(HttpServletRequest request, HttpServletResponse response) {
		
		String pid = request.getParameter("pid");
		if(pid != null && !StringUtils.isBlank(pid)){
			try{
				
				Product product = productService.getById(Long.parseLong(request.getParameter("pid")));
				Quality quality = new Quality();
				int socre =0;
				BasedataType basedataType=null;
				Set<ProductCertificate> productCertificates = product.getCertificates();
				if(productCertificates!=null  && productCertificates.size()>0){					
					int docQuality=0;
					for(ProductCertificate productCertificate : productCertificates){
						basedataType=productCertificate.getBasedataType();
						docQuality = BaseDataUtils.getProductQuality(docQuality, basedataType);						
					}
					docQuality = BaseDataUtils.addFirstProductQuality(docQuality,basedataType);
					socre +=docQuality;
					quality.setDoc(docQuality);
			
				}
				
				Set<ProductProof> productProofs = product.getProofs();
				if(productProofs !=null && productProofs.size()>0){					
					int proofQuality=0;
					for(ProductProof productProof:productProofs){
						basedataType = productProof.getBasedataType();
						proofQuality = BaseDataUtils.getProductQuality(proofQuality, basedataType);
					}
					proofQuality = BaseDataUtils.addFirstProductQuality(proofQuality, basedataType);
					socre += proofQuality;
					quality.setProof(proofQuality);
				}
				
				Set<ProductThirdproof> productThirdproofs = product.getThirdproofs();
				if(productThirdproofs !=null && productThirdproofs.size()>0){
					int proofQuality=0;
					for(ProductThirdproof productThirdproof:productThirdproofs){
						basedataType = productThirdproof.getBasedataType();
						proofQuality = BaseDataUtils.getProductQuality(proofQuality, basedataType);
					}
					proofQuality = BaseDataUtils.addFirstProductQuality(proofQuality, basedataType);
					socre += proofQuality;
					quality.setThird(proofQuality);
				}
				boolean isFree =product.getProductIsFree();
				int proofQuality=Integer.parseInt(PropertiesUtils.getPropertiesValue(BasedataTypeEnum.BTYPE_SELFPROOF.name()));
				Set<ProductSelfproof> productSelfproofs = product.getSelfProofs();
				if(productSelfproofs !=null && productSelfproofs.size()>0){					
					//int proofQuality=Integer.parseInt(PropertiesUtils.getPropertiesValue(BasedataTypeEnum.BTYPE_SELFPROOF.name()));
					socre +=proofQuality;
					quality.setSelf(proofQuality);
				}else{
					if(isFree == false){
						socre +=Integer.parseInt(PropertiesUtils.getPropertiesValue("BTYPE_SELFPROOF_NOFREE"));
						quality.setSelf(Integer.parseInt(PropertiesUtils.getPropertiesValue("BTYPE_SELFPROOF_NOFREE")));
					}
				}
				
				quality.setFree(isFree);
				quality.setScore(socre);
				return quality;
				/**
				if(socre>0){
					quality.setScore(socre);
					return quality;
				}*/
				
			}catch (Exception e){
				
			}
		}else{
			
		}
		return null;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/admin/audit/paging.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody ProductsList displayProduct(HttpServletRequest request, HttpServletResponse response) {
		int page =1;
		String strPage = request.getParameter("page");
		if(strPage !=null && !strPage.equalsIgnoreCase("1")){
			page = Integer.parseInt(strPage);
		}
		ProductCriteria criteria = new ProductCriteria();
		PaginationData paginaionData=createPaginaionData(page,this.productShowPage);
		
		
		String findName = request.getParameter("findname");
		criteria.setStartIndex((paginaionData.getOffset() -1));
		criteria.setMaxCount(paginaionData.getPageSize());
		
		try {
				//MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
				
				//查询为审核的
				criteria.setAudit("p.auditSection.audit="+com.salesmanager.core.constants.Constants.AUDIT_PRE+" OR p.auditSection.audit="+ com.salesmanager.core.constants.Constants.AUDIT_AGAIN);
								
				if(!StringUtils.isBlank(findName)){
					criteria.setFindName(findName);
				}
				criteria.setStoreId(-1l);
				
					Language language = (Language)request.getAttribute("LANGUAGE");
					ProductList productList = productService.listPsByCriteria(language, criteria);
					List<Product> plist = productList.getProducts();
					ProductsList pList = new ProductsList();
					if(plist!=null) {
						List <com.salesmanager.web.entity.catalog.product.Product> ps = new ArrayList<com.salesmanager.web.entity.catalog.product.Product>();
						for(Product product : plist) {
							
							com.salesmanager.web.entity.catalog.product.Product p = new com.salesmanager.web.entity.catalog.product.Product();
							
							ProductDescription description = product.getDescriptions().iterator().next();
							
							p.setAvailable(product.isAvailable());
							p.setCode(product.getCode());
							p.setId(product.getId());
							p.setProductName(description.getName());
							p.setProductEnName(StringUtils.trimToEmpty(description.getEnName()));
							p.setSku(product.getSku());
							p.setStoreName(product.getMerchantStore().getStorename());
							p.setId(product.getId());
							p.setAvailableDate(product.getDateAvailable()==null?null:DateUtil.formatDate(product.getDateAvailable(), "yyyy-MM-dd"));
							//p.setAudit(product.getAuditSection().getAudit());
							ps.add(p);
						}
						pList.setProducts(ps);
						pList.setTotalCount(ps.size());
						pList.setPaginationData(PageBuilderUtils.calculatePaginaionData(paginaionData,Constants.MAX_ORDERS_PAGE, productList.getTotalCount()));
						
						return pList;
					}
			}catch (Exception e) {
				LOGGER.error("When get products ..... " + e);
				
				return null;
			}
			
		
		
		//model.addAttribute("criteria",criteria);
		//this.setMenu(model, request, "customer-list");
	
		//return "admin-customers";
		return null;
		
	}
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="/admin/audit/audit.html", method=RequestMethod.GET)
	public String display(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		
			try{				
				Language language = (Language)request.getAttribute("LANGUAGE");
				MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
				Product product =null;
				if(request.getParameter("pid")!=null && !StringUtils.isBlank(request.getParameter("pid"))){
					product = productService.getById(Long.parseLong(request.getParameter("pid")));
				}else{
					ProductCriteria criteria = new ProductCriteria();
					criteria.setAudit("p.auditSection.audit="+com.salesmanager.core.constants.Constants.AUDIT_PRE+" OR p.auditSection.audit="+ com.salesmanager.core.constants.Constants.AUDIT_AGAIN);
					product = productService.getNextCriteria(language, criteria);
				}
					
				if(product==null) {
					return "admin-audit";
				}
				
				ReadableProductPopulator populator = new ReadableProductPopulator();
				populator.setPricingService(pricingService);
				
				ReadableProduct productProxy = populator.populate(product, new ReadableProduct(), store, language);
					
				model.addAttribute("product", productProxy);
			}catch (Exception e){
				
			}
			
		
			setMenu(model,request,"audit");
		return "admin-audit-audit";
	}
	
	protected PaginationData createPaginaionData( final int pageNumber, final int pageSize )
    {
        final PaginationData paginaionData = new PaginationData(pageSize,pageNumber);
       
        return paginaionData;
    }
	
private void setMenu(Model model, HttpServletRequest request, String activMenu) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("audit", "audit");
		activeMenus.put(activMenu, activMenu);
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("audit");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

/**
 * 
 * @param product
 * @param merchantStore
 * @param customerLocale
 * @param contextPath
 * @param result
 * @param better
 */
 

private void sendOrderDownloadEmail(Product product, MerchantStore merchantStore,Locale customerLocale, String contextPath,String result,String better) {
	   /** issue with putting that elsewhere **/ 
       LOGGER.info( "Sending download email to customer" );
       try {

           Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(contextPath, merchantStore, messages, customerLocale);
           templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
         //remove by cy used for add muti billing && delivery
           // templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getBilling().getFirstName());
           //templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME, customer.getBilling().getLastName());
           ProductDescription description = product.getDescriptions().iterator().next();
           templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, merchantStore.getStorename());
           String[] downloadMessage = {description.getName(), result};
           templateTokens.put(EmailConstants.PRODUCT_AUDIT, messages.getMessage("email.audit.text", downloadMessage, customerLocale));
           if(better !=null && !StringUtils.isBlank(better)){
        	   String[] betterMessage = {description.getName(), better};
        	   templateTokens.put(EmailConstants.PRODUCT_BETTER, messages.getMessage("email.audit.better", betterMessage, customerLocale));
           }else{
        	   templateTokens.put(EmailConstants.PRODUCT_BETTER, "");
           }
          
           String[] resultTitle = {description.getName()};
           
           Email email = new Email();
           email.setFrom(merchantStore.getStorename());
           email.setFromEmail(merchantStore.getStoreEmailAddress());
           email.setSubject(messages.getMessage("email.audit.title", resultTitle, customerLocale));
           email.setTo(product.getMerchantStore().getStoreEmailAddress());
           email.setTemplateName(EmailConstants.EMAIL_AUDIT_TPL);
           email.setTemplateTokens(templateTokens);

//           LOGGER.debug( "Sending email to {} with download info",product.getMerchantStore().getStoreEmailAddress());
           emailService.sendHtmlEmail(email);

       } catch (Exception e) {
           LOGGER.error("Error occured while sending order download email ",e);
       }
	
	}

	private String getQuality (String qua,Locale customerLocale){
		switch (qua) {
		case "doc":
			return (messages.getMessage("label.product.certificate",customerLocale));
		case "third":	
			return (messages.getMessage("label.product.thirdproof",customerLocale));
		case "proof":
			return (messages.getMessage("label.product.proof",customerLocale));
		case "self":
			return (messages.getMessage("label.product.selfproof",customerLocale));
		default:
			return "";
		}
		
	}
	
}
