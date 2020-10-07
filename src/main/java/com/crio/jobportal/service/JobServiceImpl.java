package com.crio.jobportal.service;

import com.crio.jobportal.dto.GetJobDto;
import com.crio.jobportal.dto.Job;
import com.crio.jobportal.exchange.GetJobsRequest;
import com.crio.jobportal.exchange.GetJobsResponse;
import com.crio.jobportal.repositoryservice.JobRepositoryService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobServiceImpl implements JobService {

  @Autowired
  private JobRepositoryService jobsRepositoryService;

  public void saveJobs(List<Job> jobs) {
    jobsRepositoryService.saveJobs(jobs);
  }

  public GetJobsResponse getJobs(GetJobsRequest getJobsRequest) {
    List<GetJobDto> jobsList = jobsRepositoryService.getMatchingJobs(getJobsRequest);
    Collections.sort(jobsList, new Comparator<GetJobDto>() {
      public int compare(GetJobDto o1, GetJobDto o2) {
          if (o1.getKeyWordsMatched() == o2.getKeyWordsMatched()) {
            return 0;
          }
          return o1.getKeyWordsMatched() > o2.getKeyWordsMatched() ? -1 : 1;
      }
    });

    GetJobsResponse getJobsResponse = new GetJobsResponse(jobsList);
    return getJobsResponse;
  }

}
