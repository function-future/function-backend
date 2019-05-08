package com.future.function.web.mapper.request.core;

import com.future.function.common.exception.BadRequestException;
import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.core.StickyNote;
import com.future.function.web.mapper.request.WebRequestMapper;
import com.future.function.web.model.request.core.StickyNoteWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StickyNoteRequestMapperTest {
  
  private static final String TITLE = "title";
  
  private static final String DESCRIPTION = "description";
  
  private static final String VALID_JSON =
    "{\"title\":\"" + TITLE + "\"," + "\"description\":\"" +
    DESCRIPTION + "\"}";
  
  private static final String INVALID_JSON = "{}";
  
  private static final StickyNoteWebRequest STICKY_NOTE_WEB_REQUEST =
    StickyNoteWebRequest.builder()
      .title(TITLE)
      .description(DESCRIPTION)
      .build();
  
  private static final StickyNote STICKY_NOTE = StickyNote.builder()
    .title(TITLE)
    .description(DESCRIPTION)
    .build();
  
  @Mock
  private WebRequestMapper requestMapper;
  
  @Mock
  private ObjectValidator validator;
  
  @InjectMocks
  private StickyNoteRequestMapper stickyNoteRequestMapper;
  
  @Before
  public void setUp() {
  
    when(requestMapper.toWebRequestObject(VALID_JSON, StickyNoteWebRequest.class
    )).thenReturn(STICKY_NOTE_WEB_REQUEST);
    when(requestMapper.toWebRequestObject(INVALID_JSON,
                                          StickyNoteWebRequest.class
    )).thenThrow(new BadRequestException("Bad Request"));
    when(validator.validate(STICKY_NOTE_WEB_REQUEST)).thenReturn(
      STICKY_NOTE_WEB_REQUEST);
  }
  
  @After
  public void tearDown() {
  
    verifyNoMoreInteractions(requestMapper, validator);
  }
  
  @Test
  public void testGivenJsonDataAsStringByParsingToStickyNoteClassReturnStickyNoteObject() {
    
    StickyNote parsedStickyNote = stickyNoteRequestMapper.toStickyNote(
      VALID_JSON);
    
    assertThat(parsedStickyNote).isEqualTo(STICKY_NOTE);
  
    verify(requestMapper).toWebRequestObject(
      VALID_JSON, StickyNoteWebRequest.class);
    verify(validator).validate(STICKY_NOTE_WEB_REQUEST);
  }
  
  @Test
  public void testGivenJsonDataWithInvalidFormatByParsingToStickyNoteClassReturnBadRequestException() {
    
    catchException(() -> stickyNoteRequestMapper.toStickyNote(INVALID_JSON));
    
    assertThat(caughtException().getClass()).isEqualTo(
      BadRequestException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Bad Request");
  
    verify(requestMapper).toWebRequestObject(
      INVALID_JSON, StickyNoteWebRequest.class);
  }
  
}
