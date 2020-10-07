package com.crio.jobportal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.crio.jobportal.dto.GetJobDto;
import com.crio.jobportal.dto.Job;
import com.crio.jobportal.exchange.GetJobsRequest;
import com.crio.jobportal.exchange.GetJobsResponse;
import com.crio.jobportal.service.JobService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JobsControllerTest {

  @InjectMocks
  JobsController jobsController;
  
  @Mock
  JobService jobService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }


  @Test  
  public void putJobsTest() {

    List<String> skillKeyWords = new ArrayList<>();
    skillKeyWords.add("C++");
    skillKeyWords.add("Algorithms");
    skillKeyWords.add("programming");
    skillKeyWords.add("Testing");

    Job dto = Job.builder()
        .companyName("LinkedIn")
        .jobTitle("SDE 1")
        .description("Job Description String")
        .location("Bangalore, Karnataka")
        .skillKeyWords(skillKeyWords)
        .expiryTime("35")
        .build();

    List<Job> jobs = new ArrayList<>();
    jobs.add(dto);
    
    Mockito.doNothing().when(jobService).saveJobs(jobs);

    String jobsString = "";
    
    try {
      jobsString = new ObjectMapper().writeValueAsString(jobs);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    jobsController.saveJobs(jobsString);

    Mockito.verify(jobService, Mockito.times(1)).saveJobs(jobs);
  }


  @Test
  public void getJobsTest() {
    
    List<String> skillKeyWords1 = new ArrayList<>();
    skillKeyWords1.add("C++");
    skillKeyWords1.add("Algorithms");
    skillKeyWords1.add("programming");
    skillKeyWords1.add("Testing");

    
    List<GetJobDto> jobs = new ArrayList<>();

    GetJobDto dto1 = GetJobDto.builder().companyName("LimedIn").jobTitle("SDE 1")
        .description("Job Description String 1").location("Bangalore, Karnataka")
        .skillKeyWords(skillKeyWords1).listDate("2020-08-21").applicantSize("3").build();

    jobs.add(dto1);
    
    List<String> skillKeyWords2 = new ArrayList<>();
    skillKeyWords2.add("Python");
    skillKeyWords2.add("Debugging");
    skillKeyWords2.add("programming");

    GetJobDto dto2 = GetJobDto.builder().companyName("Grokers").jobTitle("SDE 2")
        .description("Job Description String 2").location("Bangalore, Karnataka")
        .skillKeyWords(skillKeyWords2).listDate("2020-09-12").applicantSize("2").build();

    jobs.add(dto2);
        
    List<String> skillList = new ArrayList<>();
    skillList.add("C++");
    skillList.add("Python");

    List<String> locationList = new ArrayList<>();
    locationList.add("Bangalore");

    GetJobsResponse getQuestionResponse = new GetJobsResponse(jobs);

    GetJobsRequest getJobsRequest = new GetJobsRequest(skillList, locationList);
    Mockito.doReturn(getQuestionResponse).when(jobService).getJobs(getJobsRequest);

    String skillsInput = "C++, Python";
    String locationInput = "Bangalore";
  
    int status = jobsController.searchJobs(skillsInput, locationInput).getStatusCodeValue();

    assertEquals(200, status);
  }

}
