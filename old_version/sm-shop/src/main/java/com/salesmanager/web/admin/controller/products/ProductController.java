package com.salesmanager.web.admin.controller.products;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.model.CategoryDescription;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.file.DigitalProduct;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.image.ProductImageDescription;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.catalog.product.model.type.ProductType;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.catalog.product.service.file.DigitalProductService;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.catalog.product.service.manufacturer.ManufacturerService;
import com.salesmanager.core.business.catalog.product.service.type.ProductTypeService;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.content.model.FileContentType;
import com.salesmanager.core.business.content.model.ImageContentFile;
import com.salesmanager.core.business.content.model.InputContentFile;
import com.salesmanager.core.business.merchant.authorization.model.Authorization;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.system.service.EmailService;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.business.user.service.UserService;
import com.salesmanager.core.constants.GlobalConstants;
import com.salesmanager.core.modules.email.Email;
import com.salesmanager.core.utils.CoreConfiguration;
import com.salesmanager.core.utils.ProductPriceUtils;
import com.salesmanager.core.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.utils.ajax.AjaxResponse;
import com.salesmanager.web.admin.entity.digital.ProductFiles;
import com.salesmanager.web.admin.entity.web.Menu;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.constants.EmailConstants;
import com.salesmanager.web.entity.shop.PinYin;
import com.salesmanager.web.utils.CategoryNode;
import com.salesmanager.web.utils.DateUtil;
import com.salesmanager.web.utils.DeleteAll;
import com.salesmanager.web.utils.EmailUtils;
import com.salesmanager.web.utils.LabelUtils;
import com.salesmanager.web.utils.LocaleUtils;
import com.salesmanager.web.utils.NumberUtils;
import com.salesmanager.web.utils.PinyinUtils;
import com.salesmanager.web.utils.RadomSixNumber;
import com.salesmanager.web.utils.UserUtils;

@Controller
public class ProductController {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
	

	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ManufacturerService manufacturerService;
	
	@Autowired
	private ProductTypeService productTypeService;
	
	@Autowired
	private ProductImageService productImageService;
	
	@Autowired
	private ProductPriceUtils priceUtil;
    
	@Autowired
	LabelUtils messages;
	
	@Autowired
	private CoreConfiguration configuration;
	@Autowired
	private CategoryNode categoryNode;
	
