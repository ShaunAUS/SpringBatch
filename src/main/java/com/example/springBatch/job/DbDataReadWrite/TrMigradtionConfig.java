package com.example.springBatch.job.DbDataReadWrite;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.springBatch.core.domain.accounts.Accounts;
import com.example.springBatch.core.domain.accounts.AccountsRepository;
import com.example.springBatch.core.domain.orders.Orders;
import com.example.springBatch.core.domain.orders.OrdersRepository;

import lombok.RequiredArgsConstructor;

//데이터 이관 설정
@Configuration
@RequiredArgsConstructor
public class TrMigradtionConfig {

	private final OrdersRepository ordersRepository;
	private final AccountsRepository accountsRepository;


	@Bean
	public Job trMigrationJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
		return new JobBuilder("trMigrationJob", jobRepository)
			.incrementer(new RunIdIncrementer()) // Id 순차적으로 부여
			.start(trMigrationStep(jobRepository,platformTransactionManager))
			.build();
	}

	@Bean
	@JobScope
	public Step trMigrationStep(JobRepository jobRepository,
		PlatformTransactionManager platformTransactionManager
	) {
		return new StepBuilder("trMigrationStep ", jobRepository)
			.<Orders, Accounts>chunk(5, platformTransactionManager)// 어떤 데이터로 읽어와서 어떤데이터로 쓸건지, 5개의 사이즈만큼 처리후 데이터 커밋
			.reader(trOrdersReader())
			/*
						.writer(new itemwriter() {
							@override
							public void write(chunk chunk) throws exception {
								list<orders> orders = chunk.getitems();
								for (orders order : orders) {
									system.out.println("order = " + order);
								}
							}
						})
			*/
			.processor(trOrderProcessor())
			.writer(trOrdersWriter())
			.build();
	}
	/**
	 * wirter1
	 */

	@Bean
	@StepScope
	public RepositoryItemWriter<Accounts> trOrdersWriter() {
		return new RepositoryItemWriterBuilder<Accounts>()
			.repository(accountsRepository)
			.methodName("save")
			.build();
	}

	/**
	 * wirter2
	 */
/*
	@Bean
	@StepScope
	public ItemWriter<Accounts> trOderWriter() {
		return new ItemWriter<Accounts>() {
			@Override
			public void write(List<? extends Accounts> items) throws Exception {
				accountsRepository.saveAll(items);
			}
		};
	}
*/
	@Bean
	@StepScope
	public ItemProcessor<Orders, Accounts> trOrderProcessor() {
		return new ItemProcessor<Orders, Accounts>() {
			@Override
			public Accounts process(Orders item) throws Exception {
				return new Accounts(item);
			}
		};
	}

	@Bean
	@StepScope
	public RepositoryItemReader<Orders> trOrdersReader() {

		return new RepositoryItemReaderBuilder<Orders>()
			.name("trOrdersReader")
			.repository(ordersRepository)
			.methodName("findAll")
			.sorts(Map.of("id", Sort.Direction.ASC))
			.pageSize(5) //보통 chunk 사이즈와 같게 설정
			.saveState(false)
			.build();

	}

}
