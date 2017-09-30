package com.bettbio.core.common.service.async;

import org.springframework.core.task.TaskExecutor;
/**
 * 异步服务类
 * @author GuoChunbo
 *
 */
public class AsyncTaskService{

	TaskExecutor taskExecutor;

	public void run(Runnable runnable) {
		taskExecutor.execute(runnable);
	}
	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
}
