package com.future.function.model.vo.scoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import org.springframework.data.domain.Page;

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

  private Page<SummaryVO> scores;

  private Integer totalPoint;

  private int point;

}
