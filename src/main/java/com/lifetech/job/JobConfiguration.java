package com.lifetech.job;

import com.lifetech.job.data.Job;
import com.lifetech.job.data.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class JobConfiguration {

    @Bean
    public Function<Job, Job> saveJob(JobRepository jobRepository) {
        return jobRepository::save;
    }

    @Bean
    public Supplier<List<Job>> getJobs(JobRepository jobRepository) {
        return () -> jobRepository.getAll("ALL");
    }

    @Bean
    public Function<Integer, List<Job>> getJobsLimitedTo(JobRepository jobRepository) {
        return (Integer limit) -> jobRepository.getAll(String.valueOf(limit));
    }

    @Bean
    public Function<String, List<Job>> getJobsByTitle(JobRepository jobRepository) {
        return (String title) -> jobRepository.getByTitle(title, "ALL");
    }

    @Bean
    public BiFunction<String, Integer, List<Job>> getJobsByTitleLimitedTo(JobRepository jobRepository) {
        return (String title, Integer limit) -> jobRepository.getByTitle(title, String.valueOf(limit));
    }
}