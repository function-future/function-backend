package com.future.function.model.entity.feature.communication.questionnaire;

import com.future.function.common.enumeration.communication.ParticipantType;
import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.QUESTIONNAIRE_PARTICIPANT)
public class QuestionnaireParticipant extends BaseEntity {

  @Id
  private String id;

//  @Field(FieldName.QuestionnaireParticipant.QUESTIONNAIRE)
  @DBRef(lazy = true)
  private Questionnaire questionnaire;

//  @Field(FieldName.QuestionnaireParticipant.MEMBER)
  @DBRef(lazy = true)
  private User member;

//  @Field(FieldName.QuestionnaireParticipant.TYPE)
  private ParticipantType participantType;

}
