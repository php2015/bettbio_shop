package com.salesmanager.web.entity.catalog.product;

import java.io.Serializable;
import java.util.List;

import com.salesmanager.web.entity.catalog.ReadableCertificate;
import com.salesmanager.web.entity.catalog.ReadableImage;
import com.salesmanager.web.entity.catalog.ReadableProof;
import com.salesmanager.web.entity.catalog.ReadableSelfProof;
import com.salesmanager.web.entity.catalog.ReadableThirdProof;
import com.salesmanager.web.entity.catalog.manufacturer.ReadableManufacturer;
import com.salesmanager.web.entity.catalog.product.attribute.ReadableProductAttribute;
import com.salesmanager.web.entity.shop.Store;

public class ReadableProduct extends ProductEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ProductDescription description;	
	private ReadableImage image;
	private List<ReadableImage> images;
	private ReadableManufacturer manufacturer;
	private List<ReadableProductAttribute> attributes;
	private List<ReadableCertificate> productCertificates;
	private List<ReadableProof> productProofs;
	private List<ReadableThirdProof> thirdProofs;
	private List<ReadableSelfProof> selfProofs;
	private List<ReadableProductPrice> prices;
	private int certificateNum=0;
	private int proofNum=0;
	private int thirdNum=0;
	private int selfproofNum=0;
	private Store store; //商品所属商铺
	private Quality quality;
	
	
	public List<ReadableProof> getProductProofs() {
		return productProofs;
	}
	public void setProductProofs(List<ReadableProof> productProofs) {
		this.productProofs = productProofs;
	}
	public ProductDescription getDescription() {
		return description;
	}
	public void setDescription(ProductDescription description) {
		this.description = description;
	}
	
	public void setImages(List<ReadableImage> images) {
		this.images = images;
	}
	public List<ReadableImage> getImages() {
		return images;
	}
	public void setImage(ReadableImage image) {
		this.image = image;
	}
	public ReadableImage getImage() {
		return image;
	}
	public void setAttributes(List<ReadableProductAttribute> attributes) {
		this.attributes = attributes;
	}
	public List<ReadableProductAttribute> getAttributes() {
		return attributes;
	}
	public void setManufacturer(ReadableManufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}
	public ReadableManufacturer getManufacturer() {
		return manufacturer;
	}
	public List<ReadableCertificate> getProductCertificates() {
		return productCertificates;
	}
	public void setProductCertificates(List<ReadableCertificate> productCertificates) {
		this.productCertificates = productCertificates;
	}
	public List<ReadableThirdProof> getThirdProofs() {
		return thirdProofs;
	}
	public void setThirdProofs(List<ReadableThirdProof> thirdProofs) {
		this.thirdProofs = thirdProofs;
	}
	public List<ReadableProductPrice> getPrices() {
		return prices;
	}
	public void setPrices(List<ReadableProductPrice> prices) {
		this.prices = prices;
	}
	public int getCertificateNum() {
		return certificateNum;
	}
	public void setCertificateNum(int certificateNum) {
		this.certificateNum = certificateNum;
	}
	public int getProofNum() {
		return proofNum;
	}
	public void setProofNum(int proofNum) {
		this.proofNum = proofNum;
	}
	public int getThirdNum() {
		return thirdNum;
	}
	public void setThirdNum(int thirdNum) {
		this.thirdNum = thirdNum;
	}
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	public List<ReadableSelfProof> getSelfProofs() {
		return selfProofs;
	}
	public void setSelfProofs(List<ReadableSelfProof> selfProofs) {
		this.selfProofs = selfProofs;
	}
	public int getSelfproofNum() {
		return selfproofNum;
	}
	public Quality getQuality() {
		return quality;
	}
	public void setQuality(Quality quality) {
		this.quality = quality;
	}
	public void setSelfproofNum(int selfproofNum) {
		this.selfproofNum = selfproofNum;
	}
	
}
