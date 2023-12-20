package com.example.springBatch.core.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SampleScheduler {

	private final Job helloWorldJob;
	private final JobLauncher jobLauncher;

	@Scheduled(cron = "0 */1 * * * *") // 1분마다 실행
	public void helloWorldJobRun() throws
		JobInstanceAlreadyCompleteException,
		JobExecutionAlreadyRunningException,
		JobParametersInvalidException,
		JobRestartException {

		JobParameters jobParameters = new JobParametersBuilder()
			.addDate("requestDate", new Date()) // 시간을 파라미터로 추가
			.toJobParameters();

		jobLauncher.run(helloWorldJob, jobParameters);

	}
}
