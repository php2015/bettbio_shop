package com.salesmanager.core.searchfeed;

public interface SearchFeedCallback {

	void setTotal(String typeName, int totalRow);

	void addStarted(String typeName, int number);
	
	void addFeeded(String typeName, int number);
	
	void addIgnored(String typeName, int number);

	void allStarted(String typeName);

	void finished(String typeName);

}
