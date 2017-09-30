package com.salesmanager.web.tags;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.content.model.ProductRelatedImageType;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.utils.ImageFilePathUtils;
import com.salesmanager.web.utils.StringUtils;

public class ProductImageUrlTag extends TagSupport {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6319855234657139862L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductImageUrlTag.class);


	private String imageName;
	private String imageType;
	private Product product;
	private String productRelated; //如果定义了productRelated,说明是参考文献或者是购买凭证所关联的image，取值为：cert或者proof


	public int doStartTag() throws JspException {
		try {


			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();
			
			MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			
			HttpSession session = request.getSession();

			StringBuilder imagePath = new StringBuilder();
			
			//TODO domain from merchant, else from global config, else from property (localhost)
			
			// example -> /static/1/PRODUCT/120/product1.jpg
			
			//@SuppressWarnings("unchecked")
			//Map<String,String> configurations = (Map<String, String>)session.getAttribute("STORECONFIGURATION");
			//String scheme = (String)configurations.get("scheme");
			
			//if(StringUtils.isBlank(scheme)) {
			//	scheme = "http";
			//}
			
			@SuppressWarnings("unchecked")
			Map<String,String> configurations = (Map<String, String>)session.getAttribute(Constants.STORE_CONFIGURATION);
			String scheme = Constants.HTTP_SCHEME;
			if(configurations!=null) {
				scheme = (String)configurations.get("scheme");
			}
			

			imagePath.append(scheme).append("://")
			.append(merchantStore.getDomainName())
			.append(request.getContextPath());
			String prefix = "";
			if (!StringUtils.isNull(productRelated)) {
				if (productRelated.equalsIgnoreCase("cert")) {
					prefix = ProductRelatedImageType.CERT.name();
				} else if (productRelated.equalsIgnoreCase("proof")) {
					prefix = ProductRelatedImageType.PROOF.name();
				} else if (productRelated.equalsIgnoreCase("thirdproof")) {
					prefix = ProductRelatedImageType.THIRDPROOF.name();
//					FileContentType.PRODUCT_THIRDPROOF
				} else if (productRelated.equalsIgnoreCase("selfproof")) {
					prefix = ProductRelatedImageType.SELFPROOF.name();
				}
				
			} 
			if (StringUtils.isNull(prefix)) {
				imagePath
				//.append(scheme).append("://").append(merchantStore.getDomainName())
				//.append(Constants.STATIC_URI)
				.append(ImageFilePathUtils.buildProductImageFilePath(merchantStore, product, this.getImageName())).toString();
			} else {
				imagePath.append(ImageFilePathUtils.buildProductRelatedImageFilePath(merchantStore, product, this.getImageName(), prefix)).toString();

			}
				

			

			pageContext.getOut().print(imagePath.toString());


			
		} catch (Exception ex) {
			LOGGER.error("Error while getting content url", ex);
		}
		return SKIP_BODY;
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}


	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getImageType() {
		return imageType;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Product getProduct() {
		return product;
	}

	public String getProductRelated() {
		return productRelated;
	}

	public void setProductRelated(String productRelated) {
		this.productRelated = productRelated;
	}

}
