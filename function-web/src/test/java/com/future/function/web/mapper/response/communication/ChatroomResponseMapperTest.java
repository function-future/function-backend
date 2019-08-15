package com.future.function.web.mapper.response.communication;

import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.MessageService;
import com.future.function.service.api.feature.communication.MessageStatusService;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomDetailResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomResponse;
import com.future.function.web.model.response.feature.communication.chatting.MessageResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChatroomResponseMapperTest {

  private static final String MESSAGE_ID_1 = "messageId1";

  private static final String MESSAGE_ID_2 = "messageId2";

  private static final String TEXT_1 = "text1";

  private static final String TEXT_2 = "text2";

  private static final String THUMBNAIL_URL = "thumbnail";

  private static final String MEMBER_ID_1 = "memberId1";

  private static final String MEMBER_ID_2 = "memberId2";

  private static final String MEMBER_NAME_1 = "member1";

  private static final String MEMBER_NAME_2 = "member1";

  private static final String BATCH_ID = "batchId";

  private static final String UNIVERSITY = "itb";

  private static final String CHATROOM_ID = "chatroomId";

  private static final String CHATROOM_TITLE = "chatroomTitle";

  private static final String URL_PREFIX = "localhost:8080";

  private static final Session SESSION = Session.builder()
    .userId(MEMBER_ID_1)
    .build();

  private static final User MEMBER_1 = User.builder()
    .id(MEMBER_ID_1)
    .name(MEMBER_NAME_1)
    .pictureV2(FileV2.builder()
                 .thumbnailUrl(THUMBNAIL_URL)
                 .build())
    .batch(Batch.builder()
             .id(BATCH_ID)
             .build())
    .role(Role.STUDENT)
    .university(UNIVERSITY)
    .build();

  private static final User MEMBER_2 = User.builder()
    .id(MEMBER_ID_2)
    .name(MEMBER_NAME_2)
    .pictureV2(FileV2.builder()
                 .thumbnailUrl(THUMBNAIL_URL)
                 .build())
    .batch(Batch.builder()
             .id(BATCH_ID)
             .build())
    .role(Role.STUDENT)
    .university(UNIVERSITY)
    .build();

  private static final boolean IS_SEEN = true;

  private static Message MESSAGE_1 = Message.builder()
    .id(MESSAGE_ID_1)
    .text(TEXT_1)
    .sender(MEMBER_1)
    .build();

  private static Message MESSAGE_2 = Message.builder()
    .id(MESSAGE_ID_2)
    .text(TEXT_2)
    .sender(MEMBER_1)
    .build();

  private static Chatroom CHATROOM = Chatroom.builder()
    .id(CHATROOM_ID)
    .members(Arrays.asList(MEMBER_1, MEMBER_2))
    .title(CHATROOM_TITLE)
    .type(ChatroomType.GROUP)
    .build();

  @Mock
  private MessageService messageService;

  @Mock
  private MessageStatusService messageStatusService;

  @After
  public void tearDown() {

    verifyNoMoreInteractions(messageService, messageStatusService);
  }

  @Test
  public void testGivenChatroomByCallingToChatroomDetailDataReturnDataResponse() {

    DataResponse<ChatroomDetailResponse> data =
      ChatroomResponseMapper.toChatroomDetailDataResponse(CHATROOM, URL_PREFIX);

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData()
                 .getId()).isEqualTo(CHATROOM_ID);
    assertThat(data.getData()
                 .getMembers()
                 .size()).isEqualTo(2);
    assertThat(data.getData()
                 .getMembers()
                 .get(0)
                 .getId()).isEqualTo(MEMBER_1.getId());
    assertThat(data.getData()
                 .getMembers()
                 .get(1)
                 .getId()).isEqualTo(MEMBER_2.getId());
  }

  @Test
  public void testGivenPagedChatroomAndMessageAndBooleanByCallingToPagingChatroomResponseReturnPaging() {

    when(messageService.getLastMessage(CHATROOM_ID,
                                       SESSION.getUserId()
    )).thenReturn(MESSAGE_1);
    when(messageStatusService.getSeenStatus(CHATROOM_ID,
                                            SESSION.getUserId()
    )).thenReturn(IS_SEEN);

    PagingResponse<ChatroomResponse> data =
      ChatroomResponseMapper.toPagingChatroomResponse(new PageImpl<>(
                                                        Collections.singletonList(CHATROOM), PageHelper.toPageable(1, 1), 1),
                                                      messageService,
                                                      messageStatusService,
                                                      URL_PREFIX,
                                                      SESSION.getUserId()
      );

    assertThat(data).isNotNull();
    assertThat(data.getPaging()
                 .getPage()).isEqualTo(1);
    assertThat(data.getPaging()
                 .getSize()).isEqualTo(1);
    assertThat(data.getData()
                 .get(0)
                 .getId()).isEqualTo(CHATROOM_ID);

    verify(messageService).getLastMessage(CHATROOM_ID, SESSION.getUserId());
    verify(messageStatusService).getSeenStatus(
      CHATROOM_ID, SESSION.getUserId());
  }

  @Test
  public void testGivenPagedMessageByCallingToMessagePagingReturnPaging() {

    PagingResponse<MessageResponse> data =
      ChatroomResponseMapper.toMessagePagingResponse(new PageImpl<>(
                                                       Arrays.asList(MESSAGE_1, MESSAGE_2), PageHelper.toPageable(1, 2), 2),
                                                     URL_PREFIX
      );

    assertThat(data).isNotNull();
    assertThat(data.getPaging()
                 .getSize()).isEqualTo(2);
    assertThat(data.getPaging()
                 .getPage()).isEqualTo(1);
    assertThat(data.getData()
                 .get(0)
                 .getId()).isEqualTo(MESSAGE_ID_1);
    assertThat(data.getData()
                 .get(1)
                 .getId()).isEqualTo(MESSAGE_ID_2);
  }

}
