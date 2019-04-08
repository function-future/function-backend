package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.File;
import com.future.function.repository.TestApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class FileRepositoryTest {
  
  private static final String FILE_ID = "id";
  
  private static final String FILE_PATH = "file-path";
  
  private static final String FILE_URL = "file-url";
  
  private File file;
  
  @Autowired
  private FileRepository fileRepository;
  
  @Before
  public void setUp() {
    
    file = File.builder()
      .id(FILE_ID)
      .filePath(FILE_PATH)
      .fileUrl(FILE_URL)
      .asResource(true)
      .build();
    
    fileRepository.save(file);
  }
  
  @After
  public void tearDown() {
    
    fileRepository.deleteAll();
  }
  
  @Test
  public void testGivenIdAndAsResourceByFindingFileReturnFileObject() {
    
    Optional<File> foundFile = fileRepository.findByIdAndAsResource(FILE_ID,
                                                                    true
    );
    
    assertThat(foundFile).isNotEqualTo(Optional.empty());
    assertThat(foundFile.get()).isEqualTo(file);
  }
  
}
