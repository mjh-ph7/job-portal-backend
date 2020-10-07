package com.crio.jobportal.exchange;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetJobsRequest {

  private List<String> skillKeyWords;
  private List<String> locationKeyWords;
}
