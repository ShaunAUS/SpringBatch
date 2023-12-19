package com.example.springBatch.job.ValidatedParam.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.util.StringUtils;

public class FileParamValidator implements JobParametersValidator {
	@Override
	public void validate(JobParameters parameters) throws JobParametersInvalidException {
		String fileName = parameters.getString("fileName");

		if(!StringUtils.endsWithIgnoreCase(fileName,"csv")){
			throw new JobParametersInvalidException("this is not csv file");
		}
	}
}
