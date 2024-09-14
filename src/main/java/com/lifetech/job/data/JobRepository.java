package com.lifetech.job.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface JobRepository extends CrudRepository<Job, Long> {

    @Query(value = "SELECT j FROM jobs j ORDER BY j.startDate DESC LIMIT :limit")
    List<Job> getAll(@Param("limit") String limit);


    @Query(value = "SELECT j FROM jobs j WHERE j.title LIKE %:title% ORDER BY j.startDate DESC LIMIT :limit")
    List<Job> getByTitle(@Param("title") String title, @Param("limit") String limit);
}
