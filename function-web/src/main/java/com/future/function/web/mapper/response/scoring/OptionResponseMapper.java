package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.scoring.Option;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.OptionWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OptionResponseMapper {

  public static DataResponse<OptionWebResponse> toOptionWebResponse(Option option) {
    return ResponseHelper.toDataResponse(HttpStatus.OK, buildOptionWebResponse(option));
  }

  public static DataResponse<OptionWebResponse> toOptionWebResponse(HttpStatus httpStatus, Option option) {
    return ResponseHelper.toDataResponse(httpStatus, buildOptionWebResponse(option));
  }

  private static OptionWebResponse buildOptionWebResponse(Option option) {
    return OptionWebResponse.builder()
        .optionId(option.getId())
        .label(option.getLabel())
            .correct(option.isCorrect() ? option.isCorrect() : null)
        .build();
  }

  public static List<OptionWebResponse> toListOfOptionWebResponse(List<Option> optionList) {
    return optionList
        .stream()
        .map(OptionResponseMapper::buildOptionWebResponse)
        .collect(Collectors.toList());
  }

}
