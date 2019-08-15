package com.future.function.web.mapper.request.core;

import com.future.function.model.entity.feature.core.StickyNote;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.core.StickyNoteWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StickyNoteRequestMapperTest {

  private static final String TITLE = "title";

  private static final String DESCRIPTION = "description";

  private static final String VALID_JSON =
    "{\"title\":\"" + TITLE + "\"," + "\"description\":\"" + DESCRIPTION +
    "\"}";

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
  private RequestValidator validator;

  @InjectMocks
  private StickyNoteRequestMapper stickyNoteRequestMapper;

  @Before
  public void setUp() {

    when(validator.validate(STICKY_NOTE_WEB_REQUEST)).thenReturn(
      STICKY_NOTE_WEB_REQUEST);
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(validator);
  }

  @Test
  public void testGivenJsonDataAsStringByParsingToStickyNoteClassReturnStickyNoteObject() {

    StickyNote parsedStickyNote = stickyNoteRequestMapper.toStickyNote(
      STICKY_NOTE_WEB_REQUEST);

    assertThat(parsedStickyNote).isEqualTo(STICKY_NOTE);

    verify(validator).validate(STICKY_NOTE_WEB_REQUEST);
  }

}
