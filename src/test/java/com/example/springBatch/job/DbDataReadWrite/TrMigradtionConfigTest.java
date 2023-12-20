package com.example.springBatch.job.DbDataReadWrite;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.springBatch.core.domain.accounts.AccountsRepository;
import com.example.springBatch.core.domain.orders.Orders;
import com.example.springBatch.core.domain.orders.OrdersRepository;
import com.example.springBatch.SpringBatchTestConfig;

@SpringBootTest(classes = {SpringBatchTestConfig.class, TrMigradtionConfig.class})
@SpringBatchTest
@ActiveProfiles("test")
class TrMigradtionConfigTest {


	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private AccountsRepository accountsRepository;

	@AfterEach
	public void cleanUp() {
		ordersRepository.deleteAll();
		accountsRepository.deleteAll();
	}

	@Test
	public void success_noData() throws Exception {
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		//org.assertj.core.api.Assertions.assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
		//org.assertj.core.api.Assertions.assertThat(accountsRepository.count()).isEqualTo(0);

		Assertions.assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
		Assertions.assertEquals(0, accountsRepository.count());

	}

	@Test
	public void success_exestData()throws Exception{

		Orders order1 = new Orders(null, "kakao gitft", 15000, new Date());
		Orders order2 = new Orders(null, "naver gitft", 25000, new Date());

		ordersRepository.save(order1);
		ordersRepository.save(order2);

		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		Assertions.assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
		Assertions.assertEquals(2,accountsRepository.count());
	}

}