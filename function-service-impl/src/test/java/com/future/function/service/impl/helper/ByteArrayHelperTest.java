package com.future.function.service.impl.helper;

import com.future.function.common.exception.BadRequestException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

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
    
    String currentPath = this.getClass()
      .getClassLoader()
      .getResource("")
      .toString();
    String testFilePath = currentPath.substring(currentPath.indexOf("/"))
      .concat("TestFile.png");
    
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
