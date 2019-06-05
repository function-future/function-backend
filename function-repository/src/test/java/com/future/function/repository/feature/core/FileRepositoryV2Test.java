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

import java.util.Collections;
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
    
    Page<FileV2> foundFileV2s = fileRepositoryV2.findAllByParentId(PARENT_ID,
                                                                   new PageRequest(
                                                                     0, 5)
    );
    
    assertThat(foundFileV2s).isNotNull();
    assertThat(foundFileV2s.getContent()).isEqualTo(
      Collections.singletonList(file));
  }
  
  @Test
  public void testGivenParentIdByFindingFilesOrFoldersReturnStreamOfFileOrFolder() {
    
    Stream<FileV2> foundFilesOrFolders = fileRepositoryV2.findAllByParentId(
      PARENT_ID);
    
    assertThat(foundFilesOrFolders).isNotEqualTo(Stream.empty());
  }
  
  @Test
  public void testGivenIdAndParentIdByFindingFileOrFolderReturnFileOrFolderObject() {
    
    Optional<FileV2> foundFileOrFolder = fileRepositoryV2.findByIdAndParentId(
      FILE_ID, PARENT_ID);
    
    assertThat(foundFileOrFolder).isNotEqualTo(Optional.empty());
    assertThat(foundFileOrFolder.get()).isEqualTo(file);
  }
  
}
