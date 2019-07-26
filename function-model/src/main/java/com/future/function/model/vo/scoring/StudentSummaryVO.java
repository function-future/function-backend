package com.future.function.model.vo.scoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentSummaryVO {

  private String studentId;
  private String studentName;
  private String batchCode;
  private String university;
  private String avatar;
  private List<SummaryVO> scores;
  private int point;

}
