package com.crio.jobportal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.crio.jobportal.App;
import com.crio.jobportal.dto.GetJobDto;
import com.crio.jobportal.dto.Job;
import com.crio.jobportal.exchange.GetJobsResponse;
import com.crio.jobportal.service.JobService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = {App.class})
class JobsControllerIntegrationTest {
  
  private static final String JOB_PORTAL_API_URI = "/job-portal";
  private MockMvc mvc;

  @MockBean
  private JobService jobService;

  @InjectMocks
  private JobsController jobsController;

  @BeforeEach
  public void setupInteg() {
    mvc = MockMvcBuilders.standaloneSetup(jobsController).build();
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void saveJobEntities() throws Exception {

    doNothing().when(jobService).saveJobs(anyList());

    URI uri = UriComponentsBuilder 
        .fromPath(JOB_PORTAL_API_URI)
        .path("/put-job")
        .build().toUri();

    assertEquals(JOB_PORTAL_API_URI + "/put-job", uri.toString());

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

    String jobPutString = new ObjectMapper().writeValueAsString(jobs);

    MockHttpServletResponse response = mvc.perform(
        put(uri.toString()).content(jobPutString).accept(APPLICATION_JSON_VALUE))
        .andReturn().getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  public void getJobs() throws Exception {
    
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
    skillKeyWords2.add("API");
        
    GetJobDto dto2 = GetJobDto.builder().companyName("Grokers").jobTitle("SDE 2")
        .description("Job Description String 2").location("Bangalore, Karnataka")
        .skillKeyWords(skillKeyWords2).listDate("2020-09-12").applicantSize("2").build();

    jobs.add(dto2);
      
    GetJobsResponse getJobsResponse = new GetJobsResponse(jobs);

    doReturn(getJobsResponse).when(jobService).getJobs(any());

    URI uri = UriComponentsBuilder 
        .fromPath(JOB_PORTAL_API_URI)
        .path("/search-jobs")
        .queryParam("skills", "C++,Testing")
        .queryParam("location", "Bangalore")
        .build().toUri();

    assertEquals(JOB_PORTAL_API_URI + "/search-jobs?skills=C++,Testing&location=Bangalore", 
        uri.toString());

    MockHttpServletResponse response = mvc.perform(
        get(uri.toString()).accept(APPLICATION_JSON_VALUE))
        .andReturn().getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    
    GetJobsResponse receivedResponse = new ObjectMapper().readValue(response
        .getContentAsString(), new TypeReference<GetJobsResponse>() {});
    assertEquals(getJobsResponse, receivedResponse);
  }

}