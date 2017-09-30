package com.salesmanager.web.populator.catalog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.facet.terms.longs.InternalLongTermsFacet;
import org.elasticsearch.search.facet.terms.longs.InternalLongTermsFacet.LongEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salesmanager.core.business.catalog.category.model.Category;
import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.certificate.ProductCertificate;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.manufacturer.Manufacturer;
import com.salesmanager.core.business.catalog.product.model.manufacturer.ManufacturerDescription;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;
import com.salesmanager.core.business.catalog.product.model.price.ProductPriceDescription;
import com.salesmanager.core.business.catalog.product.model.proof.ProductProof;
import com.salesmanager.core.business.catalog.product.model.selfproof.ProductSelfproof;
import com.salesmanager.core.business.catalog.product.model.thirdproof.ProductThirdproof;
import com.salesmanager.core.business.catalog.product.service.PricingService;
import com.salesmanager.core.business.common.model.BasedataType;
import com.salesmanager.core.business.content.model.ProductRelatedImageType;
import com.salesmanager.core.business.generic.exception.ConversionException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.reference.language.model.Language;
import com.salesmanager.core.business.search.model.SearchEntry;
import com.salesmanager.core.utils.AbstractDataPopulator;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.entity.catalog.ReadableCertificate;
import com.salesmanager.web.entity.catalog.ReadableImage;
import com.salesmanager.web.entity.catalog.ReadableProof;
import com.salesmanager.web.entity.catalog.ReadableSelfProof;
import com.salesmanager.web.entity.catalog.ReadableThirdProof;
import com.salesmanager.web.entity.catalog.category.ReadableCategory;
import com.salesmanager.web.entity.catalog.manufacturer.ReadableManufacturer;
import com.salesmanager.web.entity.catalog.product.ListProduct;
import com.salesmanager.web.entity.catalog.product.ReadableProduct;
import com.salesmanager.web.entity.catalog.product.ReadableProductPrice;
import com.salesmanager.web.entity.shop.Store;
import com.salesmanager.web.utils.DateUtil;
import com.salesmanager.web.utils.ImageFilePathUtils;

