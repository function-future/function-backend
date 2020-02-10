package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.repository.TestApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class FileRepositoryV2Test {

  private static final String FILE_ID = "id";

  private static final String FILE_PATH = "file-path";

  private static final String FILE_URL = "file-url";

  private static final String PARENT_ID = "parent-id";

  private FileV2 file;

  @Autowired
  private FileRepositoryV2 fileRepositoryV2;

  @Before
  public void setUp() {

    file = FileV2.builder()
      .id(FILE_ID)
      .filePath(FILE_PATH)
      .fileUrl(FILE_URL)
      .asResource(true)
      .used(true)
      .parentId(PARENT_ID)
      .build();

    fileRepositoryV2.save(file);
  }

  @After
  public void tearDown() {

    fileRepositoryV2.deleteAll();
  }

  @Test
  public void testGivenIdAndAsResourceByFindingFileReturnFileObject() {

    Optional<FileV2> foundFile = fileRepositoryV2.findByIdAndAsResource(FILE_ID,
                                                                        true
    );

    assertThat(foundFile).isNotEqualTo(Optional.empty());
    assertThat(foundFile.get()).isEqualTo(file);
  }

  @Test
  public void testGivenParentIdAndPageableByFindingFilesOrFoldersReturnPageOfFileOrFolder() {

    FileV2 file1 = FileV2.builder()
      .id(FILE_ID + "FILE")
      .filePath(FILE_PATH)
      .fileUrl(FILE_URL)
      .asResource(false)
      .markFolder(false)
      .parentId(PARENT_ID)
      .build();

    FileV2 folder1 = FileV2.builder()
      .id(FILE_ID + "FOLDER1")
      .filePath(FILE_PATH)
      .fileUrl(FILE_URL)
      .asResource(false)
      .markFolder(true)
      .parentId(PARENT_ID)
      .name("folder1")
      .build();

    FileV2 folder2 = FileV2.builder()
      .id(FILE_ID + "FOLDER2")
      .filePath(FILE_PATH)
      .fileUrl(FILE_URL)
      .asResource(false)
      .markFolder(true)
      .parentId(PARENT_ID)
      .name("folder2")
      .build();

    fileRepositoryV2.save(file1);
    fileRepositoryV2.save(folder1);
    fileRepositoryV2.save(folder2);

    Page<FileV2> foundFileV2s =
      fileRepositoryV2.findAllByParentIdAndAsResourceFalseAndDeletedFalseOrderByMarkFolderDescNameAsc(
        PARENT_ID, new PageRequest(0, 10));

    assertThat(foundFileV2s).isNotNull();
    assertThat(foundFileV2s.getContent()).isEqualTo(
      Arrays.asList(folder1, folder2, file1));

    fileRepositoryV2.delete(FILE_ID + "FILE");
    fileRepositoryV2.delete(FILE_ID + "FOLDER");
  }

  @Test
  public void testGivenParentIdByFindingFilesOrFoldersReturnStreamOfFileOrFolder() {

    List<FileV2> foundFilesOrFolders =
      fileRepositoryV2.findAllByParentIdAndDeletedFalse(PARENT_ID);

    assertThat(foundFilesOrFolders).isNotEqualTo(Collections.emptyList());
  }

  @Test
  public void testGivenIdAndParentIdByFindingFileOrFolderReturnFileOrFolderObject() {

    Optional<FileV2> foundFileOrFolder =
      fileRepositoryV2.findByIdAndParentIdAndDeletedFalse(FILE_ID, PARENT_ID);

    assertThat(foundFileOrFolder).isNotEqualTo(Optional.empty());
    assertThat(foundFileOrFolder.get()).isEqualTo(file);
  }

  @Test
  public void testGivenMethodCallByFindingMarkedDeletedFileReturnStreamOfFile() {

    FileV2 file1 = FileV2.builder()
      .used(false)
      .build();

    fileRepositoryV2.save(file1);

    Stream<FileV2> foundFiles = fileRepositoryV2.findAllByUsedFalse();

    assertThat(foundFiles).isNotEqualTo(Stream.empty());
    assertThat(foundFiles.findFirst()).isEqualTo(Optional.of(file1));

    fileRepositoryV2.delete(file1.getId());
  }

  @Test
  public void testGivenMethodCallByFindingFileOrFolderByIdAndDeletedFalseReturnFileOrFolderObject() {

    assertThat(fileRepositoryV2.findByIdAndDeletedFalse(FILE_ID)
                 .isPresent()).isTrue();

    file.setDeleted(true);
    fileRepositoryV2.save(file);

    assertThat(fileRepositoryV2.findByIdAndDeletedFalse(FILE_ID)
                 .isPresent()).isFalse();
  }

}
