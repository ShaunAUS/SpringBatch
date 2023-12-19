package com.example.springBatch.job.fileDataReadWrite;

import com.example.springBatch.job.fileDataReadWrite.dto.PlayerYear;
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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.springBatch.job.fileDataReadWrite.dto.Player;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FileDataReadWriteConfig {

	@Bean
	public Job fileReadWriteJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
		return new JobBuilder("fileReadWriteJob",jobRepository)
			.incrementer(new RunIdIncrementer()) // Id 순차적으로 부여
			.start(fileReadWriteStep(jobRepository,platformTransactionManager))
			.build();
	}

	//step 하위에는 reader,processor,writer 등이 올 수 있는데  읽고 쓰고 할게 없으면 그냥 tasklet으로 만듬
	@Bean
	@JobScope
	public Step fileReadWriteStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager ) {
		return new StepBuilder("fileReadWriteStep", jobRepository)
			.<Player, PlayerYear>chunk(5,platformTransactionManager)
			.reader(playerFlatFileItemReader())
/*
			.writer(new ItemWriter<Player>() {
				@Override
				public void write(Chunk<? extends Player> chunk) throws Exception {
					chunk.getItems().forEach(System.out::println);
				}
			})
*/
				.processor(playerItemProcessor())
				.writer(playerFlatFileItemWriter())
			.build();
	}


	@StepScope
	@Bean
	public ItemProcessor<Player,PlayerYear> playerItemProcessor(){
		return new ItemProcessor<Player, PlayerYear>() {
			@Override
			public PlayerYear process(Player item) throws Exception {
				return new PlayerYear(item);
			}
		};
	}

	public FlatFileItemReader<Player> playerFlatFileItemReader(){
		return new FlatFileItemReaderBuilder<Player>()
			.name("playerFlatFileItemReader")
			.resource(new FileSystemResource("Players.csv"))
			.lineTokenizer(new DelimitedLineTokenizer())//데이터를 어떤 기준으로 나누어햐는지
			.fieldSetMapper(new PlayerFieldSetMapper())
			.linesToSkip(1) //첫번째 줄은 건너뛰기
			.build();
	}
	public FlatFileItemWriter<PlayerYear> playerFlatFileItemWriter(){
		BeanWrapperFieldExtractor<PlayerYear> fieldExtractor = new BeanWrapperFieldExtractor<>(); // 어떤 필드를 사용할지에 대해 명시하기위해 사용
		fieldExtractor.setNames(new String[]{"ID","lastName","firstName","yearsExperience"});
		fieldExtractor.afterPropertiesSet();

		DelimitedLineAggregator<PlayerYear> lineAggregator = new DelimitedLineAggregator<>();//어떤 기준으로 파일을 만드는지 알려주기위해
		lineAggregator.setDelimiter(","); //csv 로 다시만들기 때문
		lineAggregator.setFieldExtractor(fieldExtractor);

		FileSystemResource fileSystemResource = new FileSystemResource("players_output.txt");
		return new FlatFileItemWriterBuilder<PlayerYear>()
			.name("playerFlatFileItemWriter")
			.resource(fileSystemResource)
			.lineAggregator(lineAggregator)// 구분자
			.build();
	}


}
