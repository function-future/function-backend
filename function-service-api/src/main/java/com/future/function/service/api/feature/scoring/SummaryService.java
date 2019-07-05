package com.future.function.service.api.feature.scoring;

import com.future.function.model.dto.scoring.StudentSummaryDTO;

public interface SummaryService {

  StudentSummaryDTO findAllPointSummaryByStudentId(String studentId, String userId);

}
