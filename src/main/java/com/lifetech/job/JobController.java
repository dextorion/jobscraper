package com.lifetech.job;

import com.lifetech.job.db.Job;
import com.lifetech.tag.TagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController()
@RequestMapping("api")
public class JobController {

    JobService jobService;
    TagService tagService;

    public JobController(JobService jobService, TagService tagService) {
        this.jobService = jobService;
        this.tagService = tagService;
    }

    @GetMapping("/jobs")
    public List<Job> getJobs(
            @RequestParam(required = false) List<String> keywords,
            @RequestParam(required = false) List<String> filterwords,
            @RequestParam(required = false) String attribute,
            @RequestParam(required = false) Integer limit
    ) {
        List<Job> jobs = jobService.getJobs(keywords, limit != null ? limit : 25);

        Stream<Job> jobsStream = attribute != null ? jobs.stream().filter(job -> hasAttribute(job, attribute)) : jobs.stream().filter(job -> !hasAttribute(job, "DELETED"));

        if (filterwords != null && !filterwords.isEmpty()) {
            return jobsStream.filter(job -> filterwords.stream().allMatch(word -> hasTag(job, word))).toList();
        } else {
            return jobsStream.toList();
        }
    }

    private boolean hasAttribute(Job job, String attribute) {
        return job.getTags().stream().anyMatch(tag -> tag.getName().equals(attribute));
    }

    private boolean hasTag(Job job, String tagName) {
        return job.getTags().stream().anyMatch(tag -> tag.getName().equals(tagName));
    }

    @PostMapping("/jobs/{id}")
    public void deleteJob(@PathVariable Integer id, @RequestParam(required = false) Boolean deleted, @RequestParam(required = false) Boolean applied, @RequestParam(required = false) Boolean bookmarked) {

        if (deleted != null) {
            jobService.setAttribute(id, "DELETED", deleted);
        }

        if (applied != null) {
            jobService.setAttribute(id, "APPLIED", applied);
        }

        if (bookmarked != null) {
            jobService.setAttribute(id, "BOOKMARKED", bookmarked);
        }

    }


    @GetMapping("/fetchJobs")
    public void fetchJobs() {
        jobService.fetchJobs(24);
    }

}
