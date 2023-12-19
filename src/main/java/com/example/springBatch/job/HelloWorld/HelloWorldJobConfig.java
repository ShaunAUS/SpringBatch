package com.example.springBatch.job.HelloWorld;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class HelloWorldJobConfig {

	@Bean
	public Job helloWorldJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
		return new JobBuilder("helloWorldJob",jobRepository)
			.incrementer(new RunIdIncrementer()) // Id 순차적으로 부여
			.start(helloWorldStep(jobRepository,platformTransactionManager))
			.build();
	}

	//step 하위에는 reader,processor,writer 등이 올 수 있는데  읽고 쓰고 할게 없으면 그냥 tasklet으로 만듬
	@Bean
	@JobScope
	public Step helloWorldStep(JobRepository jobRepository,PlatformTransactionManager platformTransactionManager) {
		return new StepBuilder("helloWorldStep", jobRepository)
			.tasklet(helloWorldTasklet(), platformTransactionManager) // or .chunk(chunkSize, transactionManager)
			.build();
	}

	@Bean
	@StepScope // 스텝하위에서 실행하기때문에 명시함
	public Tasklet helloWorldTasklet(){
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("This is first taskLet step");
				return RepeatStatus.FINISHED;
			}
		};
	}

}