	@Autowired
	private EmailService emailService;
	@Autowired
	private DigitalProductService digitalProductService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	UserService userService;

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/editProduct.html", method=RequestMethod.GET)
	public String displayProductEdit(@RequestParam("id") long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayProduct(productId,model,request,response);

	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/createProduct.html", method=RequestMethod.GET)
	public String displayProductCreate(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayProduct(null,model,request,response);

	}
	
	
	
	private String displayProduct(Long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//display menu
		if(productId == null){
			setMenu(model,request,"catalogue-products-create");
		}else{
				setMenu(model,request,"catalogues");
		}
		
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		//admin has all products
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<ProductType> productTypes = productTypeService.list();
		
				
		List<Language> languages = store.getLanguages();
		if(languages==null || languages.size()==0){
			languages = new ArrayList<Language>();
			languages.add(store.getDefaultLanguage());
		}

		
		com.salesmanager.web.admin.entity.catalog.Product product = new com.salesmanager.web.admin.entity.catalog.Product();
		List<ProductDescription> descriptions = new ArrayList<ProductDescription>();
		
		String returnStr="admin-products-edit";

		if(productId!=null && productId!=0) {//edit mode

			Product dbProduct = productService.getById(productId);
			if(auth != null && request.isUserInRole("ADMIN")){
				if(dbProduct==null) {
					return "redirect:/admin/products/products.html";
				} 
			} else {
				if(dbProduct==null || dbProduct.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
					return "redirect:/admin/products/products.html";
				}
			}
			if(dbProduct.getCategoryId() !=null && !StringUtils.isBlank(dbProduct.getCategoryId())){
				Map <Long,Category> cmap = (Map <Long,Category>)request.getSession().getServletContext().getAttribute("categoryMap");
				Category c = cmap.get(Long.parseLong(dbProduct.getCategoryId()));
				returnStr = categoryNode.getReturnStri(c);		
			}
			product.setProduct(dbProduct);
			Set<ProductDescription> productDescriptions = dbProduct.getDescriptions();
			
			for(Language l : languages) {
				
				ProductDescription productDesc = null;
				for(ProductDescription desc : productDescriptions) {
					
					Language lang = desc.getLanguage();
					if(lang.getCode().equals(l.getCode())) {
						productDesc = desc;
					}

				}
				
				if(productDesc==null) {
					productDesc = new ProductDescription();
					productDesc.setLanguage(l);
				}

				descriptions.add(productDesc);
				
			}
			List<ProductImage>  imgList = new ArrayList<ProductImage>();
			for(ProductImage image : dbProduct.getImages()) {
				if(image.isDefaultImage()) {
					imgList.add(image);
				}
      
			}
			
			product.setImagesName(imgList);
			product.setImagesLength(product.getImagesName().size());
			
			ProductAvailability productAvailability = null;
			ProductPrice productPrice = null;
			
			Set<ProductAvailability> availabilities = dbProduct.getAvailabilities();
			if(availabilities!=null && availabilities.size()>0) {
				
				for(ProductAvailability availability : availabilities) {
					if(availability.getRegion().equals(com.salesmanager.core.constants.Constants.ALL_REGIONS)) {
						productAvailability = availability;
						Set<ProductPrice> prices = availability.getPrices();
						for(ProductPrice price : prices) {
							if(price.isDefaultPrice()) {
								productPrice = price;
								product.setProductPrice(priceUtil.getAdminFormatedAmount(store, productPrice.getProductPriceAmount()));
								product.setProductPriceStock(price.getProductPriceStockAmount());
							}
						}
					}
				}
			}
			
			if(productAvailability==null) {
				productAvailability = new ProductAvailability();
			}
			
			if(productPrice==null) {
				productPrice = new ProductPrice();
			}
			
			product.setAvailability(productAvailability);
			product.setPrice(productPrice);
			product.setDescriptions(descriptions);
			
			
			product.setDateAvailable(DateUtil.formatDate(dbProduct.getDateAvailable()));
			
			List<DigitalProduct> digitalProduct = digitalProductService.queryByDigitalProduct(dbProduct.getId());
			int size =0;
			if(digitalProduct==null){
				size=0;
			}else{
				size=digitalProduct.size();
			}
			model.addAttribute("size",size);
			model.addAttribute("digitalProduct", digitalProduct);

		} else {


			for(Language l : languages) {
				
				ProductDescription desc = new ProductDescription();
				desc.setLanguage(l);
				descriptions.add(desc);
				
			}
			
			Product prod = new Product();
			
			prod.setAvailable(true);
			
			ProductAvailability productAvailability = new ProductAvailability();
			ProductPrice price = new ProductPrice();
			product.setPrice(price);
			product.setAvailability(productAvailability);
			product.setProduct(prod);
			product.setDescriptions(descriptions);
			product.setDateAvailable(DateUtil.formatDate(new Date()));
			model.addAttribute("size",0);


		}
		
		Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer> manMap = (Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer>)request.getSession().getServletContext().getAttribute("manMap");
		if(product.getProduct() !=null && product.getProduct().getManufacturer() !=null){
			Manufacturer man = manMap.get(product.getProduct().getManufacturer().getId());
			model.addAttribute("manName",man.getDescription().getName());
		}
		
		model.addAttribute("product",product);
		
		model.addAttribute("productTypes", productTypes);
		
		return returnStr;
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/stores.html", method={RequestMethod.GET,RequestMethod.POST}, produces="application/json")
	public @ResponseBody List<PinYin> stores(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		try{
			List<Object[]> stores = manufacturerService.getStoreName();
			List<String> ch = new ArrayList<String>();
			ch.add("b");
			ch.add("g");
			ch.add("k");
			ch.add("n");
			ch.add("r");
			ch.add("s");
			ch.add("w");
			ch.add("z");
			List<PinYin> pinyin = PinyinUtils.getPinyinList(stores,ch);
			return pinyin;
		}catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		}
		
	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/save.html", method=RequestMethod.POST)
	public String saveProduct(@Valid @ModelAttribute("product") com.salesmanager.web.admin.entity.catalog.Product  product,@ModelAttribute(value="productFiles") @Valid final ProductFiles productFiles, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {
		//display menu
		setMenu(model,request,"catalogue-products-create");		
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		Map <Long,Category> cmap = (Map <Long,Category>)request.getSession().getServletContext().getAttribute("categoryMap");		
		boolean isnew =false;
		List<Language> languages = store.getLanguages();
		Date date = new Date();
		if(!StringUtils.isBlank(product.getDateAvailable())) {
			try {
				date = DateUtil.getDate(product.getDateAvailable());
				product.getAvailability().setProductDateAvailable(date);
				product.setDateAvailable(DateUtil.formatDate(date));
			} catch (Exception e) {
				ObjectError error = new ObjectError("dateAvailable",messages.getMessage("message.invalid.date", locale));
				result.addError(error);
			}
		}
		if(product.getProduct().getCategoryId()==null ||product.getProduct().getCategoryId()==""){
			ObjectError error = new ObjectError("dateAvailable",messages.getMessage("label.productedit.choosecategory", locale));
			result.addError(error);
		};
		
		if (result.hasErrors()) {
			
			Category c = cmap.get(Long.parseLong(product.getProduct().getCategoryId()));
			
			return categoryNode.getReturnStri(c);			
		}
		
		Product newProduct = product.getProduct();
		ProductAvailability newProductAvailability = null;
		AuditSection auditSection = null;
		Set<ProductAvailability> availabilities = new HashSet<ProductAvailability>();	
		//新增商品，则需要系统自动生成唯一编号sku和友好地址
		String sku = "";
		//更新产品
		List<ProductImage> listStr = new ArrayList<ProductImage>();
		if(product.getProduct().getId()!=null && product.getProduct().getId().longValue()>0) {
			setMenu(model,request,"catalogues");
			model.addAttribute("operator", "edit");
			//get actual product
			newProduct = productService.getById(product.getProduct().getId());
			//admin has all products
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth == null || !request.isUserInRole("ADMIN")){ //非admin角色，则需要判断是否是本店铺的商品
				if(newProduct!=null && newProduct.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
					return "redirect:/admin/products/products.html";
				} 
			} else {
				store = newProduct.getMerchantStore(); //获取原来商品所属的商品信息，防止admin用户修改商品商铺属性
			}
			
			//copy properties			
			sku = newProduct.getSku(); //获取初始的sku值，提供给后续seurl使用
			newProduct.setAvailable(product.getProduct().isAvailable());
			newProduct.setDateAvailable(date);
			auditSection = newProduct.getAuditSection();
			newProduct.setManufacturer(product.getProduct().getManufacturer());
			newProduct.setType(product.getProduct().getType());
			
			newProduct.setCode(product.getProduct().getCode());
			newProduct.setBatchnum(product.getProduct().getBatchnum());
			newProduct.setCas(product.getProduct().getCas());
			
			Set<ProductAvailability> avails = newProduct.getAvailabilities();
			if(avails !=null && avails.size()>0) {
				
				for(ProductAvailability availability : avails) {
					if(availability.getRegion().equals(com.salesmanager.core.constants.Constants.ALL_REGIONS)) {

						
						newProductAvailability = availability;						
					} else {
						availabilities.add(availability);
					}
				}
			}
			for(ProductImage image : newProduct.getImages()) {
				if(image.isDefaultImage()) {
					listStr.add(image);
				}
      
			}
		} else { //新增商品
			sku = NumberUtils.getNumByTime("a"+store.getCode().toLowerCase());
			newProduct.setSku(sku); //初始化sku
			model.addAttribute("operator", "new"); //新增产品操作
			auditSection = new AuditSection();
			newProduct.setAuditSection(auditSection);
			isnew=true;
		}
		
		newProduct.setServerPrice(product.getProduct().getServerPrice());
		
		newProduct.setMerchantStore(store);
		
		if(newProductAvailability==null) {
			newProductAvailability = new ProductAvailability();
		}
		

		newProductAvailability.setProductQuantity(product.getAvailability().getProductQuantity());
		newProductAvailability.setProductQuantityOrderMin(product.getAvailability().getProductQuantityOrderMin());
		newProductAvailability.setProductQuantityOrderMax(product.getAvailability().getProductQuantityOrderMax());
		newProductAvailability.setProduct(newProduct);
	
		availabilities.add(newProductAvailability);
			

			
		newProduct.setAvailabilities(availabilities);

		Set<ProductDescription> descriptions = new HashSet<ProductDescription>();
		if(product.getDescriptions()!=null && product.getDescriptions().size()>0) {
			
			for(ProductDescription description : product.getDescriptions()) {
				description.setMetatagTitle(description.getName()); //对于metatitle和metadescription的内容，统一用产品的名称替代。modify by sam 20150905
				description.setMetatagDescription(description.getName()); //对于metatitle和metadescription的内容，统一用产品的名称替代。modify by sam 20150905
				description.setSeUrl(sku);
				description.setEnName(description.getEnName());
				description.setProduct(newProduct);
				descriptions.add(description);
			}
		}
		
		newProduct.setDescriptions(descriptions);
		product.setDateAvailable(DateUtil.formatDate(date));

		//update product category
		String cateId = product.getProduct().getCategoryId();		
		if (StringUtils.isNotBlank(cateId)) {
			Category cate = categoryService.getById(Long.valueOf(cateId));
			Set<Category> set = new HashSet<Category>(); // 1 vs 1 for product and category
			set.add(cate);
			newProduct.setCategories(set);
			product.getProduct().setCategoryName(cate.getDescription().getName());	
		}

		//判断产品是否已经审核过，如果未审核过，则可以修改产品是否收费的状态
		if (newProduct.getAuditSection().getAudit()<1) {
			//判断是否是收费产品，还是免费产品，如果是免费产品，则需要将付费时间清空
			if(product.getProduct().getProductIsFree()) {
				newProduct.setDateChargeBegin(null);
				newProduct.setDateChargeEnd(null);
				newProduct.setProductIsFree(true);
			} else {
				newProduct.setDateChargeBegin(product.getProduct().getDateChargeBegin());
				newProduct.setDateChargeEnd(product.getProduct().getDateChargeEnd());
				newProduct.setProductIsFree(false);
				
			}
		}	
		String code = request.getParameter("searchstorename");
		if(code!= null && !StringUtils.isBlank(code)){
			Manufacturer man = product.getProduct().getManufacturer();
			List<Manufacturer> dbMans = manufacturerService.getListByName(code.trim());
			if(dbMans ==null || dbMans.size()==0){
				Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer> manMap = (Map <Long,com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer>)request.getSession().getServletContext().getAttribute("manMap");
				Set<ManufacturerDescription> mandescriptions = new HashSet<ManufacturerDescription>();
				man = new Manufacturer();
				//manufacturer.setManufacturer( man );
				
				for(Language l : languages) {// for each store language
					
					ManufacturerDescription manufacturerDesc = new ManufacturerDescription();
					manufacturerDesc.setName(code);					
					manufacturerDesc.setLanguage(l);
					mandescriptions.add(manufacturerDesc);
					manufacturerDesc.setManufacturer(man);
					man.setDescriptions(mandescriptions);
					
				}
				man.setMerchantStore(newProduct.getMerchantStore());				
				manufacturerService.saveOrUpdate(man,manMap);
			}else{
				for(int i=0;i<dbMans.size();i++){
			    	man =dbMans.get(i);
				}
			}
			
			newProduct.setManufacturer(man);
			//com.salesmanager.web.admin.entity.catalog.Manufacturer manufacturer = new com.salesmanager.web.admin.entity.catalog.Manufacturer();		
			//前台的显示
			model.addAttribute("manName",man.getDescription().getName());
			
		}else{
			newProduct.setManufacturer(null);//委婉提示
		}
		List<MultipartFile> mfList =product.getImage();
		List<ProductImageDescription> imagesDescriptions = new ArrayList<ProductImageDescription>();
		for(int i=0;i<mfList.size();i++){
			 
		    MultipartFile mf =mfList.get(i);
		    
		    String imageName= mf.getOriginalFilename();
		   
			if(imageName!=null && imageName!=""){
				ProductImage productImage= new ProductImage();
			    
				productImage.setDefaultImage(true);	
				
				ProductImageDescription imageDescription = new ProductImageDescription();
				
				productImage.setProductImage(RadomSixNumber.getImageName(imageName));
				
				productImage.setImage(mf.getInputStream());
				
				if(i==1)
				{
					for(Language l : languages) {
						imageDescription.setName(imageName);
						imageDescription.setLanguage(l);
						imageDescription.setProductImage(productImage);
						imagesDescriptions.add(imageDescription);
			
					 }
				}
				productImage.setDescriptions(imagesDescriptions);
				
				productImage.setProduct(newProduct);
				
				listStr.add(productImage);
			}
		    
			
		 }
	     
		 product.setImagesName(listStr);
		 product.setImagesLength(product.getImagesName().size());
		 newProduct.getImages().addAll(product.getImagesName());
		//判断是否已经添加过授权(对已经授权的不进行加分)
		 	List<Long> manid = new ArrayList<Long>();
		 	if (newProduct.getManufacturer() != null) {
		 		manid.add(newProduct.getManufacturer().getId());
		 	}else{
		 		LOGGER.warn("newProduct manufacture is null: "+newProduct.getId());
		 	}
		    /*
		     * 此段由Clariones注释掉。 2016-11-18. 因为对应后台代码十分诡异，硬编码DB Connection
		     * 而且下面也没有用到。待研究业务逻辑后再看如何修改
		     * List<Authorization> authList = productService.getAuthorizationbyMerchantId(manid,store.getId());
		    int auth_type_1 = 0;//厂家认证
		    int auth_type_2 = 0;//代理认证
		    for(int i=0;i<authList.size();i++){
		    	if(1==authList.get(i).getAuth_type()){
		    		auth_type_1 = 20;
		    	}else if(2==authList.get(i).getAuth_type()){
		    		auth_type_2 = 20;
		    	}
		    }*/
		// newProduct.setQualityScore(auth_type_1+auth_type_2);(有问题)
		 productService.saveOrUpdate(newProduct); 
		//判定该产品品牌是否有授权
		/*if(newProduct.getManufacturer()!=null){
			if(newProduct.getManufacturer().getDescriptions()!=null){
				Integer count = manufacturerService.queryByAuthorization(newProduct.getManufacturer().getId());
				if(count>0){
				List<Long> manid = new ArrayList<Long>();
				manid.add(newProduct.getManufacturer().getId());
				List<com.salesmanager.core.business.catalog.product.model.Product> proList =
						productService.getProductsByMan(manid,store.getId());
				//质量分数
				for (com.salesmanager.core.business.catalog.product.model.Product pro : proList) {
					com.salesmanager.core.business.catalog.product.model.Product p = new com.salesmanager.core.business.catalog.product.model.Product();
					p.setQualityScore(pro.getQualityScore()+20);
					pro.setQualityScore(p.getQualityScore());
					//最高分
					if (pro.getQualityScore() > 99) {
						pro.setQualityScore(99);
					}
					productService.update(pro);
				 }
				
				}
			}
		}*/
		//附件上传
		List<DigitalProduct>  objList = this.saveFile(newProduct,productFiles, result, model, request);
		//send mail to server
		if(isnew==true){
			int send= this.sendNewMail((Language)request.getAttribute("LANGUAGE"), newProduct, request.getContextPath());
			if(send!=0){
				LOGGER.error("Error while send mail to server fail" );
			}			
		}
		
		int size =0;
		if(objList==null){
			size=0;
		}else{
			size=objList.size();
		}
		model.addAttribute("size",size);
		model.addAttribute("digitalProduct", objList);
		model.addAttribute("success","success");
		Category c = cmap.get(Long.parseLong(newProduct.getCategoryId()));	
		return categoryNode.getReturnStri(c);
		
	}
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/audit.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String auditcharge(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();
		
		String pid = request.getParameter("pid");
		if(pid != null && !StringUtils.isBlank(pid)){
			try{
				
				Product product = productService.getById(Long.parseLong(request.getParameter("pid")));
				AuditSection auditSection =	product.getAuditSection();
				if(auditSection.getAudit()>0){
					auditSection.setAudit(com.salesmanager.core.constants.Constants.AUDIT_AGAIN);	
				}else{
					auditSection.setAudit(com.salesmanager.core.constants.Constants.AUDIT_PRE);	
				}
				
				product.setAuditSection(auditSection);
				productService.saveOrUpdate(product);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			}catch (Exception e){
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}
		}else{
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		return resp.toJSONString();
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/pcategorys.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody com.salesmanager.web.admin.entity.catalog.Category getCategorys(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			com.salesmanager.web.admin.entity.catalog.Category root = categoryNode.getRootReagent(request);
			root.setCategoryID(-1l);
			root.setCategoryName("root");
			return root;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("Error while paging category", e);
		}
		return null;
	}
	/**
	 * Creates a duplicate product with the same inner object graph
	 * Will ignore SKU, reviews and images
	 * @param id
	 * @param result
	 * @param model
	 * @param request
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/product/duplicate.html", method=RequestMethod.POST)
	public String duplicateProduct(@ModelAttribute("productId") Long  id, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {
		

		//display menu
		setMenu(model,request,"");
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
//		List<Manufacturer> manufacturers = manufacturerService.listByStore(store, language);
		List<Manufacturer> manufacturers = manufacturerService.list();
		List<ProductType> productTypes = productTypeService.list();


		model.addAttribute("manufacturers", manufacturers);
		model.addAttribute("productTypes", productTypes);

		
		Product dbProduct = productService.getById(id);
		Product newProduct = new Product();

		//admin has all products
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !request.isUserInRole("ADMIN")){ //非admin角色，则需要判断是否是本店铺的商品
			if(dbProduct!=null && dbProduct.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				return "redirect:/admin/products/products.html";
			} 
		} else {
			store = dbProduct.getMerchantStore(); //获取原来商品所属的商品信息，防止admin用户修改商品商铺属性
		}
		//新增商品，则需要系统自动生成唯一编号sku和友好地址
		String sku = NumberUtils.getNumByTime("a"+store.getCode().toLowerCase());
		//Make a copy of the product
		com.salesmanager.web.admin.entity.catalog.Product product = new com.salesmanager.web.admin.entity.catalog.Product();
		
		Set<ProductAvailability> availabilities = new HashSet<ProductAvailability>();
		//availability - price
		for(ProductAvailability pAvailability : dbProduct.getAvailabilities()) {
			
			ProductAvailability availability = new ProductAvailability();
			availability.setProductDateAvailable(pAvailability.getProductDateAvailable());
			availability.setProductIsAlwaysFreeShipping(pAvailability.getProductIsAlwaysFreeShipping());
			
			availability.setProductQuantity(pAvailability.getProductQuantity());
			availability.setProductQuantityOrderMax(pAvailability.getProductQuantityOrderMax());
			availability.setProductQuantityOrderMin(pAvailability.getProductQuantityOrderMin());
			
			availability.setProductStatus(pAvailability.getProductStatus());
			availability.setRegion(pAvailability.getRegion());
			availability.setRegionVariant(pAvailability.getRegionVariant());
			
			Set<ProductPrice> prices = pAvailability.getPrices();
			for(ProductPrice pPrice : prices) {
				
				ProductPrice price = new ProductPrice();
				price.setDefaultPrice(pPrice.isDefaultPrice());
				price.setProductPriceAmount(pPrice.getProductPriceAmount());
				price.setProductAvailability(availability);
				price.setProductPriceSpecialAmount(pPrice.getProductPriceSpecialAmount());
				price.setProductPriceSpecialEndDate(pPrice.getProductPriceSpecialEndDate());
				price.setProductPriceSpecialStartDate(pPrice.getProductPriceSpecialStartDate());
				price.setProductPriceType(pPrice.getProductPriceType());
				price.setProductPriceStockAmount(pPrice.getProductPriceStockAmount());
				price.setProductPricePeriod(pPrice.getProductPricePeriod());
				
				Set<ProductPriceDescription> priceDescriptions = new HashSet<ProductPriceDescription>();
				//price descriptions
				for(ProductPriceDescription pPriceDescription : pPrice.getDescriptions()) {
					
					ProductPriceDescription productPriceDescription = new ProductPriceDescription();
					productPriceDescription.setAuditSection(pPriceDescription.getAuditSection());
					productPriceDescription.setDescription(pPriceDescription.getDescription());
					productPriceDescription.setName(pPriceDescription.getName());
					productPriceDescription.setLanguage(pPriceDescription.getLanguage());
					productPriceDescription.setProductPrice(price);
					priceDescriptions.add(productPriceDescription);
					
				}
				price.setDescriptions(priceDescriptions);
				if(price.isDefaultPrice()) {
					product.setPrice(price);
					product.setProductPrice(priceUtil.getAdminFormatedAmount(store, price.getProductPriceAmount()));
				}
				
				availability.getPrices().add(price);
			}
			
			

			if(availability.getRegion().equals(com.salesmanager.core.constants.Constants.ALL_REGIONS)) {
				product.setAvailability(availability);
			}
			
			availabilities.add(availability);
		}
		
		newProduct.setAvailabilities(availabilities);
		
		
		
		//attributes
		Set<ProductAttribute> attributes = new HashSet<ProductAttribute>();
		for(ProductAttribute pAttribute : dbProduct.getAttributes()) {
			
			ProductAttribute attribute = new ProductAttribute();
			attribute.setAttributeDefault(pAttribute.getAttributeDefault());
			attribute.setAttributeDiscounted(pAttribute.getAttributeDiscounted());
			attribute.setAttributeDisplayOnly(pAttribute.getAttributeDisplayOnly());
			attribute.setAttributeRequired(pAttribute.getAttributeRequired());
			attribute.setProductAttributePrice(pAttribute.getProductAttributePrice());
			attribute.setProductAttributeIsFree(pAttribute.getProductAttributeIsFree());
			attribute.setProductAttributeWeight(pAttribute.getProductAttributeWeight());
			attribute.setProductOption(pAttribute.getProductOption());
			attribute.setProductOptionSortOrder(pAttribute.getProductOptionSortOrder());
			attribute.setProductOptionValue(pAttribute.getProductOptionValue());
			attributes.add(attribute);
						
		}
		newProduct.setAttributes(attributes);
		
		//relationships
		Set<ProductRelationship> relationships = new HashSet<ProductRelationship>();
		for(ProductRelationship pRelationship : dbProduct.getRelationships()) {
			
			ProductRelationship relationship = new ProductRelationship();
			relationship.setActive(pRelationship.isActive());
			relationship.setCode(pRelationship.getCode());
			relationship.setRelatedProduct(pRelationship.getRelatedProduct());
			relationship.setStore(store);
			relationships.add(relationship);

		}
		
		newProduct.setRelationships(relationships);
		
		//product description
		Set<ProductDescription> descsset = new HashSet<ProductDescription>();
		List<ProductDescription> desclist = new ArrayList<ProductDescription>();
		Set<ProductDescription> descriptions = dbProduct.getDescriptions();
		for(ProductDescription pDescription : descriptions) {
			
			ProductDescription description = new ProductDescription();
			description.setAuditSection(pDescription.getAuditSection());
			description.setName(pDescription.getName());
			description.setEnName(pDescription.getEnName());
			description.setDescription(pDescription.getDescription());
			description.setLanguage(pDescription.getLanguage());
			description.setMetatagDescription(pDescription.getMetatagDescription());
			description.setMetatagKeywords(pDescription.getMetatagKeywords());
			description.setMetatagTitle(pDescription.getMetatagTitle());
			description.setTestDescription(pDescription.getTestDescription());
			description.setSimpleDescription(pDescription.getSimpleDescription());
			description.setStorecondDescription(pDescription.getStorecondDescription());
			description.setMethodDescription(pDescription.getMethodDescription());
			description.setSeUrl(sku);
			descsset.add(description);
			desclist.add(description);
		}
		newProduct.setDescriptions(descsset);
		product.setDescriptions(desclist);
		
		AuditSection auditSection = new AuditSection();
		//auditSection.set;
		//product
		newProduct.setAuditSection(auditSection);
		newProduct.setAvailable(dbProduct.isAvailable());
		
		

		//copy
		//newProduct.setCategories(dbProduct.getCategories());
		Set<Category> categories = dbProduct.getCategories();
		for(Category category : categories) {
			newProduct.getCategories().add(category);
			//productService.update(newProduct);
		}
		newProduct.setDateAvailable(dbProduct.getDateAvailable());
		newProduct.setManufacturer(dbProduct.getManufacturer());
		newProduct.setMerchantStore(store);
	
		newProduct.setProductIsFree(dbProduct.getProductIsFree());
		newProduct.setDateChargeBegin(dbProduct.getDateChargeBegin());
		newProduct.setDateChargeEnd(dbProduct.getDateChargeEnd());
	
		newProduct.setProductOrdered(dbProduct.getProductOrdered());
	

		newProduct.setType(dbProduct.getType());
	
		newProduct.setSku(sku);
		newProduct.setCode(dbProduct.getCode());
		newProduct.setBatchnum(dbProduct.getBatchnum());
		newProduct.setCas(dbProduct.getCas());		
		
		productService.saveOrUpdate(newProduct);
		
		
		
		product.setProduct(newProduct);
		model.addAttribute("product", product);
		model.addAttribute("success","success");
		
		return "redirect:/admin/products/editProduct.html?id=" + newProduct.getId();
	}

	
	/**
	 * Removes a product image based on the productimage id
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 */
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/product/removeImage.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String removeImage(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String iid = request.getParameter("imageId");

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		AjaxResponse resp = new AjaxResponse();
		
		try {
			
			Long id = Long.parseLong(iid);
			ProductImage productImage = productImageService.getById(id);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth == null || !request.isUserInRole("ADMIN")){
				if(productImage.getProduct().getMerchantStore().getId().intValue()!=store.getId().intValue()) {
					resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);	
					return resp.toJSONString();
				}
			}
			if(productImage==null) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				
			} else {
				
				productImageService.removeProductImage(productImage);
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
				
			}
		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting product", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
	}
	
	
	/**
	 * List all categories and let the merchant associate the product to a category
	 * @param productId
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/displayProductToCategories.html", method=RequestMethod.GET)
	public String displayAddProductToCategories(@RequestParam("id") long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		
		setMenu(model,request,"catalogues");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		
		//get the product and validate it belongs to the current merchant
		Product product = productService.getById(productId);
		
		if(product==null) {
			return "redirect:/admin/products/products.html";
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !request.isUserInRole("ADMIN")){
			if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				return "redirect:/admin/products/products.html";
			}
		}

		//get parent categories
		//List<Category> categories = categoryService.listByStore(store,language);
		//set all product
		List<Category> categories = categoryService.getBySeUrl(language);
		
		model.addAttribute("product", product);
		model.addAttribute("categories", categories);
		return "catalogue-product-categories";
		
	}
	
	/**
	 * List all categories associated to a Product
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/product-categories/paging.html", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	public @ResponseBody String pageProductCategories(HttpServletRequest request, HttpServletResponse response) {

		String sProductId = request.getParameter("productId");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		
		AjaxResponse resp = new AjaxResponse();
		
		Long productId;
		Product product = null;
		
		try {
			productId = Long.parseLong(sProductId);
		} catch (Exception e) {
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString("Product id is not valid");
			String returnString = resp.toJSONString();
			return returnString;
		}

		
		try {

			product = productService.getById(productId);

			
			if(product==null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("Product id is not valid");
				String returnString = resp.toJSONString();
				return returnString;
			}
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth == null || !request.isUserInRole("ADMIN")){
				if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
					resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
					resp.setErrorString("Product id is not valid");
					String returnString = resp.toJSONString();
					return returnString;
				}
			}
			
			Language language = (Language)request.getAttribute("LANGUAGE");

			
			Set<Category> categories = product.getCategories();
			

			for(Category category : categories) {
				Map entry = new HashMap();
				entry.put("categoryId", category.getId());
				
				List<CategoryDescription> descriptions = category.getDescriptions();
				String categoryName = category.getDescriptions().get(0).getName();
				for(CategoryDescription description : descriptions){
					if(description.getLanguage().getCode().equals(language.getCode())) {
						categoryName = description.getName();
					}
				}
				entry.put("name", categoryName);
				resp.addDataEntry(entry);
			}

			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);
		
		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return returnString;


	}
	
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/product-categories/remove.html", method=RequestMethod.POST, produces="application/json")
	public @ResponseBody String deleteProductFromCategory(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sCategoryid = request.getParameter("categoryId");
		String sProductId = request.getParameter("productId");
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			Long categoryId = Long.parseLong(sCategoryid);
			Long productId = Long.parseLong(sProductId);
			
			Category category = categoryService.getById(categoryId);
			Product product = productService.getById(productId);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			if(category==null) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp.toJSONString();
			} 
			
			if(product==null) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				return resp.toJSONString();
			} 
			if(auth == null || !request.isUserInRole("ADMIN")){
				if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
					resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
					return resp.toJSONString();
				}
			}
			
			product.getCategories().remove(category);
			//当对商品分类进行修改的时候，也需要更新index库信息
//			productService.update(product);	
			productService.saveOrUpdate(product);
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting category", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		
		return returnString;
	}
	

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/addProductToCategories.html", method=RequestMethod.POST)
	public String addProductToCategory(@RequestParam("productId") long productId, @RequestParam("id") long categoryId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request,"");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		
		//get the product and validate it belongs to the current merchant
		Product product = productService.getById(productId);
		
		if(product==null) {
			return "redirect:/admin/products/products.html";
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !request.isUserInRole("ADMIN")){
			if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				return "redirect:/admin/products/products.html";
			}
		}
		

		//get parent categories
		//List<Category> categories = categoryService.listByStore(store,language);
		//set all product		
		List<Category> categories = categoryService.getBySeUrl(language);
				
		Category category = categoryService.getById(categoryId);
		
		if(category==null) {
			return "redirect:/admin/products/products.html";
		}
		
		//set all product
		/**if(category.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
			return "redirect:/admin/products/products.html";
		}*/
		
		product.getCategories().add(category);
		
		//当对商品分类进行修改的时候，也需要更新index库信息
//		productService.update(product);	
		productService.saveOrUpdate(product);
		
		model.addAttribute("product", product);
		model.addAttribute("categories", categories);
		
		return "catalogue-product-categories";
		
	}

