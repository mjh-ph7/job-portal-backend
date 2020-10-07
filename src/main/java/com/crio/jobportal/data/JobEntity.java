package com.crio.jobportal.data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "jobs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobEntity {

  @Id
  private String id;

  @NonNull
  private String companyName;
  
  @NonNull
  private String jobTitle;
  
  @NonNull
  private String description;
  
  @NonNull
  private List<String> skillKeywords;

  @NonNull
  private String location;
  
  @NonNull
  private String listDate;

  @Default
  private int applicantSize = 0;

  @Default
  private String expiryTime = "60";

}
