package com.springbatch.ChunkProcessing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JobLaunchController {
    private final JobOperator jobOperator;

    @Qualifier("firstJob")
    private final Job job;

    @GetMapping("/launchJob/{id}")
    public JobExecution handle(@PathVariable String id) throws Exception{
        JobParameters jobParameters=new JobParametersBuilder()
                .addString("param", id)
                .toJobParameters();

        return jobOperator.start(job, jobParameters);
    }
}
