package internal.baitu.dataexport.worker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import internal.baitu.dataexport.config.WorkerConfig;
import internal.baitu.dataexport.intf.DataExportConstants;
import internal.baitu.dataexport.intf.OutputHandler;
import internal.baitu.dataexport.intf.Worker;

public class SimpleJdbcWorker implements Worker {

	protected Connection connection;
	protected WorkerConfig config;
	protected PreparedStatement statement;
	protected List<String> fieldNameList = null;
	protected Map<String, WorkerConfig> subWorkers;

	@Override
	public void init(Connection conn, WorkerConfig config) throws Exception {
		this.connection = conn;
		this.config = config;
		this.statement = conn.prepareStatement(config.getSql());
		this.subWorkers = config.getSubWorkers();
		Iterator<WorkerConfig> it = subWorkers.values().iterator();
		while(it.hasNext()){
			WorkerConfig workerCfg = it.next();
			workerCfg.getWorker().init(connection, workerCfg);
		}
	}

	@Override
	public void doJob(String inputValue, Map<String, Object> data, OutputHandler outputHandler, boolean directOutput)
			throws Exception {
		fillStatementParam(inputValue);
		ResultSet rs = statement.executeQuery();
		Map<String, WorkerConfig> subWorkers = config.getSubWorkers();
		List<List<Object>> queryDatas = new ArrayList<List<Object>>();
		
		
		boolean hasData = false;
		while(rs.next()){
			if (fieldNameList == null){
				fillNameList(rs);
			}
			data.put(DataExportConstants.FIELD_NAME_LIST, fieldNameList);
			data.put(DataExportConstants.FIELD_VALUE_ROWS, queryDatas);
			hasData = true;
			List<Object> rowData = new ArrayList<Object>(fieldNameList.size());
			for(int i=0;i<fieldNameList.size();i++){
				String fieldName = fieldNameList.get(i);
				String value = rs.getString(i+1);
				if (subWorkers.containsKey(fieldName.toLowerCase())){
					Worker worker = subWorkers.get(fieldName.toLowerCase()).getWorker();
					HashMap<String, Object> subData = new HashMap<String, Object>();
					if (isEmpty(value)){
						worker.getEmptyData(subData);
					}else{
						worker.doJob(value, subData, outputHandler, false);
					}
					rowData.add(subData);
				}else{
					rowData.add(value);
				}
			}
			queryDatas.add(rowData);
			if (directOutput){
				outputHandler.outputData(data);
				queryDatas.clear();
			}
		}
		
		if (!hasData){
			getEmptyData(data);
		}
	}

	private boolean isEmpty(String value) {
		if (value == null || value.isEmpty()){
			return true;
		}
		if (value.trim().isEmpty()){
			return true;
		}
		return false;
	}

	@Override
	public void getEmptyData(Map<String, Object> data) throws Exception {
		List<String> titles = config.getTitlesWhenEmpty();
		data.put(DataExportConstants.FIELD_NAME_LIST, titles);
		List<Object> rowData = new ArrayList<Object>(titles.size());
		for(int i=0;i<titles.size();i++){
			String fieldName = titles.get(i);
			String value = "";
			if (subWorkers.containsKey(fieldName.toLowerCase())){
				Worker worker = subWorkers.get(fieldName.toLowerCase()).getWorker();
				HashMap<String, Object> subData = new HashMap<String, Object>();
				worker.getEmptyData(subData);
				rowData.add(subData);
			}else{
				rowData.add(value);
			}
		}
		List<Object> datas = new ArrayList<Object>(1);
		datas.add(rowData);
		data.put(DataExportConstants.FIELD_VALUE_ROWS, datas);
	}

	protected void fillStatementParam(String inputValue) throws Exception {
		if (inputValue == null || inputValue.isEmpty() || inputValue.trim().isEmpty() || inputValue.equalsIgnoreCase("null")){
			return;
		}
		String type = config.getInputType();
		if (type == null || type.isEmpty() || type.trim().isEmpty() || type.equalsIgnoreCase("null")){
			return;
		}
		
		if (type.trim().equalsIgnoreCase("long")){
			statement.setLong(1, Long.parseLong(inputValue));
			return;
		}
		
		if (type.trim().equalsIgnoreCase("string")){
			statement.setString(1, inputValue);
			return;
		}
	}

	protected void fillNameList(ResultSet rSet) throws Exception {
		ResultSetMetaData mData = rSet.getMetaData();
		int cnt = mData.getColumnCount();
		fieldNameList = new ArrayList<String>();
		for (int i = 0; i < cnt; i++) {
			String sqlName;
			String outputName;
			sqlName = mData.getColumnLabel(i + 1);
			outputName = config.getOutputFieldMap().get(sqlName.toLowerCase());
			// System.out.println(sqlName);
			if (outputName != null) {
				fieldNameList.add(outputName);
				continue;
			}

			sqlName = mData.getColumnName(i + 1);
			outputName = config.getOutputFieldMap().get(sqlName.toLowerCase());
			// System.out.println(sqlName);
			if (outputName != null) {
				fieldNameList.add(outputName);
				continue;
			}
			
			sqlName= mData.getTableName(i + 1) + "." + mData.getColumnName(i + 1);
			outputName = config.getOutputFieldMap().get(sqlName.toLowerCase());
			if (outputName != null) {
				fieldNameList.add(outputName);
				continue;
			}
			
			fieldNameList.add(sqlName);
		}
	}

}
