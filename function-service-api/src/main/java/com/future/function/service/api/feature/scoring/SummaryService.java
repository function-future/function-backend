package com.future.function.service.api.feature.scoring;

import com.future.function.model.vo.scoring.StudentSummaryVO;
import org.springframework.data.domain.Pageable;

public interface SummaryService {

  StudentSummaryVO findAllPointSummaryByStudentId(
    String studentId, Pageable pageable, String userId, String type
  );

}
