package com.future.function.web.mapper.response.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireParticipant;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.ParticipantDescriptionResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParticipantResponseMapper {

  public static PagingResponse<ParticipantDescriptionResponse> toPagingParticipantResponse(
          Page<QuestionnaireParticipant> data
  ){
    return ResponseHelper.toPagingResponse(HttpStatus.OK, toParticipantDescriptionResposneList(data), PageHelper.toPaging(data));
  }

  private static List<ParticipantDescriptionResponse> toParticipantDescriptionResposneList(Page<QuestionnaireParticipant> data){
    return data.getContent()
            .stream()
            .map(questionnaireParticipant -> toParticipantDescriptionResposne(questionnaireParticipant))
            .collect(Collectors.toList());
  }

  private static ParticipantDescriptionResponse toParticipantDescriptionResposne(QuestionnaireParticipant questionnaireParticipant) {
    return ParticipantDescriptionResponse.builder()
            .id(questionnaireParticipant.getId())
            .name(questionnaireParticipant.getMember().getName())
            .university(questionnaireParticipant.getMember().getUniversity())
            .role(questionnaireParticipant.getMember().getRole().toString())
            .batch(questionnaireParticipant.getMember().getBatch().getCode())
            .build();
  }
}