public class ReadableProductPopulator extends
		AbstractDataPopulator<Product, ReadableProduct> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReadableProductPopulator.class);
	private PricingService pricingService;
	private Map <Long,Manufacturer> manMap;
	private Map<Long,Category>catMap;
	List<Integer> last =new ArrayList<Integer>();

	public PricingService getPricingService() {
		return pricingService;
	}
	
	public List<ReadableCategory> getCagoryList(Object object){
		 if (object instanceof InternalLongTermsFacet) {
			 InternalLongTermsFacet ff = (InternalLongTermsFacet) object;
   	         List<ReadableCategory>  rCates = new ArrayList<ReadableCategory>();
   	         for(LongEntry o : ff.getEntries()) {
   	        	//InternalLongTermsFacet e = (InternalLongTermsFacet)o;
   	        	// com.shopizer.search.services.Entry entry = new com.shopizer.search.services.Entry();
   	        	// Entry e = (Entry)o;
   	        	ReadableCategory re = new ReadableCategory();
				Category cat = catMap.get(Long.parseLong(o.getTerm().string()));
				if(cat != null ){
					re.setProductCount(o.getCount());
					re.setName(cat.getDescinfo());
					re.setId(cat.getId());
					rCates.add(re); 
				}
   	         }
   	         return rCates;
   	    }
		return null;
	}
	
	public List<ReadableCategory> getCagoryList(Object object,Category category){
		if(category.getCategories() == null || category.getCategories().size()==0) return null;
		List<Category> subCate = category.getCategories();
		List<ReadableCategory>  rCates = new ArrayList<ReadableCategory>();
		HashMap<Long, ReadableCategory> reMap = new HashMap<Long, ReadableCategory>();
		//init 下级类别		
		for(Category c:subCate){
			ReadableCategory re = new ReadableCategory();
			//Category cat = catMap.get(c.getId());
			re.setProductCount(0);
			//re.setName(cat.getDescinfo());
			re.setId(c.getId());
			reMap.put(re.getId(), re);		
		}
		 if (object instanceof InternalLongTermsFacet) {
			 InternalLongTermsFacet ff = (InternalLongTermsFacet) object;
   	        
   	         for(LongEntry o : ff.getEntries()) {   	        	
   	        	ReadableCategory re = reMap.get(Long.parseLong(o.getTerm().string()));
   	        	Category cat = catMap.get(Long.parseLong(o.getTerm().string()));
   	        	//子集
   	        	if(re != null){
   	        		re.setProductCount(o.getCount());   	        		
   	        		if(cat != null) re.setName(cat.getDescinfo());
   	        		reMap.put(re.getId(),re);
   	        	}
				//子集的子集
   	        	else{
   	        		String lin = cat.getLineage();
   					String[] categoryPath = lin.split(Constants.CATEGORY_LINEAGE_DELIMITER);
   					for(int i=0 ; i<categoryPath.length; i++) {
   						String sId = categoryPath[i];
   						if(!StringUtils.isBlank(sId)) {
   							ReadableCategory re1 = reMap.get(Long.parseLong(sId));
   							if(re1 != null){
   								int count =re1.getProductCount();
   								Category cat1 = catMap.get(re1.getId());
   								if(cat1 != null) re1.setName(cat1.getDescinfo());
   								count += o.getCount();
   								re1.setProductCount(count);
   								reMap.put(re1.getId(),re1);
   							}
   						}
   					}
   						
   	        	}				
   	         }
   	      for (Entry<Long, ReadableCategory> entry :reMap.entrySet()) {
   	    	  ReadableCategory  readableCategory = entry.getValue();
   	    	  if(readableCategory.getProductCount()>0)  rCates.add(entry.getValue());
   	      }
   	        
   	         return rCates;
   	    }
		return null;
	}
	
	public List<ReadableManufacturer> getManufacturerList(Object object){
		 if (object instanceof InternalLongTermsFacet) {
			 InternalLongTermsFacet ff = (InternalLongTermsFacet) object;
  	         List<ReadableManufacturer>  rCates = new ArrayList<ReadableManufacturer>();
  	         for(LongEntry o : ff.getEntries()) {
  	        	//TermEntry e = (TermEntry)o;
  	        	// com.shopizer.search.services.Entry entry = new com.shopizer.search.services.Entry();
  	        	// Entry e = (Entry)o;
  	        	ReadableManufacturer manuf  = new ReadableManufacturer();
				Manufacturer man = manMap.get(Long.parseLong(o.getTerm().string()));
				if(man != null ){
					//re.setProductCount(e.getCount());
					manuf.setId(man.getId());
					manuf.setManName(man.getDescription().getName());
					manuf.setManCount(o.getCount());
					rCates.add(manuf); 
				}
  	         }
  	         return rCates;
  	    }
		return null;
	}
	
		
	public List<ListProduct> getListProducts(List<SearchEntry> hits){
		if(hits != null && hits.size()>0){
				List<ListProduct> listProducts = new ArrayList<ListProduct>();
				for(SearchEntry entry: hits){
					try{
					Map<String,Object> metaEntries = entry.getProduct();
					//Map sourceEntries = (Map)metaEntries.get("source");
					ListProduct listProduct = new ListProduct();
					listProduct.setId(Long.parseLong(metaEntries.get("id").toString()));
					listProduct.setFriendlyUrl(metaEntries.get("friendlyUrl").toString());
					listProduct.setProductName(metaEntries.get("pname").toString());
					if(metaEntries.get("penname") !=null)
					listProduct.setProductEnname(StringUtils.trimToNull(metaEntries.get("penname").toString()));
					if(metaEntries.get("simpledesc") !=null)
					listProduct.setProductDesc(StringUtils.trimToNull(metaEntries.get("simpledesc").toString()));
					listProduct.setStoreName(metaEntries.get("store").toString());
					if(metaEntries.get("image") !=null)
					listProduct.setImageUrl(StringUtils.trimToNull(metaEntries.get("image").toString()));
					if(metaEntries.get("quality")!= null)
					listProduct.setQuality(Integer.parseInt(metaEntries.get("quality").toString()));
					if(metaEntries.get("manufacturer") !=null){
						Manufacturer man = manMap.get(Long.parseLong(metaEntries.get("manufacturer").toString()));
						if (man!=null) {
							listProduct.setManufacturer(man.getDescription().getName());
						}
					}
					if( metaEntries.get("isfree") !=null)
					listProduct.setFree((boolean) metaEntries.get("isfree"));
					/**
					if(null != metaEntries.get("certificates")) {
						listProduct.setCertificateNum(((ArrayList<HashMap<String, String>>)metaEntries.get("certificates")).size());
					}
					
					if(null != metaEntries.get("proofs")){
						listProduct.setCertificateNum(((ArrayList<HashMap<String, String>>)metaEntries.get("proofs")).size());
					}
					
					if(null != metaEntries.get("thirdproofs")){
						listProduct.setCertificateNum(((ArrayList<HashMap<String, String>>)metaEntries.get("thirdproofs")).size());
					}*/
					this.getSortByQulity(listProducts,listProduct,entry.getSocre());
					}catch (Exception e){
						LOGGER.debug("get listproduct  search error",e);
						continue;
					}
				}
				return listProducts;
			
		}
		
		return null;
	}
	
	List<ListProduct> getSortByQulity(List<ListProduct> listProducts,ListProduct p,float so){
		System.out.println(">>>>>>>>>>>>>calu>>>>>>>>>>>" + so);
		//相差因子占定为7
		int diff=0;
		if(listProducts.size()==0) {
			this.last = new ArrayList<Integer>();
			this.last.add((int)so);
			listProducts.add(p);
		}else{
			int pcsocre = (int)so;
			if((pcsocre+diff)>=  this.last.get(0)){
				int j=listProducts.size()-1;
				for(int k=0;k<last.size();k++){
					//遇到大于值加相差因子的删除并退出
					if((pcsocre+diff)<last.get(k)){
						for(int n=last.size()-1;n>=k;n--)last.remove(n);
						break;
					}else{
						if(p.getQuality()<listProducts.get(j).getQuality()){
							listProducts.add(j+1,p);
							this.last.add((int)so);	
							return listProducts;
						}
						j--;
					}
				}
				//如果质量相等判断收费
				if(p.isFree()==true){
					for(int k=0;k<last.size();k++){
						if(p.getQuality()>listProducts.get(j+1).getQuality()){
							break;
						}else{
							if(listProducts.get(j+1).isFree()==true){
								break;
							}
							j++;
						}
					}
				}
					listProducts.add(j+1,p);
					this.last.add((int)so);	
					return listProducts;
				
				
			}else{
				this.last = new ArrayList<Integer>();
				this.last.add((int)so);	
				listProducts.add(p);
			}
		}
		
		return listProducts;
	}
	
	public List<ReadableProduct> getPrice(List<ProductAvailability> prices,MerchantStore store){
		List<ReadableProduct> rProduct = new ArrayList<ReadableProduct>();
		
		for(ProductAvailability price:prices){
			try{
				Set<ProductPrice> prs = price.getPrices();
				ReadableProduct rp = new ReadableProduct();
				rp.setId(price.getProduct().getId());
				List<ReadableProductPrice> rPrices = new ArrayList<ReadableProductPrice>();
				for(ProductPrice pr:prs){
					ReadableProductPrice price1 = new ReadableProductPrice();
					price1.setId(pr.getId());
					for(ProductPriceDescription pname : pr.getDescriptions()){
						price1.setCode(pname.getName());
						break;
					}
					BigDecimal sprice = pricingService.getFinalPrice(pr);
					if(null != sprice ){
						price1.setSpecial(pricingService.getDisplayAmount(sprice, store));
					}
					price1.setPrice(pricingService.getDisplayAmount(pr.getProductPriceAmount(), store));
					BigDecimal tmp = pr.getProductPriceAmount();
					if (tmp!=null&&tmp.intValue()==0) {
						price1.setPriceValue("0"); 
					}
					rPrices.add(price1);
				}
				rp.setPrices(rPrices);	
				rProduct.add(rp);
			}catch (Exception e){
				LOGGER.error("get listproduct price priceID: ",price.getProduct().getId());		
				continue;
			}
		}
		return rProduct;
	}

	public List<ListProduct> getListProduct(List<com.salesmanager.core.business.catalog.product.model.Product> products){
		if(products != null && products.size()>0){
			try{
				List<ListProduct> listProducts = new ArrayList<ListProduct>();
				for(com.salesmanager.core.business.catalog.product.model.Product product :products){
					ListProduct listProduct = new ListProduct();
					listProduct.setId(product.getId());
					if(product.getProductReviewCount() != null)	listProduct.setReviewNum(product.getProductReviewCount());
					ProductDescription description = product.getProductDescription();
					
					if(description !=null){
						listProduct.setFriendlyUrl(description.getSeUrl());
						listProduct.setProductName(description.getName());
						listProduct.setProductDesc(description.getSimpleDescription());
					}
					
					if(product.getManufacturer() != null){
						listProduct.setManufacturer(product.getManufacturer().getDescription().getName());
					}
					
					ProductImage image = product.getProductImage();
					if(image !=null){
						String imagePath = ImageFilePathUtils.buildProductImageFilePath(product.getMerchantStore(), product.getSku(), image.getProductImage());
						listProduct.setImageUrl(imagePath);
					}
					
					Set<ProductCertificate> productCertificates = product.getCertificates();
					if(productCertificates!=null  && productCertificates.size()>0){
						listProduct.setCertificateNum(productCertificates.size());
					}
					Set<ProductProof> productProofs = product.getProofs();
					if(productProofs !=null && productProofs.size()>0){
						listProduct.setProofNum(productProofs.size());
					}
					Set<ProductThirdproof> productThirdproofs = product.getThirdproofs();
					if(productThirdproofs !=null && productThirdproofs.size()>0){
						listProduct.setThirdNum(productThirdproofs.size());
					}
					listProduct.setStoreName(product.getMerchantStore().getStorename());
					/**
					List<ReadableProductPrice> prices = new ArrayList<ReadableProductPrice>();
					//price					
					for(ProductAvailability availability : product.getAvailabilities()){
						for(ProductPrice price:availability.getPrices()){
							ReadableProductPrice price1 = new ReadableProductPrice();
							for(ProductPriceDescription pname : price.getDescriptions()){
								price1.setName(pname.getName());
								break;
							}
							try{
								BigDecimal sprice = pricingService.getFinalPrice(price);
								if(null != sprice ){
									price1.setSpecial(pricingService.getDisplayAmount(sprice, product.getMerchantStore()));
								}
								price1.setPrice(pricingService.getDisplayAmount(price.getProductPriceAmount(), product.getMerchantStore()));
								price1.setCode(price.getCode());
								price1.setId(price.getId());
								price1.setPeriod(price.getProductPricePeriod());
								prices.add(price1);
							}catch (Exception e){
								LOGGER.error("get listproduct price productID: ",product.getId());		
								continue;
							}
						}
					}
					listProduct.setPrices(prices);
					*/
					listProducts.add(listProduct);
				}
				return listProducts;
			}catch (Exception e){
				LOGGER.debug("get listproduct error",e);			}
			
		}
		return null;		
	}

	public void setPricingService(PricingService pricingService) {
		this.pricingService = pricingService;
	}




	@Override
	public ReadableProduct populate(Product source,
			ReadableProduct target, MerchantStore store, Language language)
			throws ConversionException {
		Validate.notNull(pricingService, "Requires to set PricingService");
		
		try {
			
			//设定商品所属商铺信息
			MerchantStore pdt_store = source.getMerchantStore();
			Store readableStore = new Store();
			BeanUtils.copyProperties(readableStore, pdt_store);
			target.setStore(readableStore);
			
			ProductDescription description = source.getProductDescription();
			
			target.setId(source.getId());
			target.setAvailable(source.isAvailable());
			target.setCas(source.getCas());
			target.setBatchnum(source.getBatchnum());
			if(source.getProductReviewAvg()!=null) {
				double avg = source.getProductReviewAvg().doubleValue();
				double rating = Math.round(avg * 2) / 2.0f;
				target.setRating(rating);
			}
			//target.setProductVirtual(source.getProductVirtual());
			if(source.getProductReviewCount()!=null) {
				target.setRatingCount(source.getProductReviewCount().intValue());
			}
			if(description!=null) {
				com.salesmanager.web.entity.catalog.product.ProductDescription tragetDescription = new com.salesmanager.web.entity.catalog.product.ProductDescription();
				tragetDescription.setFriendlyUrl(description.getSeUrl());
				tragetDescription.setName(description.getName());
				tragetDescription.setEnName(description.getEnName());
				if(!StringUtils.isBlank(description.getMetatagTitle())) {
					tragetDescription.setTitle(description.getMetatagTitle());
				} else {
					tragetDescription.setTitle(description.getName());
				}
				tragetDescription.setMetaDescription(description.getMetatagDescription());
				tragetDescription.setDescription(description.getDescription());
				tragetDescription.setHighlights(description.getProductHighlight());
				//add sample desc
				tragetDescription.setSimpleDescription(description.getSimpleDescription());
				tragetDescription.setStorecondDescription(description.getStorecondDescription());
				tragetDescription.setTestDescription(description.getTestDescription());
				tragetDescription.setMethodDescription(description.getMethodDescription());
				target.setDescription(tragetDescription);
			}
			target.setQualitysocre(source.getQualityScore());
			
			if(source.getManufacturer()!=null) {
				ManufacturerDescription manufacturer = source.getManufacturer().getDescriptions().iterator().next(); 
				ReadableManufacturer manufacturerEntity = new ReadableManufacturer();
				com.salesmanager.web.entity.catalog.manufacturer.ManufacturerDescription d = new com.salesmanager.web.entity.catalog.manufacturer.ManufacturerDescription(); 
				d.setName(manufacturer.getName());
				manufacturerEntity.setDescription(d);
				manufacturerEntity.setId(manufacturer.getId());
				manufacturerEntity.setOrder(source.getManufacturer().getOrder());
				target.setManufacturer(manufacturerEntity);
			}
			
			ProductImage image = source.getProductImage();
			if(image!=null) {
				ReadableImage rimg = new ReadableImage();
				rimg.setImageName(image.getProductImage());
				String imagePath = ImageFilePathUtils.buildProductImageFilePath(store, source.getSku(), image.getProductImage());
				rimg.setImageUrl(imagePath);
				rimg.setId(image.getId());
				target.setImage(rimg);
				
				//other images
				Set<ProductImage> images = source.getImages();
				if(images!=null && images.size()>0) {
					List<ReadableImage> imageList = new ArrayList<ReadableImage>();
					for(ProductImage img : images) {
						ReadableImage prdImage = new ReadableImage();
						prdImage.setImageName(img.getProductImage());
						String imgPath = ImageFilePathUtils.buildProductImageFilePath(store, source.getSku(), img.getProductImage());
						prdImage.setImageUrl(imgPath);
						prdImage.setId(img.getId());
						imageList.add(prdImage);
					}
					target
					.setImages(imageList);
				}
			}
			//设定商品的类型
			target.setProductIsFree(source.getProductIsFree());	
			target.setSku(source.getSku());
			//target.setLanguage(language.getCode());
			// for certifacte
			
			Set<ProductCertificate> productCertificates = source.getCertificates();
			if(productCertificates!=null  && productCertificates.size()>0){
				List<ReadableCertificate> productCertificates2 =new ArrayList<ReadableCertificate>();
				for(ProductCertificate productCertificate : productCertificates){
					ReadableCertificate readableCertificate = new ReadableCertificate();
					readableCertificate.setBaseinfo(productCertificate.getBaseinfo());					
					readableCertificate.setDescription(productCertificate.getDescription());					
					readableCertificate.setDocUrl(productCertificate.getDocUrl());
					readableCertificate.setId(productCertificate.getId());
					readableCertificate.setName(productCertificate.getBasedataType().getName());
					readableCertificate.setTitle(productCertificate.getTitle());
					readableCertificate.setCertificateImage(productCertificate.getCertificateImage());
					readableCertificate.setDisplayName(productCertificate.getBasedataType().getName());
					readableCertificate.setDisplayDesc(productCertificate.getBaseinfo());
					//处理certificate的图片信息
					if (!StringUtils.isBlank(productCertificate.getCertificateImage())) {
						String imagePath = ImageFilePathUtils.buildProductRelatedImageFilePath(store, source, productCertificate.getCertificateImage(), ProductRelatedImageType.CERT.name());
						readableCertificate.setRimage(imagePath);
					}
					productCertificates2.add(readableCertificate);
				}				
				//target.setProductCertificates(source.getCertificates());
				target.setProductCertificates(productCertificates2);
				target.setCertificateNum(productCertificates.size());
			}
			
			//productproof
			Set<ProductProof> productProofs = source.getProofs();
			if(productProofs !=null && productProofs.size()>0){
				List<ReadableProof> proof = new ArrayList<ReadableProof>();
				for(ProductProof productProof:productProofs){
					ReadableProof readableProof = new ReadableProof();
					readableProof.setId(productProof.getId());
					readableProof.setBuyer(productProof.getBasedataType().getName());
					readableProof.setDateBuyed((productProof.getDateBuyed()!=null)?DateUtil.formatDate(productProof.getDateBuyed(), "yyyy-MM-dd"):"");
					readableProof.setDescription(productProof.getDescription());
					readableProof.setTitle(productProof.getTitle());
					readableProof.setDisplayName(productProof.getBasedataType().getName());
					readableProof.setDisplayDesc(productProof.getDescription());
					
					readableProof.setProofImage(productProof.getProofImage());
					//处理certificate的图片信息
					if (!StringUtils.isBlank(productProof.getProofImage())) {
						String imagePath = ImageFilePathUtils.buildProductRelatedImageFilePath(store, source, productProof.getProofImage(), ProductRelatedImageType.PROOF.name());
						readableProof.setRimage(imagePath);
					}
					proof.add(readableProof);
				}
				target.setProductProofs(proof);
				target.setProofNum(productProofs.size());
			}
			
			//thirdproof
			Set<ProductThirdproof> productThirdproofs = source.getThirdproofs();
			if(productThirdproofs !=null && productThirdproofs.size()>0){
				List<ReadableThirdProof> tProofs = new ArrayList<ReadableThirdProof>();
				for(ProductThirdproof productThirdproof:productThirdproofs){
					ReadableThirdProof readableThirdProof = new ReadableThirdProof();
					readableThirdProof.setId(productThirdproof.getId());
					readableThirdProof.setDescription(productThirdproof.getDescription());
					readableThirdProof.setTitle(productThirdproof.getTitle());
					BasedataType  basedataType = productThirdproof.getBasedataType();
					readableThirdProof.setName(basedataType.getName());
					readableThirdProof.setDisplayName(basedataType.getName());
					readableThirdProof.setDisplayDesc(productThirdproof.getDescription());
					
					readableThirdProof.setThirdproofImage(productThirdproof.getThirdproofImage());
					//处理certificate的图片信息
					if (!StringUtils.isBlank(readableThirdProof.getThirdproofImage())) {
						String imagePath = ImageFilePathUtils.buildProductRelatedImageFilePath(store, source, readableThirdProof.getThirdproofImage(), ProductRelatedImageType.THIRDPROOF.name());
						readableThirdProof.setRimage(imagePath);
					}
					tProofs.add(readableThirdProof);
				}
				target.setThirdProofs(tProofs);
				target.setThirdNum(productThirdproofs.size());
			}
			
			//self proof
			Set<ProductSelfproof> productSelfproofs = source.getSelfProofs();
			if(productSelfproofs !=null && productSelfproofs.size()>0){
				List<ReadableSelfProof> tProofs = new ArrayList<ReadableSelfProof>();
				for(ProductSelfproof productSelfproof:productSelfproofs){
					ReadableSelfProof readableSelfProof = new ReadableSelfProof();
					readableSelfProof.setId(productSelfproof.getId());
					readableSelfProof.setDescription(productSelfproof.getDescription());
					readableSelfProof.setTitle(productSelfproof.getTitle());
					readableSelfProof.setDisplayDesc(productSelfproof.getDescription());
					readableSelfProof.setSelfproofImage(productSelfproof.getSelfproofImage());
					//处理selfproof的图片信息
					if (!StringUtils.isBlank(readableSelfProof.getSelfproofImage())) {
						String imagePath = ImageFilePathUtils.buildProductRelatedImageFilePath(store, source, readableSelfProof.getSelfproofImage(), ProductRelatedImageType.SELFPROOF.name());
						readableSelfProof.setRimage(imagePath);
					}
					tProofs.add(readableSelfProof);
				}
				target.setSelfProofs(tProofs);
				target.setSelfproofNum(productSelfproofs.size());
			}
			
			target.setDateChargeBegin(source.getDateChargeBegin());
			target.setDateChargeEnd(source.getDateChargeEnd());
			
			List<ReadableProductPrice> prices = new ArrayList<ReadableProductPrice>();
			//price
			for(ProductAvailability availability : source.getAvailabilities()){
				for(ProductPrice price:availability.getPrices()){
					ReadableProductPrice price1 = new ReadableProductPrice();
					for(ProductPriceDescription pname : price.getDescriptions()){
						price1.setName(pname.getName());
						break;
					}
					BigDecimal sprice = pricingService.getFinalPrice(price); //获取产品打折价格
					if(null != sprice ){
						price1.setSpecial(pricingService.getDisplayAmount(sprice, store));
					}
					price1.setPrice(pricingService.getDisplayAmount(price.getProductPriceAmount(), store));
					BigDecimal tmp = price.getProductPriceAmount();
					if (tmp!=null&&tmp.intValue()==0) {
						price1.setPriceValue("0"); 
					}
					price1.setCode(price.getCode());
					price1.setId(price.getId());
					price1.setPeriod(price.getProductPricePeriod());
					prices.add(price1);
				}
			}
			target.setPrices(prices);
			
			target.setAudit(source.getAuditSection().getAudit());
	/**
			FinalPrice price = pricingService.calculateProductPrice(source);

			target.setFinalPrice(pricingService.getDisplayAmount(price.getFinalPrice(), store));
			target.setPrice(price.getFinalPrice());
	
			if(price.isDiscounted()) {
				target.setDiscounted(true);
				target.setOriginalPrice(pricingService.getDisplayAmount(price.getOriginalPrice(), store));
			}
			*/
			//availability
			/**
			for(ProductAvailability availability : source.getAvailabilities()) {
				
				
				if(availability.getRegion().equals(Constants.ALL_REGIONS)) {//TODO REL 2.1 accept a region
					target.setQuantity(availability.getProductQuantity());
					target.setQuantityOrderMaximum(availability.getProductQuantityOrderMax());
					target.setQuantityOrderMinimum(availability.getProductQuantityOrderMin());
				}
			}*/
			
			
			return target;
		
		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}




	@Override
	protected ReadableProduct createTarget() {
		// TODO Auto-generated method stub
		return null;
	}
	public Map<Long, Manufacturer> getManMap() {
		return manMap;
	}
	public void setManMap(Map<Long, Manufacturer> manMap) {
		this.manMap = manMap;
	}


	public Map<Long, Category> getCatMap() {
		return catMap;
	}


	public void setCatMap(Map<Long, Category> catMap) {
		this.catMap = catMap;
	}

}
