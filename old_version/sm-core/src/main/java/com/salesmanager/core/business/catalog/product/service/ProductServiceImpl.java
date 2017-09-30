package com.salesmanager.core.business.catalog.product.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.category.service.CategoryService;
import com.salesmanager.core.business.catalog.common.CatalogServiceHelper;
import com.salesmanager.core.business.catalog.product.dao.ProductDao;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.ProductCriteria;
import com.salesmanager.core.business.catalog.product.model.ProductList;
import com.salesmanager.core.business.catalog.product.model.attribute.ProductAttribute;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.certificate.ProductCertificate;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.proof.ProductProof;
import com.salesmanager.core.business.catalog.product.model.relationship.ProductRelationship;
import com.salesmanager.core.business.catalog.product.model.review.ProductReview;
import com.salesmanager.core.business.catalog.product.model.selfproof.ProductSelfproof;
import com.salesmanager.core.business.catalog.product.model.thirdproof.ProductThirdproof;
import com.salesmanager.core.business.catalog.product.service.attribute.ProductAttributeService;
import com.salesmanager.core.business.catalog.product.service.availability.ProductAvailabilityService;
import com.salesmanager.core.business.catalog.product.service.browse.ProductBrowseService;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.catalog.product.service.price.ProductPriceService;
import com.salesmanager.core.business.catalog.product.service.relationship.ProductRelationshipService;
import com.salesmanager.core.business.catalog.product.service.review.ProductReviewService;
import com.salesmanager.core.business.common.model.BasedataType;
import com.salesmanager.core.business.common.model.Criteria;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.content.model.FileContentType;
import com.salesmanager.core.business.content.model.ImageContentFile;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.authorization.model.Authorization;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.search.service.SearchService;
import com.salesmanager.core.business.system.service.SepcialConfigureService;
import com.salesmanager.core.utils.BaseDataUtils;
import com.salesmanager.core.utils.CoreConfiguration;
import com.salesmanager.core.utils.PropertiesUtils;


