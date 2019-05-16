package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Announcement;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.repository.feature.core.AnnouncementRepository;
import com.future.function.service.api.feature.core.ResourceService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnnouncementServiceImplTest {
  
  private static final String ID = "id";
  
  private static final String TITLE = "title";
  
  private static final String SUMMARY = "summary";
  
  private static final String DESCRIPTION_HTML = "description-html";
  
  private static final String FILE_ID = "file-id";
  
  private static final List<String> FILE_IDS = Collections.singletonList(
    FILE_ID);
  
  private static final FileV2 FILE_V2 = FileV2.builder()
    .id(FILE_ID)
    .build();
  
  private static final List<FileV2> FILE_V2S = Collections.singletonList(
    FILE_V2);
  
  private static final Announcement ANNOUNCEMENT = Announcement.builder()
    .id(ID)
    .title(TITLE)
    .summary(SUMMARY)
    .descriptionHtml(DESCRIPTION_HTML)
    .fileV2s(FILE_V2S)
    .build();
  
  private static final Pageable PAGEABLE = new PageRequest(0, 4);
  
  private static final Page<Announcement> ANNOUNCEMENT_PAGE = new PageImpl<>(
    Collections.singletonList(ANNOUNCEMENT), PAGEABLE, 1);
  
  @Mock
  private AnnouncementRepository announcementRepository;
  
  @Mock
  private ResourceService resourceService;
  
  @InjectMocks
  private AnnouncementServiceImpl announcementService;
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(announcementRepository, resourceService);
  }
  
  @Test
  public void testGivenPageableObjectByGettingAnnouncementsReturnAnnouncementsPage() {
    
    when(announcementRepository.findAll(PAGEABLE)).thenReturn(
      ANNOUNCEMENT_PAGE);
    
    Page<Announcement> foundAnnouncementsPage =
      announcementService.getAnnouncements(PAGEABLE);
    
    assertThat(foundAnnouncementsPage).isNotNull();
    assertThat(foundAnnouncementsPage).isEqualTo(ANNOUNCEMENT_PAGE);
    
    verify(announcementRepository).findAll(PAGEABLE);
  }
  
  @Test
  public void testGivenIdAndExistingAnnouncementInDatabaseByGettingAnnouncementReturnAnnouncementObject() {
    
    when(announcementRepository.findOne(ID)).thenReturn(ANNOUNCEMENT);
    
    Announcement foundAnnouncement = announcementService.getAnnouncement(ID);
    
    assertThat(foundAnnouncement).isNotNull();
    assertThat(foundAnnouncement).isEqualTo(ANNOUNCEMENT);
    
    verify(announcementRepository).findOne(ID);
  }
  
  @Test
  public void testGivenIdAndNonExistingAnnouncementInDatabaseByGettingAnnouncementReturnNotFoundException() {
    
    when(announcementRepository.findOne(ID)).thenReturn(null);
    
    catchException(() -> announcementService.getAnnouncement(ID));
    
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Get Announcement Not Found");
    
    verify(announcementRepository).findOne(ID);
  }
  
  @Test
  public void testGivenAnnouncementDataByCreatingAnnouncementReturnNewAnnouncementObject() {
    
    when(resourceService.getFile(FILE_ID)).thenReturn(FILE_V2);
    when(resourceService.markFilesUsed(FILE_IDS, true)).thenReturn(true);
    when(announcementRepository.save(ANNOUNCEMENT)).thenReturn(ANNOUNCEMENT);
    
    Announcement createdAnnouncement = announcementService.createAnnouncement(
      ANNOUNCEMENT);
    
    assertThat(createdAnnouncement).isNotNull();
    assertThat(createdAnnouncement).isEqualTo(ANNOUNCEMENT);
    
    verify(resourceService).getFile(FILE_ID);
    verify(resourceService).markFilesUsed(FILE_IDS, true);
    verify(announcementRepository).save(ANNOUNCEMENT);
  }
  
  @Test
  public void testGivenAnnouncementDataByUpdatingAnnouncementReturnUpdatedAnnouncementObject() {
    
    when(announcementRepository.findOne(ID)).thenReturn(ANNOUNCEMENT);
    when(resourceService.markFilesUsed(FILE_IDS, false)).thenReturn(true);
    when(resourceService.getFile(FILE_ID)).thenReturn(FILE_V2);
    when(resourceService.markFilesUsed(FILE_IDS, true)).thenReturn(true);
    
    Announcement announcementData = Announcement.builder()
      .id(ID)
      .title(TITLE)
      .summary(null)
      .descriptionHtml(DESCRIPTION_HTML)
      .fileV2s(FILE_V2S)
      .build();
    
    when(announcementRepository.save(announcementData)).thenReturn(
      announcementData);
    
    Announcement updatedAnnouncement = announcementService.updateAnnouncement(
      announcementData);
    
    assertThat(updatedAnnouncement).isNotNull();
    assertThat(updatedAnnouncement).isEqualTo(announcementData);
    
    verify(announcementRepository).findOne(ID);
    verify(resourceService).markFilesUsed(FILE_IDS, false);
    verify(resourceService).getFile(FILE_ID);
    verify(resourceService).markFilesUsed(FILE_IDS, true);
    verify(announcementRepository).save(announcementData);
  }
  
  @Test
  public void testGivenIdAndExistingAnnouncementInDatabaseByDeletingAnnouncementReturnSuccessfulDeletion() {
    
    when(announcementRepository.findOne(ID)).thenReturn(ANNOUNCEMENT);
    when(resourceService.markFilesUsed(FILE_IDS, false)).thenReturn(true);
    
    announcementService.deleteAnnouncement(ID);
    
    verify(announcementRepository).findOne(ID);
    verify(resourceService).markFilesUsed(FILE_IDS, false);
    verify(announcementRepository).delete(ID);
  }
  
}
