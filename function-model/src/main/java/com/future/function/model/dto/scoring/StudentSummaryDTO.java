package com.future.function.model.dto.scoring;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentSummaryDTO {

  private String studentName;
  private String batchCode;
  private String university;
  private List<SummaryDTO> scores;

}
