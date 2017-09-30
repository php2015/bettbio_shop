package internal.baitu.simpledataexport.worker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import internal.baitu.simpledataexport.config.Configuration;

public class Worker {
	protected static final String SQL_QUERY_CATEGORY = "select"
			+ " c.CATEGORY_ID as id, cdscp.NAME as name, c.PARENT_ID as parent"
			+ " from CATEGORY c, CATEGORY_DESCRIPTION cdscp"
			+ " where c.CATEGORY_ID=? and cdscp.CATEGORY_ID=c.CATEGORY_ID";
	protected static final String SQL_QUERY_PRICE = "select "
			+ " price.PRODUCT_PRICE_PERIOD as period, price.PRODUCT_PRICE_AMOUNT as amount, prcDscp.NAME as name"
			+ " from PRODUCT_PRICE price "
			+ " inner join PRODUCT_AVAILABILITY pAval on price.PRODUCT_AVAIL_ID=pAval.PRODUCT_AVAIL_ID "
			+ " inner join PRODUCT p on pAval.PRODUCT_ID=p.PRODUCT_ID "
			+ " left outer join PRODUCT_PRICE_DESCRIPTION prcDscp on price.PRODUCT_PRICE_ID=prcDscp.PRODUCT_PRICE_ID"
			+ " where p.PRODUCT_ID=?";
	private Logger logger = Logger.getLogger(Worker.class.getName());
	private Configuration config;
	private Connection connection;
	private ResultSet resultSet;
	private List<Map<String, String>> resultDataMap;
	private ArrayList<String> fieldNameList;
	private String categoryFieldName;
	private ArrayList<String> categoryOutputNames;
	private PreparedStatement categeoryStmt;
	private HashMap<Long, CategoryData> categories;
	private String priceFieldName;
	private ArrayList<String> priceOutputNames;
	private PreparedStatement priceStmt;
	private File outPutFile;
	private OutputStreamWriter out;

	public void workOn(Configuration cfg) throws Exception {
		this.config = cfg;
		Class.forName(config.getDbDriver());
		this.connection = (Connection) DriverManager.getConnection(config.getDbUrl(), config.getDbUser(),
				config.getDbPassword());
		logger.info("Connect to DB success");

		beforeRunQuery();
		runQuery();
		collectResultData();
		generateOutput();
		connection.close();
		
		if (out != null){
			out.flush();
			out.close();
		}
	}

	private void beforeRunQuery() throws Exception {
		List<String> list = config.getProcessCategory();
		this.categoryFieldName = list.get(0);
		this.categoryOutputNames = new ArrayList<String>(list.subList(1, list.size()));
		categeoryStmt = connection.prepareStatement(SQL_QUERY_CATEGORY);
		this.categories = new HashMap<Long, CategoryData>();
		
		list = config.getProcessPrice();
		this.priceFieldName = list.get(0);
		this.priceOutputNames = new ArrayList<String>(list.subList(1, list.size()));
		priceStmt = connection.prepareStatement(SQL_QUERY_PRICE);
		
		this.outPutFile = config.getOutputFile();
	}

	private void generateOutput() {
		// writeHeaderLine();
		//
		// for(Map<String, String> data: resultDataMap){
		// writeNormalDataLine(data);
		// }
	}

	private void writeHeaderLine() throws Exception {
		if (!outPutFile.exists()){
			outPutFile.createNewFile();
		}
		this.out = new OutputStreamWriter(new FileOutputStream(outPutFile), "UTF-8");
		for (String name : fieldNameList) {
			if (name.equals(categoryFieldName)) {
				for (String cName : this.categoryOutputNames) {
					writeCsvCell(cName);
				}
			} else if (name.equals(priceFieldName)){
				for (String cName : this.priceOutputNames) {
					writeCsvCell(cName);
				}
			} else {
				writeCsvCell(name);
			}
		}
		writeCsvLine();
	}

	private void writeNormalDataLine(Map<String, String> data) throws Exception {
		List<Map<String, String>> priceList = null;
		for (String name : fieldNameList) {
			if (name.equals(categoryFieldName)) {
				List<String> categories = parseCategories(data.get(name));
				for (String cName : categories) {
					writeCsvCell(cName);
				}
			} else if (name.equals(priceFieldName)){
				priceList = parsePrice(data.get(name));
				for (String pName : this.priceOutputNames) {
					writeCsvCell(priceList.get(0).get(pName));
				}
			} else {
				writeCsvCell(data.get(name));
			}
		}
		writeCsvLine();
		if (priceList == null || priceList.size()<=1){
			return;
		}
		// only need append price rows if this product has more than 1 price-info
		for(int i=1;i<priceList.size();i++){
			for (String name : fieldNameList) {
				if (name.equals(categoryFieldName)) {
					for (String cName : categoryOutputNames) {
						writeCsvCell("");
					}
				} else if (name.equals(priceFieldName)){
					for (String pName : this.priceOutputNames) {
						writeCsvCell(priceList.get(i).get(pName));
					}
				} else {
					writeCsvCell("");
				}
			}
			writeCsvLine();
		}
	}

