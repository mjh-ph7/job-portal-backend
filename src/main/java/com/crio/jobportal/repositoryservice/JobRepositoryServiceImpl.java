package com.crio.jobportal.repositoryservice;

import com.crio.jobportal.data.JobEntity;
import com.crio.jobportal.dto.GetJobDto;
import com.crio.jobportal.dto.Job;
import com.crio.jobportal.exchange.GetJobsRequest;
import com.crio.jobportal.repository.JobRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Provider;

import lombok.extern.log4j.Log4j2;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class JobRepositoryServiceImpl implements JobRepositoryService {
    
  @Autowired
  private JobRepository jobRepository;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private Provider<ModelMapper> modelMapperProvider;

  /** 
   * Saves jobs' data into MongoDB. 
  */
  public void saveJobs(List<Job> jobs) {

    for (Job job : jobs) {
      JobEntity jobEntity = JobEntity.builder()
          .companyName(job.getCompanyName())
          .jobTitle(job.getJobTitle())
          .description(job.getDescription())
          .skillKeywords(job.getSkillKeyWords())
          .location(job.getLocation())
          .listDate(LocalDate.now().toString())
          .build();

      jobRepository.save(jobEntity);
    }
  }

  /**
   * Get jobs matching user parameters.
   */
  public List<GetJobDto> getMatchingJobs(GetJobsRequest getJobsRequest) {
    
    List<JobEntity> jobEntities = jobRepository.findAll();
    
    List<GetJobDto> matchingJobs = new ArrayList<>();
    ModelMapper modelMapper = modelMapperProvider.get();

    for (JobEntity jobEntity: jobEntities) {
      if (validateDate(jobEntity)) {
        if (locationMatch(jobEntity, getJobsRequest.getLocationKeyWords())) {
          int skillKeywordsMatched = skillMatch(jobEntity, getJobsRequest.getSkillKeyWords());
          if (skillKeywordsMatched > 0) {
            GetJobDto currentJob = modelMapper.map(jobEntity, GetJobDto.class);
            currentJob.setKeyWordsMatched(skillKeywordsMatched);
            matchingJobs.add(currentJob);
          }
        } else if (getJobsRequest.getLocationKeyWords().size() == 0) {
          int skillKeywordsMatched = skillMatch(jobEntity, getJobsRequest.getSkillKeyWords());
          if (skillKeywordsMatched > 0) {
            GetJobDto currentJob = modelMapper.map(jobEntity, GetJobDto.class);
            currentJob.setKeyWordsMatched(skillKeywordsMatched);
            matchingJobs.add(currentJob);
          }
        }
      }
    }
    return matchingJobs;
  }

  /**
   * Gets the number of near matches for the skills input by the user for a job's skills.
   * @param jobEntity represents current job from DB
   * @param skillStrings skills sent in user parameters
   * @return noOfSkillMatches
   */
  public int skillMatch(JobEntity jobEntity, List<String> skillStrings) {
    List<String> jobSkills = jobEntity.getSkillKeywords();

    int foundSkills = 0;
    for (String keyword: skillStrings) {
      for (String jobSkill: jobSkills) {
        int dist = editDistance(jobSkill.toLowerCase(), keyword.toLowerCase());
        double similarity = (jobSkill.length() - dist) / (double) jobSkill.length();
        if (jobSkill.toLowerCase().contains(keyword.toLowerCase()) || similarity >= 0.75) {
          foundSkills++;
        }
      }
    }
    return foundSkills;
  }

  /**
   * Finds jobs with near matching locations using user's input location.
   * @param jobEntity represents current job from DB
   * @param locationStrings location sent in by user parameters
   * @return ifLocationMatched
   */
  public boolean locationMatch(JobEntity jobEntity, List<String> locationStrings) {

    List<String> jobLocationList = Arrays.asList(jobEntity.getLocation().toLowerCase()
        .split("\\s*(,|\\s)\\s*"));

    for (String jobLocation: jobLocationList) {
      for (String keyword: locationStrings) {
        int dist = editDistance(jobLocation.toLowerCase(), keyword.toLowerCase());
        double similarity = (jobLocation.length() - dist) / (double) jobLocation.length();
        if (jobLocation.contains(keyword.toLowerCase()) || similarity >= 0.60) {
          return true;
        }
      }
    }   

    return false;
  }

  /**
   * Checks if date of the job hasn't expired.
   * @param jobEntity represents current job from DB
   * @return ifJobExpired
   */
  public boolean validateDate(JobEntity jobEntity) {
    LocalDate jobPosted = LocalDate.parse(jobEntity.getListDate());
    Period period = Period.between(jobPosted, LocalDate.now());

    if (period.getDays() < (-(Integer.parseInt(jobEntity.getExpiryTime())))) {
      return false;
    }
    return true;
  }

  /**
   * finds edit distance between two strings.
   */
  public int editDistance(String s1, String s2) {
    int[] dp = new int[s2.length() + 1];
    
    for (int i = 0; i <= s1.length(); i++) {
      int lastValue = i;
      for (int j = 0; j <= s2.length(); j++) {
        if (i == 0) {
          dp[j] = j;
        } else {
          if (j > 0) {
            int newValue = dp[j - 1];
            if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
              newValue = Math.min(Math.min(newValue, lastValue), dp[j]) + 1;
            }
            dp[j - 1] = lastValue;
            lastValue = newValue;
          }
        }
      }
      if (i > 0) {
        dp[s2.length()] = lastValue;
      }
    }
    return dp[s2.length()];
  }
}
