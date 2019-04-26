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

/**
 * Service class used to manipulate Assignment Entity
 * Used AssignmentRepository and FileService to support manipulation of Assignment Entity
 */
@Service
public class AssignmentServiceImpl implements AssignmentService {

  private AssignmentRepository assignmentRepository;

  private FileService fileService;

  @Autowired
  public AssignmentServiceImpl(AssignmentRepository assignmentRepository, FileService fileService) {
    this.assignmentRepository = assignmentRepository;
    this.fileService = fileService;
  }

  /**
   * Used to find All Assignment Object from Repository With Paging, Filtering, And Search Keyword
   *
   * @param pageable (Pageable Object)
   * @param filter   (String)
   * @param search   (String)
   * @return Page<Assignment>
   */
  @Override
  public Page<Assignment> findAllByPageableAndFilterAndSearch(Pageable pageable, String filter, String search) {
    filter = Optional.ofNullable(filter)
            .orElse("");
    search = Optional.ofNullable(search)
            .orElse("");
    //TODO using filter and search to find the asssignment page
    return assignmentRepository.findAll(pageable);
  }

  /**
   * Used to Find Assignment Object From Repository With Passed Id and Not Deleted
   *
   * @param id (String)
   * @return Assignment Object
   * @throws NotFoundException if Assignment Object is not found or the id is null
   */
  @Override
  public Assignment findById(String id) {
    return Optional.ofNullable(id)
            .map(assignmentRepository::findByIdAndDeletedFalse)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .orElseThrow(() -> new NotFoundException("Assignment Not Found"));
  }

  /**
   * Used to create new Assignment Data in Repository With / Without file
   *
   * @param request (Assignment Object)
   * @param file    (MultipartFile Object)
   * @return Saved Assignment Object
   */
  @Override
  public Assignment createAssignment(Assignment request, MultipartFile file) {
    //TODO save batch to shared assignment entity
    Assignment assignment = storeAssignmentFile(request, file);
    return assignmentRepository.save(assignment);
  }

  /**
   * Used to store Multipart File by using FileService which sent with Assignment object
   *
   * @param request (Assignment Object)
   * @param file    (MultipartFile Object)
   * @return Assignment with / without the saved file
   */
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

  /**
   * Used to update existed Assignment in Repository with Assignment Object request and / no Multipart File
   *
   * @param request (Assignment Object)
   * @param file    (MultipartFile Object)
   * @return Saved Assignment Object
   */
  @Override
  public Assignment updateAssignment(Assignment request, MultipartFile file) {
    //TODO save batch to shared assignment entity
    Assignment oldAssignment = this.findById(request.getId());
    Assignment newAssignment = new Assignment();
    BeanUtils.copyProperties(oldAssignment, newAssignment);
    newAssignment = storeAssignmentFile(newAssignment, file);
    return assignmentRepository.save(newAssignment);
  }

  /**
   * Used to delete existing Assignment From Repository With passed id
   *
   * @param id (String)
   */
  @Override
  public void deleteById(String id) {
    //TODO delete file associated with the assignment
    Optional.ofNullable(id)
            .map(this::findById)
            .ifPresent(val -> {
              val.setDeleted(true);
              assignmentRepository.save(val);
            });
  }
}
