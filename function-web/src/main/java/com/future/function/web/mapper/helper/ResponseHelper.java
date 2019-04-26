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

/**
 * Helper class for creating response objects.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseHelper {
  
  /**
   * Constructs
   * {@link com.future.function.web.model.response.base.BaseResponse} given
   * {@link org.springframework.http.HttpStatus}.
   *
   * @param httpStatus Http status to be shown in the response.
   *
   * @return {@code BaseResponse} - The response object generated based on
   * the given status.
   */
  public static BaseResponse toBaseResponse(HttpStatus httpStatus) {
    
    return new BaseResponse(
      httpStatus.value(), toProperStatusFormat(httpStatus.getReasonPhrase()));
  }
  
  /**
   * Converts a status to upper case format, with space replaced with
   * underscore.
   *
   * @param status Status (in form of String) to be converted.
   *
   * @return {@code String} - Converted status.
   */
  public static String toProperStatusFormat(String status) {
    
    return status.toUpperCase()
      .replace(" ", "_");
  }
  
  /**
   * Constructs
   * {@link com.future.function.web.model.response.base.ErrorResponse} given
   * {@link org.springframework.http.HttpStatus} and set of
   * {@link javax.validation.ConstraintViolation}.
   *
   * @param httpStatus Http status to be shown in the response.
   * @param violations Violations that is retrieved from the thrown exception
   *                   from service.
   *
   * @return {@code ErrorResponse} - The response object generated based on
   * the given status.
   */
  public static ErrorResponse toErrorResponse(
    HttpStatus httpStatus, Set<ConstraintViolation<?>> violations
  ) {
    
    return ErrorResponse.builder()
      .code(httpStatus.value())
      .status(ResponseHelper.toProperStatusFormat(httpStatus.getReasonPhrase()))
      .errors(ErrorHelper.toErrors(violations))
      .build();
  }
  
  /**
   * Constructs
   * {@link com.future.function.web.model.response.base.DataResponse} given
   * {@link org.springframework.http.HttpStatus} and data.
   *
   * @param httpStatus Http status to be shown in the response.
   * @param data       Data to be wrapped in {@code DataResponse}, must be of
   *                   type {@code <T>}.
   * @param <T>        Type of class of the specified data.
   *
   * @return {@code DataResponse<T>} - The response object wrapping the given
   * data.
   */
  public static <T> DataResponse<T> toDataResponse(
    HttpStatus httpStatus, T data
  ) {
    
    return DataResponse.<T>builder().code(httpStatus.value())
      .status(ResponseHelper.toProperStatusFormat(httpStatus.getReasonPhrase()))
      .data(data)
      .build();
  }
  
  /**
   * Constructs
   * {@link com.future.function.web.model.response.base.PagingResponse} given
   * {@link org.springframework.http.HttpStatus}, data, and paging object.
   *
   * @param httpStatus Http status to be shown in the response.
   * @param data       Data to be wrapped in {@code PagingResponse}, must be of
   *                   type {@code java.util.List<T>}.
   * @param <T>        Type of class of the specified data.
   * @param paging     Paging object to be wrapped in {@code PagingResponse}.
   *
   * @return {@code PagingResponse<T>} - The response object wrapping the given
   * data.
   */
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
