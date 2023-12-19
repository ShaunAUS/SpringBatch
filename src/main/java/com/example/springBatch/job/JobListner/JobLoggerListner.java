package com.example.springBatch.job.JobListner;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.repository.dao.JobExecutionDao;

import lombok.extern.slf4j.Slf4j;

/**
 * 리스너
 */
@Slf4j
public class JobLoggerListner implements JobExecutionListener {

	private static String BEFORE_MESSAGE = "{} Job is running";
	private static String AFTER_MESSAGE = "{} Job is finished (Status : {})";

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info(BEFORE_MESSAGE, jobExecution.getJobInstance().getJobName());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		log.info(AFTER_MESSAGE, jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());

		if (jobExecution.getStatus() == BatchStatus.FAILED) {
			//email
			log.error("job failed");

		}
	}
}
