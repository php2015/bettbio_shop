package internal.baitu.dataexport.worker;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import internal.baitu.dataexport.intf.DataExportConstants;
import internal.baitu.dataexport.intf.OutputHandler;

public class OneLineJdbcWorker extends SimpleJdbcWorker {

	@Override
	public void doJob(String inputValue, Map<String, Object> data, OutputHandler outputHandler, boolean directOutput)
			throws Exception {
		fillStatementParam(inputValue);
		ResultSet rs = statement.executeQuery();
		List<List<Object>> queryDatas = new ArrayList<List<Object>>();
		data.put(DataExportConstants.FIELD_VALUE_ROWS, queryDatas);
		
		
		boolean hasData = false;
		List<Object> rowData = new ArrayList<Object>(config.getTitlesWhenEmpty().size());
		queryDatas.add(rowData);
		while(rs.next()){
			if (fieldNameList == null){
				fillNameList(rs);
				data.put(DataExportConstants.FIELD_NAME_LIST, fieldNameList);
			}
			hasData = true;
			for(int i=0;i<fieldNameList.size();i++){
				String value = rs.getString(i+1);
				if (rowData.size() <= i){
					rowData.add(value);
				}else{
					rowData.set(i, rowData.get(i)+", "+value);
				}
			}
			if (directOutput){
				outputHandler.outputData(data);
				queryDatas.clear();
			}
		}
		
		if (!hasData){
			getEmptyData(data);
		}
	}

	@Override
	public void getEmptyData(Map<String, Object> data) throws Exception {
		List<String> titles = config.getTitlesWhenEmpty();
		data.put(DataExportConstants.FIELD_NAME_LIST, titles);
		List<Object> rowData = new ArrayList<Object>(titles.size());
		for(int i=0;i<titles.size();i++){
			rowData.add("");
		}
		List<Object> datas = new ArrayList<Object>(1);
		datas.add(rowData);
		data.put(DataExportConstants.FIELD_VALUE_ROWS, datas);
	}

}