package com.salesmanager.web.images;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.catalog.product.model.file.ProductImageSize;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.content.model.FileContentType;
import com.salesmanager.core.business.content.model.OutputContentFile;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.generic.exception.ServiceException;

@Controller
public class ImagesController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImagesController.class);
	

	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private ProductImageService productImageService;
	
	/**
	 * Logo, content image
	 * @param storeId
	 * @param imageType (LOGO, CONTENT, IMAGE)
	 * @param imageName
	 * @return
	 * @throws IOException
	 * @throws ServiceException 
	 */
	@RequestMapping("/static/{storeCode}/{imageType}/{imageName}.{extension}")
	public @ResponseBody byte[] printImage(@PathVariable final String storeCode, @PathVariable final String imageType, @PathVariable final String imageName, @PathVariable final String extension) throws IOException, ServiceException {

		// example -> /static/mystore/CONTENT/myImage.png
		
		FileContentType imgType = null;
		
		if(FileContentType.LOGO.name().equals(imageType)) {
			imgType = FileContentType.LOGO;
		}
		
		if(FileContentType.IMAGE.name().equals(imageType)) {
			imgType = FileContentType.IMAGE;
		}
		
		if(FileContentType.PROPERTY.name().equals(imageType)) {
			imgType = FileContentType.PROPERTY;
		}
		
		if(FileContentType.STORELICENCE.name().equals(imageType)) {
			imgType = FileContentType.STORELICENCE;
		}
		
		OutputContentFile image =contentService.getContentFile(storeCode, imgType, new StringBuilder().append(imageName).append(".").append(extension).toString());
		
		
		if(image!=null) {
			return image.getFile().toByteArray();
		} else {
			//empty image placeholder
			return null;
		}

	}
	

	@RequestMapping("/static/{storeCode}/{imageType}/{productCode}/{imageName}.{extension}")
	public @ResponseBody byte[] printImage(@PathVariable final String storeCode, @PathVariable final String productCode, @PathVariable final String imageType, @PathVariable final String imageName, @PathVariable final String extension) throws IOException {

		// product image
		// example small product image -> /static/1/PRODUCT/120/product1.jpg
		
		// example large product image -> /static/1/PRODUCTLG/120/product1.jpg
		

		ProductImageSize size = ProductImageSize.SMALL;
		
		if(imageType.equals(FileContentType.PRODUCTLG.name())) {
			size = ProductImageSize.LARGE;
		} 
		

		
		OutputContentFile image = null;
		try {
			image = productImageService.getProductImage(storeCode, productCode, new StringBuilder().append(imageName).append(".").append(extension).toString(), size);
		} catch (ServiceException e) {
			LOGGER.error("Cannot retrieve image " + imageName, e);
		}
		if(image!=null) {
			return image.getFile().toByteArray();
		} else {
			//empty image placeholder
			return null;
		}

	}
	
	/**
	 * 访问获取产品相关的参考文献或者是购买凭证的图片信息
	 * @param storeCode
	 * @param productCode
	 * @param imageType
	 * @param imageName
	 * @param productRelated
	 * @param extension
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/static/{storeCode}/{imageType}/{productCode}/{productRelated}/{imageName}.{extension}")
	public @ResponseBody byte[] printProductRelatedImage(@PathVariable final String storeCode, @PathVariable final String productCode, @PathVariable final String imageType, @PathVariable final String imageName, @PathVariable final String productRelated, @PathVariable final String extension) throws IOException {

		// product image
		// example productrelated image -> /static/1/PRODUCT/120/CERT/product1.jpg /static/1/PRODUCT/120/PROOF/product1.jpg
		
		OutputContentFile image = null;
		try {
			image = productImageService.getProductRelatedImage(storeCode, productCode, new StringBuilder().append(imageName).append(".").append(extension).toString(), productRelated);
		} catch (ServiceException e) {
			LOGGER.error("Cannot retrieve image " + imageName, e);
		}
		if(image!=null) {
			return image.getFile().toByteArray();
		} else {
			//empty image placeholder
			return null;
		}

	}

}
