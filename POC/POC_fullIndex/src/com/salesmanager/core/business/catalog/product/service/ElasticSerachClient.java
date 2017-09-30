package com.salesmanager.core.business.catalog.product.service;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.deletebyquery.DeleteByQueryResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;

public class ElasticSerachClient {
	protected TransportClient client;
	private String indexName;
	private String typeName;

	public TransportClient getClient() {
		return client;
	}

	public void setClient(TransportClient client) {
		this.client = client;
	}

	@SuppressWarnings("deprecation")
	public void clearAllData(String indexName, String typeName) {
		if (client == null) {
			initClient(indexName, typeName);
		}

		// TODO Auto-generated method stub
		DeleteByQueryResponse rsp = client.prepareDeleteByQuery(indexName).setQuery(QueryBuilders.matchAllQuery())
				.setTypes(typeName).execute().actionGet();
		System.out.println(rsp.status());
	}

	private void initClient(String indexName, String typeName) {
		this.indexName = indexName;
		this.typeName = typeName;

		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "BettBio").build();
		client = new TransportClient(settings);
		client.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
	}

	public BulkResponse bulkIndex(String indexName, String typeName, List<Map<String, Object>> datas) {
		if (client == null) {
			initClient(indexName, typeName);
		}
		BulkRequestBuilder bulkRequest = client.prepareBulk();

		for (Map<String, Object> data : datas) {
			bulkRequest.add(client.prepareIndex(indexName, typeName, String.valueOf(data.get("_id"))).setSource(data));
		}
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		return bulkResponse;
	}

}
