package com.salesmanager.web.admin.entity.catalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import com.salesmanager.core.business.catalog.product.model.availability.ProductAvailability;
import com.salesmanager.core.business.catalog.product.model.description.ProductDescription;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.model.price.ProductPrice;

public class Product implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4531526676134574984L;

	/**
	 * 
	 */

	//provides wrapping to the main product entity
	@Valid
	private com.salesmanager.core.business.catalog.product.model.Product product;
	
	@Valid
	private List<ProductDescription> descriptions = new ArrayList<ProductDescription>();
	
	@Valid
	private ProductAvailability availability = null;
	
	@Valid
	private ProductPrice price = null;
	
	private List<MultipartFile> image = null;
	
	private ProductImage productImage = null;
	
	private List<ProductImage> imagesName = null;
	
	private int ImagesLength=0;
	
	private List<Integer> imgid = null;
	
	@NotEmpty
	private String productPrice = "0";
	
	private Integer productPriceStock = 0;
	
	private String dateAvailable;

	private ProductDescription description = null;
	
	public String getDateAvailable() {
		return dateAvailable;
	}
	public void setDateAvailable(String dateAvailable) {
		this.dateAvailable = dateAvailable;
	}
	public com.salesmanager.core.business.catalog.product.model.Product getProduct() {
		return product;
	}
	public void setProduct(com.salesmanager.core.business.catalog.product.model.Product product) {
		this.product = product;
	}
	
	public List<ProductDescription> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(List<ProductDescription> descriptions) {
		this.descriptions = descriptions;
	}
	public void setAvailability(ProductAvailability availability) {
		this.availability = availability;
	}
	public ProductAvailability getAvailability() {
		return availability;
	}
	public void setPrice(ProductPrice price) {
		this.price = price;
	}
	public ProductPrice getPrice() {
		return price;
	}

	public List<MultipartFile> getImage() {
		return image;
	}
	public void setImage(List<MultipartFile> image) {
		this.image = image;
	}
	public List<ProductImage> getImagesName() {
		return imagesName;
	}
	public void setImagesName(List<ProductImage> imagesName) {
		this.imagesName = imagesName;
	}
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public void setProductImage(ProductImage productImage) {
		this.productImage = productImage;
	}
	public ProductImage getProductImage() {
		return productImage;
	}
	public void setDescription(ProductDescription description) {
		this.description = description;
	}
	public ProductDescription getDescription() {
		return description;
	}
	public Integer getProductPriceStock() {
		return productPriceStock;
	}
	public void setProductPriceStock(Integer productPriceStock) {
		this.productPriceStock = productPriceStock;
	}
	public int getImagesLength() {
		return ImagesLength;
	}
	public void setImagesLength(int imagesLength) {
		ImagesLength = imagesLength;
	}
	public List<Integer> getImgid() {
		return imgid;
	}
	public void setImgid(List<Integer> imgid) {
		this.imgid = imgid;
	}
	

}
