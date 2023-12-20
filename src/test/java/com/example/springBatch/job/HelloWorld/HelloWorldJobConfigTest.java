package com.example.springBatch.job.HelloWorld;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.springBatch.SpringBatchTestConfig;

@SpringBootTest(classes = {SpringBatchTestConfig.class, HelloWorldJobConfig.class})
@SpringBatchTest
@ActiveProfiles("test")
class HelloWorldJobConfigTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	public void success() throws Exception {
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		Assertions.assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
	}



}