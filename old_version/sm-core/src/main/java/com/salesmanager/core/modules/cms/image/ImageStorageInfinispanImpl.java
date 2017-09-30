package com.salesmanager.core.modules.cms.image;

import org.apache.commons.io.IOUtils;
import org.infinispan.tree.Fqn;
import org.infinispan.tree.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.modules.cms.content.CmsStaticContentFileManagerInfinispanImpl;
import com.salesmanager.core.modules.cms.impl.CacheManager;

/**
 * Manager for storing in retrieving image files from the CMS This is a layer on
 * top of Infinispan https://docs.jboss.org/author/display/ISPN/Tree+API+Module
 * 
 * Manages - Product images
 * 
 * @author Carl Samson
 */
public class ImageStorageInfinispanImpl implements ImageStorage {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageStorageInfinispanImpl.class);

	private static ImageStorageInfinispanImpl fileManager;

	private CacheManager cacheManager;

	public void stopFileManager() {
		try {
			cacheManager.getManager().stop();
			LOGGER.info("Stopping ImageStorage");
		} catch (final Exception e) {
			LOGGER.error("Error while stopping ImageStorage", e);
		}
	}

	public static ImageStorageInfinispanImpl getInstance() {
		if (fileManager == null) {
			fileManager = new ImageStorageInfinispanImpl();
		}
		return fileManager;
	}

	@SuppressWarnings({ "unchecked" })
	private Node<String, Object> getNode(final String node) {
		LOGGER.debug("Fetching node for image {0} from Infinispan", node);
		Fqn contentFilesFqn = Fqn.fromString(node);
		Node<String, Object> nd = cacheManager.getTreeCache().getRoot().getChild(contentFilesFqn);
		if (nd == null) {
			cacheManager.getTreeCache().getRoot().addChild(contentFilesFqn);
			nd = cacheManager.getTreeCache().getRoot().getChild(contentFilesFqn);
		}
		return nd;
	}

	@Override
	public void saveImage(ImageDataInfo imgData) throws ServiceException {
		if (cacheManager.getTreeCache() == null) {
			LOGGER.error("Unable to find cacheManager.getTreeCache() in Infinispan..");
			throw new ServiceException(
					"ImageStorageInfinispanImpl has a null cacheManager.getTreeCache()");
		}
		try {
			String nodePath = imgData.getUri();
			final Node<String, Object> imageNode = this.getNode(nodePath);
			imageNode.put(nodePath, imgData);
			LOGGER.info("Image data saved successfully.");
		} catch (final Exception e) {
			LOGGER.error("Error while saving image data", e);
			throw new ServiceException(e);
		}
	}

	@Override
	public ImageDataInfo getImage(String uri) throws ServiceException {
		if (cacheManager.getTreeCache() == null) {
			LOGGER.error("Unable to find cacheManager.getTreeCache() in Infinispan..");
			throw new ServiceException(
					"ImageStorageInfinispanImpl has a null cacheManager.getTreeCache()");
		}
		try {
			String nodePath = uri;
			final Node<String, Object> imageNode = this.getNode(nodePath);
			ImageDataInfo result = (ImageDataInfo) imageNode.get(nodePath);
			LOGGER.info("Image data loaded successfully.");
			return result;
		} catch (final Exception e) {
			LOGGER.error("Error while load image data", e);
			throw new ServiceException(e);
		}
	}

	@Override
	public void removeImage(String uri) throws ServiceException {
		if (cacheManager.getTreeCache() == null) {
			LOGGER.error("Unable to find cacheManager.getTreeCache() in Infinispan..");
			throw new ServiceException(
					"ImageStorageInfinispanImpl has a null cacheManager.getTreeCache()");
		}
		try {
			String nodePath = uri;
			final Node<String, Object> imageNode = this.getNode(nodePath);
			imageNode.remove(nodePath);
			LOGGER.info("Image data removed successfully.");
		} catch (final Exception e) {
			LOGGER.error("Error while remove image data", e);
			throw new ServiceException(e);
		}
	}
	
	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
}
