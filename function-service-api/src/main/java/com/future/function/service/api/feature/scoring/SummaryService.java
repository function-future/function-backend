package com.future.function.service.api.feature.scoring;

import com.future.function.model.dto.scoring.SummaryDTO;

import java.util.List;

public interface SummaryService {

    List<SummaryDTO> findAllPointSummaryByStudentId(String studentId, String userId);

}
