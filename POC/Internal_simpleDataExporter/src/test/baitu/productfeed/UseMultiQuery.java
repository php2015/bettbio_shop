package test.baitu.productfeed;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import internal.baitu.simpledataexport.config.Configuration;

public class UseMultiQuery {

	public static void main(String[] args) throws Exception {
		File cfgFile = new File("testinput/config.properties");
		Configuration cfg = new Configuration();
		cfg.load(cfgFile);
		
		String sqlP = "select p.PRODUCT_ID as id, "
				+ "p.PRODUCT_FREE, p.CAS, p.PRODUCT_DIAMOND, "
				+ "p.CODE "
				+ "from PRODUCT p";
		String sqlD = "select d.EN_NAME as en_name, d.NAME as name, "
				+ "d.LANGUAGE_ID as language, "
				+ "d.SIMPLE_DESCRIPTION, d.SEF_URL "
				+ "from PRODUCT_DESCRIPTION d where d.PRODUCT_ID=?";
		Connection connection = (Connection) DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUser(),
				cfg.getDbPassword());
		System.out.println("Starting...");
		PreparedStatement stmtP = connection.prepareStatement(sqlP);
		PreparedStatement stmtD = connection.prepareStatement(sqlD);
		long sts = System.currentTimeMillis();
		ResultSet rs = stmtP.executeQuery();
		System.out.println(rs);
		long qts = System.currentTimeMillis();
		int row=0;
		while(rs.next()){
			for(int i=0;i<5;i++){
				rs.getString(i+1);
			}
			long id = rs.getLong(1);
			stmtD.setLong(1,id);
			ResultSet rs2 = stmtD.executeQuery();
			while(rs2.next()){
				for(int i=0;i<5;i++){
					rs.getString(i+1);
				}
			}
			row++;
		}
		long ets = System.currentTimeMillis();
		System.out.println(row+" rows, "+((qts-sts)/1000.0)+"Sec, " + ((ets-qts)/1000.0)+"Sec");
		
		connection.close();
	}

}
