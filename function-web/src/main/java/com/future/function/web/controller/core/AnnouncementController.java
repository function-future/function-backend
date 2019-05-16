package com.future.function.web.controller.core;

import com.future.function.service.api.feature.core.AnnouncementService;
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
  
  @Autowired
  public AnnouncementController(
    AnnouncementService announcementService,
    AnnouncementRequestMapper announcementRequestMapper
  ) {
    
    this.announcementService = announcementService;
    this.announcementRequestMapper = announcementRequestMapper;
  }
  
  /**
   * Retrieves announcements based on given parameters.
   *
   * @param page Current page of data.
   * @param size Size of data to be displayed per page.
   *
   * @return {@code PagingResponse<AnnouncementWebResponse} - The retrieved
   * announcements data, wrapped in
   * {@link com.future.function.web.model.response.base.PagingResponse} and
   * {@link com.future.function.web.model.response.feature.core.AnnouncementWebResponse}
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public PagingResponse<AnnouncementWebResponse> getAnnouncements(
    @RequestParam(defaultValue = "1")
      int page,
    @RequestParam(defaultValue = "4")
      int size
  ) {
    
    return AnnouncementResponseMapper.toAnnouncementsPagingResponse(
      announcementService.getAnnouncements(PageHelper.toPageable(page, size)));
  }
  
  /**
   * Retrieves a announcement based on given parameter.
   *
   * @param announcementId Id of announcement to be retrieved.
   *
   * @return {@code DataResponse<AnnouncementWebResponse} - The retrieved
   * announcement data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.AnnouncementWebResponse}
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{announcementId}")
  public DataResponse<AnnouncementWebResponse> getAnnouncement(
    @PathVariable
      String announcementId
  ) {
    
    return AnnouncementResponseMapper.toAnnouncementDataResponse(
      announcementService.getAnnouncement(announcementId));
  }
  
  /**
   * Creates new announcement in database.
   *
   * @param request Data of new announcement in JSON format.
   *
   * @return {@code DataResponse<AnnouncementWebResponse} - The created
   * announcement request, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.AnnouncementWebResponse}
   */
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public DataResponse<AnnouncementWebResponse> createAnnouncement(
    @RequestBody
      AnnouncementWebRequest request
  ) {
    
    return AnnouncementResponseMapper.toAnnouncementDataResponse(
      HttpStatus.CREATED, announcementService.createAnnouncement(
        announcementRequestMapper.toAnnouncement(request)));
  }
  
  /**
   * Updates existing announcement in database.
   *
   * @param announcementId Id of to-be-updated announcement.
   *
   * @return {@code DataResponse<AnnouncementWebResponse} - The updated
   * announcement data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.AnnouncementWebResponse}
   */
  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = "/{announcementId}")
  public DataResponse<AnnouncementWebResponse> updateAnnouncement(
    @PathVariable
      String announcementId,
    @RequestBody
      AnnouncementWebRequest request
  ) {
    
    return AnnouncementResponseMapper.toAnnouncementDataResponse(
      HttpStatus.OK, announcementService.updateAnnouncement(
        announcementRequestMapper.toAnnouncement(
          announcementId, request)));
  }
  
  /**
   * Deletes announcement from database.
   *
   * @param announcementId Id of to be deleted announcement.
   *
   * @return {@code BaseResponse} - Indicating successful deletion.
   */
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping(value = "/{announcementId}")
  public BaseResponse deleteAnnouncement(
    @PathVariable
      String announcementId
  ) {
    
    announcementService.deleteAnnouncement(announcementId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }
  
}
