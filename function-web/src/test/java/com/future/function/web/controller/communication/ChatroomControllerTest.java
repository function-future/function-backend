package com.future.function.web.controller.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.model.entity.feature.communication.chatting.MessageStatus;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.ChatroomService;
import com.future.function.service.api.feature.communication.MessageService;
import com.future.function.service.api.feature.communication.MessageStatusService;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.communication.ChatroomRequestMapper;
import com.future.function.web.mapper.request.communication.MessageRequestMapper;
import com.future.function.web.mapper.response.communication.ChatroomResponseMapper;
import com.future.function.web.model.request.communication.ChatroomRequest;
import com.future.function.web.model.request.communication.MessageRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomDetailResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomResponse;
import com.future.function.web.model.response.feature.communication.chatting.MessageResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Author: priagung.satyagama
 * Created At: 1:33 PM 6/13/2019
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ChatroomController.class)
public class ChatroomControllerTest {

    private final String KEYWORD = "chat";

    private final String CHATROOM_ID = "chatroomId";

    private final String CHATROOM_TITLE = "chatroomTitle";

    private final ChatroomType CHATROOM_TYPE = ChatroomType.GROUP;

    private final String MESSAGE_ID = "messageId";

    private final String MEMBER_ID_1 = "5d01be122dee811d8c5f32e7";

    private final String MEMBER_ID_2 = "idUser2";

    private final User MEMBER_1 = User.builder()
            .id(MEMBER_ID_1)
            .build();

    private final User MEMBER_2 = User.builder()
            .id(MEMBER_ID_2)
            .build();

    private final Chatroom CHATROOM = Chatroom.builder()
            .type(CHATROOM_TYPE)
            .members(Arrays.asList(MEMBER_1, MEMBER_2))
            .title(CHATROOM_TITLE)
            .id(CHATROOM_ID)
            .build();

    private final ChatroomRequest CHATROOM_WEB_REQUEST = ChatroomRequest.builder()
            .members(Arrays.asList(MEMBER_ID_1, MEMBER_ID_2))
            .name(CHATROOM_TITLE)
            .build();

    private final Message MESSAGE = Message.builder()
            .id(MESSAGE_ID)
            .chatroom(CHATROOM)
            .text("test")
            .sender(MEMBER_1)
            .build();

    private final DataResponse<ChatroomDetailResponse> CHATROOM_DETAIL_DATA_RESPONSE =
            ChatroomResponseMapper.toChatroomDetailDataResponse(CHATROOM);

    private final Pageable PAGEABLE = new PageRequest(0, 10);

    private final MessageRequest MESSAGE_REQUEST = MessageRequest.builder()
            .message("test")
            .build();

    private JacksonTester<DataResponse<ChatroomDetailResponse>>
            dataResponseJacksonTester;

    private JacksonTester<PagingResponse<ChatroomResponse>>
            pagingChatroomResponseJacksonTester;

    private JacksonTester<PagingResponse<MessageResponse>>
            pagingMessageResponseJacksonTester;

    private JacksonTester<BaseResponse> baseResponseJacksonTester;

    private JacksonTester<ChatroomRequest> chatroomRequestJacksonTester;

    private JacksonTester<MessageRequest> messageRequestJacksonTester;


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatroomRequestMapper chatroomRequestMapper;

    @MockBean
    private MessageRequestMapper messageRequestMapper;

    @MockBean
    private ChatroomService chatroomService;

    @MockBean
    private MessageService messageService;

