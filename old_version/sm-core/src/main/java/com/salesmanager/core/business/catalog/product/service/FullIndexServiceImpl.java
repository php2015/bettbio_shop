package com.salesmanager.core.business.catalog.product.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.search.service.SearchService;
import com.shopizer.search.utils.SearchClient;

@Service("fullIndexService")
public class FullIndexServiceImpl implements FullIndexService {
	protected Logger logger = Logger.getLogger(FullIndexServiceImpl.class);
//	protected Connection connection;
	protected DataSource dataSource;
	protected FullIndexParams params;
	protected FullIndexCallback callback;
	protected ExecutorService executorService;
	protected AtomicInteger runningThreads = new AtomicInteger(0);
	
	@Autowired
	protected SearchClient searchClient;
	@Autowired
	protected SearchService searchService;
	@Autowired
	protected ProductService productService;


	class ClearJob implements Runnable {

		public ArrayList<PreparedStatement> statements;
		public ArrayList<Long[]> ids;

		@Override
		public void run() {
			System.out.println("Clear JOB is running");
			clearIndex();
			getCallback().onIndexCleared();
			runningThreads.set(0);
			for (int i = 0; i < ids.size(); i++) {
				System.out.println("Start a thread to do feed");
				FeedJob job = new FeedJob();
				Long[] sIds = ids.get(i);
				job.lowId = sIds[0];
				job.hiId = sIds[1];
//				job.statement = statements.get(i);
				runningThreads.incrementAndGet();
				executorService.execute(job);
			}
		}
	}

