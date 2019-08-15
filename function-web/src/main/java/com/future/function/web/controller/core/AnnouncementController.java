package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.service.api.feature.core.AnnouncementService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.AnnouncementRequestMapper;
import com.future.function.web.mapper.response.core.AnnouncementResponseMapper;
import com.future.function.web.model.request.core.AnnouncementWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.AnnouncementWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/core/announcements")
public class AnnouncementController {

  private final AnnouncementService announcementService;

  private final AnnouncementRequestMapper announcementRequestMapper;

  private final FileProperties fileProperties;

  @Autowired
  public AnnouncementController(
    AnnouncementService announcementService,
    AnnouncementRequestMapper announcementRequestMapper,
    FileProperties fileProperties
  ) {

    this.announcementService = announcementService;
    this.announcementRequestMapper = announcementRequestMapper;
    this.fileProperties = fileProperties;
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public PagingResponse<AnnouncementWebResponse> getAnnouncements(
    @RequestParam(defaultValue = "1")
      int page,
    @RequestParam(defaultValue = "4")
      int size
  ) {

    return AnnouncementResponseMapper.toAnnouncementsPagingResponse(
      announcementService.getAnnouncements(PageHelper.toPageable(page, size)),
      fileProperties.getUrlPrefix()
    );
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{announcementId}")
  public DataResponse<AnnouncementWebResponse> getAnnouncement(
    @PathVariable
      String announcementId
  ) {

    return AnnouncementResponseMapper.toAnnouncementDataResponse(
      announcementService.getAnnouncement(announcementId),
      fileProperties.getUrlPrefix()
    );
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public DataResponse<AnnouncementWebResponse> createAnnouncement(
    @WithAnyRole(roles = Role.ADMIN)
      Session session,
    @RequestBody
      AnnouncementWebRequest request
  ) {

    return AnnouncementResponseMapper.toAnnouncementDataResponse(
      HttpStatus.CREATED, announcementService.createAnnouncement(
        announcementRequestMapper.toAnnouncement(request)),
      fileProperties.getUrlPrefix()
    );
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = "/{announcementId}")
  public DataResponse<AnnouncementWebResponse> updateAnnouncement(
    @WithAnyRole(roles = Role.ADMIN)
      Session session,
    @PathVariable
      String announcementId,
    @RequestBody
      AnnouncementWebRequest request
  ) {

    return AnnouncementResponseMapper.toAnnouncementDataResponse(
      HttpStatus.OK, announcementService.updateAnnouncement(
        announcementRequestMapper.toAnnouncement(
          announcementId, request)), fileProperties.getUrlPrefix());
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping(value = "/{announcementId}")
  public BaseResponse deleteAnnouncement(
    @WithAnyRole(roles = Role.ADMIN)
      Session session,
    @PathVariable
      String announcementId
  ) {

    announcementService.deleteAnnouncement(announcementId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

}