    @MockBean
    private MessageStatusService messageStatusService;


    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(chatroomRequestMapper, messageRequestMapper, chatroomService, messageService, messageStatusService);
    }

    @Test
    public void testGivenCalToChatroomsApiByGettingChatroomByIdReturnDataResponseChatroomDetail() throws Exception {
        given(chatroomService.getChatroom(CHATROOM_ID)).willReturn(CHATROOM);

        mockMvc.perform(get("/api/communication/chatrooms/" + CHATROOM_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(dataResponseJacksonTester.write(CHATROOM_DETAIL_DATA_RESPONSE).getJson()));

        verify(chatroomService).getChatroom(CHATROOM_ID);
    }

    @Test
    public void testGivenCallToChatroomsWithKeywordApiByGettingChatroomsReturnPagingResponse() throws Exception {
        given(chatroomService.getChatroomsWithKeyword(KEYWORD, MEMBER_ID_1, PAGEABLE)).willReturn(
                new PageImpl<>(Arrays.asList(CHATROOM, CHATROOM), PAGEABLE, 2)
        );
        given(messageService.getLastMessage(CHATROOM_ID)).willReturn(MESSAGE);
        given(messageStatusService.getSeenStatus(CHATROOM_ID, MEMBER_ID_1)).willReturn(false);

        PagingResponse<ChatroomResponse> response = ChatroomResponseMapper.toPagingChatroomResponse(
                chatroomService.getChatroomsWithKeyword(KEYWORD, MEMBER_ID_1, PAGEABLE),
                messageService,
                messageStatusService,
                MEMBER_ID_1
        );

        mockMvc.perform(get("/api/communication/chatrooms").param("search", KEYWORD))
                .andExpect(status().isOk())
                .andExpect(content().json(pagingChatroomResponseJacksonTester.write(response).getJson()));

        verify(chatroomService, times(2)).getChatroomsWithKeyword(KEYWORD, MEMBER_ID_1, PAGEABLE);
        verify(messageService, times(4)).getLastMessage(CHATROOM_ID);
        verify(messageStatusService, times(4)).getSeenStatus(CHATROOM_ID, MEMBER_ID_1);
    }

    @Test
    public void testGivenCallToChatroomsApiByGettingChatroomsReturnPagingResponse() throws Exception {
        given(chatroomService.getChatrooms("GROUP", MEMBER_ID_1, PAGEABLE)).willReturn(
                new PageImpl<>(Arrays.asList(CHATROOM, CHATROOM), PAGEABLE, 2)
        );
        given(messageService.getLastMessage(CHATROOM_ID)).willReturn(MESSAGE);
        given(messageStatusService.getSeenStatus(CHATROOM_ID, MEMBER_ID_1)).willReturn(false);

        PagingResponse<ChatroomResponse> response = ChatroomResponseMapper.toPagingChatroomResponse(
                chatroomService.getChatrooms("GROUP", MEMBER_ID_1, PAGEABLE),
                messageService,
                messageStatusService,
                MEMBER_ID_1
        );

        mockMvc.perform(get("/api/communication/chatrooms").param("type", "GROUP"))
                .andExpect(status().isOk())
                .andExpect(content().json(pagingChatroomResponseJacksonTester.write(response).getJson()));

        verify(chatroomService, times(2)).getChatrooms("GROUP", MEMBER_ID_1, PAGEABLE);
        verify(messageService, times(4)).getLastMessage(CHATROOM_ID);
        verify(messageStatusService, times(4)).getSeenStatus(CHATROOM_ID, MEMBER_ID_1);
    }

    @Test
    public void testGivenCallToChatroomsApiByGettingMessagesReturnPaging() throws Exception {
        Page<Message> messagePage = new PageImpl<>(
                Arrays.asList(MESSAGE, MESSAGE), PAGEABLE, 2);
        given(messageService.getMessages(CHATROOM_ID, PAGEABLE)).willReturn(messagePage);

        PagingResponse<MessageResponse> response = ChatroomResponseMapper.toMessagePagingResponse(messagePage);

        mockMvc.perform(get("/api/communication/chatrooms/" + CHATROOM_ID + "/messages"))
                .andExpect(status().isOk())
                .andExpect(content().json(pagingMessageResponseJacksonTester.write(response).getJson()));

        verify(messageService).getMessages(CHATROOM_ID, PAGEABLE);
    }

    @Test
    public void testGivenChatroomDataByCreatingChatroomReturnDataResponseChatroom() throws Exception {
        given(chatroomRequestMapper.toChatroom(CHATROOM_WEB_REQUEST, null)).willReturn(CHATROOM);
        given(chatroomService.createChatroom(CHATROOM)).willReturn(CHATROOM);

        DataResponse<ChatroomDetailResponse> response = ChatroomResponseMapper.toChatroomDetailDataResponse(CHATROOM);

        mockMvc.perform(post("/api/communication/chatrooms")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(chatroomRequestJacksonTester.write(CHATROOM_WEB_REQUEST).getJson()))
                .andExpect(status().isOk())
                .andExpect(content().json(dataResponseJacksonTester.write(response).getJson()));

        verify(chatroomRequestMapper).toChatroom(CHATROOM_WEB_REQUEST, null);
        verify(chatroomService).createChatroom(CHATROOM);
    }

    @Test
    public void testGivenMessageDataByCreatingMessageReturnBaseResponseCreated() throws Exception {
        given(messageRequestMapper.toMessage(MESSAGE_REQUEST, MEMBER_ID_1, CHATROOM_ID)).willReturn(MESSAGE);
        given(messageService.createMessage(MESSAGE)).willReturn(MESSAGE);
        given(chatroomService.getChatroom(CHATROOM_ID)).willReturn(CHATROOM);
        given(messageStatusService.createMessageStatus(any(MessageStatus.class))).willReturn(null);

        mockMvc.perform(post("/api/communication/chatrooms/" + CHATROOM_ID + "/messages")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(messageRequestJacksonTester.write(MESSAGE_REQUEST).getJson()))
                .andExpect(status().isCreated())
                .andExpect(content().json(baseResponseJacksonTester.write(ResponseHelper.toBaseResponse(HttpStatus.CREATED)).getJson()));

        verify(messageRequestMapper).toMessage(MESSAGE_REQUEST, MEMBER_ID_1, CHATROOM_ID);
        verify(messageService).createMessage(MESSAGE);
        verify(messageStatusService, times(2)).createMessageStatus(any(MessageStatus.class));
        verify(chatroomService).getChatroom(CHATROOM_ID);
    }

    @Test
    public void testGivenChatroomDataByUpdatingChatroomReturnBaseResponseOk() throws Exception {
        given(chatroomRequestMapper.toChatroom(CHATROOM_WEB_REQUEST, CHATROOM_ID)).willReturn(CHATROOM);
        given(chatroomService.updateChatroom(CHATROOM)).willReturn(CHATROOM);

        mockMvc.perform(put("/api/communication/chatrooms/" + CHATROOM_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(chatroomRequestJacksonTester.write(CHATROOM_WEB_REQUEST).getJson()))
                .andExpect(status().isOk())
                .andExpect(content().json(baseResponseJacksonTester.write(ResponseHelper.toBaseResponse(HttpStatus.OK)).getJson()));

        verify(chatroomRequestMapper).toChatroom(CHATROOM_WEB_REQUEST, CHATROOM_ID);
        verify(chatroomService).updateChatroom(CHATROOM);
    }

    @Test
    public void testGivenCallToChatroomsApiByUpdatingMessageStatusReturnBaseResponseOk() throws Exception {
        doNothing().when(messageStatusService).updateSeenStatus(CHATROOM_ID, MESSAGE_ID, MEMBER_ID_1);

        mockMvc.perform(put("/api/communication/chatrooms/" + CHATROOM_ID + "/messages/" + MESSAGE_ID + "/_read"))
                .andExpect(status().isOk())
                .andExpect(content().json(baseResponseJacksonTester.write(ResponseHelper.toBaseResponse(HttpStatus.OK)).getJson()));

        verify(messageStatusService).updateSeenStatus(CHATROOM_ID, MESSAGE_ID, MEMBER_ID_1);
    }
}
