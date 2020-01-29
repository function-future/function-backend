package com.future.function.web.model.response.feature.communication.questionnaire;

import com.future.function.web.model.response.feature.core.BatchWebResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: RickyKennedy
 * Created At:2:29 PM 1/24/2020
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionnaireDoneResponse {

  private AppraiseeResponse appraiseeResponse;

  private Float score;

}

