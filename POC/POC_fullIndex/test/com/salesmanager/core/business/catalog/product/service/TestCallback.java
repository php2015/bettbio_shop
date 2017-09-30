package com.salesmanager.core.business.catalog.product.service;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.elasticsearch.action.bulk.BulkItemResponse.Failure;

public class TestCallback implements FullIndexCallback {
	protected AtomicLong total = new AtomicLong(0);
	protected AtomicLong indexed = new AtomicLong(0);
	protected AtomicLong ignored = new AtomicLong(0);
	protected AtomicLong failed = new AtomicLong(0);
	protected long startTime;
	protected AtomicBoolean running = new AtomicBoolean(false);

	@Override
	public void onFeedStart() {
		System.out.println("Start to feed");
		startTime = System.currentTimeMillis();
		running.set(true);
	}

	@Override
	public void onFeedFinished() {
		long curTs = System.currentTimeMillis();
		System.out.println("Feed done. Used " + (curTs - startTime) / 1000.0 + "seconds");
		running.set(false);
	}

	@Override
	public void setTotalProductNumber(long total) {
		this.total.set(total);
		System.out.println("total number is " + total);
	}

	@Override
	public void onProductIndexed(long productId) {
		indexed.incrementAndGet();
	}

	@Override
	public void onProductIgnored(long productId) {
		ignored.incrementAndGet();
	}

	@Override
	public void onProductException(long productId, Exception e) {
		failed.incrementAndGet();
	}

	@Override
	public void onIndexCleared() {
		long curTs = System.currentTimeMillis();
		System.out.println("Old index cleared. @" + (curTs - startTime) / 1000.0 + "seconds");
	}

	@Override
	public void onException(Exception e) {
		e.printStackTrace();
	}

	@Override
	public void onProductFailure(long id, Failure failure) {
		failed.incrementAndGet();
		System.out.println("Failure is " + failure.getMessage());
	}

	public void dump() {
		long curTs = System.currentTimeMillis();
		System.out.printf("(%8.3f)Finished: %b, total=%d, fail/ignore/done=%d/%d/%d\n", ((curTs - startTime) / 1000.0), !running.get(), total.get(),
				failed.get(), ignored.get(), indexed.get());
	}

}
