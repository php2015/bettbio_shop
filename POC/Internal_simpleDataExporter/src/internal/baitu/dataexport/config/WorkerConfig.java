package internal.baitu.dataexport.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import internal.baitu.dataexport.intf.Worker;

public class WorkerConfig extends BaseConfiguration {
	protected String sql;
	protected Map<String, String> outputFieldMap;
	protected Map<String, WorkerConfig> subWorkers;
	protected Worker worker;
	protected String inputType;
	protected List<String> titlesWhenEmpty;
	
	@Override
	protected void loadProperties() throws Exception {
		setSql(getString("sql_main"));
		setOutputFieldMap(getOutputFieldMapFromProps());
		setSubWorkers(getSubworkConfigs());
		setInputType(getString("input_type"));
		worker = (Worker) Class.forName(getString("class")).newInstance();
		titlesWhenEmpty = getStringList("titles_when_empty");
	}


	private Map<String, WorkerConfig> getSubworkConfigs() throws Exception {
		Map<String, WorkerConfig> result = new HashMap<String, WorkerConfig>();
		Set<Object> propNames = props.keySet();
		for(Object propName: propNames){
			if (!(propName instanceof String)){
				continue;
			}
			String name = (String) propName;
			if (!name.startsWith("worker_")){
				continue;
			}
			WorkerConfig subWorkerCfg = new WorkerConfig();
			subWorkerCfg.setBaseFolder(getBaseFolder());
			subWorkerCfg.load(getFile(name, null, getBaseFolder()));
			result.put(name.substring("worker_".length()).toLowerCase(), subWorkerCfg);
		}
		return result;
	}


	public String getSql() {
		return sql;
	}


	public void setSql(String sql) {
		this.sql = sql;
	}


	public Map<String, String> getOutputFieldMap() {
		return outputFieldMap;
	}


	public void setOutputFieldMap(Map<String, String> outputFieldMap) {
		this.outputFieldMap = outputFieldMap;
	}


	public Map<String, WorkerConfig> getSubWorkers() {
		return subWorkers;
	}


	public void setSubWorkers(Map<String, WorkerConfig> subWorkers) {
		this.subWorkers = subWorkers;
	}


	public Worker getWorker() {
		return worker;
	}


	public void setWorker(Worker worker) {
		this.worker = worker;
	}


	public String getInputType() {
		return inputType;
	}


	public void setInputType(String inputType) {
		this.inputType = inputType;
	}


	public List<String> getTitlesWhenEmpty() {
		return titlesWhenEmpty;
	}


	public void setTitlesWhenEmpty(List<String> titlesWhenEmpty) {
		this.titlesWhenEmpty = titlesWhenEmpty;
	}

}
