package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.AssignmentWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssignmentResponseMapper {

  public static DataResponse<AssignmentWebResponse> toAssignmentDataResponse(
    Assignment assignment, String urlPrefix
  ) {

    return ResponseHelper.toDataResponse(
      HttpStatus.OK, buildAssignmentWebResponse(assignment, urlPrefix));
  }

  private static AssignmentWebResponse buildAssignmentWebResponse(
    Assignment assignment, String urlPrefix
  ) {

    AssignmentWebResponse response = new AssignmentWebResponse();
    BeanUtils.copyProperties(assignment, response);
    response.setBatchCode(assignment.getBatch()
                            .getCode());
    response.setUploadedDate(assignment.getCreatedAt());
    response = setNullableFile(response, assignment, urlPrefix);
    return response;
  }

  private static AssignmentWebResponse setNullableFile(
    AssignmentWebResponse response, Assignment assignment, String urlPrefix
  ) {

    return Optional.ofNullable(assignment)
      .map(Assignment::getFile)
      .map(file -> validateAndSetFile(response, urlPrefix, file))
      .orElse(response);
  }

  private static AssignmentWebResponse validateAndSetFile(AssignmentWebResponse response, String urlPrefix, FileV2 file) {
    if(file.getFileUrl() == null) {
      return null;
    } else {
      response.setFileId(file.getId());
      response.setFile(urlPrefix.concat(file.getFileUrl()));
      return response;
    }
  }

  public static DataResponse<AssignmentWebResponse> toAssignmentDataResponse(
    HttpStatus httpStatus, Assignment assignment, String urlPrefix
  ) {

    return ResponseHelper.toDataResponse(
      httpStatus, buildAssignmentWebResponse(assignment, urlPrefix));
  }

  public static PagingResponse<AssignmentWebResponse> toAssignmentsPagingResponse(
    Page<Assignment> data, String urlPrefix
  ) {

    return ResponseHelper.toPagingResponse(
      HttpStatus.OK, data.getContent()
        .stream()
        .map(assignment -> AssignmentResponseMapper.buildAssignmentWebResponse(
          assignment, urlPrefix))
        .collect(Collectors.toList()), PageHelper.toPaging(data));
  }

}
