package com.future.function.service.api.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.scoring.Assignment;
import java.util.Observer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssignmentService {

  Page<Assignment> findAllByBatchCodeAndPageable(
    String batchCode, Pageable pageable, Role role, String sessionBatchId, boolean deadline
  );

  Assignment findById(String id, Role role, String sessionBatchId);

  Assignment copyAssignment(String assignmentId, String targetBatchCode);

  Assignment createAssignment(Assignment request);

  Assignment updateAssignment(Assignment request);

  void deleteById(String id);

  void addObserver(Observer observer);

}
