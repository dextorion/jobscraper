package com.lifetech.job;

import com.lifetech.job.db.Job;
import com.lifetech.tag.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("api")
public class JobController {

    JobService jobService;

    public JobController(JobService jobService, TagService tagService) {
        this.jobService = jobService;
    }

    @GetMapping("/jobs")
    public List<Job> getJobs(@RequestParam(required = false) List<String> keywords, @RequestParam(required = false) List<String> filterwords, @RequestParam(required = false) Integer limit) {
        List<Job> jobs = jobService.getJobs(keywords, limit != null ? limit : 25);

        if (filterwords != null && !filterwords.isEmpty()) {
            return jobs.stream().filter(job -> filterwords.stream().allMatch(word -> job.getTags().stream().anyMatch(tag -> tag.getName().equals(word)))).toList();
        } else {
            return jobs;
        }
    }

    @GetMapping("/fetchJobs")
    public void fetchJobs() {
        jobService.fetchJobs(24);
    }
}
