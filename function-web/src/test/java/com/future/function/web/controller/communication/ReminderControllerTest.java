package com.future.function.web.controller.communication;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.reminder.Reminder;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.ReminderService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.communication.ReminderRequestMapper;
import com.future.function.web.mapper.response.communication.ReminderResponseMapper;
import com.future.function.web.model.request.communication.ReminderRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.reminder.ReminderDetailResponse;
import com.future.function.web.model.response.feature.communication.reminder.ReminderResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Author: priagung.satyagama
 * Created At: 10:00 AM 7/11/2019
 */
@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(ReminderController.class)
public class ReminderControllerTest extends TestHelper {

    private static final String REMINDER_ID = "reminderId";
    private static final String USER_ID = "userId";
    private static final User USER = User.builder().id(USER_ID).build();
    private static final Reminder REMINDER = Reminder.builder()
            .isRepeatedMonthly(true)
            .id(REMINDER_ID)
            .hour(10)
            .minute(10)
            .members(new ArrayList<>())
            .build();
    private static final Pageable PAGEABLE = PageHelper.toPageable(1, 10);
    private static final PageImpl<Reminder> REMINDER_PAGE = new PageImpl<>(Collections.singletonList(REMINDER),
            PAGEABLE, 1);
    private static final ReminderRequest REMINDER_REQUEST = ReminderRequest.builder().build();

    private JacksonTester<ReminderRequest> reminderRequestJacksonTester;

    @MockBean
    private ReminderService reminderService;

    @MockBean
    private ReminderRequestMapper reminderRequestMapper;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        super.setCookie(Role.ADMIN);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(reminderRequestMapper, reminderService);
    }

    @Test
    public void testGivenCallToGetRemindersReturnPagingResponse() throws Exception {
        when(reminderService.getAllPagedReminder(PAGEABLE)).thenReturn(REMINDER_PAGE);
        PagingResponse<ReminderResponse> response = ReminderResponseMapper.toPagingReminderResponse(REMINDER_PAGE);
        mockMvc.perform(get("/api/communication/reminders").cookie(cookies))
                .andExpect(status().isOk())
                .andExpect(content().json(pagingResponseJacksonTester.write(response).getJson()));
        verify(reminderService).getAllPagedReminder(PAGEABLE);
    }

    @Test
    public void testGivenReminderIdToGetReminderByIdReturnDataResponse() throws Exception {
        when(reminderService.getReminder(REMINDER_ID)).thenReturn(REMINDER);
        DataResponse<ReminderDetailResponse> response = ReminderResponseMapper.toSingleReminderDataResponse(REMINDER);
        mockMvc.perform(get("/api/communication/reminders/" + REMINDER_ID).cookie(cookies))
                .andExpect(status().isOk())
                .andExpect(content().json(dataResponseJacksonTester.write(response).getJson()));
        verify(reminderService).getReminder(REMINDER_ID);
    }

    @Test
    public void testGivenReminderRequestToCreateReminderReturnDataResponse() throws Exception {
        when(reminderService.createReminder(REMINDER)).thenReturn(REMINDER);
        when(reminderRequestMapper.toReminder(REMINDER_REQUEST, null)).thenReturn(REMINDER);
        DataResponse<ReminderDetailResponse> response = ReminderResponseMapper.toSingleReminderDataResponse(REMINDER);
        mockMvc.perform(post("/api/communication/reminders")
                .cookie(cookies).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(reminderRequestJacksonTester.write(REMINDER_REQUEST).getJson()))
                .andExpect(status().isOk())
                .andExpect(content().json(dataResponseJacksonTester.write(response).getJson()));
        verify(reminderService).createReminder(REMINDER);
        verify(reminderRequestMapper).toReminder(REMINDER_REQUEST, null);
    }

    @Test
    public void testGivenReminderRequestToUpdateReminderReturnDataResponse() throws Exception {
        when(reminderService.updateReminder(REMINDER)).thenReturn(REMINDER);
        when(reminderRequestMapper.toReminder(REMINDER_REQUEST, REMINDER_ID)).thenReturn(REMINDER);
        DataResponse<ReminderDetailResponse> response = ReminderResponseMapper.toSingleReminderDataResponse(REMINDER);
        mockMvc.perform(put("/api/communication/reminders/" + REMINDER_ID)
                .cookie(cookies).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(reminderRequestJacksonTester.write(REMINDER_REQUEST).getJson()))
                .andExpect(status().isOk())
                .andExpect(content().json(dataResponseJacksonTester.write(response).getJson()));
        verify(reminderService).updateReminder(REMINDER);
        verify(reminderRequestMapper).toReminder(REMINDER_REQUEST, REMINDER_ID);
    }

    @Test
    public void testGivenReminderIdToDeleteReminderReturnBaseResponse() throws Exception {
        doNothing().when(reminderService).deleteReminder(REMINDER_ID);
        BaseResponse response = ResponseHelper.toBaseResponse(HttpStatus.OK);
        mockMvc.perform(delete("/api/communication/reminders/" + REMINDER_ID).cookie(cookies))
                .andExpect(status().isOk())
                .andExpect(content().json(baseResponseJacksonTester.write(response).getJson()));
        verify(reminderService).deleteReminder(REMINDER_ID);
    }
}
