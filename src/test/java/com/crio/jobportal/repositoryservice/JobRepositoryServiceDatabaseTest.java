package com.crio.jobportal.repositoryservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.crio.jobportal.App;
import com.crio.jobportal.data.JobEntity;
import com.crio.jobportal.dto.GetJobDto;
import com.crio.jobportal.exchange.GetJobsRequest;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;


@SpringBootTest(classes = {App.class})
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class JobRepositoryServiceDatabaseTest {
  
  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private JobRepositoryService repositoryServiceTest;

  private List<JobEntity> allJobs = new ArrayList<>();

  @BeforeEach
  void setup() {
    
    List<String> skillKeyWords = new ArrayList<>();
    skillKeyWords.add("C++");
    skillKeyWords.add("Algorithms");
    skillKeyWords.add("programming");
    skillKeyWords.add("Testing");

    JobEntity dto = JobEntity.builder()
        .companyName("LinkedIn")
        .jobTitle("SDE 1")
        .description("Job Description String")
        .location("Bangalore, Karnataka")
        .skillKeywords(skillKeyWords)
        .listDate("2020-08-22")
        .applicantSize(3)
        .build();
        
    allJobs.add(dto);
    for (JobEntity jobEntity : allJobs) {
      mongoTemplate.save(jobEntity, "jobs");
    }
  }

  @AfterEach
  void teardown() {
    mongoTemplate.dropCollection("jobs");
  }

  @Test
  void testGetFromDataBase(@Autowired MongoTemplate mongoTemplate) {
    assertNotNull(mongoTemplate);
    assertNotNull(repositoryServiceTest);

    List<String> skillList = new ArrayList<>();
    skillList.add("C++");
    skillList.add("Testing");

    List<String> locationList = new ArrayList<>();
    locationList.add("Bangalore");

    GetJobsRequest getJobsRequest = new GetJobsRequest(skillList, locationList);

    List<GetJobDto> allJobs = repositoryServiceTest.getMatchingJobs(getJobsRequest);

    assertEquals(1, allJobs.size());
    assertEquals("LinkedIn", allJobs.get(0).getCompanyName());
    assertEquals("Bangalore, Karnataka", allJobs.get(0).getLocation());
    assertEquals(4, allJobs.get(0).getSkillKeyWords().size());
  }
      
}
