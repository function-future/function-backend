package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.StickyNote;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.StickyNoteWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class StickyNoteResponseMapperTest {
  
  private static final String TITLE = "title";
  
  private static final String DESCRIPTION = "description";
  
  private static final Long UPDATED_AT = 1L;
  
  private StickyNote stickyNote;
  
  private StickyNoteWebResponse stickyNoteWebResponse;
  
  private DataResponse<StickyNoteWebResponse> createdDataResponse;
  
  private DataResponse<StickyNoteWebResponse> retrievedDataResponse;
  
  @Before
  public void setUp() {
    
    stickyNote = StickyNote.builder()
      .title(TITLE)
      .description(DESCRIPTION)
      .build();
    stickyNote.setUpdatedAt(UPDATED_AT);
    
    stickyNoteWebResponse = StickyNoteWebResponse.builder()
      .noteTitle(TITLE)
      .noteDescription(DESCRIPTION)
      .updatedAt(UPDATED_AT)
      .build();
    
    createdDataResponse = DataResponse.<StickyNoteWebResponse>builder().code(
      201)
      .status("CREATED")
      .data(stickyNoteWebResponse)
      .build();
    
    retrievedDataResponse = DataResponse.<StickyNoteWebResponse>builder().code(
      200)
      .status("OK")
      .data(stickyNoteWebResponse)
      .build();
  }
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenStickyNoteDataByMappingToDataResponseReturnDataResponseObject() {
    
    DataResponse<StickyNoteWebResponse> createdDataResponse =
      StickyNoteResponseMapper.toStickyNoteDataResponse(
        HttpStatus.CREATED, stickyNote);
    
    assertThat(createdDataResponse).isNotNull();
    assertThat(createdDataResponse).isEqualTo(this.createdDataResponse);
    
    DataResponse<StickyNoteWebResponse> retrievedDataResponse =
      StickyNoteResponseMapper.toStickyNoteDataResponse(stickyNote);
    
    assertThat(retrievedDataResponse).isNotNull();
    assertThat(retrievedDataResponse).isEqualTo(this.retrievedDataResponse);
  }
  
}