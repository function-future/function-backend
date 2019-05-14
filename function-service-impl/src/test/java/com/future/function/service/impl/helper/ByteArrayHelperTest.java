package com.future.function.service.impl.helper;

import com.future.function.common.exception.BadRequestException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;

public class ByteArrayHelperTest {
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenJavaIoFileByConvertingToByteArrayReturnByteArray() {
    
    String testFilePath = Paths.get(
      "src", "test", "resources", "TestFile" + ".png")
      .toString();
    
    byte[] convertedFile = ByteArrayHelper.getBytesFromJavaIoFile(
      new File(testFilePath));
    
    assertThat(convertedFile.length).isNotEqualTo(0);
  }
  
  @Test
  public void testGivenInvalidJavaIoFileByConvertingToByteArrayReturnBadRequestException() {
    
    catchException(
      () -> ByteArrayHelper.getBytesFromJavaIoFile(new File("test")));
    
    assertThat(caughtException().getClass()).isEqualTo(
      BadRequestException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Unsupported Operation");
  }
  
}