	class FeedJob implements Runnable {
		public long lowId;
		public long hiId;
		@Override
		public void run() {
			System.out.println("Feed JOB is running");
			try {
				doFullFeedProducts(lowId, hiId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (runningThreads.decrementAndGet() == 0) {
				getCallback().onFeedFinished();
			}
		}
	}
	
	class IndexJob implements Runnable {
		public long startFrom;
		public long endBefore;
		public List<Long> pIds;
		public boolean autoAvailable = false;
		
		@Override
		public void run() {
			System.out.println("Index JOB is running");
			try {
				doIndexProducts(startFrom, endBefore, pIds, autoAvailable);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (runningThreads.decrementAndGet() == 0) {
				getCallback().onFeedFinished();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.salesmanager.core.business.catalog.product.service.FullIndexService#getConnection()
	 */
	public Connection getConnectionFromDS() throws SQLException {
		return dataSource.getConnection();
	}

	/* (non-Javadoc)
	 * @see com.salesmanager.core.business.catalog.product.service.FullIndexService#getParams()
	 */
	@Override
	public FullIndexParams getParams() {
		return params;
	}

	/* (non-Javadoc)
	 * @see com.salesmanager.core.business.catalog.product.service.FullIndexService#setParams(com.salesmanager.core.business.catalog.product.service.FullIndexParams)
	 */
	@Override
	public void setParams(FullIndexParams params) {
		this.params = params;
	}

	/* (non-Javadoc)
	 * @see com.salesmanager.core.business.catalog.product.service.FullIndexService#getCallback()
	 */
	@Override
	public FullIndexCallback getCallback() {
		return callback;
	}

	/* (non-Javadoc)
	 * @see com.salesmanager.core.business.catalog.product.service.FullIndexService#setCallback(com.salesmanager.core.business.catalog.product.service.FullIndexCallback)
	 */
	@Override
	public void setCallback(FullIndexCallback calback) {
		this.callback = calback;
	}

	/* (non-Javadoc)
	 * @see com.salesmanager.core.business.catalog.product.service.FullIndexService#startProcessing()
	 */
	@Override
	public boolean startProcessing() {
		if (callback == null) {
			logger.fatal("Full index callback was not set");
			return false;
		}
		if (params == null) {
			logger.fatal("Full index parameters was not set");
			return false;
		}
		if (dataSource == null) {
			logger.fatal("Full index has no valid DB connection");
			return false;
		}

		if (executorService == null) {
			executorService = Executors.newFixedThreadPool(params.getPieces() + 1);
		}
		Connection connection = null;
		try {
			// notify processing start
			getCallback().onFeedStart();
			connection = getConnectionFromDS();
			// count total product numbers
			long totalNumber = FullIndexHelper.getTotalNumber(connection);
			long invalidNumber = FullIndexHelper.getInvalidNumber(connection);
			getCallback().setTotalProductNumber(totalNumber);
			getCallback().setIgnoredProductNumber(invalidNumber);

			// split product IDs into several segments, each segments will have
			// a thread
			long segsize = (totalNumber + params.getPieces() - 1) / params.getPieces();
			List<Long> idSegmentValus = FullIndexHelper.getIdSegmentStartValues(connection, totalNumber, segsize);
			System.out.println("ids=" + idSegmentValus);
			FullIndexHelper.prepare(connection);
			// first job is clear all old index datas
			ClearJob clearJob = new ClearJob();
			clearJob.ids = new ArrayList<Long[]>();
			for (int i = 1; i < idSegmentValus.size(); i++) {
				clearJob.ids.add(new Long[]{idSegmentValus.get(i - 1), idSegmentValus.get(i)});
			}
			new Thread(clearJob).start();

		} catch (Exception e) {
			callback.onException(e);
			return false;
		} finally {
			if (connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see com.salesmanager.core.business.catalog.product.service.FullIndexService#clearIndex()
	 */
	@Override
	public void clearIndex() {
		searchClient.clearAllData(params.getIndexName(), params.getTypeName());
		searchService.forceInitService();
	}

	public void doFullFeedProducts(long lowerId, long higherId) {
		List<PreparedStatement> sts = new ArrayList<PreparedStatement>();
		Connection connection = null;
		Connection connection2 = null;
		PreparedStatement statement = null;
		try {
			connection = getConnectionFromDS();
			connection2 = getConnectionFromDS();
			statement = FullIndexHelper.createProductQueryStatement(connection2, lowerId, higherId);
			PreparedStatement priceSt = FullIndexHelper.createGetPriceStatement(connection);
			sts.add(priceSt);
			PreparedStatement imageSt = FullIndexHelper.createGetImageStatement(connection);
			sts.add(imageSt);
			PreparedStatement authSt = FullIndexHelper.createGetAuthStatement(connection);
			sts.add(authSt);
			PreparedStatement certificatesSt = FullIndexHelper.createGetCertificatesStatement(connection);
			sts.add(certificatesSt);
			PreparedStatement proofSt = FullIndexHelper.createGetProofStatement(connection);
			sts.add(proofSt);
			PreparedStatement thirdProofSt = FullIndexHelper.createGetThirdProofStatement(connection);
			sts.add(thirdProofSt);
			PreparedStatement testreportSt = FullIndexHelper.createGetTestreportStatement(connection);
			sts.add(testreportSt);
			int count = 0;
			int sleepCount = 0;
			System.out.println("Statement prepared");
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				long id = FullIndexHelper.getProductId(rs);
				boolean isAvailable = FullIndexHelper.getAvailable(rs);
				long audit = FullIndexHelper.getAudit(rs);
				if (!isAvailable || audit <= 0) {
					// 没有审核通过的删除, 不上架的不做索引
					getCallback().onProductIgnored(id);
					continue;
				}
				count++;
				sleepCount++;
				datas.add(FullIndexHelper.makeProductData(rs, priceSt, imageSt, authSt, certificatesSt, proofSt,
						thirdProofSt, testreportSt));
				if (count < params.getBatchSize()) {
					continue;
				}
				BulkResponse bulkResp = searchClient.bulkIndex(params.getIndexName(), params.getTypeName(), datas);
				for (BulkItemResponse resp : bulkResp.getItems()) {
					if (resp.isFailed()) {
						getCallback().onProductFailure(resp.getItemId(), resp.getFailure());
					} else {
						getCallback().onProductIndexed(resp.getItemId());
					}
				}
				count = 0;
				datas = null;
				datas = new ArrayList<Map<String, Object>>();
				if (sleepCount > 10000){
					sleepCount = 0;
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
					}
				}
			}
			rs.close();
			
			if (datas.isEmpty()) {
				return;
			}
			BulkResponse bulkResp = searchClient.bulkIndex(params.getIndexName(), params.getTypeName(), datas);
			for (BulkItemResponse resp : bulkResp.getItems()) {
				if (resp.isFailed()) {
					getCallback().onProductFailure(resp.getItemId(), resp.getFailure());
				} else {
					getCallback().onProductIndexed(resp.getItemId());
				}
			}
		} catch (SQLException e) {
			getCallback().onException(e);
		} finally {
			for (PreparedStatement st : sts) {
				try {
					st.close();
				} catch (SQLException e) {
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
			if (connection2 != null) {
				try {
					connection2.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public boolean startIndexingByIds(List<Long> pIds, boolean autoAvailable) {
		if (callback == null) {
			logger.fatal("Callback was not set");
			return false;
		}
		if (params == null) {
			logger.fatal("Parameters was not set");
			return false;
		}
		if (dataSource == null) {
			logger.fatal("Has no valid DB connection");
			return false;
		}

		if (executorService == null) {
			executorService = Executors.newFixedThreadPool(params.getPieces() + 1);
		}
		Connection connection = null;
		try {
			// notify processing start
			getCallback().onFeedStart();
			connection = getConnectionFromDS();

			long totalNumber = pIds.size();
			getCallback().setTotalProductNumber(totalNumber);

			long segsize = (totalNumber + params.getPieces() - 1) / params.getPieces();
			FullIndexHelper.prepare(connection);
			int pos = 0;
			
			runningThreads.set(0);
			while(pos < totalNumber){
				IndexJob job = new IndexJob();
				job.autoAvailable = autoAvailable;
				job.startFrom = pos;
				pos += segsize;
				job.endBefore = Math.min(pos, totalNumber);
				job.pIds = pIds;
				System.out.println("Start a thread to do index");
				runningThreads.incrementAndGet();
				executorService.execute(job);
			}
		} catch (Exception e) {
			callback.onException(e);
			return false;
		} finally {
			if (connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}

		return true;
	}

	public void doIndexProducts(long lowId, long hiId, List<Long> pIds, boolean autoAvailable) {
		List<PreparedStatement> sts = new ArrayList<PreparedStatement>();
		Connection connection = null;
		Connection connection2 = null;
		PreparedStatement statement = null;
		try {
			connection = getConnectionFromDS();
			connection2 = getConnectionFromDS();
			statement = FullIndexHelper.createProductQueryByIdStatement(connection2);
			PreparedStatement setAvaliableSt = FullIndexHelper.createSetAvailableStatement(connection);
			sts.add(setAvaliableSt);
			PreparedStatement priceSt = FullIndexHelper.createGetPriceStatement(connection);
			sts.add(priceSt);
			PreparedStatement imageSt = FullIndexHelper.createGetImageStatement(connection);
			sts.add(imageSt);
			PreparedStatement authSt = FullIndexHelper.createGetAuthStatement(connection);
			sts.add(authSt);
			PreparedStatement certificatesSt = FullIndexHelper.createGetCertificatesStatement(connection);
			sts.add(certificatesSt);
			PreparedStatement proofSt = FullIndexHelper.createGetProofStatement(connection);
			sts.add(proofSt);
			PreparedStatement thirdProofSt = FullIndexHelper.createGetThirdProofStatement(connection);
			sts.add(thirdProofSt);
			PreparedStatement testreportSt = FullIndexHelper.createGetTestreportStatement(connection);
			sts.add(testreportSt);
			int count = 0;
			int sleepCount = 0;
			System.out.println("Statement prepared");
			List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
			for (long i = lowId; i < hiId; i++) {
				long productId = pIds.get((int) i);
				if (autoAvailable){
					setAvaliableSt.setLong(1, productId);
					setAvaliableSt.addBatch();
				}
				statement.setLong(1, productId);
				ResultSet rs = statement.executeQuery();
				while (rs.next()) {
					long id = FullIndexHelper.getProductId(rs);
					boolean isAvailable = FullIndexHelper.getAvailable(rs);
					long audit = FullIndexHelper.getAudit(rs);
					if ((!isAvailable && !autoAvailable) || audit <= 0) {
						// 没有审核通过的删除, 不上架的不做索引
						getCallback().onProductIgnored(id);
						continue;
					}
					count++;
					sleepCount++;
					datas.add(FullIndexHelper.makeProductData(rs, priceSt, imageSt, authSt, certificatesSt, proofSt,
							thirdProofSt, testreportSt));
					if (count < params.getBatchSize()) {
						continue;
					}
					setAvaliableSt.executeBatch();
					if (!connection2.getAutoCommit()){
						connection2.commit();
					}
					BulkResponse bulkResp = searchClient.bulkIndex(params.getIndexName(), params.getTypeName(), datas);
					for (BulkItemResponse resp : bulkResp.getItems()) {
						if (resp.isFailed()) {
							getCallback().onProductFailure(resp.getItemId(), resp.getFailure());
						} else {
							getCallback().onProductIndexed(resp.getItemId());
						}
					}
					datas = null;
					count = 0;
					datas = new ArrayList<Map<String, Object>>();
					if (sleepCount > 10000) {
						sleepCount = 0;
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
						}
					}
					break;
				}
				rs.close();
			}
			if (datas.isEmpty()) {
				return;
			}
			setAvaliableSt.executeBatch();
			if (!connection2.getAutoCommit()){
				connection2.commit();
			}
			BulkResponse bulkResp = searchClient.bulkIndex(params.getIndexName(), params.getTypeName(), datas);
			for (BulkItemResponse resp : bulkResp.getItems()) {
				if (resp.isFailed()) {
					getCallback().onProductFailure(resp.getItemId(), resp.getFailure());
				} else {
					getCallback().onProductIndexed(resp.getItemId());
				}
			}
		} catch (SQLException e) {
			getCallback().onException(e);
		} finally {
			for (PreparedStatement st : sts) {
				try {
					st.close();
				} catch (SQLException e) {
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
			if (connection2 != null) {
				try {
					connection2.close();
				} catch (SQLException e) {
				}
			}
		}
	}
}
