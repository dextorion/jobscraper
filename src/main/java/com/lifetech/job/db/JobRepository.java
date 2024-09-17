package com.lifetech.job.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Repository
public class JobRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public JobRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }


    @Transactional
    public void save(final Job job) {
        entityManager.persist(job);
    }


    public List<Job> findJobs(List<String> keywords, Integer limit) {
        CriteriaQuery<Job> criteriaQuery = criteriaBuilder.createQuery(Job.class);
        Root<Job> root = criteriaQuery.from(Job.class);

        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("startDate")));

        if (keywords != null && !keywords.isEmpty()) {
            List<Predicate> predicates = new ArrayList<>();
            keywords.forEach(keyword -> predicates.add(criteriaBuilder.like(root.get("title"), "%" + keyword + "%")));
            criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));
        }

        TypedQuery<Job> query = entityManager.createQuery(criteriaQuery);
        if (limit != null) {
            query.setMaxResults(limit);
        }

        return query.getResultList();
    }
}
