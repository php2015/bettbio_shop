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

import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;

public class FullIndexService {
	protected Logger logger = Logger.getLogger(FullIndexService.class);
	protected Connection connection;
	protected FullIndexParams params;
	protected FullIndexCallback callback;
	protected ExecutorService executorService;
	protected AtomicInteger runningThreads = new AtomicInteger(0);
	protected ElasticSerachClient searchClient;

	class ClearJob implements Runnable {

		public ArrayList<PreparedStatement> statements;

		@Override
		public void run() {
			System.out.println("Clear JOB is running");
			clearIndex();
			getCallback().onIndexCleared();
			runningThreads.set(0);
			for (int i = 0; i < statements.size(); i++) {
				System.out.println("Start a thread to do feed");
				FeedJob job = new FeedJob();
				job.statement = statements.get(i);
				runningThreads.incrementAndGet();
				executorService.execute(job);
			}
		}
	}

	class FeedJob implements Runnable {
		public PreparedStatement statement;

		@Override
		public void run() {
			System.out.println("Feed JOB is running");
			try {
				doFeedProducts(statement);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (runningThreads.decrementAndGet() == 0) {
				getCallback().onFeedFinished();
			}
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public FullIndexParams getParams() {
		return params;
	}

	public void setParams(FullIndexParams params) {
		this.params = params;
	}

	public FullIndexCallback getCallback() {
		return callback;
	}

	public void setCallback(FullIndexCallback calback) {
		this.callback = calback;
	}

	public boolean startProcessing() {
		if (callback == null) {
			logger.fatal("Full index callback was not set");
			return false;
		}
		if (params == null) {
			logger.fatal("Full index parameters was not set");
			return false;
		}
		if (connection == null) {
			logger.fatal("Full index has no valid DB connection");
			return false;
		}

		if (executorService == null) {
			executorService = Executors.newFixedThreadPool(params.getPieces() + 1);
		}

		try {
			// notify processing start
			getCallback().onFeedStart();

			// count total product numbers
			long totalNumber = FullIndexHelper.getTotalNumber(getConnection());
			getCallback().setTotalProductNumber(totalNumber);

			// split product IDs into several segments, each segments will have
			// a thread
			long segsize = (totalNumber + params.getPieces() - 1) / params.getPieces();
			List<Long> idSegmentValus = FullIndexHelper.getIdSegmentStartValues(getConnection(), totalNumber, segsize);
			System.out.println("ids=" + idSegmentValus);
			FullIndexHelper.prepare(getConnection());
			// first job is clear all old index datas
			ClearJob clearJob = new ClearJob();
			clearJob.statements = new ArrayList<PreparedStatement>();
			for (int i = 1; i < idSegmentValus.size(); i++) {
				clearJob.statements.add(FullIndexHelper.createProductQueryStatement(getConnection(),
						idSegmentValus.get(i - 1), idSegmentValus.get(i)));
			}
			new Thread(clearJob).start();

		} catch (Exception e) {
			callback.onException(e);
			return false;
		}

		return true;
	}

	public void clearIndex() {
		searchClient.clearAllData(params.getIndexName(), params.getTypeName());
	}

	public void doFeedProducts(PreparedStatement statement) {
		List<PreparedStatement> sts = new ArrayList<PreparedStatement>();
		try {
			PreparedStatement priceSt = FullIndexHelper.createGetPriceStatement(getConnection());
			sts.add(priceSt);
			PreparedStatement imageSt = FullIndexHelper.createGetImageStatement(getConnection());
			sts.add(imageSt);
			PreparedStatement authSt = FullIndexHelper.createGetAuthStatement(getConnection());
			sts.add(authSt);
			PreparedStatement certificatesSt = FullIndexHelper.createGetCertificatesStatement(getConnection());
			sts.add(certificatesSt);
			PreparedStatement proofSt = FullIndexHelper.createGetProofStatement(getConnection());
			sts.add(proofSt);
			PreparedStatement thirdProofSt = FullIndexHelper.createGetThirdProofStatement(getConnection());
			sts.add(thirdProofSt);
			PreparedStatement testreportSt = FullIndexHelper.createGetTestreportStatement(getConnection());
			sts.add(testreportSt);
			int count = 0;
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
				datas.clear();
			}
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
		}finally{
			for(PreparedStatement st: sts){
				try {
					st.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public ElasticSerachClient getSearchClient() {
		return searchClient;
	}

	public void setSearchClient(ElasticSerachClient searchClient) {
		this.searchClient = searchClient;
	}
}
