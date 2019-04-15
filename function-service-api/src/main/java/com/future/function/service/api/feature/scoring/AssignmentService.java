package com.future.function.service.api.feature.scoring;

import com.future.function.model.entity.feature.scoring.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AssignmentService {

  Page<Assignment> findAllBuPageable(Pageable pageable);

  Assignment findById(String id);

  Assignment createAssignment(Assignment request, MultipartFile file);

  Assignment updateAssignment(Assignment request, MultipartFile file);

  void deleteById(String id);

}
