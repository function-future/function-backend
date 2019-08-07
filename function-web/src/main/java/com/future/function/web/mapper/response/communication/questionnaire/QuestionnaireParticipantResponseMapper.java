package com.future.function.web.mapper.response.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireParticipant;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireParticipantDescriptionResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireParticipantResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionnaireParticipantResponseMapper {

  public static PagingResponse<QuestionnaireParticipantDescriptionResponse> toPagingParticipantDescriptionResponse(
          Page<QuestionnaireParticipant> data
  ){
    return ResponseHelper.toPagingResponse(HttpStatus.OK, toParticipantDescriptionResposneList(data), PageHelper.toPaging(data));
  }

  private static List<QuestionnaireParticipantDescriptionResponse> toParticipantDescriptionResposneList(Page<QuestionnaireParticipant> data){
    return data.getContent()
            .stream()
            .map(questionnaireParticipant -> toParticipantDescriptionResposne(questionnaireParticipant))
            .collect(Collectors.toList());
  }

  private static QuestionnaireParticipantDescriptionResponse toParticipantDescriptionResposne(QuestionnaireParticipant questionnaireParticipant) {
    return QuestionnaireParticipantDescriptionResponse.builder()
            .id(questionnaireParticipant.getMember().getId())
            .participantId(questionnaireParticipant.getId())
            .name(questionnaireParticipant.getMember().getName())
            .university(questionnaireParticipant.getMember().getUniversity())
            .role(questionnaireParticipant.getMember().getRole().toString())
            .batch(cekRole(questionnaireParticipant.getMember()))
            .avatar(getThumnailUrl(questionnaireParticipant.getMember()))
            .build();
  }

  public static DataResponse<QuestionnaireParticipantResponse> toDataResponseQuestionnaireParticipantResponse(
          QuestionnaireParticipant questionnaireParticipant,
          HttpStatus httpStatus
  ) {
    return ResponseHelper.toDataResponse(httpStatus, toQuestionnaireParticipantResponse(questionnaireParticipant));
  }

  private static QuestionnaireParticipantResponse toQuestionnaireParticipantResponse(QuestionnaireParticipant questionnaireParticipant){
    return QuestionnaireParticipantResponse.builder()
            .id(questionnaireParticipant.getId())
            .questionnaireId(questionnaireParticipant.getQuestionnaire().getId())
            .memberId(questionnaireParticipant.getMember().getId())
            .participantType(questionnaireParticipant.getParticipantType().toString())
            .build();
  }

  private static String cekRole(User user) {
    if (user.getRole().toString().equals("MENTOR")) {
      return "No Batch";
    }
    return user.getBatch().getCode();
  }

  private static String getThumnailUrl(User user) {
    return Optional.ofNullable(user)
      .map(User::getPictureV2)
      .map(FileV2::getThumbnailUrl)
      .orElse(null);
  }
}
