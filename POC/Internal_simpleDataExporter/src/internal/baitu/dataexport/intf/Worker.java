package internal.baitu.dataexport.intf;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import internal.baitu.dataexport.config.WorkerConfig;

public interface Worker {
	public void init(Connection conn, WorkerConfig config) throws Exception;

	public void doJob(String inputValue, Map<String, Object> data, OutputHandler outputHandler, boolean directOutput)
			throws Exception;

	void getEmptyData(Map<String, Object> data) throws Exception;

}
