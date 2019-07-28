package com.future.function.web.model.response.feature.communication.logging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author : Ricky Kennedy
 * Created At : 23:07 27/07/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicResponse {

  private String id;

  private String title;

}
