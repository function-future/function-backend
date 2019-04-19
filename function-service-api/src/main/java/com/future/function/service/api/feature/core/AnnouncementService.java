package com.future.function.service.api.feature.core;

import com.future.function.model.entity.feature.core.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface class for announcement logic operations declaration.
 */
public interface AnnouncementService {

  /**
   * Retrieves announcements from database.
   *
   * @param pageable Pageable object for paging data.
   *
   * @return {@code Page<Announcement>} - Page of announcements found in
   * database.
   */
  Page<Announcement> getAnnouncements(Pageable pageable);

  /**
   * Retrieves an announcement from database given the announcement's id. If
   * not found, then throw
   * {@link com.future.function.common.exception.NotFoundException} exception.
   *
   * @param announcementId Id of announcement to be retrieved.
   *
   * @return {@code Announcement} - The announcement object found in database.
   */
  Announcement getAnnouncement(String announcementId);

  /**
   * Creates announcement object and saves any other data related to the
   * announcement.
   *
   * @param announcement Announcement data of new announcement.
   * @param file         File to be attached to announcement. May be null.
   *
   * @return {@code Announcement} - The announcement object of the saved data.
   */
  Announcement createAnnouncement(
    Announcement announcement, MultipartFile file
  );

  /**
   * Updates announcement object and saves any other data related to the
   * announcement. If not found, then throw
   * {@link com.future.function.common.exception.NotFoundException} exception.
   *
   * @param announcement Announcement data of new announcement.
   * @param file         File to be attached to announcement. May be null.
   *
   * @return {@code Announcement} - The announcement object of the saved data.
   */
  Announcement updateAnnouncement(
    Announcement announcement, MultipartFile file
  );

  /**
   * Deletes announcement object from database.
   *
   * @param announcementId Id of announcement to be deleted.
   */
  void deleteAnnouncement(String announcementId);

}