@Service("productService")
public class ProductServiceImpl extends SalesManagerEntityServiceImpl<Long, Product> implements ProductService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	ProductDao productDao;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductAvailabilityService productAvailabilityService;
	
	@Autowired
	ProductPriceService productPriceService;
	
	
	@Autowired
	ProductAttributeService productAttributeService;
	
	@Autowired
	ProductRelationshipService productRelationshipService;
	
	@Autowired
	SearchService searchService;
	
	@Autowired
	ProductImageService productImageService;
	
	@Autowired
	CoreConfiguration configuration;
	
	@Autowired
	ProductBrowseService productBrowseService;
	
	@Autowired
	ProductReviewService productReviewService;
	
	@Autowired
	SepcialConfigureService specialConfigurationService;
	
	@Autowired
	public ProductServiceImpl(ProductDao productDao) {
		super(productDao);
		this.productDao = productDao;
	}
	
	@Override
	public List<Product> getListByCriteria(Criteria criteria) {
		return productDao.getListByCriteria(criteria);
	}
	
	@Override
	public void addProductDescription(Product product, ProductDescription description)
			throws ServiceException {
		
		
		if(product.getDescriptions()==null) {
			product.setDescriptions(new HashSet<ProductDescription>());
		}
		
		product.getDescriptions().add(description);
		description.setProduct(product);
		//update(product);
		//searchService.index(product.getMerchantStore(), product);
	}
	
	@Override
	public List<Product> getProducts(List<Long> categoryIds) throws ServiceException {
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Set ids = new HashSet(categoryIds);
		return productDao.getProductsListByCategories(ids);
		
	}
	@Override
	public List<Product> getProductsByMan(List<Long> manIds,Long merchantId) throws ServiceException {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Set ids = new HashSet(manIds);
		return productDao.getProductsListByMan(ids,merchantId);
		
	}
	@Override
	public List<Product> getProducts(List<Long> categoryIds, Language language) throws ServiceException {
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Set<Long> ids = new HashSet(categoryIds);
		return productDao.getProductsListByCategories(ids, language);
		
	}
	

	@Override
	public ProductDescription getProductDescription(Product product, Language language) {
		for (ProductDescription description : product.getDescriptions()) {
			if (description.getLanguage().equals(language)) {
				return description;
			}
		}
		return null;
	}
	
	@Override
	public Product getBySeUrl(MerchantStore store, String seUrl, Locale locale) {
		return productDao.getBySeUrl(store, seUrl, locale);
	}

	@Override
	public Product getProductForLocale(long productId, Language language, Locale locale)
			throws ServiceException {
		Product product =  productDao.getProductForLocale(productId, language, locale);
		

		CatalogServiceHelper.setToAvailability(product, locale);
		CatalogServiceHelper.setToLanguage(product, language.getId());
		return product;
	}

	@Override
	public List<Product> getProductsForLocale(Category category,
			Language language, Locale locale) throws ServiceException {
		
		if(category==null) {
			throw new ServiceException("The category is null");
		}
		
		//Get the category list
		StringBuilder lineage = new StringBuilder().append(category.getLineage()).append(category.getId()).append("/");
		List<Category> categories = categoryService.listByLineage(category.getMerchantStore(),lineage.toString());
		Set<Long> categoryIds = new HashSet<Long>();
		for(Category c : categories) {
			
			categoryIds.add(c.getId());
			
		}
		
		categoryIds.add(category.getId());
		
		//Get products
		List<Product> products = productDao.getProductsForLocale(category.getMerchantStore(), categoryIds, language, locale);
		
		//Filter availability
		
		return products;
	}
	
	@Override
	public ProductList listByStore(MerchantStore store,
			Language language, ProductCriteria criteria) {
		
		return productDao.listByStore(store, language, criteria);
	}
	
	@Override
	public List<Product> listByStore(MerchantStore store) {
		
		return productDao.listByStore(store);
	}
	
		
	@Override
	public Product getByCode(String productCode, Language language) {
		return productDao.getByCode(productCode, language);
	}
		


	
    /**
     * delete
     * */
	@Override
	public void delete(Product product) throws ServiceException {
		LOGGER.debug("Deleting product");
		Validate.notNull(product, "Product cannot be null");
		Validate.notNull(product.getMerchantStore(), "MerchantStore cannot be null in product");
		product = this.getById(product.getId());//Prevents detached entity error
		product.setCategories(null);
		
		Set<ProductImage> images = product.getImages();
		
		for(ProductImage image : images) {
			productImageService.removeProductImage(image);
		}
		
		product.setImages(null);
		
		
		
		//related - featured
		productRelationshipService.deleteByProductId(product.getId());
		/**
		List<ProductRelationship> relationships = productRelationshipService.listByProduct(product);
		for(ProductRelationship relationship : relationships) {
			productRelationshipService.delete(relationship);
		}*/
		
		//delet browse
		productBrowseService.deleteByProductId(product.getId());
		/**
		List<ProductBrowse> pbs = productBrowseService.getByProductID(product.getId());
		if(pbs !=null && pbs.size()>0){
			for(ProductBrowse pb:pbs){
				productBrowseService.delete(pb);
			}
		}*/
		
		//delet review
		//productReviewService.deleteByProductId(product.getId());
		
		List<ProductReview> prs = productReviewService.getByProduct(product);
		if(prs !=null && prs.size()>0){
			for(ProductReview pr :prs ){
				productReviewService.delete(pr);
			}
		}
				
		super.delete(product);
		searchService.deleteIndex(product.getMerchantStore(), product);
		
	}

	@Override
	public void create(Product product) throws ServiceException {
		//种子用户不需要审核
		if(product.getMerchantStore().getSeeded()== true){
			AuditSection auditSection  = product.getAuditSection();
			auditSection.setAudit(com.salesmanager.core.constants.Constants.AUDIT_AUDITED);
			product.setAuditSection(auditSection);
			product.setQualityScore(this.getScore(product));
		}
		super.create(product);
		//新增产品，如果是审核通过的，并且是上架，则需要加入ES
		if ((product.getAuditSection().getAudit()>0)&&product.isAvailable()){
			try {
				searchService.index(product.getMerchantStore(), product);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	
	
	/**
	 * 新增或更新
	 * @author 百图生物
	 * */
	@Override	
	public void saveOrUpdate(Product product) throws ServiceException {
		boolean isAdd = false;
		LOGGER.debug("Save or update product ");
		Validate.notNull(product,"product cannot be null");
		Validate.notNull(product.getAvailabilities(),"product must have at least one availability");
		Validate.notEmpty(product.getAvailabilities(),"product must have at least one availability");

		//List of original availabilities
		Set<ProductAvailability> originalAvailabilities = null;
		
		//List of original attributes
		Set<ProductAttribute> originalAttributes = null;
		
		//List of original reviews
		Set<ProductRelationship> originalRelationships = null;
		
		//List of original images
		Set<ProductImage> originalProductImages = null;
		
		
		if(product.getId()!=null && product.getId()>0) {
			LOGGER.debug("Update product",product.getId());
			//get original product
			Product originalProduct = this.getById(product.getId());
			originalAvailabilities = originalProduct.getAvailabilities();
			originalAttributes = originalProduct.getAttributes();
			originalRelationships = originalProduct.getRelationships();
			originalProductImages = originalProduct.getImages();
		} else {
			isAdd = true;
			Set<ProductDescription> productDescriptions = product.getDescriptions();
			product.setDescriptions(null);
			
			super.create(product);
			
			for(ProductDescription productDescription : productDescriptions) {
				if(product.getDescriptions()==null) {
					product.setDescriptions(new HashSet<ProductDescription>());
				}
				
				product.getDescriptions().add(productDescription);
				productDescription.setProduct(product);
			}
		}

		
		LOGGER.debug("Creating availabilities");
		
		//get availabilities
		Set<ProductAvailability> availabilities = product.getAvailabilities();
		List<Long> newAvailabilityIds = new ArrayList<Long>();
		if(availabilities!=null && availabilities.size()>0) {
			for(ProductAvailability availability : availabilities) {
				availability.setProduct(product);
				productAvailabilityService.saveOrUpdate(availability);
				newAvailabilityIds.add(availability.getId());
				//check prices
				Set<ProductPrice> prices = availability.getPrices();
				if(prices!=null && prices.size()>0) {

					for(ProductPrice price : prices) {
						price.setProductAvailability(availability);
						productPriceService.saveOrUpdate(price);
					}
				}	
			}
		}
		
		//cleanup old availability
		if(originalAvailabilities!=null) {
			for(ProductAvailability availability : originalAvailabilities) {
				if(!newAvailabilityIds.contains(availability.getId())) {
					//删除产品
					productAvailabilityService.delete(availability);
				}
			}
		}
		
		LOGGER.debug("Creating attributes");
		List<Long> newAttributesIds = new ArrayList<Long>();
		if(product.getAttributes()!=null && product.getAttributes().size()>0) {
			Set<ProductAttribute> attributes = product.getAttributes();
			for(ProductAttribute attribute : attributes) {
				attribute.setProduct(product);
				productAttributeService.saveOrUpdate(attribute);
				newAttributesIds.add(attribute.getId());
			}
		}
		
		//cleanup old attributes
		if(originalAttributes!=null) {
			for(ProductAttribute attribute : originalAttributes) {
				if(!newAttributesIds.contains(attribute.getId())) {
					productAttributeService.delete(attribute);
				}
			}
		}
		
		/**
		LOGGER.debug("Creating relationships");
		List<Long> newRelationshipIds = new ArrayList<Long>();
		if(product.getRelationships()!=null && product.getRelationships().size()>0) {
			Set<ProductRelationship> relationships = product.getRelationships();
			for(ProductRelationship relationship : relationships) {
				relationship.setProduct(product);
				productRelationshipService.saveOrUpdate(relationship);
				newRelationshipIds.add(relationship.getId());
			}
		}
		//cleanup old relationships
		if(originalRelationships!=null) {
			for(ProductRelationship relationship : originalRelationships) {
				if(!newRelationshipIds.contains(relationship.getId())) {
					productRelationshipService.delete(relationship);
				}
			}
		}
		*/
		
		LOGGER.debug("Creating images");

		//get images
		List<Long> newImageIds = new ArrayList<Long>();
		Set<ProductImage> images = product.getImages();
		if(images!=null && images.size()>0) {
			for(ProductImage image : images) {
				if(image.getImage()!=null && (image.getId()==null || image.getId()==0L)) {
					image.setProduct(product);
			        ImageContentFile cmsContentImage = new ImageContentFile();
			        cmsContentImage.setFileName( image.getProductImage() );
			        cmsContentImage.setFile( image.getImage() );
			        cmsContentImage.setFileContentType(FileContentType.PRODUCT);
					
					
					productImageService.addProductImage(product, image, cmsContentImage);
					newImageIds.add(image.getId());
				} else {
					productImageService.update(image);
					newImageIds.add(image.getId());
				}
			}
		}
		
		//cleanup old images
		if(originalProductImages!=null) {
			for(ProductImage image : originalProductImages) {
				if(!newImageIds.contains(image.getId())) {
					productImageService.delete(image);
				}
			}
		}
		
		//种子用户不需要审核
		if(product.getMerchantStore().getSeeded()== true){
			AuditSection auditSection  = product.getAuditSection();
			auditSection.setAudit(com.salesmanager.core.constants.Constants.AUDIT_AUDITED);
			product.setAuditSection(auditSection);
			product.setQualityScore(this.getScore(product));
		}else{
			int weight = Integer.parseInt(PropertiesUtils.getPropertiesValue("AUDIT"));	
			AuditSection auditSection =product.getAuditSection();			
			if(product.getQualityScore()<weight && product.getProductIsFree()==true){
				//已审核的重申
				if(auditSection.getAudit()>0){
					auditSection.setAudit(com.salesmanager.core.constants.Constants.AUDIT_FAILED);
					searchService.deleteIndex(product.getMerchantStore(), product);
				}
			}
		}
		if(product.getId()!=null && product.getId()>0) {
			super.update(product);
		}
		
		/**
		boolean isFree= product.getProductIsFree();
		if(isFree == false){
			Long now = new Date().getTime();
			if(product.getDateChargeBegin().getTime()<= now && now <=product.getDateChargeEnd().getTime() ){
				isFree = true;
			}
		}*/
		
		//未审核不入ES
		if(product.getAuditSection().getAudit()>0){
			//产品未上架，不入ES，如果是新增产品，未上架则不入ES，如果是更新产品，而产品下架，则从ES中删除
			//如果产品上架，直接更新ES
			LOGGER.info("begin inserting or updating a document.");			
			if (product.isAvailable()) {
				try{
				  searchService.index(product.getMerchantStore(), product);
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
			} else if (!product.isAvailable()&&!isAdd) {
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>delete>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				searchService.deleteIndex(product.getMerchantStore(), product);
			}
			LOGGER.info("finish inserting or updating a document.");
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>audit"+product.getAuditSection().getAudit()+">>>>>>seed"+product.getMerchantStore().getSeeded()+">>>>qua"+product.getQualityScore());
	}

	@Override
	public ProductList listByCriteria(Language language,
			ProductCriteria criteria) {
		// TODO Auto-generated method stub
		return productDao.listByCriteria(language, criteria);
	}
	
	@Override
	public ProductList listPsByCriteria(Language language,
			ProductCriteria criteria){
		if (specialConfigurationService.getBooleanCfg(SepcialConfigureService.KEY_ADMIN_SEARCH_PRODUCT_WAY)){
			return productDao.listPsByCriteria_jdbc(language, criteria);
		}
		return productDao.listPsByCriteria(language, criteria);
		
	}

	@Override
	public List<ProductAvailability> getPrice(ProductCriteria criteria) {
		// TODO Auto-generated method stub
		return productDao.getPrice(criteria);
	}

	@Override
	public Product getNextCriteria(Language language, ProductCriteria criteria) {
		// TODO Auto-generated method stub
		return productDao.getNextCriteria(language,criteria);
	}
	
	
	private int getScore(Product product){
		
		int socre =0;
		
		BasedataType basedataType=null;
		Set<ProductCertificate> productCertificates = product.getCertificates();
		boolean flag=false;
		if(productCertificates!=null  && productCertificates.size()>0){					
			int docQuality=0;
			for(ProductCertificate productCertificate : productCertificates){
				basedataType=productCertificate.getBasedataType();
				docQuality = BaseDataUtils.getProductQuality(docQuality, basedataType);						
			}
			docQuality = BaseDataUtils.addFirstProductQuality(docQuality,basedataType);
			socre +=docQuality;
			flag=true;
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
			flag=true;
		}
		
		Set<ProductSelfproof> productSelfproofs = product.getSelfProofs();
		/**
		 * 如果以上三个质量分没有，图片价格描述加20分,如果只有图片价格加10分
		 */
		
		if(socre<=0){
			//判断是否有图片
			Set<ProductImage> image= product.getImages();
			
			if(image!=null && image.size()>0){
				socre +=this.getPriceScore(product);
				//判断是否有描述
				Set<ProductDescription> ds = product.getDescriptions();
				if(ds !=null  && ds.size()>0){
					socre +=this.getDescScore(ds);
				}
			}
			
			
		}else{
			//只有4个凭证的加加20分
			if(flag==false){
				//实验报告也没有
				if(!(productSelfproofs !=null && productSelfproofs.size()>0)){
					if(productProofs.size()>=4){
						socre=40;
					}
				}
				
			}
		}
		
		
		if(productSelfproofs !=null && productSelfproofs.size()>0){
			for(ProductSelfproof productSelfproof:productSelfproofs){
				int proofQuality=Integer.parseInt(PropertiesUtils.getPropertiesValue(productSelfproof.getSelfproofType()));
				socre +=proofQuality;
				break;
			}
			
		}
		if(socre>=100) socre=98;
		return socre;
		
	}

	@Override
	public List<Object> getAllProductID() {
		// TODO Auto-generated method stub
		return productDao.getAllProductID();
	}

	@Override
	public List<Object> getAllProductIDByStoreID(Long storeId){
		return productDao.getAllProductIDByStoreID(storeId);
	}
	
	@Override
	public List<Object[]> getServicePrice(String ids) {
		// TODO Auto-generated method stub
		return productDao.getServicePrice(ids);
	}

	/**
	 * 商品报表
	 * */
	public List<Product> queryByProductPoi(String beginTime,String endTime){
		return productDao.queryByProductPoi(beginTime, endTime);
	}

	private int getPriceScore(Product product){
		int score=0;
		Set<ProductAvailability> abilit = product.getAvailabilities();
		if(abilit !=null && abilit.size()>0){
			for(ProductAvailability av : abilit) {
				Set<ProductPrice> pr = av.getPrices();
				for(ProductPrice p:pr){
					int result=p.getProductPriceAmount().compareTo(BigDecimal.ZERO); 						
					if(result==1){
						score=10;
						return score ;
					}
					
				}
			}
			
		}
		return score;
	}
	
	private int getDescScore(Set<ProductDescription> ds){
		int score=0;
		for(ProductDescription d:ds){
			if(d.getDescription()!=null && !d.getDescription().equalsIgnoreCase("")){
				score=10;
				return score;
			}
		}
		return score;
	}

	@Override
	public void saveOrUpdateNoQulity(Product product) throws ServiceException {
		// TODO Auto-generated method stub
		boolean isAdd = false;
		LOGGER.debug("Save or update product ");
		Validate.notNull(product,"product cannot be null");
		Validate.notNull(product.getAvailabilities(),"product must have at least one availability");
		Validate.notEmpty(product.getAvailabilities(),"product must have at least one availability");

		//List of original availabilities
		Set<ProductAvailability> originalAvailabilities = null;
		
		//List of original attributes
		Set<ProductAttribute> originalAttributes = null;
		
		//List of original reviews
		Set<ProductRelationship> originalRelationships = null;
		
		//List of original images
		Set<ProductImage> originalProductImages = null;
		
		
		if(product.getId()!=null && product.getId()>0) {
			LOGGER.debug("Update product",product.getId());
			//get original product
			Product originalProduct = this.getById(product.getId());
			originalAvailabilities = originalProduct.getAvailabilities();
			originalAttributes = originalProduct.getAttributes();
			originalRelationships = originalProduct.getRelationships();
			originalProductImages = originalProduct.getImages();
		} else {
			isAdd = true;
			Set<ProductDescription> productDescriptions = product.getDescriptions();
			product.setDescriptions(null);
			
			super.create(product);
			
			for(ProductDescription productDescription : productDescriptions) {
				if(product.getDescriptions()==null) {
					product.setDescriptions(new HashSet<ProductDescription>());
				}
				
				product.getDescriptions().add(productDescription);
				productDescription.setProduct(product);
			}
		}

		
		LOGGER.debug("Creating availabilities");
		
		//get availabilities
		Set<ProductAvailability> availabilities = product.getAvailabilities();
		List<Long> newAvailabilityIds = new ArrayList<Long>();
		if(availabilities!=null && availabilities.size()>0) {
			for(ProductAvailability availability : availabilities) {
				availability.setProduct(product);
				productAvailabilityService.saveOrUpdate(availability);
				newAvailabilityIds.add(availability.getId());
				//check prices
				Set<ProductPrice> prices = availability.getPrices();
				if(prices!=null && prices.size()>0) {

					for(ProductPrice price : prices) {
						price.setProductAvailability(availability);
						productPriceService.saveOrUpdate(price);
					}
				}	
			}
		}
		
		//cleanup old availability
		if(originalAvailabilities!=null) {
			for(ProductAvailability availability : originalAvailabilities) {
				if(!newAvailabilityIds.contains(availability.getId())) {
					//删除产品
					productAvailabilityService.delete(availability);
				}
			}
		}
		
		LOGGER.debug("Creating attributes");
		List<Long> newAttributesIds = new ArrayList<Long>();
		if(product.getAttributes()!=null && product.getAttributes().size()>0) {
			Set<ProductAttribute> attributes = product.getAttributes();
			for(ProductAttribute attribute : attributes) {
				attribute.setProduct(product);
				productAttributeService.saveOrUpdate(attribute);
				newAttributesIds.add(attribute.getId());
			}
		}
		
		//cleanup old attributes
		if(originalAttributes!=null) {
			for(ProductAttribute attribute : originalAttributes) {
				if(!newAttributesIds.contains(attribute.getId())) {
					productAttributeService.delete(attribute);
				}
			}
		}
		
		
		LOGGER.debug("Creating relationships");
		List<Long> newRelationshipIds = new ArrayList<Long>();
		if(product.getRelationships()!=null && product.getRelationships().size()>0) {
			Set<ProductRelationship> relationships = product.getRelationships();
			for(ProductRelationship relationship : relationships) {
				relationship.setProduct(product);
				productRelationshipService.saveOrUpdate(relationship);
				newRelationshipIds.add(relationship.getId());
			}
		}
		//cleanup old relationships
		if(originalRelationships!=null) {
			for(ProductRelationship relationship : originalRelationships) {
				if(!newRelationshipIds.contains(relationship.getId())) {
					productRelationshipService.delete(relationship);
				}
			}
		}
		
		
		LOGGER.debug("Creating images");

		//
		//get images
		List<Long> newImageIds = new ArrayList<Long>();
		Set<ProductImage> images = product.getImages();
		if(images!=null && images.size()>0) {
			for(ProductImage image : images) {
				if(image.getImage()!=null && (image.getId()==null || image.getId()==0L)) {
					image.setProduct(product);
					
			        InputStream inputStream = image.getImage();
			        ImageContentFile cmsContentImage = new ImageContentFile();
			        cmsContentImage.setFileName( image.getProductImage() );
			        cmsContentImage.setFile( inputStream );
			        cmsContentImage.setFileContentType(FileContentType.PRODUCT);
					
					
					productImageService.addProductImage(product, image, cmsContentImage);
					newImageIds.add(image.getId());
				} else {
					productImageService.update(image);
					newImageIds.add(image.getId());
				}
			}
		}
		
		//cleanup old images
		if(originalProductImages!=null) {
			for(ProductImage image : originalProductImages) {
				if(!newImageIds.contains(image.getId())) {
					productImageService.delete(image);
				}
			}
		}
		
		//种子用户不需要审核
		if(product.getMerchantStore().getSeeded()== true){
			AuditSection auditSection  = product.getAuditSection();
			auditSection.setAudit(com.salesmanager.core.constants.Constants.AUDIT_AUDITED);
			product.setAuditSection(auditSection);
			product.setQualityScore(0);
		}else{
			int weight = Integer.parseInt(PropertiesUtils.getPropertiesValue("AUDIT"));	
			AuditSection auditSection =product.getAuditSection();			
			if(product.getQualityScore()<weight && product.getProductIsFree()==true){
				//已审核的重申
				if(auditSection.getAudit()>0){
					auditSection.setAudit(com.salesmanager.core.constants.Constants.AUDIT_FAILED);
					searchService.deleteIndex(product.getMerchantStore(), product);
				}
			}
		}
		if(product.getId()!=null && product.getId()>0) {
			super.update(product);
		}
		
		/**
		boolean isFree= product.getProductIsFree();
		if(isFree == false){
			Long now = new Date().getTime();
			if(product.getDateChargeBegin().getTime()<= now && now <=product.getDateChargeEnd().getTime() ){
				isFree = true;
			}
		}*/
		
		//未审核不入ES
		if(product.getAuditSection().getAudit()>0){
			//产品未上架，不入ES，如果是新增产品，未上架则不入ES，如果是更新产品，而产品下架，则从ES中删除
			//如果产品上架，直接更新ES
			LOGGER.info("begin inserting or updating a document.");			
			if (product.isAvailable()) {
				try {
					searchService.index(product.getMerchantStore(), product);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (!product.isAvailable()&&!isAdd) {
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>delete>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				searchService.deleteIndex(product.getMerchantStore(), product);
			}
			LOGGER.info("finish inserting or updating a document.");
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>audit"+product.getAuditSection().getAudit()+">>>>>>seed"+product.getMerchantStore().getSeeded()+">>>>qua"+product.getQualityScore());
	}

	@Override
	public Product getBySeUrlId(MerchantStore store, String seUrl, Locale locale) {
		// TODO Auto-generated method stub
		return productDao.getBySeUrlId(store, seUrl, locale);
	}
	
	/**
	 * 根据产品添加时间段查询出产品Id
	 * */
	public List<BigInteger> queryProductById(Date...dateTemp)
	{
		return productDao.queryProductById(dateTemp);
	}
	//品牌
	public Authorization queryByAuthorization(Long productId){
		
		return this.productDao.queryByAuthorization(productId);
	}
	//商家
	public List<Authorization> getAuthorizationbyMerchantId(List<Long> manIds,Long merchantId){
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Set ids = new HashSet(manIds);
		return this.productDao.queryAuthorizationbyMerchantId(ids,merchantId);
	}

	@Override
	public int getExistedDiomandProductNumber(Language language, MerchantStore store) {
		ProductCriteria criteria = new ProductCriteria();
		criteria.setDiamond(true);
		criteria.setStoreId(store.getId());
		criteria.setAvailableP(true);
		
		ProductList productList = this.listByCriteria(language, criteria);
		if (productList == null){
			return 0;
		}
		return productList.getTotalCount();
	}

	@Override
	public int markDiomandProducts(List<Product> products, boolean setAsDiamond) throws ServiceException {
		// TODO Auto-generated method stub
		for(Product product : products){
			product.setProductIsDiamond(setAsDiamond);
			update(product);
			searchService.index(product.getMerchantStore(), product);
		}
		return products.size();
	}

	@Override
	public List<Long> getAllByManufactureIds(Set<Manufacturer> manufacturers) {
		return productDao.getAllByManufactureIds(manufacturers);
	}

	@Override
	public List<Long> getAllByManufactureAndMerchant(MerchantStore mcht, Set<Manufacturer> menufactures) {
		return productDao.getAllByManufactureAndMerchant(mcht, menufactures);
	}

	@Override
	public List<Long> getAllByNotManufactureNorMerchant(Set<MerchantStore> merchants, Set<Manufacturer> menufactures) {
		return productDao.getAllByNotManufactureNorMerchant(merchants, menufactures);
	}

	@Override
	public List<Long> getIdsByNativeSql(String sqlStr) throws ServiceException {
		try {
			return productDao.getIdsByNativeSql(sqlStr);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Long> getAllInvalidProductIdsByMerchantAndManufacturer(String storeName, List<Long> brandIds) throws ServiceException {
		try {
			return productDao.getAllInvalidProductIdsByMerchantAndManufacturer(storeName, brandIds);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}
	@Override
	public List<Long> getAllInvalidProductIdsByMerchantName(String storeName) throws ServiceException {
		try {
			return productDao.getAllInvalidProductIdsByMerchantName(storeName);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Long> getAllInvalidProductIdsByManufacturers(List<Long> brandIds) throws ServiceException {
		try {
			return productDao.getAllInvalidProductIdsByManufacturers(brandIds);
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Product> getProductListByIdsForPricing(List<Long> prodIds) {
		return productDao.getProductListByIdsForPricing(prodIds);
	}
	
	
	
}
