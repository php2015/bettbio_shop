package com.salesmanager.core.searchfeed;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductSearchFeedProcessor implements SearchFeedProcessor {
	class FeedWorker implements Runnable {

		HashMap<Object, List<Object>> datas;

		public void setData(HashMap<Object, List<Object>> datas) {
			this.datas = datas;
		}

		@Override
		public void run() {
			processBatchData(this);
		}

	}
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSearchFeedProcessor.class);
	protected FeedConfig config;
	protected SearchFeedHelper categoryHelper;
	protected SearchFeedHelper merchantHelper;
	protected SearchFeedHelper priceHelper;
	protected SearchFeedHelper languageHelper;
	protected SearchFeedHelper testReportHelper;
	protected SearchFeedHelper thirdProofHelper;
	protected SearchFeedHelper certificateHelper;
	private Connection connection;
	private PreparedStatement stmt;
	private HashMap<Object, List<Object>> datas;
	private ArrayList<String> outputNames;
	private int[] fieldDataTypes;
	private int idFieldIdx;
	private ResultSet resultSet;
	private SearchFeedCallback searchFeedCallback;
	private LinkedBlockingQueue<FeedWorker> workerQueue;
	private ExecutorService threadPool;
	private List<SearchFeedHelper>  helpers;
	@Override
	public void setFeedProcessCallback(SearchFeedCallback searchFeedCallback) {
		this.searchFeedCallback = searchFeedCallback;
	}

	@SuppressWarnings("unchecked")
	public void processBatchData(FeedWorker feedWorker) {
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("Start to process data batch: "+feedWorker.datas.size());
		}
		// pick out all data which name not starts with "_" to JSON
		List<Integer> usedPos = new ArrayList<Integer>();
		for(int i=0;i<this.outputNames.size();i++){
			if (outputNames.get(i).startsWith("_")){
				continue;
			}
			usedPos.add(i);
		}

		// handle each data
		Iterator<List<Object>> it = feedWorker.datas.values().iterator();
		int handled = 0;
		int ignored = 0;
		JSONArray batchData = new JSONArray();
		while(it.hasNext()){
			List<Object> data = it.next();
			if (!preprocess(data)){
				ignored++;
				continue;
			}else{
				handled++;
			}
			
			// fill already haved data to JSON first
			JSONObject json = new JSONObject();
			for(int pos: usedPos){
				json.put(outputNames.get(pos), data.get(pos));
			}
			
			batchData.add(json);
		}
		// then, go through the helpers
		for(SearchFeedHelper helper: helpers){
			helper.process(batchData, outputNames, feedWorker.datas);
		}
		feedBatch(batchData);
		if (ignored>0){
			searchFeedCallback.addIgnored(getConfig().getTypeName(), ignored);
		}
		if (handled>0){
			searchFeedCallback.addFeeded(getConfig().getTypeName(), handled);
		}
		workerQueue.offer(feedWorker);
	}

	private void feedBatch(JSONArray batchData) {
		// TODO: now just print on the screen
		for(int i=0;i<batchData.size();i++){
			JSONObject data = (JSONObject) batchData.get(i);
			System.out.println(data.toJSONString());
		}
	}

	private boolean preprocess(List<Object> data) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public FeedConfig getConfig() {
		return config;
	}

	@Override
	public void setConfig(FeedConfig config) {
		this.config = config;
	}

	@Override
	public void loadSpecialConfig(ConfigLoader cfg) throws Exception {
//		categoryHelper = createHelper(cfg.getHelperCfg(config.getTypeName(), "category"));
		helpers = new ArrayList<SearchFeedHelper>();
		for(String helperName : getConfig().getHelpers()){
			SearchFeedHelper helper = createHelper(cfg.getHelperCfg(config.getTypeName(), helperName));
			helpers.add(helper);
			LOGGER.info("Created helper " + helperName);
		}
	}

	private SearchFeedHelper createHelper(FeedHelperConfig cfg) throws Exception {
		SearchFeedHelper helper = (SearchFeedHelper) Class.forName(cfg.getProcessor()).newInstance();
		helper.setConfig(cfg);
		helper.setConnection(connection);
		return helper;
	}

	@Override
	public void setDbConnection(Connection connection) {
		setConnection(connection);
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void prepare() throws Exception {
		workerQueue = new LinkedBlockingQueue<FeedWorker>();
		for(int i=0;i<getConfig().getWorkers();i++){
			workerQueue.offer(new FeedWorker());
		}
		threadPool = Executors.newFixedThreadPool(getConfig().getWorkers());
		this.stmt = connection.prepareStatement(getConfig().getQuerySql());
		for(SearchFeedHelper helper: helpers){
			helper.prepare();
		}
	}

	@Override
	public void startFeed() throws Exception {
		ResultSet rs = stmt.executeQuery();
		rs.last();
		int totalRow = rs.getRow();
		rs.beforeFirst();
		LOGGER.info("Totally has " + totalRow+" "+getConfig().getTypeName()+" will be handled to feed to search.");
		searchFeedCallback.setTotal(getConfig().getTypeName(), totalRow);
		this.resultSet = rs;
		Thread thread = new Thread(getConfig().getTypeName()+"_feed_thread"){
			public void run(){
				try {
					doFeedJob();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.setDaemon(false);
		thread.start();
	}
	protected void doFeedJob() throws Exception, Exception {
		outputNames = null;
		datas = new HashMap<Object, List<Object>>();
		int idInBatch = 0;
		while(resultSet.next()){
			if (outputNames == null){
				constructOutputNames(resultSet);
			}
			List<Object> rowData = new ArrayList<Object>();
			for(int i=0;i<outputNames.size();i++){
				Object obj = resultSet.getObject(i+1);
				if (obj == null){
					rowData.add(null);
					continue;
				}
				switch(fieldDataTypes[i]){
				case FeedHelperAbsImpl.DATA_TYPE_INT:
					rowData.add(resultSet.getInt(i+1));
					break;
				case FeedHelperAbsImpl.DATA_TYPE_LONG:
					rowData.add(resultSet.getLong(i+1));
					break;
				case FeedHelperAbsImpl.DATA_TYPE_BOOL:
					rowData.add(resultSet.getBoolean(i+1));
					break;
				case FeedHelperAbsImpl.DATA_TYPE_DATE:
					rowData.add(resultSet.getDate(i+1));
					break;
				default:
					rowData.add(resultSet.getString(i+1));
					break;
				}
			}
			datas.put(rowData.get(idFieldIdx), rowData);
			idInBatch++;
			if (idInBatch >= getConfig().getBatchSize()){
				startBatch(datas);
				searchFeedCallback.addStarted(getConfig().getTypeName(), idInBatch);
				datas=new HashMap<Object, List<Object>>();
				idInBatch = 0;
			}
		}
		if (idInBatch>0){
			startBatch(datas);
			searchFeedCallback.addStarted(getConfig().getTypeName(), idInBatch);
		}
		searchFeedCallback.allStarted(getConfig().getTypeName());
		waitAllFinished();
		searchFeedCallback.finished(getConfig().getTypeName());
		threadPool.shutdown();
	}

	private void waitAllFinished() {
		for(int i=0;i<getConfig().workers;i++){
			try {
				workerQueue.take();
			} catch (InterruptedException e) {
				// e.printStackTrace();
				LOGGER.info("interrupted. Stop all pending work");
			}
			LOGGER.info("Got idle worker " + (i+1));
		}
	}

	private void startBatch(HashMap<Object, List<Object>> datas) {
		try {
			FeedWorker worker = workerQueue.take();
			worker.setData(datas);
			threadPool.execute(worker);
		} catch (InterruptedException e) {
			//e.printStackTrace();
			LOGGER.info("Interrupted, stop all pending work");
		}
	}

	protected void constructOutputNames(ResultSet rSet) throws Exception {
		ResultSetMetaData mData = rSet.getMetaData();
		outputNames = new ArrayList<String>();
		ArrayList<Integer> outputTypes = new ArrayList<Integer>();
		FeedHelperAbsImpl.constructOutputNames(mData, config.getOutputMap(), outputNames, outputTypes);
		LOGGER.info(getConfig().getTypeName()+" will retrieve " + outputNames);
		this.fieldDataTypes = new int[outputTypes.size()];
		for(int i=0;i<outputTypes.size();i++){
			fieldDataTypes[i] = outputTypes.get(i);
			if (outputNames.get(i).equals(getConfig().getIdFieldName())){
				this.idFieldIdx = i;
			}
		}
	}

	
	
	
}
