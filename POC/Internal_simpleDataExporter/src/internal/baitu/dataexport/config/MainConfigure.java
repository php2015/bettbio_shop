package internal.baitu.dataexport.config;

import java.io.File;

import internal.baitu.dataexport.intf.OutputHandler;
import internal.baitu.dataexport.intf.Worker;

public class MainConfigure extends BaseConfiguration {
	protected String dbUrl;
	protected String dbUser;
	protected String dbPassword;
	protected String dbDriver;
	protected File workerCfgFile;
	protected File outputCfgFile;
	protected int fileSplitSize;
	protected String outputFileName;
	protected Worker mainWorker;
	protected OutputHandler outputHandler;
	
	protected WorkerConfig  mainWorkerCfg;
	protected OutputConfig  outputHandlerCfg;
	
	@Override
	protected void loadProperties() throws Exception {
		setDbUrl(getString("db_url"));
		setDbUser(getString("db_username"));
		setDbPassword(getString("db_password"));
		setDbDriver(getString("db_jdbc_driver"));
		setOutputFileName(getString("out_file"));
		
		setBaseFolder(getFile("base_folder","."));
		setWorkerCfgFile(getFile("start_worker", null, baseFolder));
		setOutputCfgFile(getFile("output_worker",null,baseFolder));
		
		setFileSplitSize(getInt("out_file.split", 2000));
		
		mainWorkerCfg = new WorkerConfig();
		mainWorkerCfg.setBaseFolder(getBaseFolder());
		mainWorkerCfg.load(workerCfgFile);
		mainWorker = mainWorkerCfg.getWorker();
		
		outputHandlerCfg = new OutputConfig();
		outputHandlerCfg.setBaseFolder(getBaseFolder());
		outputHandlerCfg.load(outputCfgFile);
		outputHandler = outputHandlerCfg.getOutputHandler(); 
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



	public String getDbDriver() {
		return dbDriver;
	}



	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}



	public File getWorkerCfgFile() {
		return workerCfgFile;
	}



	public void setWorkerCfgFile(File workerCfgFile) {
		this.workerCfgFile = workerCfgFile;
	}



	public int getFileSplitSize() {
		return fileSplitSize;
	}



	public void setFileSplitSize(int fileSplitSize) {
		this.fileSplitSize = fileSplitSize;
	}



	public String getOutputFileName() {
		return outputFileName;
	}



	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}



	public Worker getMainWorker() {
		return mainWorker;
	}



	public void setMainWorker(Worker mainWorker) {
		this.mainWorker = mainWorker;
	}



	public OutputHandler getOutputHandler() {
		return outputHandler;
	}



	public void setOutputHandler(OutputHandler outputHandler) {
		this.outputHandler = outputHandler;
	}

	public File getOutputCfgFile() {
		return outputCfgFile;
	}

	public void setOutputCfgFile(File outputCfgFile) {
		this.outputCfgFile = outputCfgFile;
	}

	public WorkerConfig getMainWorkerCfg() {
		return mainWorkerCfg;
	}

	public void setMainWorkerCfg(WorkerConfig mainWorkerCfg) {
		this.mainWorkerCfg = mainWorkerCfg;
	}

	public OutputConfig getOutputHandlerCfg() {
		return outputHandlerCfg;
	}

	public void setOutputHandlerCfg(OutputConfig outputHandlerCfg) {
		this.outputHandlerCfg = outputHandlerCfg;
	}



}
