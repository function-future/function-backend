package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ReportRepository extends MongoRepository<Report, String> {

    Optional<Report> findByIdAndDeletedFalse(String id);

    Page<Report> findAllByUsedAtEqualsAndDeletedFalse(LocalDate usedAt, Pageable pageable);

}