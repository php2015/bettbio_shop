package com.salesmanager.core.business.catalog.product.service;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class FullIndexServiceTest {
	protected static Connection connection;
	protected static FullIndexParams params;
	protected static ElasticSerachClient searchClient;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		connection = (Connection) DriverManager.getConnection(
				"jdbc:mysql://127.0.0.1:3306/SALESMANAGER?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8",
				"bshopizer", "mySQL@2015Bio");
		params = new FullIndexParams();
		params.setIndexName("product_zh_backup");
		params.setTypeName("product_zh");
		params.setBatchSize(500);
		params.setPieces(4);
		searchClient = new ElasticSerachClient();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		connection.close();
	}

	@Test
	public void test() {
		FullIndexService service = new FullIndexService();
		TestCallback calback = new TestCallback();
		service.setCallback(calback);
		service.setParams(params);
		service.setConnection(connection);
		service.setSearchClient(searchClient);

		boolean started = service.startProcessing();
		System.out.println("Create jobs " + started);

		try {
			do {
				calback.dump();
				Thread.sleep(1000);
			} while (calback.running.get());
			Thread.sleep(1000);
			calback.dump();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("All finished");
	}

}
