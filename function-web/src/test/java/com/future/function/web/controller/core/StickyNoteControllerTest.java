package com.future.function.web.controller.core;

import com.future.function.model.entity.feature.core.StickyNote;
import com.future.function.service.api.feature.core.StickyNoteService;
import com.future.function.web.JacksonTestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.request.core.StickyNoteRequestMapper;
import com.future.function.web.mapper.response.core.StickyNoteResponseMapper;
import com.future.function.web.model.request.core.StickyNoteWebRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.StickyNoteWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(value = StickyNoteController.class)
public class StickyNoteControllerTest extends JacksonTestHelper {
  
  private static final String TITLE = "title";
  
  private static final String DESCRIPTION = "description";
  
  private static final String VALID_JSON =
    "{\"title\":\"" + TITLE + "\"," + "\"description\":\"" + DESCRIPTION +
    "\"}";
  
  private static final StickyNote STICKY_NOTE = StickyNote.builder()
    .title(TITLE)
    .description(DESCRIPTION)
    .build();
  
  private static final StickyNoteWebRequest STICKY_NOTE_WEB_REQUEST =
    StickyNoteWebRequest.builder()
      .title(TITLE)
      .description(DESCRIPTION)
      .build();
  
  private static final Pageable PAGEABLE = new PageRequest(0, 1);
  
  private static final Page<StickyNote> STICKY_NOTE_PAGE = new PageImpl<>(
    Collections.singletonList(STICKY_NOTE), PAGEABLE, 1);
  
  private static final PagingResponse<StickyNoteWebResponse>
    RETRIEVED_PAGING_RESPONSE =
    StickyNoteResponseMapper.toStickyNotePagingResponse(STICKY_NOTE_PAGE);
  
  private static final DataResponse<StickyNoteWebResponse>
    CREATED_DATA_RESPONSE = StickyNoteResponseMapper.toStickyNoteDataResponse(
    HttpStatus.CREATED, STICKY_NOTE);
  
  private JacksonTester<StickyNoteWebRequest> stickyNoteWebRequestJacksonTester;
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private StickyNoteService stickyNoteService;
  
  @MockBean
  private StickyNoteRequestMapper stickyNoteRequestMapper;
  
  @Before
  public void setUp() {
  
    super.setUp();
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(stickyNoteService, stickyNoteRequestMapper);
  }
  
  @Test
  public void testGivenCallToStickyNoteApiByGettingStickyNoteReturnDataResponseOfStickyNote()
    throws Exception {
    
    given(stickyNoteService.getStickyNote(PAGEABLE)).willReturn(STICKY_NOTE_PAGE);
    
    mockMvc.perform(get("/api/core/sticky-notes"))
      .andExpect(status().isOk())
      .andExpect(content().json(
        pagingResponseJacksonTester.write(RETRIEVED_PAGING_RESPONSE)
          .getJson()));
    
    verify(stickyNoteService).getStickyNote(PAGEABLE);
  }
  
  @Test
  public void testGivenCallToStickyNoteApiByCreatingStickyNoteReturnDataResponseOfStickyNote()
    throws Exception {
  
    given(
      stickyNoteRequestMapper.toStickyNote(STICKY_NOTE_WEB_REQUEST)).willReturn(
      STICKY_NOTE);
    given(stickyNoteService.createStickyNote(STICKY_NOTE)).willReturn(
      STICKY_NOTE);
  
    mockMvc.perform(post("/api/core/sticky-notes").contentType(
      MediaType.APPLICATION_JSON_VALUE)
                      .content(stickyNoteWebRequestJacksonTester.write(
                        STICKY_NOTE_WEB_REQUEST)
                                 .getJson()))
      .andExpect(status().isCreated())
      .andExpect(content().json(
        dataResponseJacksonTester.write(CREATED_DATA_RESPONSE)
          .getJson()));
  
    verify(stickyNoteRequestMapper).toStickyNote(STICKY_NOTE_WEB_REQUEST);
    verify(stickyNoteService).createStickyNote(STICKY_NOTE);
  }
  
}
