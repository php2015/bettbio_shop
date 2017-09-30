package internal.baitu.simpledataexport.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropsUtils {
	protected Properties props;
	public void setSource(Properties props) {
		this.props = props;
	}
	public String getString(String name) {
		return props.getProperty(name).trim();
	}
	public Map<String, String> getOutputFieldMap() {
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

}
