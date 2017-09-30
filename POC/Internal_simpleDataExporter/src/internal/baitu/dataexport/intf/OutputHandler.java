package internal.baitu.dataexport.intf;

import java.util.Map;

import internal.baitu.dataexport.config.OutputConfig;

public interface OutputHandler {
	public void init(String fileName, int splitSize, OutputConfig config) throws Exception;
	public void outputData(Map<String, Object>data) throws Exception;
	public void close();
}
