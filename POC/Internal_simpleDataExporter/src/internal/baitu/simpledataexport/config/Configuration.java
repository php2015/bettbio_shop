package internal.baitu.simpledataexport.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Configuration {
	protected String dbUrl;
	protected String dbUser;
	protected String dbPassword;
	protected String sqlQuery;
	protected String dbDriver;
	protected Map<String, String> outputFieldMap;
	protected List<String> processCategory;
	protected List<String> processPrice;
	protected File outputFile;
	protected int outputSplit;
	
	
	public int getOutputSplit() {
		return outputSplit;
	}
	public void setOutputSplit(int outputSplit) {
		this.outputSplit = outputSplit;
	}
	public File getOutputFile() {
		return outputFile;
	}
	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}
	public List<String> getProcessPrice() {
		return processPrice;
	}
	public void setProcessPrice(List<String> processPrice) {
		this.processPrice = processPrice;
	}
	public List<String> getProcessCategory() {
		return processCategory;
	}
	public void setProcessCategory(List<String> processCategory) {
		this.processCategory = processCategory;
	}
	public String getDbUrl() {
		return dbUrl;
	}
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	public String getDbUser() {
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	public String getSqlQuery() {
		return sqlQuery;
	}
	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}
	public Map<String, String> getOutputFieldMap() {
		return outputFieldMap;
	}
	public void setOutputFieldMap(Map<String, String> outputFieldMap) {
		this.outputFieldMap = outputFieldMap;
	}
	
	public String getDbDriver() {
		return dbDriver;
	}
	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}
	public void load(File cfgFile) throws Exception {
		Properties props = new Properties();
		FileInputStream fins = new FileInputStream(cfgFile);
		InputStreamReader rins = new InputStreamReader(fins, "UTF-8");
		props.load(rins);
		fins.close();
		
		PropsUtils utils = new PropsUtils();
		utils.setSource(props);
		
		setDbUrl(utils.getString("db_url"));
		setDbUser(utils.getString("db_username"));
		setDbPassword(utils.getString("db_password"));
		setDbDriver(utils.getString("db_jdbc_driver"));
		setSqlQuery(utils.getString("sql_query"));
		setProcessCategory(utils.getStringList("process_categories"));
		setProcessPrice(utils.getStringList("process_price"));
		setOutputFieldMap(utils.getOutputFieldMap());
		setOutputFile(new File(utils.getString("out_file")));
		setOutputSplit(utils.getInt("out_file.split", 2000));
	}
}
