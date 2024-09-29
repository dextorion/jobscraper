package com.lifetech.job;

import com.lifetech.job.db.Job;
import com.lifetech.job.db.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class JobConfiguration {


    @Bean
    public Consumer<Job> saveJob(JobRepository jobRepository) {
        return jobRepository::save;
    }

    @Bean
    public BiFunction<List<String>, Integer, List<Job>> findJobs(JobRepository jobRepository) {
        return jobRepository::findJobs;
    }

    @Bean
    public Function<Integer, Job> findJob(JobRepository jobRepository) {
        return jobRepository::findJob;
    }
}