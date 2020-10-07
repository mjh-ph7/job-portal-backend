package com.crio.jobportal.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Job {
  
  private String companyName;
  private String jobTitle;
  private String description;
  private List<String> skillKeyWords;
  private String location;
  private String expiryTime; 
}
