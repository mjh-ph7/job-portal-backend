package com.crio.jobportal.exchange;

import com.crio.jobportal.dto.GetJobDto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetJobsResponse {
  List<GetJobDto> jobs;
}
