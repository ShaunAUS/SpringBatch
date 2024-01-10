package com.example.springBatch.job.ValidatedParam;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.springBatch.job.ValidatedParam.validator.FileParamValidator;

import ch.qos.logback.core.joran.spi.NoAutoStartUtil;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ValidatedParamJobConfig {


	@Bean
	public Job validatedParamJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
		return new JobBuilder("validatedParamJob",jobRepository)
			.incrementer(new RunIdIncrementer()) // Id 순차적으로 부여
			//validator(new FileParamValidator()) // 검증 단독으로 할때
			.validator(multipleValidator()) // validators // 멀티 검증
			.start(validatedStep(jobRepository,platformTransactionManager))
			.build();
	}


	private CompositeJobParametersValidator multipleValidator(){
		CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
		validator.setValidators(Arrays.asList(new FileParamValidator()));
		return validator;
	}

	//step 하위에는 reader,processor,writer 등이 올 수 있는데  읽고 쓰고 할게 없으면 그냥 tasklet으로 만듬
	@Bean
	@JobScope
	public Step validatedStep(JobRepository jobRepository,PlatformTransactionManager platformTransactionManager) {
		return new StepBuilder("validatedStepParamStep", jobRepository)
			.tasklet(validatedTasklet(),platformTransactionManager) // or .chunk(chunkSize, transactionManager)
			.build();
	}

	@Bean
	@StepScope // 스텝하위에서 실행하기때문에 명시함
	public Tasklet validatedTasklet(){
		return (contribution, chunkContext) -> {

			JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters(); //설정으로 넘어오는 파라미터값
			String fileName = jobParameters.getString("fileName");

			//TODO check
			System.out.println(fileName);
			System.out.println("This is first taskLet step");
			return RepeatStatus.FINISHED;
		};
	}
}
