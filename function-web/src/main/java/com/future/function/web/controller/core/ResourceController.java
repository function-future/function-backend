package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.request.core.ResourceRequestMapper;
import com.future.function.web.mapper.response.core.ResourceResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.FileContentWebResponse;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/core/resources")
public class ResourceController {

  private final ResourceService resourceService;

  private final ResourceRequestMapper resourceRequestMapper;

  private final FileProperties fileProperties;

  public ResourceController(
    ResourceService resourceService,
    ResourceRequestMapper resourceRequestMapper, FileProperties fileProperties
  ) {

    this.resourceService = resourceService;
    this.resourceRequestMapper = resourceRequestMapper;
    this.fileProperties = fileProperties;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<FileContentWebResponse> createFile(
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT })
      Session session,
    @RequestParam(required = false)
      MultipartFile file,
    @RequestParam
      String origin
  ) {

    Pair<String, byte[]> pair = resourceRequestMapper.toStringAndByteArrayPair(
      file);

    return ResourceResponseMapper.toResourceDataResponse(
      resourceService.storeAndSaveFile(null, pair.getFirst(), pair.getSecond(),
                                       FileOrigin.toFileOrigin(origin)
      ), fileProperties.getUrlPrefix());
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{origin}/{fileName:.+}")
  public ResponseEntity getFileAsByteArray(
    HttpServletRequest servletRequest,
    @WithAnyRole(noUnauthorized = true)
      Session session,
    @PathVariable
      String origin,
    @PathVariable
      String fileName,
    @RequestParam(required = false)
      Long version
  ) {

    Pair<String, byte[]> fileData = resourceService.getFileAsByteArray(
      session.getRole(), fileName, FileOrigin.toFileOrigin(origin), version);

    return ResponseEntity.ok()
      .contentType(resourceRequestMapper.getMediaType(fileName, servletRequest))
      .header(
        HttpHeaders.CONTENT_DISPOSITION,
        String.format("attachment; filename=\"%s\"", fileData.getFirst())
      )
      .body(fileData.getSecond());
  }

}
