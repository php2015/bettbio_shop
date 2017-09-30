package com.salesmanager.web.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.catalog.product.model.image.ProductImage;
import com.salesmanager.core.business.catalog.product.service.ProductService;
import com.salesmanager.core.business.content.model.FileContentType;

@Component("readImgDataThread")
@Scope("prototype")
public class ReadImgDataThread implements Runnable {

	private static Logger logger = Logger.getLogger(ReadImgDataThread.class);
	
	private Long storeId;
	private String filepath="/udisk/file";
	private FileOutputStream outputStream;
	private BufferedOutputStream bufferOutputStream;
	@Autowired
	private ProductService productService;

	@Override
	public void run() {

		File file = new File(filepath+"/imgfile.txt");
		try {
			outputStream = new FileOutputStream(file);
			bufferOutputStream = new BufferedOutputStream(outputStream);
		} catch (Exception e) {
			logger.info("Create FileOutputStream Error " + e.getMessage());
		}
		
		List<Object> ids = new ArrayList<Object>();
		if(storeId!=null&&storeId!=1) ids = productService.getAllProductIDByStoreID(storeId);
		else ids = productService.getAllProductID();
		
		int idSize = ids.size();
		logger.info("Wait For Processing  Product Size " + idSize);
		String threadName = Thread.currentThread().getName();
		long startTime = System.currentTimeMillis();
		logger.info("Current Processing... Thread Name "+threadName+ " Begin Time " + DateUtil.generateTimeStamp() + " Process Size " + ids.size());
		try {
			for(Object id : ids) {
		 		BigInteger bi = new BigInteger(id.toString());
		 		Product product = productService.getById(bi.longValue());
		 		ProductImage image = product.getProductImage();
				if(image!=null) {		
					StringBuilder imgpath = new StringBuilder().append("/static")
								.append("/").append(product.getMerchantStore().getCode())
								.append("/").append(FileContentType.PRODUCT.name())
								.append("/").append(product.getSku())
								.append("/").append(image.getProductImage());
					logger.info("imgpath >>> " + imgpath);
					
					bufferOutputStream.write(imgpath.append("\n").toString().getBytes());
					bufferOutputStream.flush();
				}
			}
		} catch (Exception e) {
			logger.info("Write FileOutputStream Error " + e.getMessage());
		}finally{
			try {
				bufferOutputStream.close();
				outputStream.close();
			} catch (Exception ex) {
				logger.info("Close FileOutputStream Error " + ex.getMessage());
			}
		}
		
		long endTime = System.currentTimeMillis();
		long minute = (endTime - startTime) / (1000 * 60);
		logger.info("Current Procesed Thread Name "+threadName+" " + " End Time "+DateUtil.generateTimeStamp() +" Procese Minute " + minute);
	
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
}
