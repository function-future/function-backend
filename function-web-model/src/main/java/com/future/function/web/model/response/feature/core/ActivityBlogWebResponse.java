package com.future.function.web.model.response.feature.core;

import com.future.function.web.model.response.feature.core.embedded.EmbeddedFileWebResponse;
import com.future.function.web.model.response.feature.embedded.AuthorWebResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityBlogWebResponse {

  private String id;

  private String title;

  private String description;

  private List<EmbeddedFileWebResponse> files;

  private long updatedAt;

  private AuthorWebResponse author;

}
