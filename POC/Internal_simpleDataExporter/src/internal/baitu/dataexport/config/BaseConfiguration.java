package internal.baitu.dataexport.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import internal.baitu.simpledataexport.config.PropsUtils;

public abstract class BaseConfiguration {

	protected Properties props;
	protected File baseFolder;
	
	public void setSource(Properties props) {
		this.props = props;
	}
	public String getString(String name) {
		return props.getProperty(name).trim();
	}
	public Map<String, String> getOutputFieldMapFromProps() {
		Map<String, String> fdmap = new HashMap<>();
		Set<Object> propNames = props.keySet();
		for(Object propName: propNames){
			if (!(propName instanceof String)){
				continue;
			}
			String name = (String) propName;
			if (!name.startsWith("out_field_")){
				continue;
			}
			String value = props.getProperty(name);
			fdmap.put(name.substring("out_field_".length()).toLowerCase(), value);
		}
		return fdmap;
	}
	public List<String> getStringList(String name) {
		String value = props.getProperty(name).trim();
		List<String> result = new ArrayList<String>();
		for(String str: value.split(",")){
			if (str.isEmpty()){
				continue;
			}
			result.add(str.trim());
		}
		return result;
	}
	public int getInt(String name, int defaultValue) {
		String value = props.getProperty(name).trim();
		if (value == null){
			return defaultValue;
		}
		try{
			return Integer.parseInt(value);
		}catch (Exception e){
			return defaultValue;
		}
	}

	public File getFile(String name, String defaultFileName) {
		String value = props.getProperty(name).trim();
		if (value == null){
			return new File(defaultFileName);
		}
		return new File(value);
	}
	
	public File getFile(String name, String defaultFileName, File baseFile) {
		String value = props.getProperty(name).trim();
		if (value == null){
			return new File(baseFile, defaultFileName);
		}
		return new File(baseFile, value);
	}
	public void load(File cfgFile) throws Exception {
		Properties props = new Properties();
		FileInputStream fins = new FileInputStream(cfgFile);
		InputStreamReader rins = new InputStreamReader(fins, "UTF-8");
		props.load(rins);
		fins.close();
		setSource(props);
		
		loadProperties();
	}
	protected abstract void loadProperties() throws Exception;
	public File getBaseFolder() {
		return baseFolder;
	}
	public void setBaseFolder(File baseFolder) {
		this.baseFolder = baseFolder;
	}
}
