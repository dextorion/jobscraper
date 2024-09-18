package com.lifetech.job.db;

import com.lifetech.tag.db.Tag;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long linkedinId;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDate endDate;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "jobs_tags", joinColumns = @JoinColumn(name = "jobs_id"), inverseJoinColumns = @JoinColumn(name = "tags_id"))
    private Set<Tag> tags;

    public Job() {
    }

    public Job(Long linkedinId, String title, String description, LocalDateTime startDate, LocalDate endDate, Set<Tag> tags) {
        this(null, linkedinId, title, description, startDate, endDate, tags);
    }

    public Job(Integer id, Long linkedinId, String title, String description, LocalDateTime startDate, LocalDate endDate, Set<Tag> tags) {
        this.id = id;
        this.linkedinId = linkedinId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tags = tags;
    }


    public int getId() {
        return id;
    }

    public Long getLinkedinId() {
        return linkedinId;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
}