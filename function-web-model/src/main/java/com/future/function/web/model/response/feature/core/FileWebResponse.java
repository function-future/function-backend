package com.future.function.web.model.response.feature.core;

import com.future.function.web.model.response.feature.core.embedded.PathWebResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileWebResponse<T> {

  private List<PathWebResponse> paths;

  private T content;

}
