package com.future.function.web.model.request.communication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author: PriagungSatyagama
 * Created At: 15:40 01/06/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomRequest {

  private String name;

  private List<String> members;

}
