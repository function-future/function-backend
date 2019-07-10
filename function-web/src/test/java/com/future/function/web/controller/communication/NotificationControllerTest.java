package com.future.function.web.controller.communication;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.reminder.Notification;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.NotificationService;
import com.future.function.session.model.Session;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.request.communication.NotificationRequestMapper;
import com.future.function.web.mapper.response.communication.NotificationResponseMapper;
import com.future.function.web.model.request.communication.NotificationRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.reminder.NotificationResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Author: PriagungSatyagama
 * Created At: 0:28 11/07/2019
 */
@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(NotificationController.class)
public class NotificationControllerTest extends TestHelper {

  public static final String NOTIFICATION_ID = "notificationId";
  public static final String USER_ID = "userId";
  public static final User USER = User.builder().id(USER_ID).build();
  public static final Notification NOTIFICATION = Notification.builder().id(NOTIFICATION_ID).member(USER).build();

  private JacksonTester<NotificationRequest> notificationRequestJacksonTester;

  @MockBean
  private NotificationService notificationService;

  @MockBean
  private NotificationRequestMapper notificationRequestMapper;

  @Override
  @Before
  public void setUp() {
    super.setUp();
    super.setCookie(Role.ADMIN);
  }

  @After
  public void tearDown() {
    verifyNoMoreInteractions(notificationService, notificationRequestMapper);
  }

  @Test
  public void testGivenCallToGetNotificationsReturnPagingResponse() throws Exception {
    PageImpl<Notification> notifications = new PageImpl<>(Collections.singletonList(NOTIFICATION),
            PageHelper.toPageable(1, 10), 1);
    when(notificationService.getNotifications(any(Session.class), eq(PageHelper.toPageable(1, 10))))
            .thenReturn(notifications);
    PagingResponse<NotificationResponse> pagingResponse = NotificationResponseMapper.toPagingNotificationResponse(notifications);
    mockMvc.perform(get("/api/communication/notifications").cookie(cookies))
            .andExpect(status().isOk())
            .andExpect(content().json(pagingResponseJacksonTester.write(pagingResponse).getJson()));
    verify(notificationService).getNotifications(any(Session.class), any(Pageable.class));
  }

  @Test
  public void testGivenChatroomWebRequestToPostNotificationReturnDataResponse() throws Exception {
    NotificationRequest request = NotificationRequest.builder().build();
    when(notificationService.createNotification(NOTIFICATION)).thenReturn(NOTIFICATION);
    when(notificationRequestMapper.toNotification(request)).thenReturn(NOTIFICATION);

    DataResponse<NotificationResponse> response = NotificationResponseMapper.toSingleNotificationResponse(NOTIFICATION);

    mockMvc.perform(post("/api/communication/notifications").cookie(cookies)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(notificationRequestJacksonTester.write(request).getJson()))
            .andExpect(status().isOk())
            .andExpect(content().json(dataResponseJacksonTester.write(response).getJson()));

    verify(notificationService).createNotification(NOTIFICATION);
    verify(notificationRequestMapper).toNotification(request);
  }

}
