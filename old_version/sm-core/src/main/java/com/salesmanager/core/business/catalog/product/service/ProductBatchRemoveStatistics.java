package com.salesmanager.core.business.catalog.product.service;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class ProductBatchRemoveStatistics {
	private Date startTime;
	private Date endTime;
	private AtomicLong totalPlanned = new AtomicLong(0);
	private AtomicLong totalScheduled = new AtomicLong(0);
	private AtomicLong totalDeleted = new AtomicLong(0);
	private AtomicBoolean running = new AtomicBoolean(false);
	private String sql;
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public AtomicLong getTotalPlanned() {
		return totalPlanned;
	}
	public void setTotalPlanned(AtomicLong totalPlanned) {
		this.totalPlanned = totalPlanned;
	}
	public AtomicLong getTotalScheduled() {
		return totalScheduled;
	}
	public void setTotalScheduled(AtomicLong totalScheduled) {
		this.totalScheduled = totalScheduled;
	}
	public AtomicLong getTotalDeleted() {
		return totalDeleted;
	}
	public void setTotalDeleted(AtomicLong totalDeleted) {
		this.totalDeleted = totalDeleted;
	}
	public AtomicBoolean getRunning() {
		return running;
	}
	public void setRunning(AtomicBoolean running) {
		this.running = running;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public synchronized void setExecutionSql(String sql){
		setSql(sql);
	}
	
}
