package com.salesmanager.core.business.catalog.product.service;

import org.elasticsearch.action.bulk.BulkItemResponse.Failure;

public interface FullIndexCallback {
	public void onFeedStart();
	public void onFeedFinished();
	
	public void setTotalProductNumber(long total);
	
	public void onProductIndexed(long productId);
	public void onProductIgnored(long productId);
	public void onProductException(long productId, Exception e);
	public void onIndexCleared();
	public void onException(Exception e);
	public void onProductFailure(long id, Failure failure);
	
	public boolean isRunning();
	public void reset();
	public void setIgnoredProductNumber(long invalidNumber);
}
