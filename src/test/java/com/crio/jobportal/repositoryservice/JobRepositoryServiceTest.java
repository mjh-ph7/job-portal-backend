package com.crio.jobportal.repositoryservice;

import com.crio.jobportal.dto.Job;
import com.crio.jobportal.repository.JobRepository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class JobRepositoryServiceTest {

  @InjectMocks
  private JobRepositoryServiceImpl jobRepositoryService;

  @Mock
  protected JobRepository jobRepository;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testPutJob() {
    
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

    jobRepositoryService.saveJobs(jobs);

    String companyName = "LinkedIn";
    String jobTitle = "SDE 1";
    Mockito.verify(jobRepository).save(ArgumentMatchers.argThat(job -> 
        companyName.equalsIgnoreCase(job.getCompanyName()) 
        && job.getJobTitle().equalsIgnoreCase(jobTitle)));
  }
  
}