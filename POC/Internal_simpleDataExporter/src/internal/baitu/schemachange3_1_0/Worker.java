package internal.baitu.schemachange3_1_0;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Worker {

	private Configuration config;
	private Connection connection;
	private Logger logger = Logger.getLogger(Worker.class.getName());
	private long mobileNumber;
	private PreparedStatement queryMerchantStmt;
	private PreparedStatement queryUserStmt;
	private PreparedStatement queryUntouchedUserStmt;
	private PreparedStatement updateUserStmt;

	public void workOn(Configuration cfg) throws Exception {
		this.config = cfg;
		createConnection();

		beforeHandleMerchant();
		handleMerchant();
		handleUsers();
		closeConnection();
	}

	private void handleUsers() throws Exception {
		ResultSet rst = queryUntouchedUserStmt.executeQuery();
		while(rst.next()){
			UserData user = new UserData();
			user.id = rst.getLong(1);
			user.email = rst.getString(2);
			user.phone = getUniqueMobileNumber();
			updateUser(user);
		}
	}

	private void beforeHandleMerchant() throws Exception {
		mobileNumber = (int) (System.currentTimeMillis() % 100000000);
		String sql = "select MERCHANT_ID, STORE_NAME, STORE_MOBILE from MERCHANT_STORE";
		queryMerchantStmt = connection.prepareStatement(sql);

		sql = "select u.USER_ID, u.ADMIN_EMAIL from USER u, MERCHANT_STORE m where u.MERCHANT_ID=m.MERCHANT_ID and m.MERCHANT_ID=?";
		queryUserStmt = connection.prepareStatement(sql);
		
		sql = "select u.USER_ID, u.ADMIN_EMAIL from USER u where u.ADMIN_MOBILE is null";
		queryUntouchedUserStmt = connection.prepareStatement(sql);
		
		sql = "update USER set ADMIN_MOBILE=? where USER_ID=?";
		updateUserStmt = connection.prepareStatement(sql);
	}

	private void handleMerchant() throws Exception {
		ResultSet mIdRst = queryMerchantStmt.executeQuery();
		Set<String> existedMobile = new HashSet<String>();
		while (mIdRst.next()) {
			long mId = mIdRst.getLong(1);
			String storName = mIdRst.getString(2);
			String storeMobile = mIdRst.getString(3);
			
			if(storeMobile == null || storeMobile.trim().isEmpty()){
				storeMobile = getUniqueMobileNumber();
			}
			if (existedMobile.contains(storeMobile)){
				storeMobile = getUniqueMobileNumber();
			}
			existedMobile.add(storeMobile);
			
			queryUserStmt.setLong(1, mId);
			ResultSet userRst = queryUserStmt.executeQuery();
			if (!userRst.next()) {
				logger.log(Level.INFO, "Merchant {0} ID={1} Mobile={2} has no related user",
						new Object[] { storName, mId + "", storeMobile });
				continue;
			}

			List<UserData> users = new ArrayList<UserData>();
			do {
				UserData data = new UserData();
				data.id = userRst.getLong(1);
				data.email = userRst.getString(2);
				users.add(data);
			} while (userRst.next());

			logger.log(Level.INFO, "Merchant {0} ID={1} Mobile={2} has {3} related user",
					new Object[] { storName, mId + "", storeMobile, users.size() });
			if (users.size() == 0) {
				users.get(0).phone = storeMobile;
			} else {
				setMobileNumber(users, storeMobile);
			}
			for(UserData data: users){
				System.out.println("\t"+data.id+", "+data.email+", "+data.phone);
				updateUser(data);
			}
		}
	}

	private String getUniqueMobileNumber() {
		return String.format("2%010d", mobileNumber++);
	}

	private void updateUser(UserData data) throws Exception {
		System.out.printf("update USER set MOBILE='%s' where USER_ID=%d\n", data.phone, data.id);
		updateUserStmt.setString(1, data.phone);
		updateUserStmt.setLong(2, data.id);
		boolean ok = updateUserStmt.execute();
	}

	private void setMobileNumber(List<UserData> users, String storeMobile) {
		List<UserData> tempList = new ArrayList<UserData>(users);
		Iterator<UserData> it = tempList.iterator();
		while (it.hasNext()) {
			UserData user = it.next();
			if (user.email.toLowerCase().endsWith("@bettbio.com")) {
				it.remove();
			}
		}

		UserData ownerUser;
		if (tempList.size() > 0) {
			ownerUser = tempList.get(0);
		} else {
			ownerUser = users.get(0);
		}
		for (UserData user : users) {
			if (user.id == ownerUser.id) {
				user.phone = storeMobile;
			} else {
				user.phone = getUniqueMobileNumber();
			}
		}
		return;

	}

	private void closeConnection() throws Exception {
		this.connection.close();
	}

	private void createConnection() throws Exception {

		Class.forName(config.getDbDriver());
		this.connection = (Connection) DriverManager.getConnection(config.getDbUrl(), config.getDbUser(),
				config.getDbPassword());
		logger.info("Connect to DB success");
	}

	class UserData {
		long id;
		String email;
		String phone;
	}
}
