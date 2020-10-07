package com.crio.jobportal.repositoryservice;

import com.crio.jobportal.dto.GetJobDto;
import com.crio.jobportal.dto.Job;
import com.crio.jobportal.exchange.GetJobsRequest;

import java.util.List;

public interface JobRepositoryService {
  void saveJobs(List<Job> jobs);
  
  List<GetJobDto> getMatchingJobs(GetJobsRequest getJobsRequest);
}
