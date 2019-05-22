package com.future.function.web.mapper.request.core;

import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.core.Announcement;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.web.mapper.request.WebRequestMapper;
import com.future.function.web.model.request.core.AnnouncementWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnnouncementRequestMapperTest {
  
  private static final String DATA = "data";
  
  private static final String ID = "id";
  
  private static final String TITLE = "title";
  
  private static final String SUMMARY = "summary";
  
  private static final String DESCRIPTION_HTML = "description-html";
  
  private static final AnnouncementWebRequest REQUEST =
    AnnouncementWebRequest.builder()
      .title(TITLE)
      .summary(SUMMARY)
      .description(DESCRIPTION_HTML)
      .build();
  
  private static final Announcement ANNOUNCEMENT_WITHOUT_ID =
    Announcement.builder()
      .title(TITLE)
      .summary(SUMMARY)
      .description(DESCRIPTION_HTML)
      .build();
  
  private static final Announcement ANNOUNCEMENT_WITH_ID_AND_EMPTY_FILE_LIST =
    Announcement.builder()
      .id(ID)
      .title(TITLE)
      .summary(SUMMARY)
      .description(DESCRIPTION_HTML)
      .fileV2s(Collections.emptyList())
      .build();
  
  private static final String FILE_ID = "file-id";
  
  private static final AnnouncementWebRequest REQUEST_WITH_FILE_IDS =
    AnnouncementWebRequest.builder()
      .title(TITLE)
      .summary(SUMMARY)
      .description(DESCRIPTION_HTML)
      .files(Collections.singletonList(FILE_ID))
      .build();
  
  private static final Announcement ANNOUNCEMENT_WITH_ID_AND_FILE_LIST =
    Announcement.builder()
      .id(ID)
      .title(TITLE)
      .summary(SUMMARY)
      .description(DESCRIPTION_HTML)
      .fileV2s(Collections.singletonList(FileV2.builder()
                                           .id(FILE_ID)
                                           .build()))
      .build();
  
  @Mock
  private ObjectValidator validator;
  
  @Mock
  private WebRequestMapper requestMapper;
  
  @InjectMocks
  private AnnouncementRequestMapper announcementRequestMapper;
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(validator, requestMapper);
  }
  
  @Test
  public void testGivenDataByParsingToAnnouncementClassReturnAnnouncementObject() {
    
    when(requestMapper.toWebRequestObject(DATA,
                                          AnnouncementWebRequest.class
    )).thenReturn(REQUEST);
    
    Announcement parsedAnnouncement = announcementRequestMapper.toAnnouncement(
      DATA);
    
    assertThat(parsedAnnouncement).isNotNull();
    assertThat(parsedAnnouncement.getId()).isNotBlank();
    assertThat(parsedAnnouncement.getTitle()).isEqualTo(
      ANNOUNCEMENT_WITHOUT_ID.getTitle());
    assertThat(parsedAnnouncement.getSummary()).isEqualTo(
      ANNOUNCEMENT_WITHOUT_ID.getSummary());
    assertThat(parsedAnnouncement.getDescription()).isEqualTo(
      ANNOUNCEMENT_WITHOUT_ID.getDescription());
    
    verify(requestMapper).toWebRequestObject(
      DATA, AnnouncementWebRequest.class);
    verify(validator).validate(REQUEST);
  }
  
  @Test
  public void testGivenIdAndDataByParsingToAnnouncementClassReturnAnnouncementObject() {
    
    when(requestMapper.toWebRequestObject(DATA,
                                          AnnouncementWebRequest.class
    )).thenReturn(REQUEST);
    
    Announcement parsedAnnouncement = announcementRequestMapper.toAnnouncement(
      ID, DATA);
    
    assertThat(parsedAnnouncement).isNotNull();
    assertThat(parsedAnnouncement).isEqualTo(
      ANNOUNCEMENT_WITH_ID_AND_EMPTY_FILE_LIST);
    
    verify(requestMapper).toWebRequestObject(
      DATA, AnnouncementWebRequest.class);
    verify(validator).validate(REQUEST);
  }
  
  @Test
  public void testGivenIdAndAnnouncementWebRequestByParsingToAnnouncementClassReturnAnnouncementObject() {
    
    Announcement parsedAnnouncement = announcementRequestMapper.toAnnouncement(
      ID, REQUEST_WITH_FILE_IDS);
    
    assertThat(parsedAnnouncement).isNotNull();
    assertThat(parsedAnnouncement).isEqualTo(
      ANNOUNCEMENT_WITH_ID_AND_FILE_LIST);
    
    verifyZeroInteractions(requestMapper);
    verify(validator).validate(REQUEST_WITH_FILE_IDS);
  }
  
  @Test
  public void testGivenAnnouncementWebRequestByParsingToAnnouncementClassReturnAnnouncementObject() {
    
    Announcement parsedAnnouncement = announcementRequestMapper.toAnnouncement(
      REQUEST);
    
    assertThat(parsedAnnouncement).isNotNull();
    assertThat(parsedAnnouncement.getId()).isNotBlank();
    assertThat(parsedAnnouncement.getTitle()).isEqualTo(
      ANNOUNCEMENT_WITHOUT_ID.getTitle());
    assertThat(parsedAnnouncement.getSummary()).isEqualTo(
      ANNOUNCEMENT_WITHOUT_ID.getSummary());
    assertThat(parsedAnnouncement.getDescription()).isEqualTo(
      ANNOUNCEMENT_WITHOUT_ID.getDescription());
    
    verifyZeroInteractions(requestMapper);
    verify(validator).validate(REQUEST);
  }
  
}
