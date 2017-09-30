package com.salesmanager.web.images;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

import com.salesmanager.core.business.catalog.product.model.file.ProductImageSize;
import com.salesmanager.core.business.catalog.product.service.image.ProductImageService;
import com.salesmanager.core.business.content.model.FileContentType;
import com.salesmanager.core.business.content.model.OutputContentFile;
import com.salesmanager.core.business.content.service.ContentService;
import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.order.model.SubOrder;
import com.salesmanager.core.business.order.service.OrderService;
import com.salesmanager.core.business.order.service.SubOrderService;
import com.salesmanager.core.business.user.model.Group;
import com.salesmanager.core.business.user.model.User;
import com.salesmanager.core.modules.cms.image.ImageDataInfo;
import com.salesmanager.core.modules.cms.image.ImageStorage;
import com.salesmanager.core.modules.cms.image.PayProofImageUtils;
import com.salesmanager.web.constants.Constants;
import com.salesmanager.web.shop.controller.customer.facade.CustomerFacade;
import com.salesmanager.web.utils.SessionUtil;

@Controller
public class ImagesController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImagesController.class);
	

	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private ProductImageService productImageService;
	
	@Autowired
	private ImageStorage imageStorage;
	
	/**
	 * Logo, content image
	 * @param storeId
	 * @param imageType (LOGO, CONTENT, IMAGE)
	 * @param imageName
	 * @return
	 * @throws IOException
	 * @throws ServiceException 
	 */
	@RequestMapping(value="/static/{storeCode}/{imageType}/{imageName}.{extension}", produces="image/png")
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
	

	@RequestMapping(value="/static/{storeCode}/{imageType}/{productCode}/{imageName}.{extension}", produces="image/png")
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
	@RequestMapping(value="/static/{storeCode}/{imageType}/{productCode}/{productRelated}/{imageName}.{extension}", produces="image/png")
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

	@Autowired
    private CustomerFacade customerFacade;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	SubOrderService subOrderService;

	@RequestMapping(value="orderstatic/**", produces="image/png")
	public @ResponseBody byte[] printPayProofImage(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String imageUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		Map<String, Object> result = PayProofImageUtils.parseImageUri(imageUrl);
		if (result == null) {
			LOGGER.error("Cannot retrieve image " + imageUrl);
			return null;
		}

		try {
			long orderId = Long.parseLong((String) result.get(PayProofImageUtils.ORDER_ID));
			long subOrderId = Long.parseLong((String) result.get(PayProofImageUtils.SUBORDER_ID));
			LOGGER.info( "Fetching orders for current customer" );
			Order order = orderService.getById(orderId);
			SubOrder subOrder = subOrderService.getById(subOrderId);

	        MerchantStore admStore = getSessionAttribute(Constants.ADMIN_STORE, request);
			Customer customer = getSessionAttribute(Constants.CUSTOMER, request);
			User user = getSessionAttribute(Constants.ADMIN_USER, request);
			boolean isAdminUser = false;
			if (user != null){
				for (Group group : user.getGroups()){
					if (group.getGroupName().equalsIgnoreCase("ADMIN")){
						isAdminUser = true;
						break;
					}
				}
			}
			
			if (!isAdminUser && !canAccessOrder(order, subOrder, admStore, customer)){
				LOGGER.error("Current session cannot access " + imageUrl);
				return null;
			}
			
			String imageUri = PayProofImageUtils.getImageUri(orderId, subOrderId,
					(String) result.get(PayProofImageUtils.POSTFIX));
			ImageDataInfo imageData = imageStorage.getImage(imageUri);
			if (imageData == null || imageData.getImageBlob() == null || imageData.getImageBlob().length == 0) {
				LOGGER.error("Did not found image " + imageUrl);
				return null;
			}
			// response.setContentType(imageData.getContentType());
			response.addHeader("Content-Type", imageData.getContentType());
			return imageData.getImageBlob();
		} catch (ServiceException e) {
			LOGGER.error("Cannot retrieve image " + imageUrl, e);
		}
		return null;

	}
	
	@RequestMapping(value="comimg/**", produces="image/png")
	public @ResponseBody byte[] printCommonImage(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String imageUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		if (imageUrl.startsWith("/")){
			imageUrl = imageUrl.substring(1);
		}

		try {
			ImageDataInfo imageData = imageStorage.getImage(imageUrl);
			if (imageData == null || imageData.getImageBlob() == null || imageData.getImageBlob().length == 0) {
				LOGGER.error("Did not found image " + imageUrl);
				return null;
			}
			// response.setContentType(imageData.getContentType());
			response.addHeader("Content-Type", imageData.getContentType());
			return imageData.getImageBlob();
		} catch (ServiceException e) {
			LOGGER.error("Cannot retrieve image " + imageUrl, e);
		}
		return null;

	}


	private boolean canAccessOrder(Order order, SubOrder subOrder, MerchantStore admStore, Customer customer) {
		if (order == null || subOrder == null){
			return false;
		}
		
		if (admStore != null){
			MerchantStore mcht = subOrder.getMerchant();
			if (mcht != null){
				if (mcht.getId().equals(admStore.getId()) || admStore.getCode().equals(Constants.DEFAULT_STORE)){
					LOGGER.debug("Current merchant can access this sub order");
					return true;
				}
			}
		}
		
		if (customer != null){
			if (customer.getId().equals(order.getCustomerId())){
				LOGGER.debug("Current customer can access this sub order");
				return true;
			}
		}
		return false;
	}


	@SuppressWarnings("unchecked")
	private <T> T getSessionAttribute(String key, HttpServletRequest request) {
		return (T)SessionUtil.getSessionAttribute(key, request);
	}
}
