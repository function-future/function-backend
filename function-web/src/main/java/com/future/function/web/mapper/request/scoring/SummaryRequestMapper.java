package com.future.function.web.mapper.request.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.scoring.ScoreStudentWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SummaryRequestMapper {

  private RequestValidator validator;

  @Autowired
  public SummaryRequestMapper(RequestValidator validator) {

    this.validator = validator;
  }

  public ReportDetail toReportDetail(ScoreStudentWebRequest request) {

    return Optional.ofNullable(request)
      .map(validator::validate)
      .map(this::buildReportDetail)
      .orElseThrow(
        () -> new BadRequestException("BAD_REQUEST"));
  }

  private ReportDetail buildReportDetail(ScoreStudentWebRequest request) {

    return ReportDetail.builder()
      .user(User.builder()
              .id(request.getStudentId())
              .build())
      .point(request.getScore())
      .build();
  }

}
