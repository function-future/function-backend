package com.future.function.web.controller.core;

import com.future.function.service.api.feature.core.AnnouncementService;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.AnnouncementRequestMapper;
import com.future.function.web.mapper.response.core.AnnouncementResponseMapper;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.AnnouncementWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/core/announcements")
public class AnnouncementController {
  
  private final AnnouncementService announcementService;
  
  private final AnnouncementRequestMapper announcementRequestMapper;
  
  @Autowired
  public AnnouncementController(
    AnnouncementService announcementService,
    AnnouncementRequestMapper announcementRequestMapper
  ) {
    
    this.announcementService = announcementService;
    this.announcementRequestMapper = announcementRequestMapper;
  }
  
  @GetMapping
  public PagingResponse<AnnouncementWebResponse> getAnnouncements(
    @RequestParam(defaultValue = "1")
      int page,
    @RequestParam(defaultValue = "4")
      int size
  ) {
    
    return AnnouncementResponseMapper.toAnnouncementsPagingResponse(
      announcementService.getAnnouncements(PageHelper.toPage(page, size)));
  }
  
  @GetMapping(value = "/{announcementId}")
  public DataResponse<AnnouncementWebResponse> getAnnouncement(
    @PathVariable
      String announcementId
  ) {
    
    return AnnouncementResponseMapper.toAnnouncementDataResponse(
      announcementService.getAnnouncement(announcementId));
  }
  
  @PostMapping
  public DataResponse<AnnouncementWebResponse> createAnnouncement(
    @RequestParam
      String data,
    @RequestParam(required = false)
      MultipartFile file
  ) {
    
    return AnnouncementResponseMapper.toAnnouncementDataResponse(
      HttpStatus.CREATED, announcementService.createAnnouncement(
        announcementRequestMapper.toAnnouncement(data), file));
  }
  
  @PutMapping(value = "/{announcementId}")
  public DataResponse<AnnouncementWebResponse> updateAnnouncement(
    @PathVariable
      String announcementId,
    @RequestParam
      String data,
    @RequestParam(required = false)
      MultipartFile file
  ) {
    
    return AnnouncementResponseMapper.toAnnouncementDataResponse(
      HttpStatus.OK, announcementService.updateAnnouncement(
        announcementRequestMapper.toAnnouncement(announcementId, data), file));
  }
  
  @DeleteMapping(value = "/{announcementId}")
  public BaseResponse deleteAnnouncement(
    @PathVariable
      String announcementId
  ) {
    
    announcementService.deleteAnnouncement(announcementId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }
  
}
