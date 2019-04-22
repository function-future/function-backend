package com.future.function.web.mapper.helper;

import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.ErrorResponse;
import java.util.Set;
import javax.validation.ConstraintViolation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

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

}
