package com.future.function.web.mapper.response.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.File;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.base.paging.Paging;
import com.future.function.web.model.response.feature.core.UserWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserResponseMapperTest {
  
  private static final String EMAIL = "email";
  
  private static final String NAME = "name";
  
  private static final Role STUDENT_ROLE = Role.STUDENT;
  
  private static final Role MENTOR_ROLE = Role.MENTOR;
  
  private static final String PHONE = "phone";
  
  private static final String ADDRESS = "address";
  
  private static final File PICTURE = new File();
  
  private static final Batch BATCH = new Batch(1L);
  
  private static final String UNIVERSITY = "university";
  
  private static final User STUDENT = User.builder()
    .email(EMAIL)
    .name(NAME)
    .role(STUDENT_ROLE)
    .phone(PHONE)
    .address(ADDRESS)
    .picture(PICTURE)
    .batch(BATCH)
    .university(UNIVERSITY)
    .build();
  
  private static final UserWebResponse STUDENT_WEB_RESPONSE =
    UserWebResponse.builder()
      .role(STUDENT_ROLE.name())
      .email(EMAIL)
      .name(NAME)
      .phone(PHONE)
      .address(ADDRESS)
      .deleted(false)
      .pictureUrl(PICTURE.getFileUrl())
      .thumbnailUrl(PICTURE.getThumbnailUrl())
      .batch(BATCH.getNumber())
      .university(UNIVERSITY)
      .build();
  
  private static final DataResponse<UserWebResponse>
    CREATED_STUDENT_WEB_RESPONSE = DataResponse.<UserWebResponse>builder().code(
    201)
    .status("CREATED")
    .data(STUDENT_WEB_RESPONSE)
    .build();
  
  private static final User MENTOR = User.builder()
    .email(EMAIL)
    .name(NAME)
    .role(MENTOR_ROLE)
    .phone(PHONE)
    .address(ADDRESS)
    .picture(PICTURE)
    .build();
  
  private static final UserWebResponse MENTOR_WEB_RESPONSE =
    UserWebResponse.builder()
      .role(MENTOR_ROLE.name())
      .email(EMAIL)
      .name(NAME)
      .phone(PHONE)
      .address(ADDRESS)
      .deleted(false)
      .pictureUrl(PICTURE.getFileUrl())
      .thumbnailUrl(PICTURE.getThumbnailUrl())
      .build();
  
  private static final DataResponse<UserWebResponse>
    RETRIEVED_MENTOR_WEB_RESPONSE =
    DataResponse.<UserWebResponse>builder().code(200)
      .status("OK")
      .data(MENTOR_WEB_RESPONSE)
      .build();
  
  private static final Pageable PAGEABLE = new PageRequest(0, 2);
  
  private static final List<User> USERS = Arrays.asList(STUDENT, MENTOR);
  
  private static final List<UserWebResponse> USER_WEB_RESPONSES = Arrays.asList(
    STUDENT_WEB_RESPONSE, MENTOR_WEB_RESPONSE);
  
  private static final Page<User> PAGE = new PageImpl<>(
    USERS, PAGEABLE, USERS.size());
  
  private static final Paging PAGING = Paging.builder()
    .page(0)
    .size(2)
    .totalRecords(2)
    .build();
  
  private static final PagingResponse<UserWebResponse> PAGING_RESPONSE =
    PagingResponse.<UserWebResponse>builder().code(200)
      .status("OK")
      .data(USER_WEB_RESPONSES)
      .paging(PAGING)
      .build();
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenUserDataByMappingToDataResponseReturnDataResponseObject() {
    
    DataResponse<UserWebResponse> createdStudentDataResponse =
      UserResponseMapper.toUserDataResponse(HttpStatus.CREATED, STUDENT);
    
    assertThat(createdStudentDataResponse).isNotNull();
    assertThat(createdStudentDataResponse).isEqualTo(
      CREATED_STUDENT_WEB_RESPONSE);
    
    DataResponse<UserWebResponse> retrievedMentorDataResponse =
      UserResponseMapper.toUserDataResponse(MENTOR);
    
    assertThat(retrievedMentorDataResponse).isNotNull();
    assertThat(retrievedMentorDataResponse).isEqualTo(
      RETRIEVED_MENTOR_WEB_RESPONSE);
  }
  
  @Test
  public void testGivenUsersDataByMappingToPagingResponseReturnPagingResponseObject() {
    
    PagingResponse<UserWebResponse> pagingResponse =
      UserResponseMapper.toUsersPagingResponse(PAGE);
    
    assertThat(pagingResponse).isNotNull();
    assertThat(pagingResponse).isEqualTo(PAGING_RESPONSE);
  }
  
}