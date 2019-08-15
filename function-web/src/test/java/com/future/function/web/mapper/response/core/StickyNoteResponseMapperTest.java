package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.StickyNote;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.StickyNoteWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class StickyNoteResponseMapperTest {

  private static final String TITLE = "title";

  private static final String DESCRIPTION = "description";

  private static final Long UPDATED_AT = 1L;

  private StickyNote stickyNote;

  private StickyNoteWebResponse stickyNoteWebResponse;

  private Page<StickyNote> stickyNotePage;

  private DataResponse<StickyNoteWebResponse> createdDataResponse;

  private PagingResponse<StickyNoteWebResponse> retrievedPagingResponse;

  @Before
  public void setUp() {

    stickyNote = StickyNote.builder()
      .title(TITLE)
      .description(DESCRIPTION)
      .build();
    stickyNote.setUpdatedAt(UPDATED_AT);

    stickyNoteWebResponse = StickyNoteWebResponse.builder()
      .title(TITLE)
      .description(DESCRIPTION)
      .updatedAt(UPDATED_AT)
      .build();

    createdDataResponse = DataResponse.<StickyNoteWebResponse>builder().code(
      201)
      .status("CREATED")
      .data(stickyNoteWebResponse)
      .build();

    stickyNotePage = new PageImpl<>(
      Collections.singletonList(stickyNote), new PageRequest(0, 1), 1);
    retrievedPagingResponse =
      PagingResponse.<StickyNoteWebResponse>builder().code(200)
        .status("OK")
        .data(Arrays.asList(stickyNoteWebResponse))
        .paging(PageHelper.toPaging(stickyNotePage))
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
  }

  @Test
  public void testGivenStickyNotePageByMappingToPagingResponseReturnPagingResponseObject() {

    PagingResponse<StickyNoteWebResponse> retrievedPagingResponse =
      StickyNoteResponseMapper.toStickyNotePagingResponse(stickyNotePage);

    assertThat(retrievedPagingResponse).isNotNull();
    assertThat(retrievedPagingResponse).isEqualTo(this.retrievedPagingResponse);
  }

}
