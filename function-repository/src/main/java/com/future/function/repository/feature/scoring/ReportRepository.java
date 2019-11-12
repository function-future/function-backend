package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.scoring.Report;
import com.future.function.model.entity.feature.scoring.ReportDetail;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReportRepository extends MongoRepository<Report, String> {

  Optional<Report> findByIdAndDeletedFalse(String id);

  Page<Report> findAllByBatchAndDeletedFalse(Batch batch, Pageable pageable);

  Optional<Report> findByStudentsAndDeletedFalse(List<ReportDetail> students);

}