	private List<Map<String, String>> parsePrice(String productId) throws Exception {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		priceStmt.setLong(1, Long.parseLong(productId));
		ResultSet rs = priceStmt.executeQuery();
		if (!rs.next()){
			// empty
			result.add(new HashMap<String, String>());
			return result;
		}
		do {
			Map<String, String> data = new HashMap<String, String>();
			data.put(priceOutputNames.get(0), rs.getString("name"));
			data.put(priceOutputNames.get(1), rs.getString("amount"));
			data.put(priceOutputNames.get(2), rs.getString("period"));
			result.add(data);
		}while(rs.next());
		return result;
	}

	private List<String> parseCategories(String categoryId) {
		try {
			Long id = Long.parseLong(categoryId);
			CategoryData category;
			category = getCategoryData(id);
			if (category == null) {
				return Arrays.asList(new String[] { "", "", "" });
			}
			List<String> result = new ArrayList<String>(this.categoryOutputNames.size());
			while (category != null) {
				result.add(category.getName());
				if (category.getParent() != null && category.getParent() != 0) {
					category = getCategoryData(category.getParent());
				} else {
					break;
				}
			}
			Collections.reverse(result);
			for (int i = result.size(); i < categoryOutputNames.size(); i++) {
				result.add("");
			}
			return result;
		} catch (Exception e) {
			return Arrays.asList(new String[]{"","",""});
		}
	}

	private CategoryData getCategoryData(Long id) throws Exception {
		CategoryData data = this.categories.get(id);
		if (data == null) {
			categeoryStmt.setLong(1, id.longValue());
			categeoryStmt.executeQuery();
			ResultSet rs = categeoryStmt.getResultSet();
			rs.next();
			data = new CategoryData();
			data.setId(rs.getLong("id"));
			data.setName(rs.getString("name"));
			data.setParent(rs.getLong("parent"));
			categories.put(id, data);
		}
		return data;
	}

	private void writeCsvLine() throws Exception {
		out.write("\r\n");
	}

	private void writeCsvCell(String value) throws Exception {
		String output;
		if (value == null) {
			output = "";
		} else {
			if (value.indexOf(',') >= 0 || value.indexOf('\n') >= 0 || value.indexOf('\r') >= 0) {
				output = value.replace("\"", "\"\"");
				output = '"' + output + '"';
			} else {
				output = value;
			}
		}
		out.write(output + ",");
	}

	private void collectResultData() throws Exception {
		this.resultDataMap = new ArrayList<Map<String, String>>();
		this.fieldNameList = null;
		int rowCount=0;
		int batchNumber=0;
		while (resultSet.next()) {
			if (fieldNameList == null) {
				filleNameList(resultSet);
				writeHeaderLine();
			}

			Map<String, String> data = new HashMap<String, String>();
			for (int i = 0; i < fieldNameList.size(); i++) {
				data.put(fieldNameList.get(i), resultSet.getString(i + 1));
			}
			// resultDataMap.add(data);
			writeNormalDataLine(data);
			rowCount++;
			if (rowCount >= config.getOutputSplit()){
				closeOutput();
				createNewOutout(++batchNumber);
				rowCount =0;
			}
		}
	}

	private void createNewOutout(int batchNumber) {
		String newName = config.getOutputFile().getAbsolutePath();
		int pos = newName.lastIndexOf('.');
		newName = String.format("%s%06d%s", newName.substring(0, pos), batchNumber, newName.substring(pos));
		outPutFile = new File(newName);
		try {
			writeHeaderLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void closeOutput() {
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
		}
		
	}

	private void filleNameList(ResultSet rSet) throws Exception {
		ResultSetMetaData mData = rSet.getMetaData();
		int cnt = mData.getColumnCount();
		fieldNameList = new ArrayList<String>();
		for (int i = 0; i < cnt; i++) {
			String sqlName;
			String outputName;
			sqlName = mData.getColumnLabel(i + 1);
			outputName = config.getOutputFieldMap().get(sqlName.toLowerCase());
			// System.out.println(sqlName);
			if (outputName != null) {
				fieldNameList.add(outputName);
				continue;
			}

			sqlName = mData.getColumnName(i + 1);
			outputName = config.getOutputFieldMap().get(sqlName.toLowerCase());
			// System.out.println(sqlName);
			if (outputName != null) {
				fieldNameList.add(outputName);
				continue;
			}
			
			sqlName= mData.getTableName(i + 1) + "." + mData.getColumnName(i + 1);
			outputName = config.getOutputFieldMap().get(sqlName.toLowerCase());
			if (outputName != null) {
				fieldNameList.add(outputName);
				continue;
			}
			
			fieldNameList.add(sqlName);
		}
	}

	private void runQuery() throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(config.getSqlQuery());
		stmt.setFetchSize(1000);
		boolean ok = stmt.execute();
		if (!ok) {
			String msg = "I cannot execute update statement. Not ready for that.";
			logger.severe(msg);
			throw new RuntimeException(msg);
		}
		this.resultSet = stmt.getResultSet();
	}

}
