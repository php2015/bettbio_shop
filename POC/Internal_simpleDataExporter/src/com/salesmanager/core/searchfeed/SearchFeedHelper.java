package com.salesmanager.core.searchfeed;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;

public interface SearchFeedHelper {

	void setConfig(FeedHelperConfig cfg);

	void setConnection(Connection connection);

	void prepare() throws Exception;

	void process(JSONArray batchData, ArrayList<String> outputNames, HashMap<Object, List<Object>> datas);


}
