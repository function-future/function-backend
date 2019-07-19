package com.future.function.web.model.response.feature.scoring;

import com.future.function.web.model.response.feature.embedded.AuthorWebResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentWebResponse {

  private String id;
  private AuthorWebResponse author;
  private String comment;
  private Long createdAt;

}
