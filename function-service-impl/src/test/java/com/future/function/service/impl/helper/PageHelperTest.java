package com.future.function.service.impl.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PageHelperTest {

  private static final Pageable PAGEABLE = new PageRequest(0, 5);

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  @Test
  public void testGivenPageableObjectByCreatingEmptyPageReturnEmptyPage() {

    Page expectedPage = new PageImpl<>(Collections.emptyList(), PAGEABLE, 0);

    Page page = PageHelper.empty(PAGEABLE);

    assertThat(page).isEqualTo(expectedPage);
  }

  @Test
  public void testGivenListOfDataAndPageableObjectByCreatingPageReturnPageOfData() {

    List<String> strings = Collections.singletonList("");

    Page<String> expectedPage = new PageImpl<>(strings, PAGEABLE, 0);

    Page<String> page = PageHelper.toPage(strings, PAGEABLE);

    assertThat(page).isEqualTo(expectedPage);
  }

}
