package com.lifetech.job;

import com.lifetech.job.db.Job;
import com.lifetech.tag.data.Tag;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
public class JobService {

    private static final Logger log = LoggerFactory.getLogger(JobService.class);

    private final Function<Integer, Document> jobDocumentSupplier;
    private final BiFunction<List<String>, Integer, List<Job>> findJobs;
    private final Consumer<Job> saveJob;
    private final Function<Tag, Tag> saveTag;
    private final Function<String, Tag> finTagByName;

    private final Integer weekdaysTimePeriodHours;
    private final Integer weekendsTimePeriodHours;
    private final Integer nightlyTimePeriodHours;


    public JobService(
            final Function<Integer, Document> jobDocumentSupplier,
            final BiFunction<List<String>, Integer, List<Job>> findJobs,
            final Consumer<Job> saveJob,
            final Function<Tag, Tag> saveTag,
            final Function<String, Tag> finTagByName,
            @Value("${jobs.search.weekdaysTimePeriodHours}") final Integer weekdaysTimePeriodHours,
            @Value("${jobs.search.weekendsTimePeriodHours}") final Integer weekendsTimePeriodHours,
            @Value("${jobs.search.nightlyTimePeriodHours}") final Integer nightlyTimePeriodHours) {
        this.jobDocumentSupplier = jobDocumentSupplier;
        this.findJobs = findJobs;
        this.saveJob = saveJob;
        this.saveTag = saveTag;
        this.finTagByName = finTagByName;
        this.weekdaysTimePeriodHours = weekdaysTimePeriodHours;
        this.weekendsTimePeriodHours = weekendsTimePeriodHours;
        this.nightlyTimePeriodHours = nightlyTimePeriodHours;
    }

    void fetchJobs(Integer timePeriodHours) {
        Document doc = jobDocumentSupplier.apply(timePeriodHours);
        Elements jobs = doc.select(".base-card");
        log.info("Found {} jobs", jobs.size());

        for (Element job : jobs) {
            try {
                Long linkedinId = extractLinkedinId(job);
                String title = extractTitle(job);
                String company = extractCompany(job);
                String location = extractLocation(job);
                LocalDateTime startDate = LocalDateTime.now();

                Set<Tag> tags = new HashSet<>();
                tags.add(createTag(company, "COMPANY"));
                tags.add(createTag(location, "LOCATION"));

                saveJob.accept(new Job(linkedinId, title, null, startDate, null, tags));
            } catch (DataIntegrityViolationException e) {
                log.warn(e.getMostSpecificCause().getMessage());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private Tag createTag(String name, String type) {
        Tag tag = finTagByName.apply(name);
        if (tag != null) {
            return tag;
        } else {
            return saveTag.apply(new Tag(type, name));
        }
    }

    public List<Job> getJobs(List<String> keywords, Integer limit) {
        return findJobs.apply(keywords, limit);
    }


    private Long extractLinkedinId(Element baseCardElement) throws Exception {
        String attribute = baseCardElement.attr("data-entity-urn");
        attribute = attribute.replaceAll("\"", "");
        int lastColonIdx = attribute.lastIndexOf(':');
        String jobidstring = attribute.substring(lastColonIdx + 1);
        if (lastColonIdx > 0) {
            return Long.parseLong(jobidstring.trim());
        } else {
            throw new Exception("Could not parse linkedinId from attribute: " + attribute);
        }
    }

    private String extractCompany(Element haseCardElement) {
        Elements subTitle = haseCardElement.select(".base-search-card__subtitle");
        Elements companyEl = subTitle.select(".hidden-nested-link");
        return !companyEl.isEmpty() ? companyEl.text() : subTitle.text();
    }

    private String extractTitle(Element baseCardElement) {
        Elements title = baseCardElement.select(".base-search-card__title");
        return !title.isEmpty() ? title.text() : "";
    }

    private String extractLocation(Element baseCardElement) {
        Elements location = baseCardElement.select(".job-search-card__location");
        return !location.isEmpty() ? location.text() : "";
    }


    @Scheduled(cron = "${jobs.search.scheduleWeekdaysCron}")
    public void fetchWeekdaysJobs() {
        fetchJobs(weekdaysTimePeriodHours);
    }

    @Scheduled(cron = "${jobs.search.scheduleNightlyCron}")
    public void fetchNightlyCron() {
        fetchJobs(nightlyTimePeriodHours);
    }

    @Scheduled(cron = "${jobs.search.scheduleWeekendsCron}")
    public void fetchJobsWeekends() {
        fetchJobs(weekendsTimePeriodHours);
    }
}