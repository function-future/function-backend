package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Announcement;
import com.future.function.model.entity.feature.core.File;
import com.future.function.repository.feature.core.AnnouncementRepository;
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
  
  private static final Announcement ANNOUNCEMENT = Announcement.builder()
    .id(ID)
    .title(TITLE)
    .summary(SUMMARY)
    .descriptionHtml(DESCRIPTION_HTML)
    .file(new File())
    .build();
  
  private static final Pageable PAGEABLE = new PageRequest(0, 4);
  
  private static final Page<Announcement> ANNOUNCEMENT_PAGE = new PageImpl<>(
    Collections.singletonList(ANNOUNCEMENT), PAGEABLE, 1);
  
  @Mock
  private AnnouncementRepository announcementRepository;
  
  @InjectMocks
  private AnnouncementServiceImpl announcementService;
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(announcementRepository);
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
    
    when(announcementRepository.save(ANNOUNCEMENT)).thenReturn(ANNOUNCEMENT);
    when(announcementRepository.findOne(ID)).thenReturn(ANNOUNCEMENT);
    
    Announcement createdAnnouncement = announcementService.createAnnouncement(
      ANNOUNCEMENT, null);
    
    assertThat(createdAnnouncement).isNotNull();
    assertThat(createdAnnouncement).isEqualTo(ANNOUNCEMENT);
    
    verify(announcementRepository).save(ANNOUNCEMENT);
    verify(announcementRepository).findOne(ID);
  }
  
  @Test
  public void testGivenAnnouncementDataByUpdatingAnnouncementReturnUpdatedAnnouncementObject() {
    
    when(announcementRepository.findOne(ID)).thenReturn(ANNOUNCEMENT);
    
    Announcement announcementData = Announcement.builder()
      .id(ID)
      .title(TITLE)
      .summary(null)
      .descriptionHtml(DESCRIPTION_HTML)
      .file(new File())
      .build();
    
    when(announcementRepository.save(announcementData)).thenReturn(
      announcementData);
    
    Announcement updatedAnnouncement = announcementService.updateAnnouncement(
      announcementData, null);
    
    assertThat(updatedAnnouncement).isNotNull();
    assertThat(updatedAnnouncement).isEqualTo(announcementData);
    
    verify(announcementRepository).findOne(ID);
    verify(announcementRepository).save(announcementData);
  }
  
  @Test
  public void testGivenAnnouncementDataWithNonExistingIdInDatabaseByUpdatingAnnouncementReturnNotFoundException() {
    
    when(announcementRepository.findOne(ID)).thenReturn(null);
    
    Announcement announcementData = Announcement.builder()
      .id(ID)
      .title(TITLE)
      .summary(null)
      .descriptionHtml(DESCRIPTION_HTML)
      .file(new File())
      .build();
    
    catchException(
      () -> announcementService.updateAnnouncement(announcementData, null));
    
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Get Announcement Not Found");
    
    verify(announcementRepository).findOne(ID);
  }
  
  @Test
  public void testGivenIdAndExistingAnnouncementInDatabaseByDeletingAnnouncementReturnSuccessfulDeletion() {
    
    when(announcementRepository.findOne(ID)).thenReturn(ANNOUNCEMENT);
    
    announcementService.deleteAnnouncement(ID);
    
    verify(announcementRepository).findOne(ID);
    verify(announcementRepository).delete(ANNOUNCEMENT);
  }
  
  @Test
  public void testGivenIdAndNonExistingAnnouncementInDatabaseByDeletingAnnouncementReturnNotFoundException() {
    
    when(announcementRepository.findOne(ID)).thenReturn(null);
    
    catchException(() -> announcementService.deleteAnnouncement(ID));
    
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Get Announcement Not Found");
    
    verify(announcementRepository).findOne(ID);
  }
  
}
