package com.future.function.web.mapper.request.core;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
public class ResourceRequestMapper extends MultipartFileRequestMapper {

  public MediaType getMediaType(
    String fileName, HttpServletRequest servletRequest
  ) {

    return MediaType.parseMediaType(
      this.getMediaTypeValue(fileName, servletRequest));
  }

  private String getMediaTypeValue(
    String fileName, HttpServletRequest servletRequest
  ) {

    return Optional.of(fileName)
      .map(name -> servletRequest.getServletContext()
        .getMimeType(name))
      .orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE);
  }

}
