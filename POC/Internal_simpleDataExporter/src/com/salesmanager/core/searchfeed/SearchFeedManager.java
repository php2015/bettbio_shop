package com.salesmanager.core.searchfeed;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchFeedManager implements SearchFeedCallback{
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchFeedManager.class);
	protected SearchFeedCallback feedCallback;
	private Properties orignalProps;
	private List<SearchFeedProcessor> processorList;
	private Connection connection;
	protected Map<String, StatisticData> statistics;
	
	public void loadConfig(InputStream cfgInputStream) throws Exception {
		Properties props = new Properties();
		InputStreamReader rins = new InputStreamReader(cfgInputStream, "UTF-8");
		props.load(rins);
		this.orignalProps = props;
	}

	public void setProcessCallback(SearchFeedCallback feedCallback) {
		this.feedCallback = feedCallback;
	}

	public Properties getOrignalProps() {
		return orignalProps;
	}

	public void setOrignalProps(Properties orignalProps) {
		this.orignalProps = orignalProps;
	}

	protected void beforeFeed() {
		// by default, nothing
	}
	
	protected void afterFeed() {
		// by default, nothing
	}

	public void startFeed() throws Exception{
		statistics = new ConcurrentHashMap<String, StatisticData>();
		beforeFeed();
		prepareFeed();
		for(SearchFeedProcessor poc:processorList){
			poc.startFeed();
		}
		afterFeed();
	}

	protected void prepareFeed() throws Exception{
		// first, find out how many feed will be execute
		ConfigLoader cfg = new ConfigLoader();
		cfg.setProps(orignalProps);
		
		List<String> types = cfg.getStringList("feed.types");
		this.processorList = new ArrayList<SearchFeedProcessor>();
		for(String type: types){
			FeedConfig proCfg = cfg.getFeedCfg(type);
			SearchFeedProcessor processor = (SearchFeedProcessor) Class.forName(proCfg.getProcessor()).newInstance();
			processor.setConfig(proCfg);
			processor.setDbConnection(connection);
			processor.loadSpecialConfig(cfg);
			processor.prepare();
			processor.setFeedProcessCallback(this);
			processorList.add(processor);
		}
//		String types = orignalProps.getProperty();
//		for (Object key :orignalProps.keySet()){
//			if (key instanceof String){
//				String propName = (String) key;
//				
//			}
//		}
		
	}

	public void setDbConnection(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void setTotal(String typeName, int totalRow) {
		StatisticData sts = getStatisticData(typeName);
		sts.total.set(totalRow);
		if (feedCallback != null){
			feedCallback.setTotal(typeName, totalRow);
		}
		LOGGER.info(sts.toString());
	}

	private StatisticData getStatisticData(String typeName) {
		StatisticData sts = statistics.get(typeName);
		if (sts == null){
			sts = new StatisticData();
			sts.name = typeName;
			statistics.put(typeName, sts);
		}
		return sts;
	}

	@Override
	public void addStarted(String typeName, int number) {
		StatisticData sts = getStatisticData(typeName);
		sts.started.addAndGet(number);
		if (feedCallback != null){
			feedCallback.addStarted(typeName, number);
		}
		LOGGER.info(sts.toString());
	}

	@Override
	public void addFeeded(String typeName, int number) {
		StatisticData sts = getStatisticData(typeName);
		sts.feeded.addAndGet(number);
		if (feedCallback != null){
			feedCallback.addFeeded(typeName, number);
		}
		LOGGER.info(sts.toString());
	}

	@Override
	public void addIgnored(String typeName, int number) {
		StatisticData sts = getStatisticData(typeName);
		sts.ignored.addAndGet(number);
		if (feedCallback != null){
			feedCallback.addIgnored(typeName, number);
		}
		LOGGER.info(sts.toString());
	}

	@Override
	public void allStarted(String typeName) {
		StatisticData sts = getStatisticData(typeName);
		sts.started.set(sts.total.get());
		if (feedCallback != null){
			feedCallback.allStarted(typeName);
		}
		LOGGER.info(sts.toString());
	}
	

	@Override
	public void finished(String typeName) {
		StatisticData sts = getStatisticData(typeName);
		if (feedCallback != null){
			feedCallback.finished(typeName);
		}
		LOGGER.info(sts.toString());
	}


	class StatisticData {
		String name;
		AtomicInteger total = new AtomicInteger(0);
		AtomicInteger started = new AtomicInteger(0);
		AtomicInteger ignored = new AtomicInteger(0);
		AtomicInteger feeded = new AtomicInteger(0);
		
		public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append(name).append(": total ").append(total.get());
			sb.append(", started ").append(started.get());
			sb.append(", ignored ").append(ignored.get());
			sb.append(", feeded ").append(feeded.get());
			return sb.toString();
		}
	}
}
