package com.future.function.web.mapper.response.scoring;

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

import java.util.stream.Collectors;

/**
 * Static class to map Assignment into a proper DataResponse / PagingResponse of AssignmentWebResponse
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AssignmentResponseMapper {


  /**
   * used to convert assignment into a proper DataResponse of AssignmentWebResponse
   *
   * @param assignment
   * @return DataResponse<AssignmentWebResponse>
   */
  public static DataResponse<AssignmentWebResponse> toAssignmentDataResponse(Assignment assignment) {
    return ResponseHelper.toDataResponse(HttpStatus.OK, buildAssignmentWebResponse(assignment));
  }

  /**
   * used to convert assignment into a DataResponse of Assignment Web Response with specific HttpStatus
   *
   * @param httpStatus
   * @param assignment
   * @return DataResponse<AssignmentWebResponse> with specific HttpStatus
   */
  public static DataResponse<AssignmentWebResponse> toAssignmentDataResponse(HttpStatus httpStatus, Assignment assignment) {
    return ResponseHelper.toDataResponse(httpStatus, buildAssignmentWebResponse(assignment));
  }

  /**
   * used to convert assignment into a proper AssignmentWebResponse
   *
   * @param assignment
   * @return AsssignmentWebResponse
   */
  private static AssignmentWebResponse buildAssignmentWebResponse(Assignment assignment) {
    AssignmentWebResponse response = new AssignmentWebResponse();
    BeanUtils.copyProperties(assignment, response);
    return response;
  }

  /**
   * used to convert page of assignment into a proper PagingResponse of AssignmentWebResponse
   *
   * @param data (Page<Assignment>)
   * @return PagingResponse<AssignmentWebResponse>
   */
  public static PagingResponse<AssignmentWebResponse> toAssignmentsPagingResponse(Page<Assignment> data) {
    return ResponseHelper.toPagingResponse(
            HttpStatus.OK,
            data
              .getContent()
              .stream()
              .map(AssignmentResponseMapper::buildAssignmentWebResponse)
              .collect(Collectors.toList()),
            PageHelper.toPaging(data));
  }

}