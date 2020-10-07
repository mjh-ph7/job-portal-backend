package com.crio.jobportal.repository;

import com.crio.jobportal.data.JobEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRepository extends MongoRepository<JobEntity, String> {

}
