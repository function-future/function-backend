package com.future.function.service.api.feature.scoring;

import com.future.function.model.vo.scoring.StudentSummaryVO;

public interface SummaryService {

  StudentSummaryVO findAllPointSummaryByStudentId(
    String studentId, String userId
  );

}
