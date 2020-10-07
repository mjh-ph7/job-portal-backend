package com.crio.jobportal.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetJobDto {

  private String companyName;
  private String jobTitle;
  private String description;
  private List<String> skillKeyWords;
  private String location;
  private String listDate;
  private String applicantSize;
  private int keyWordsMatched;
}
