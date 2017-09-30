package test.baitu.productfeed;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import com.salesmanager.core.searchfeed.ConfigLoader;
import com.salesmanager.core.searchfeed.SearchFeedCallback;
import com.salesmanager.core.searchfeed.SearchFeedManager;

public class FullFeed {

	public static class ProcessCallback implements SearchFeedCallback {

		boolean done = false;
		public boolean finished() {
			return done;
		}

		@Override
		public void setTotal(String typeName, int totalRow) {
			System.out.printf("Total has %d %s\n", totalRow, typeName);
		}

		@Override
		public void addStarted(String typeName, int number) {
			System.out.printf("%d %s started\n", number, typeName);
		}

		@Override
		public void addFeeded(String typeName, int number) {
			System.out.printf("%d %s added\n", number, typeName);
		}

		@Override
		public void addIgnored(String typeName, int number) {
			System.out.printf("%d %s ignored\n", number, typeName);
		}

		@Override
		public void allStarted(String typeName) {
			System.out.printf("%s all started\n", typeName);
		}

		@Override
		public void finished(String typeName) {
			System.out.printf("%s all finished\n", typeName);
			done = true;
		}

	}

	public static void main(String[] args) throws Exception {
		Long sTs = System.currentTimeMillis();
		SearchFeedManager manager = new SearchFeedManager();
		File path= new File("testinput/datafeed.properties");
		FileInputStream fins = new FileInputStream(path);
		manager.loadConfig(fins);
		fins.close();
		
		ConfigLoader cfg = new ConfigLoader();
		cfg.setProps(manager.getOrignalProps());
		
		Class.forName(cfg.getDbDriver());
		Connection connection = (Connection) DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(),
				cfg.getDbPassword());
		
		ProcessCallback callback = new ProcessCallback();
		manager.setProcessCallback(callback);
		manager.setDbConnection(connection);
		manager.startFeed();
		
		while(!callback.finished()){
			Thread.sleep(1000);
		}
		connection.close();
		Long eTs = System.currentTimeMillis();
		System.out.println("Totally " + (eTs-sTs)/1000.0 +"Sec");
	}

}
