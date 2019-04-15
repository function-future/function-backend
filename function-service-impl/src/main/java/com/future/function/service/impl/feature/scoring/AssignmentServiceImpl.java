package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.repository.feature.scoring.AssignmentRepository;
import com.future.function.service.api.feature.core.FileService;
import com.future.function.service.api.feature.scoring.AssignmentService;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AssignmentServiceImpl implements AssignmentService {

  private AssignmentRepository assignmentRepository;

  private FileService fileService;

  @Autowired
  public AssignmentServiceImpl(AssignmentRepository assignmentRepository, FileService fileService) {
    this.assignmentRepository = assignmentRepository;
    this.fileService = fileService;
  }


  @Override
  public Page<Assignment> findAllBuPageable(Pageable pageable) {
    return assignmentRepository.findAll(pageable);
  }

  @Override
  public Assignment findById(String id) {
    return Optional.ofNullable(id)
            .map(assignmentRepository::findByIdAndDeletedFalse)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .orElseThrow(() -> new NotFoundException("Assignment Not Found"));
  }

  @Override
  public Assignment createAssignment(Assignment request, MultipartFile file) {
    Assignment assignment = storeAssignmentFile(request, file);
    return assignmentRepository.save(assignment);
  }

  private Assignment storeAssignmentFile(Assignment request, MultipartFile file) {
    return Optional.ofNullable(file)
            .map(val -> fileService.storeFile(val, FileOrigin.ASSIGNMENT))
            .map(val -> fileService.getFile(val.getId()))
            .map(val -> {
              request.setFile(val);
              return request;
            })
            .orElse(request);
  }

  @Override
  public Assignment updateAssignment(Assignment request, MultipartFile file) {
    Assignment oldAssignment = this.findById(request.getId());
    Assignment newAssignment = new Assignment();
    BeanUtils.copyProperties(oldAssignment, newAssignment);
    newAssignment = storeAssignmentFile(newAssignment, file);
    return assignmentRepository.save(newAssignment);
  }

  @Override
  public void deleteById(String id) {
    Optional.ofNullable(id)
            .map(this::findById)
            .ifPresent(val -> {
              val.setDeleted(true);
              assignmentRepository.save(val);
            });
  }
}
