package com.salesmanager.core.business.catalog.product.service;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.emory.mathcs.backport.java.util.Collections;

public class ProductBatchDeletorJdbcImpl extends BatchDeletorWithDBBackupImpl {
	private static final int WORKER_NUMBER = 4;
	private static ExecutorService executors = Executors.newFixedThreadPool((int)WORKER_NUMBER);
	class ProductBatchDeleteJob implements Runnable {
		List<Long> pIds;
		Connection conn;
		
		@Override
		public void run() {
			doProductDeleteOnce(pIds, conn);
			workerQueue.offer(this);
		}
	}
	protected final static String SQL_HISTORY_FILE_PATH="BRP_JDBC/usedSqls.json";
	private BlockingQueue<ProductBatchDeleteJob> workerQueue = null;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private String rootFolder;
	
	protected Map<String, String> usedSqls = null;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getRootFolder() {
		return rootFolder;
	}

	public void setRootFolder(String rootFolder) {
		this.rootFolder = rootFolder;
	}

	private static final ProductBatchRemoveStatistics statisData = new ProductBatchRemoveStatistics();
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getUsedSqlNoteList() throws Exception {
		
			ObjectMapper mapper = new ObjectMapper();
			File file = new File(getRootFile(), SQL_HISTORY_FILE_PATH);
			if (!file.exists()){
				return null;
			}
			usedSqls = mapper.readValue(file, Map.class);
		
		List<String> result = new ArrayList<String>(usedSqls.keySet());
		Collections.sort(result);
		return result;
	}

	private File getRootFile() {
		return new File(getRootFolder());
	}

	private static final Pattern ptnSql = Pattern.compile("select\\s+([\\s]*?) from .*", Pattern.CASE_INSENSITIVE);

