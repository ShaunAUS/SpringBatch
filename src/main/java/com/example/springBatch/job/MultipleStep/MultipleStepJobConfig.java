/*
package com.example.springBatch.job.MultipleStep;


import com.example.springBatch.job.fileDataReadWrite.dto.Player;
import com.example.springBatch.job.fileDataReadWrite.dto.PlayerYear;
import lombok.RequiredArgsConstructor;
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
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class MultipleStepJobConfig {

    @Bean
    public Job multipleJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new JobBuilder("fileReadWriteJob",jobRepository)
                .incrementer(new RunIdIncrementer()) // Id 순차적으로 부여
                .start(multipleStepV1(jobRepository,platformTransactionManager))
                .next(multipleStepV2(jobRepository,platformTransactionManager))
                .next(multipleStepV3(jobRepository,platformTransactionManager))
                .build();
    }

    @Bean
    @JobScope
    public Step multipleStepV1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager ) {
        return new StepBuilder("multipleStepV1", jobRepository)
                .tasklet(multipleTaskletV1(), platformTransactionManager) // or .chunk(chunkSize, transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step multipleStepV2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager ) {
        return new StepBuilder("multipleStepV2", jobRepository)
                .tasklet(multipleTaskletV2(), platformTransactionManager) // or .chunk(chunkSize, transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step multipleStepV3(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager ) {
        return new StepBuilder("multipleStepV3", jobRepository)
                .tasklet(multipleTaskletV3(), platformTransactionManager) // or .chunk(chunkSize, transactionManager)
                .build();
    }


    @Bean
    @StepScope // 스텝하위에서 실행하기때문에 명시함
    public Tasklet multipleTaskletV1(){
        return (contribution, chunkContext) -> {
            //step  에서 step으로 데이터 넘길때
            ExecutionContext jobExecutionContext = chunkContext
                    .getStepContext()
                    .getStepExecution()
                    .getJobExecution()
                    .getExecutionContext();

            jobExecutionContext.put("monkey","banana");

            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    @StepScope // 스텝하위에서 실행하기때문에 명시함
    public Tasklet multipleTaskletV2(){
        return (contribution, chunkContext) -> {

            //step1 에서 던진 데이터 받기
            ExecutionContext jobExecutionContext = chunkContext
                    .getStepContext()
                    .getStepExecution()
                    .getJobExecution()
                    .getExecutionContext();

            jobExecutionContext.get("monkey");

            return RepeatStatus.FINISHED;
        };
    }
    @Bean
    @StepScope // 스텝하위에서 실행하기때문에 명시함
    public Tasklet multipleTaskletV3(){
        return (contribution, chunkContext) -> {
            System.out.println("This is first taskLet step");
            return RepeatStatus.FINISHED;
        };
    }
}
*/
