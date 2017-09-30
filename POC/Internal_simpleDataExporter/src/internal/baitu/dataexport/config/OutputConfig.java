package internal.baitu.dataexport.config;

import internal.baitu.dataexport.intf.OutputHandler;

public class OutputConfig extends BaseConfiguration {
	protected OutputHandler outputHandler;
	@Override
	protected void loadProperties() throws Exception {
		outputHandler = (OutputHandler) Class.forName(getString("class")).newInstance();
	}
	public OutputHandler getOutputHandler() {
		return outputHandler;
	}
	public void setOutputHandler(OutputHandler outputHandler) {
		this.outputHandler = outputHandler;
	}

}
