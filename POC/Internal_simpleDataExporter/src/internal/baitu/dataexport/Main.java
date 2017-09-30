package internal.baitu.dataexport;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.logging.Logger;

import internal.baitu.dataexport.config.MainConfigure;
import internal.baitu.dataexport.intf.OutputHandler;
import internal.baitu.dataexport.intf.Worker;

public class Main {
	static Logger logger = Logger.getLogger(Main.class.getName());
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			showUsage();
			return;
		}

		long sts = System.currentTimeMillis();
		File cfgFile = new File(args[0]);
		if (cfgFile.isFile() && cfgFile.exists()) {
			MainConfigure cfg = new MainConfigure();
			cfg.load(cfgFile);
			
			
			Class.forName(cfg.getDbDriver());
			Connection connection = (Connection) DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(),
					cfg.getDbPassword());
			logger.info("Connect to DB success");
			
			Worker worker = cfg.getMainWorker();
			worker.init(connection, cfg.getMainWorkerCfg());
			
			OutputHandler outHandler = cfg.getOutputHandler();
			outHandler.init(cfg.getOutputFileName(), cfg.getFileSplitSize(), cfg.getOutputHandlerCfg());
			
			worker.doJob(null, new HashMap<String, Object>(), outHandler, true);
			
			outHandler.close();
			connection.close();
			
		} else {
			System.out.println("File " + cfgFile.getAbsolutePath() + " not valid");
		}
		long ets = System.currentTimeMillis();
		System.out.println("Used " + ((ets - sts) / 1000.0) + "Secs");
	}

	private static void showUsage() {
		System.out.println(Main.class.getName() + " <config file>");
	}

}
