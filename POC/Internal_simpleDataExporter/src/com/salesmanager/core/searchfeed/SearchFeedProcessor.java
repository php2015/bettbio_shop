package com.salesmanager.core.searchfeed;

import java.sql.Connection;

public interface SearchFeedProcessor {

	void setConfig(FeedConfig config);

	FeedConfig getConfig();

	void loadSpecialConfig(ConfigLoader cfg) throws Exception;

	void setDbConnection(Connection connection);

	void prepare() throws Exception;

	void startFeed() throws Exception;

	void setFeedProcessCallback(SearchFeedCallback searchFeedCallback);

}
