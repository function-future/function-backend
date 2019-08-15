package com.future.function.repository.listener.core;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.repository.TestApplication;
import com.future.function.repository.feature.core.FileRepositoryV2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class FileMongoEventListenerTest {

  @Autowired
  private FileRepositoryV2 fileRepository;

  @Before
  public void setUp() {}

  @After
  public void tearDown() {

    fileRepository.deleteAll();
  }

  @Test
  public void testGivenFileNotMarkedFolderSaveOperationByListeningToBeforeConversionEventToUpdateFileVersionReturnSuccessfulUpdate() {

    String id = "id";
    String filePath = "file-path";
    String fileUrl = "file-url";

    FileV2 file = FileV2.builder()
      .id(id)
      .filePath(filePath)
      .fileUrl(fileUrl)
      .build();

    fileRepository.save(file);
    fileRepository.save(file);
    fileRepository.save(file);

    FileV2 foundFile = fileRepository.findOne(id);

    assertThat(foundFile.getId()).isEqualTo(id);
    assertThat(foundFile.getFilePath()).isEqualTo(filePath);
    assertThat(foundFile.getFileUrl()).isEqualTo(fileUrl);
    assertThat(foundFile.getVersion()).isEqualTo(2);

    assertThat(foundFile.getVersions()).isNotEmpty();
    assertThat(foundFile.getVersions()
                 .get(1L)
                 .getTimestamp()).isInstanceOf(Long.class);
    assertThat(foundFile.getVersions()
                 .get(1L)
                 .getUrl()).isEqualTo(fileUrl + "?version=1");
    assertThat(foundFile.getVersions()
                 .get(1L)
                 .getPath()).isEqualTo(filePath);
    assertThat(foundFile.getVersions()
                 .get(2L)
                 .getTimestamp()).isInstanceOf(Long.class);
    assertThat(foundFile.getVersions()
                 .get(2L)
                 .getUrl()).isEqualTo(fileUrl + "?version=2");
    assertThat(foundFile.getVersions()
                 .get(2L)
                 .getPath()).isEqualTo(filePath);
  }

  @Test
  public void testGivenFileMarkedAsResourceSaveOperationByListeningToBeforeConversionEventToUpdateFileVersionReturnSuccessfulUpdate() {

    String id = "id";
    String filePath = "file-path";
    String fileUrl = "file-url";

    FileV2 file = FileV2.builder()
      .id(id)
      .filePath(filePath)
      .fileUrl(fileUrl)
      .asResource(true)
      .build();

    fileRepository.save(file);

    FileV2 foundFile = fileRepository.findOne(id);

    assertThat(foundFile.getId()).isEqualTo(id);
    assertThat(foundFile.getFilePath()).isEqualTo(filePath);
    assertThat(foundFile.getFileUrl()).isEqualTo(fileUrl);
    assertThat(foundFile.getVersion()).isEqualTo(0);

    assertThat(foundFile.getVersions()).isEmpty();
  }

  @Test
  public void testGivenFileV2MarkedFolderSaveOperationByListeningToBeforeConversionEventToUpdateFileVersionReturnSuccessfulUpdate() {

    String id = "id";
    String filePath = "file-path";
    String fileUrl = "file-url";

    FileV2 file = FileV2.builder()
      .id(id)
      .filePath(filePath)
      .fileUrl(fileUrl)
      .markFolder(true)
      .build();

    fileRepository.save(file);

    FileV2 foundFile = fileRepository.findOne(id);

    assertThat(foundFile.getId()).isEqualTo(id);
    assertThat(foundFile.getFilePath()).isEqualTo(filePath);
    assertThat(foundFile.getFileUrl()).isEqualTo(fileUrl);
    assertThat(foundFile.getVersion()).isEqualTo(0);

    assertThat(foundFile.getVersions()).isEmpty();
  }

}
