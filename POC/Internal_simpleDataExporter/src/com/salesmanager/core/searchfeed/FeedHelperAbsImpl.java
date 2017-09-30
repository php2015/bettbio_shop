package com.salesmanager.core.searchfeed;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FeedHelperAbsImpl implements SearchFeedHelper {
	public static final int DATA_TYPE_INT = 0;
	public static final int DATA_TYPE_LONG = 1;
	public static final int DATA_TYPE_STRING = 2;
	public static final int DATA_TYPE_BOOL = 3;
	public static final int DATA_TYPE_DATE = 4;
	protected static final Map<String, Integer> fieldTypes;

	static {
		fieldTypes = new HashMap<String, Integer>();
		fieldTypes.put("int", DATA_TYPE_INT);
		fieldTypes.put("long", DATA_TYPE_LONG);
		fieldTypes.put("string", DATA_TYPE_STRING);
		fieldTypes.put("bool", DATA_TYPE_BOOL);
		fieldTypes.put("date", DATA_TYPE_DATE);
	}

	protected FeedHelperConfig config;
	protected Connection connection;

	public FeedHelperConfig getConfig() {
		return config;
	}

	public void setConfig(FeedHelperConfig config) {
		this.config = config;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public static void constructOutputNames(ResultSetMetaData mData, Map<String, String> outputMap,
			List<String> names, List<Integer> types) throws SQLException {
		int cnt = mData.getColumnCount();
		for (int i = 0; i < cnt; i++) {
			String sqlName;
			String outputName;
			sqlName = mData.getColumnLabel(i + 1);
			outputName = outputMap.get(sqlName.toLowerCase());
			// System.out.println(sqlName);
			if (outputName != null) {
				String[] strs = outputName.split(",");
				names.add(strs[0]);
				types.add(fieldTypes.get(strs[1]));
				continue;
			}

			sqlName = mData.getColumnName(i + 1);
			outputName = outputMap.get(sqlName.toLowerCase());
			// System.out.println(sqlName);
			if (outputName != null) {
				String[] strs = outputName.split(",");
				names.add(strs[0]);
				types.add(fieldTypes.get(strs[1]));
				continue;
			}

			sqlName = mData.getTableName(i + 1) + "." + mData.getColumnName(i + 1);
			outputName = outputMap.get(sqlName.toLowerCase());
			if (outputName != null) {
				String[] strs = outputName.split(",");
				names.add(strs[0]);
				types.add(fieldTypes.get(strs[1]));
				continue;
			}

			names.add(mData.getColumnLabel(i + 1));
			types.add(DATA_TYPE_STRING);
		}
	}
}
