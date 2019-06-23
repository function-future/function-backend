package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.Report;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends MongoRepository<Report, String> {

    Optional<Report> findByIdAndDeletedFalse(String id);

}
