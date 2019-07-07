package com.future.function.model.dto.scoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentSummaryDTO {

  private String studentName;
  private String batchCode;
  private String university;
    private String avatar;
  private List<SummaryDTO> scores;

}
