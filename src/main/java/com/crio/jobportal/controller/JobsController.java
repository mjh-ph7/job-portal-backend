package com.crio.jobportal.controller;

import com.crio.jobportal.dto.Job;
import com.crio.jobportal.exchange.GetJobsRequest;
import com.crio.jobportal.exchange.GetJobsResponse;
import com.crio.jobportal.service.JobService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(JobsController.PORTAL_API_ENDPOINT)
public class JobsController {

  @Autowired
  private JobService jobService;

  protected static final String PORTAL_API_ENDPOINT = "/job-portal";

  @PutMapping("/put-job")
  public ResponseEntity saveJobs(@RequestBody String jobsString) {
    log.warn("put");
    List<Job> jobs = new ArrayList<>();

    try {
      jobs = new ObjectMapper().readValue(jobsString,
      new TypeReference<List<Job>>() {});
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    jobService.saveJobs(jobs);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/search-jobs")
  public ResponseEntity<GetJobsResponse> searchJobs(@RequestParam(name = "skills",
      required = false) String skills, @RequestParam(name = "location", 
      required = true) String location) {
    log.warn("get");
    
    List<String> skillList = Arrays.asList(skills.split("\\s*(,|\\s)\\s*"));
    List<String> locationList = Arrays.asList(location.split("\\s*(,|\\s)\\s*"));

    GetJobsRequest getJobsRequest = new GetJobsRequest(skillList, locationList);

    return ResponseEntity.ok().body(jobService.getJobs(getJobsRequest));
  }

}
