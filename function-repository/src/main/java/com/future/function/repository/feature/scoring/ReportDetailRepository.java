package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.ReportDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportDetailRepository extends MongoRepository<ReportDetail, String> {

    List<ReportDetail> findAllByReportIdAndDeletedFalse(String reportId);

}
