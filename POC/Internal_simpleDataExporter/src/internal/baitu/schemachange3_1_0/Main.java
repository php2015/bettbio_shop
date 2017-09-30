package internal.baitu.schemachange3_1_0;

import java.io.File;

public class Main {

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			showUsage();
			return;
		}

		long sts = System.currentTimeMillis();
		File cfgFile = new File(args[0]);
		if (cfgFile.isFile() && cfgFile.exists()) {
			Configuration cfg = new Configuration();
			cfg.load(cfgFile);
			Worker worker = new Worker();
			worker.workOn(cfg);
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
