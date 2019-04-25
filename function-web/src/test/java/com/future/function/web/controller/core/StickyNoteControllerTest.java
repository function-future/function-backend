package com.future.function.web.controller.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.model.entity.feature.core.StickyNote;
import com.future.function.service.api.feature.core.StickyNoteService;
import com.future.function.web.mapper.request.core.StickyNoteRequestMapper;
import com.future.function.web.mapper.response.core.StickyNoteResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.StickyNoteWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StickyNoteController.class)
public class StickyNoteControllerTest {
  
  private static final String TITLE = "title";
  
  private static final String DESCRIPTION = "description";
  
  private static final String VALID_JSON =
    "{\"title\":\"" + TITLE + "\"," + "\"description\":\"" + DESCRIPTION +
    "\"}";
  
  private static final StickyNote STICKY_NOTE = StickyNote.builder()
    .title(TITLE)
    .description(DESCRIPTION)
    .build();
  
  private static final DataResponse<StickyNoteWebResponse>
    RETRIEVED_DATA_RESPONSE = StickyNoteResponseMapper.toStickyNoteDataResponse(
    STICKY_NOTE);
  
  private static final DataResponse<StickyNoteWebResponse>
    CREATED_DATA_RESPONSE = StickyNoteResponseMapper.toStickyNoteDataResponse(
    HttpStatus.CREATED, STICKY_NOTE);
  
  private JacksonTester<DataResponse<StickyNoteWebResponse>>
    dataResponseJacksonTester;
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private StickyNoteService stickyNoteService;
  
  @MockBean
  private StickyNoteRequestMapper stickyNoteRequestMapper;
  
  @Before
  public void setUp() {
    
    JacksonTester.initFields(this, new ObjectMapper());
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(stickyNoteService, stickyNoteRequestMapper);
  }
  
  @Test
  public void testGivenCallToStickyNoteApiByGettingStickyNoteReturnDataResponseOfStickyNote()
    throws Exception {
    
    given(stickyNoteService.getStickyNote()).willReturn(STICKY_NOTE);
    
    mockMvc.perform(get("/api/core/sticky-notes"))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(RETRIEVED_DATA_RESPONSE)
          .getJson()));
    
    verify(stickyNoteService).getStickyNote();
  }
  
  @Test
  public void testGivenCallToStickyNoteApiByCreatingStickyNoteReturnDataResponseOfStickyNote()
    throws Exception {
    
    given(stickyNoteRequestMapper.toStickyNote(VALID_JSON)).willReturn(
      STICKY_NOTE);
    given(stickyNoteService.createStickyNote(STICKY_NOTE)).willReturn(
      STICKY_NOTE);
    
    mockMvc.perform(post("/api/core/sticky-notes").contentType(
      MediaType.APPLICATION_JSON_VALUE)
                      .content(VALID_JSON))
      .andExpect(status().isCreated())
      .andExpect(content().json(
        dataResponseJacksonTester.write(CREATED_DATA_RESPONSE)
          .getJson()));
    
    verify(stickyNoteRequestMapper).toStickyNote(VALID_JSON);
    verify(stickyNoteService).createStickyNote(STICKY_NOTE);
  }
  
}
