package com.future.function.web.model.response.feature.communication.chatting;

import com.future.function.web.model.response.feature.core.BatchWebResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: PriagungSatyagama
 * Created At: 15:29 01/06/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomParticipantDetailResponse {

  private String id;

  private String name;

  private String avatar;

  private String university;

  private BatchWebResponse batch;

  private String type;

}
