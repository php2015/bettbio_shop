package test.baitu.productfeed;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import internal.baitu.simpledataexport.config.Configuration;

public class UseJoin {

	public static void main(String[] args) throws Exception {
		File cfgFile = new File("testinput/config.properties");
		Configuration cfg = new Configuration();
		cfg.load(cfgFile);
		
		String sql = "select p.PRODUCT_ID as id, d.EN_NAME as en_name, d.NAME as name, "
				+ "d.LANGUAGE_ID as language, p.PRODUCT_FREE, p.CAS, p.PRODUCT_DIAMOND, "
				+ "d.SIMPLE_DESCRIPTION, d.SEF_URL, p.CODE "
				+ "from PRODUCT p left join PRODUCT_DESCRIPTION d on p.PRODUCT_ID=d.PRODUCT_ID";
		Connection connection = (Connection) DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(),
				cfg.getDbPassword());
		System.out.println("Starting...");
		PreparedStatement stmt = connection.prepareStatement(sql);
		long sts = System.currentTimeMillis();
		ResultSet rs = stmt.executeQuery();
		System.out.println(rs);
		long qts = System.currentTimeMillis();
		int row=0;
		while(rs.next()){
			for(int i=0;i<10;i++){
				rs.getString(i+1);
			}
			row++;
		}
		long ets = System.currentTimeMillis();
		System.out.println(row+" rows, "+((qts-sts)/1000.0)+"Sec, " + ((ets-qts)/1000.0)+"Sec");
		connection.close();
	}

}
