package com.future.function.web.mapper.helper;

import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.ErrorResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.base.paging.Paging;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseHelper {

  public static BaseResponse toBaseResponse(HttpStatus httpStatus) {

    return new BaseResponse(
      httpStatus.value(), toProperStatusFormat(httpStatus.getReasonPhrase()));
  }

  public static String toProperStatusFormat(String status) {

    return status.toUpperCase()
      .replace(" ", "_");
  }

  public static ErrorResponse toErrorResponse(
    HttpStatus httpStatus, Set<ConstraintViolation<?>> violations
  ) {

    return ErrorResponse.builder()
      .code(httpStatus.value())
      .status(ResponseHelper.toProperStatusFormat(httpStatus.getReasonPhrase()))
      .errors(ErrorHelper.toErrors(violations))
      .build();
  }

  public static <T> DataResponse<T> toDataResponse(
    HttpStatus httpStatus, T data
  ) {

    return DataResponse.<T>builder().code(httpStatus.value())
      .status(ResponseHelper.toProperStatusFormat(httpStatus.getReasonPhrase()))
      .data(data)
      .build();
  }

  public static <T> PagingResponse<T> toPagingResponse(
    HttpStatus httpStatus, List<T> data, Paging paging
  ) {

    return PagingResponse.<T>builder().code(httpStatus.value())
      .status(ResponseHelper.toProperStatusFormat(httpStatus.getReasonPhrase()))
      .data(data)
      .paging(paging)
      .build();
  }

}