	private void setMenu(Model model, HttpServletRequest request,String activeMenu) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue-products", "catalogue-products");
		activeMenus.put(activeMenu, activeMenu);
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue-products");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	
	private int sendNewMail(Language userLanguage,Product product,String contextPath){
		try{
						
			Locale customerLocale = LocaleUtils.getLocale(userLanguage);
			Map<String, String> templateTokens = EmailUtils.createEmailObjectsMap(contextPath, product.getMerchantStore(), messages, customerLocale);
			templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
	        templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, "BettBio");
	       // String[] storeEmail = {store.getStoreEmailAddress()};
	        templateTokens.put(EmailConstants.EMAIL_NOTIFICATION_MESSAGE, "product sku:  "+product.getSku()+"\n product name:  "+product.getProductDescription().getName());
			Email email = new Email();
			email.setFrom(product.getMerchantStore().getStorename());
			email.setFromEmail(GlobalConstants.EMAIL_SERVICE);
			email.setSubject("new product create from  "+product.getMerchantStore().getStorename());
			// 2017-3-3 new requirement: 1.新增商品，发邮件给商家，抄送server<server@bettbio.com>，wbfang@bettbio.com，zgzhang@bettbio.com
			//email.setTo("pingmao@bettbio.com");
			email.setTo(product.getMerchantStore().getStoreEmailAddress());
			email.setTemplateName(EmailConstants.EMAIL_NEW_PRODUCT_TMPL);
			email.setTemplateTokens(templateTokens);
			emailService.sendHtmlEmail(email);
			return 0;
		}catch (Exception e) {
			LOGGER.error("Cannot send email to user",e);
			return -1;
		}
	}
	/**
	 * 上传附件
	 * */
	  @RequestMapping(value="/shop/customer/upload.html",method=RequestMethod.POST)
      public List<DigitalProduct> saveFile(Product product,@ModelAttribute(value="productFiles") @Valid final ProductFiles productFiles, final BindingResult bindingResult,final Model model, final HttpServletRequest request) throws Exception{
	    
    	this.setMenu(model, request);
  		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
  		List<DigitalProduct> paramList = new ArrayList<DigitalProduct>();
  		
  		//DigitalProduct digitalProduct = new DigitalProduct();
  		if(product==null) {
  			return null;
  		}
  		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
  		if(auth == null || !request.isUserInRole("ADMIN")){
  			if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
  				return null;
  			}
  		}
  		
  		
  	    
  		if (bindingResult.hasErrors()) {
  	        LOGGER.info( "Found {} Validation errors", bindingResult.getErrorCount());
  	        //return ControllerConstants.Tiles.Product.digitalProduct;
          }

  	    
  	    final List<InputContentFile> contentFilesList=new ArrayList<InputContentFile>();
          if(CollectionUtils.isNotEmpty( productFiles.getFile() )){
             // LOGGER.info("Saving {} product files for merchant {}",productFiles.getFile().size(),store.getId());
              for(final MultipartFile multipartFile:productFiles.getFile()){
            	  System.out.println(""+multipartFile.getBytes()+"");
                  if(!multipartFile.isEmpty()){
                      ByteArrayInputStream inputStream = new ByteArrayInputStream( multipartFile.getBytes() );
                      InputContentFile cmsContentImage = new InputContentFile();
                      cmsContentImage.setFileName(multipartFile.getOriginalFilename() );
                      cmsContentImage.setFileContentType( FileContentType.PRODUCT_DIGITAL );
                      cmsContentImage.setFile( inputStream );
                      contentFilesList.add( cmsContentImage);
                  }
              }
              
             
              if(CollectionUtils.isNotEmpty( contentFilesList )){
            	  
            	  for(int i=0; i<contentFilesList.size();i++){
            		  DigitalProduct digitalProduct = new DigitalProduct();
            		  digitalProduct.setProductFileName(contentFilesList.get(i).getFileName());
                      digitalProductService.addProductFile(product, digitalProduct, contentFilesList.get(i));
            	  }

              	//refresh digital product
            	  paramList =  digitalProductService.queryByDigitalProduct(product.getId()) ;
     
              }
          }
         
          return paramList;
      }
      /**
       * 删除
       * */
  		@PreAuthorize("hasRole('PRODUCTS')")
  		@RequestMapping(value="/admin/products/product/removeFile.html", method=RequestMethod.POST, produces="application/json")
      public @ResponseBody String removeFile(@RequestParam("fileId") long fileId, HttpServletRequest request, HttpServletResponse response, Locale locale) {

  		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
  		
  		AjaxResponse resp = new AjaxResponse();

  		
  		try {
  			
  			DigitalProduct digitalProduct = digitalProductService.getById(fileId);
  			
  			//validate store
  			if(digitalProduct==null) {
  				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
  				return resp.toJSONString();
  			}
  			
  			Product product = digitalProduct.getProduct();
  			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
  			if(auth == null || !request.isUserInRole("ADMIN")){
  				if(product.getMerchantStore().getId().intValue()!= store.getId().intValue()) {
  					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
  					return resp.toJSONString();
  				}
  			}
  			
  			digitalProductService.delete(digitalProduct);
  			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
  		
  		
  		} catch (Exception e) {
  			LOGGER.error("Error while deleting product", e);
  			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
  			resp.setErrorMessage(e);
  		}
  		
  		String returnString = resp.toJSONString();
  		
  		return returnString;
  	}
    
        private void setMenu(Model model, HttpServletRequest request) throws Exception {
    		
    		//display menu
    		Map<String,String> activeMenus = new HashMap<String,String>();
    		activeMenus.put("catalogue-products", "catalogue-products");
    		activeMenus.put("catalogues", "catalogues");
    		
    		@SuppressWarnings("unchecked")
    		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
    		
    		Menu currentMenu = (Menu)menus.get("catalogue-products");
    		model.addAttribute("currentMenu",currentMenu);
    		model.addAttribute("activeMenus",activeMenus);
    		//
    		
    	}
     /**
      * 根据产品新增时间段删除产品信息
      * */
     @RequestMapping(value="/admin/products/deletePro.html",method=RequestMethod.POST,produces="application/json")
     public @ResponseBody String deleteByProdu(HttpServletResponse response,HttpServletRequest request,Locale locale)
     {
    	 AjaxResponse resp = new AjaxResponse();
    	 try{
    		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	         Date  beginTime = format.parse(request.getParameter("beginTime").trim());
	         String endTime = request.getParameter("endTime").trim()==""?null:request.getParameter("endTime").trim();
	         List<BigInteger> proList = null;
	         if(endTime ==null  && beginTime != null)
	         {
	        	 proList = productService.queryProductById(beginTime);
	        	 Long id = Long.parseLong(proList.get(0).toString());
					Product product = productService.getById(id);
					//get store information
					MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
					if(!product.getMerchantStore().getCode().equalsIgnoreCase(store.getCode())){
						User user = userService.getByUserName(request.getRemoteUser());					
						if(!UserUtils.userInGroup(user, Constants.GROUP_SUPERADMIN)) {
							resp.setStatus(1);
							return resp.toJSONString();
						}
					}else{
					   productService.delete(product);
					}
					resp.setStatus(0);
	         }
	         else
	         {
	        	proList = productService.queryProductById(beginTime,format.parse(endTime));
	     		DeleteAll delteAll = new DeleteAll();
	     		
	     		try{
	     			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);	
	     			delteAll.setStore(store);
	     			User user = userService.getByUserName(request.getRemoteUser());
	     			delteAll.setUser(user);
	     			delteAll.setService(productService);
	     			delteAll.setResp(resp);
	     			String [] array = new String[proList.size()];
	     			if(proList !=null && proList.size()>0){
	     				for(int i=0;i<array.length;i++){
	     		    		Product product = productService.getById(Long.parseLong(proList.get(i)+""));
	     		    		array[i]+=product.getId();
	     				}
	     				if(array !=null && array.length>0){
	     					delteAll.setSid(array);
	     					resp = delteAll.call();
	     				}
	     			}else{
	     				Criteria criteria = new Criteria();
	     				//admin has all products
	     				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	     				
	     				if(auth != null &&
	     		        		 request.isUserInRole("ADMIN")){
	     					criteria.setStoreId(-1l);
	     				}else{
	     					criteria.setStoreId(store.getId());
	     				}
	     				delteAll.setCriteria(criteria);
	     				delteAll.setRequest(request);
	     				resp = delteAll.call();
	     			}
	     			resp.setStatus(0);
	     		}catch (Exception e){
	     			resp.setStatus(0);
	     		}
	     		
	         }
	         /*Integer count =proList.size();
	    	 if(count > 0 && proList != null)
	    	 {
	    		 Integer con =0;
	    		 if(count == 1){
	    			 con = 1;
	    		 }
	    		 else if(count>1)
	    		 {
	    			 con = proList.get(1) - proList.get(0);
	    		 }
	    		
	    		 for(int i = 0;i < con ;i++)
	    		 {
	    			  Product product = productService.getById(Long.parseLong(proList.get(i)+""));
	    			  productService.delete(product);
	    		 }
	    		 resp.setStatus(0);
	    		 
	    	 }
	    	 else
	    	 {
	    		 resp.setStatus(1);
	    	 }
	    	 */
    	 }catch(Exception e){
    		 e.printStackTrace();
    		 resp.setStatus(1);
    	 }
    	 return resp.toJSONString();
     }
	
}
