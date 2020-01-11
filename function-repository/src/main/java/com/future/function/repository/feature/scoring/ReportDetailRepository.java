package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.ReportDetail;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReportDetailRepository
  extends MongoRepository<ReportDetail, String> {

  Optional<ReportDetail> findByUserIdAndDeletedFalse(String studentId);

}
