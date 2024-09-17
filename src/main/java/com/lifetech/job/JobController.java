package com.lifetech.job;

import com.lifetech.job.db.Job;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("api")
public class JobController {

    JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/jobs")
    public List<Job> getJobs(@RequestParam(required = false) List<String> keywords, @RequestParam(required = false) Integer limit) {
        return jobService.getJobs(keywords, limit != null ? limit : 25);
    }


    @GetMapping("/fetchJobs")
    public void fetchJobs() {
        jobService.fetchJobs(24);
    }
}