	@Override
	public long getProductCountBySql(String sqlStatement) throws Exception {
		String sqlStr = sqlStatement.trim().replaceAll("\\s+", " ");
//		Matcher m = ptnSql.matcher(sqlStatement);
//		if (!m.matches()){
//			throw new Exception("sql not match the required format");
//		}
//		String selectColumns = m.group(1);
//		int pos = selectColumns.trim().indexOf(' ');
//		if (pos > 0){
//			selectColumns = selectColumns.substring(0, pos);
//		}
		sqlStr = "select count(*) from ("+sqlStr+") as tmptbl4cnt";
		System.out.println("[BatchRemoveProduct] count with SQL: " + sqlStr);
		Connection conn = getDataSource().getConnection();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sqlStr);
		rs.next();
		long result = rs.getLong(1);
		rs.close();
		st.close();
		conn.close();
		return result;
	}

	private static final String SQL_NOT_FOUND = "这句SQL还没输入过。你自己写吧。";
	@Override
	public String getSqlByNote(String sqlNote) throws Exception {
		if (usedSqls == null){
			return SQL_NOT_FOUND;
		}
		String sql = usedSqls.get(sqlNote);
		
		return sql == null ? SQL_NOT_FOUND : sql;
	}

	@Override
	public void doProductRemove(String sqlNote, String sqlStatement, Long totalCount) throws Exception {
		saveSqlHistory(sqlNote.trim(), sqlStatement.trim());
		String strSql = sqlStatement.trim().replaceAll("\\s+", " ");
		if (statisData.getRunning().get()){
			return;
		}
		synchronized(statisData){
			if (statisData.getRunning().get()){
				return;
			}
			statisData.getRunning().set(true);
		}
		Connection conn = getDataSource().getConnection();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(strSql);
		startProductRemoveJob(conn, st, rs, strSql, totalCount);
	}

	private void startProductRemoveJob(final Connection conn, final Statement st, final ResultSet rs, final String strSql, final Long totalCount) throws Exception {
		boolean jobCreated = false;
		clearStopFlag();
		try{
			
			statisData.getTotalPlanned().set(totalCount);
			statisData.getTotalDeleted().set(0);
			statisData.getTotalScheduled().set(0);
			statisData.setStartTime(new Date());
			statisData.setExecutionSql(strSql);
			statisData.setEndTime(null);
			jobCreated = true;
			new Thread(){
				public void run(){
					createProductRemoveJob(conn, st, rs, strSql, totalCount);
				}
			}.start();
		}finally{
			if (!jobCreated){
				rs.close();
				st.close();
				conn.close();
			}
		}
	}

	private void createProductRemoveJob(Connection conn, Statement st, ResultSet rs, String strSql, Long totalCount) {
		if (workerQueue == null){
			workerQueue = new LinkedBlockingQueue<ProductBatchDeleteJob>();
		}
		for(int i=0;i<WORKER_NUMBER;i++){
			workerQueue.offer(new ProductBatchDeleteJob());
		}
		try {
			List<Long> pids = new ArrayList<Long>(BATCH_SIZE);
			while(rs.next()){
				pids.add(rs.getLong(1));
				if (pids.size() < BATCH_SIZE){
					continue;
				}
				if (requiredStop()){
					pids.clear();
					break;
				}
				scheduleOneJob(pids);
				pids.clear();
			}
			
			if (!pids.isEmpty() && !requiredStop()){
				scheduleOneJob(pids);
			}
			waitAllJobsDone();
		} catch (Exception e) {
			waitAllJobsDone();
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				st.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected void scheduleOneJob(List<Long> pids) throws InterruptedException, SQLException {
		ProductBatchDeleteJob worker = workerQueue.take();
		if (requiredStop()){
			workerQueue.offer(worker);
			return;
		}
		if (worker.conn == null){
			worker.conn = dataSource.getConnection();
		}
		worker.pIds = new ArrayList<Long>(pids);
		statisData.getTotalScheduled().addAndGet(worker.pIds.size());
		executors.execute(worker);
		
	}
	private void waitAllJobsDone() {
		for(int i=0;i<WORKER_NUMBER;i++){
			try {
				ProductBatchDeleteJob worker = workerQueue.take();
				System.out.println("close connection of worker " + worker.hashCode());
				worker.conn.close();
				worker.conn = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		statisData.setEndTime(new Date());
		statisData.getRunning().set(false);
		System.out.println("[BatchRemoveProduct] job started from " + statisData.getStartTime()+" finished");
	}

	private void doProductDeleteOnce(List<Long> pIds, Connection conn) {
		try {
			this.cascadeDeleteProduct(conn, "PRODUCT_ID", pIds);
			statisData.getTotalDeleted().addAndGet(pIds.size());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private void saveSqlHistory(String sqlNote, String sqlStatement) throws Exception {
		if (usedSqls == null){
			usedSqls = new HashMap<String, String>();
		}
		if (sqlNote == null || sqlNote.isEmpty()){
			sqlNote = sqlStatement.substring(0, 80);
		}
		usedSqls.put(sqlNote, sqlStatement);
		ObjectMapper mapper = new ObjectMapper();
		File file = new File(getRootFile(), SQL_HISTORY_FILE_PATH);
		if (!file.exists()){
			file.getParentFile().mkdirs();
		}
		mapper.writerWithDefaultPrettyPrinter().writeValue(file, usedSqls);
	}

	private static final SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public Map<String, Object> getStatisticData() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("StartTime", dateFmt.format(statisData.getStartTime()));
		result.put("EndTime", statisData.getEndTime() == null ? null : dateFmt.format(statisData.getEndTime()));
		result.put("TotalDeleted", statisData.getTotalDeleted());
		result.put("TotalPlanned", statisData.getTotalPlanned());
		result.put("TotalScheduled", statisData.getTotalScheduled());
		result.put("Sql", statisData.getSql());
		result.put("Running", statisData.getRunning());
		return result;
	}

	@Override
	public void stopProductDeleting() {
		setStopFlag();
	}
	
	
	
}
