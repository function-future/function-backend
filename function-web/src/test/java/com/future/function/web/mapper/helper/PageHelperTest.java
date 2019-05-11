package com.future.function.web.mapper.helper;

import com.future.function.web.dummy.data.DummyData;
import com.future.function.web.model.response.base.paging.Paging;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PageHelperTest {
  
  private static final int PAGE = 1;
  
  private static final int SIZE = 10;
  
  private static final Pageable PAGEABLE = new PageRequest(PAGE - 1, SIZE);
  
  private static final DummyData DUMMY_DATA = new DummyData();
  
  private static final List<DummyData> DUMMY_DATA_LIST = Arrays.asList(
    DUMMY_DATA, DUMMY_DATA, DUMMY_DATA, DUMMY_DATA, DUMMY_DATA, DUMMY_DATA,
    DUMMY_DATA, DUMMY_DATA, DUMMY_DATA, DUMMY_DATA, DUMMY_DATA, DUMMY_DATA
  );
  
  private static final int TOTAL_PAGE = 2;
  
  private static final Page<DummyData> DUMMY_DATA_PAGE = new PageImpl<>(
    DUMMY_DATA_LIST, PAGEABLE, DUMMY_DATA_LIST.size());
  
  private static final Paging PAGING = Paging.builder()
    .page(PAGE - 1)
    .size(SIZE)
    .totalRecords(DUMMY_DATA_LIST.size())
    .build();
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenPageAndSizeByCreatingPageableReturnPageableObject() {
    
    Pageable pageable = PageHelper.toPageable(PAGE, SIZE);
    
    assertThat(pageable).isNotNull();
    assertThat(pageable.getPageNumber()).isEqualTo(PAGE - 1);
    assertThat(pageable.getPageSize()).isEqualTo(SIZE);
    assertThat(pageable).isEqualTo(PAGEABLE);
  }
  
  @Test
  public void testGivenPageOfDataByCreatingPagingObjectReturnPagingObject() {
    
    Paging paging = PageHelper.toPaging(DUMMY_DATA_PAGE);
    
    assertThat(paging).isNotNull();
    assertThat(paging.getPage()).isEqualTo(PAGE - 1);
    assertThat(paging.getSize()).isEqualTo(SIZE);
    assertThat(paging.getTotalRecords()).isEqualTo(DUMMY_DATA_LIST.size());
    assertThat(paging).isEqualTo(PAGING);
  }
  
}
