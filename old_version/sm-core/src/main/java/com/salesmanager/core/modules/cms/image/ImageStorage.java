package com.salesmanager.core.modules.cms.image;

import com.salesmanager.core.business.generic.exception.ServiceException;

public interface ImageStorage {
	void saveImage(ImageDataInfo imageData) throws ServiceException;
	ImageDataInfo getImage(String imageUri) throws ServiceException;
	void removeImage(String oldImage) throws ServiceException;
}
