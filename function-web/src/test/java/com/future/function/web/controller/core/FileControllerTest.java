package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.service.api.feature.core.FileService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FileController.class)
public class FileControllerTest {
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private FileService fileService;
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenCallToGetFileApiByGettingFileByOriginAndFilenameReturnByteArray()
    throws Exception {
    
    String fileName = "name.png";
    String origin = "user";
    byte[] bytes = new byte[100];
    
    given(fileService.getFileAsByteArray(fileName, FileOrigin.USER)).willReturn(
      bytes);
  
    mockMvc.perform(get("/files/resource/" + origin + "/" + fileName))
      .andExpect(status().isOk())
      .andExpect(content().bytes(bytes))
      .andReturn()
      .getResponse();
    
    verify(fileService).getFileAsByteArray(fileName, FileOrigin.USER);
  }
  
}
