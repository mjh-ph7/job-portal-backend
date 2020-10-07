package com.crio.jobportal.service;

import com.crio.jobportal.dto.Job;
import com.crio.jobportal.exchange.GetJobsRequest;
import com.crio.jobportal.exchange.GetJobsResponse;

import java.util.List;

public interface JobService {
  void saveJobs(List<Job> jobs);

  GetJobsResponse getJobs(GetJobsRequest getJobsRequest);
}
