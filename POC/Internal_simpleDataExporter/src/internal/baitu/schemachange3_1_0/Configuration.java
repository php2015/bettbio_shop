package internal.baitu.schemachange3_1_0;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import internal.baitu.simpledataexport.config.PropsUtils;

public class Configuration extends internal.baitu.simpledataexport.config.Configuration{
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
	}
}
