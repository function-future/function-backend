package com.future.function.web.controller.scoring;

import com.future.function.service.api.feature.scoring.SummaryService;
import com.future.function.web.mapper.response.scoring.ScoreSummaryResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.SummaryResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scoring/summary/{studentId}")
public class SummaryController {

  @Autowired
  private SummaryService summaryService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<List<SummaryResponse>> findAllSummaryByStudentId(@PathVariable(value = "studentId") String studentId) {
    return ScoreSummaryResponseMapper.toDataListSummaryResponse(summaryService.findAllPointSummaryByStudentId(studentId));
  }

}
