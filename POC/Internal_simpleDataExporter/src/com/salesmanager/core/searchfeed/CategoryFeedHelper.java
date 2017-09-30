package com.salesmanager.core.searchfeed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CategoryFeedHelper extends FeedHelperAbsImpl{
	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryFeedHelper.class);
	protected List<String> outputNames = null;
	protected Map<Object, List<Object>> datas;
	private int[] fieldDataTypes;
	private int idFieldIdx;
	@Override
	public void prepare() throws Exception {
		PreparedStatement stmt = connection.prepareStatement(getConfig().getQuerySql());
		ResultSet rs = stmt.executeQuery();
		outputNames = null;
		datas = new HashMap<Object, List<Object>>();
		rs.last();
		int rowNum = rs.getRow();
		LOGGER.info("Totally has " + rowNum+" " + getConfig().getHelperName()+" records will be cached");
		rs.beforeFirst();
		while(rs.next()){
			if (outputNames == null){
				constructOutputNames(rs);
			}
			List<Object> rowData = new ArrayList<Object>();
			for(int i=0;i<outputNames.size();i++){
				Object obj = rs.getObject(i+1);
				if (obj == null){
					rowData.add(null);
					continue;
				}
				switch(fieldDataTypes[i]){
				case DATA_TYPE_INT:
					rowData.add(rs.getInt(i+1));
					break;
				case DATA_TYPE_LONG:
					rowData.add(rs.getLong(i+1));
					break;
				case DATA_TYPE_BOOL:
					rowData.add(rs.getBoolean(i+1));
					break;
				case DATA_TYPE_DATE:
					rowData.add(rs.getDate(i+1));
					break;
				default:
					rowData.add(rs.getString(i+1));
					break;
				}
			}
			//System.out.println(rowData);
			datas.put(rowData.get(idFieldIdx), rowData);
		}
	}
	protected void constructOutputNames(ResultSet rSet) throws Exception {
		ResultSetMetaData mData = rSet.getMetaData();
		outputNames = new ArrayList<String>();
		ArrayList<Integer> outputTypes = new ArrayList<Integer>();
		constructOutputNames(mData, config.getOutputMap(), outputNames, outputTypes);
		LOGGER.info(getConfig().getHelperName()+" will retrieve " + outputNames);
		this.fieldDataTypes = new int[outputTypes.size()];
		for(int i=0;i<outputTypes.size();i++){
			fieldDataTypes[i] = outputTypes.get(i);
			if (outputNames.get(i).equals(getConfig().getIdFieldName())){
				this.idFieldIdx = i;
			}
		}
	}
	@Override
	public void process(JSONArray batchData, ArrayList<String> outputNames, HashMap<Object, List<Object>> datas) {
		String name = getConfig().getInputFieldName();
		for(Object obj: batchData){
			JSONObject json = (JSONObject) obj;
			Object leafCategoryID = json.get(name);
			if (leafCategoryID == null){
				continue;
			}
			JSONArray categories = createCategoryArray(leafCategoryID);
			json.put("category", categories);
		}
	}
	private JSONArray createCategoryArray(Object leafCategoryID) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
