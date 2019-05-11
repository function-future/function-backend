package com.future.function.web.controller.core;

import com.future.function.service.api.feature.core.StickyNoteService;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.request.core.StickyNoteRequestMapper;
import com.future.function.web.mapper.response.core.StickyNoteResponseMapper;
import com.future.function.web.model.request.core.StickyNoteWebRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.StickyNoteWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for sticky note APIs.
 */
@RestController
@RequestMapping(value = "/api/core/sticky-notes")
public class StickyNoteController {
  
  private final StickyNoteService stickyNoteService;
  
  private final StickyNoteRequestMapper stickyNoteRequestMapper;
  
  @Autowired
  public StickyNoteController(
    StickyNoteService stickyNoteService,
    StickyNoteRequestMapper stickyNoteRequestMapper
  ) {
    
    this.stickyNoteService = stickyNoteService;
    this.stickyNoteRequestMapper = stickyNoteRequestMapper;
  }
  
  /**
   * Retrieves last inserted sticky note in database.
   *
   * @return {@code DataResponse<StickyNoteWebResponse>} - The retrieved
   * sticky note data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.StickyNoteWebResponse}
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public PagingResponse<StickyNoteWebResponse> getStickyNote(
    @RequestParam(required = false,
                  defaultValue = "1")
      int page,
    @RequestParam(required = false,
                  defaultValue = "1")
      int size
  ) {
    
    return StickyNoteResponseMapper.toStickyNotePagingResponse(
      stickyNoteService.getStickyNote(PageHelper.toPage(page, size)));
  }
  
  /**
   * Creates new sticky note in database.
   *
   * @param request Data of new sticky note in JSON format.
   *
   * @return {@code DataResponse<StickyNoteWebResponse>} - The created
   * sticky note data, wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.StickyNoteWebResponse}
   */
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public DataResponse<StickyNoteWebResponse> createStickyNote(
    @RequestBody
      StickyNoteWebRequest request
  ) {
    
    return StickyNoteResponseMapper.toStickyNoteDataResponse(
      HttpStatus.CREATED, stickyNoteService.createStickyNote(
        stickyNoteRequestMapper.toStickyNote(request)));
  }
  
}
