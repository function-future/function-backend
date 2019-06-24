package com.future.function.web.mapper.request.communication;

import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.ChatroomRequest;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Author: priagung.satyagama
 * Created At: 2:36 PM 6/10/2019
 */
@RunWith(MockitoJUnitRunner.class)
public class ChatroomRequestMapperTest {

    private static final String CHATROOM_ID = "chatroomId";

    private static final String MEMBERS_ID_1 = "memberId1";

    private static final String MEMBERS_ID_2 = "memberId2";

    private static final String MEMBERS_ID_3 = "memberId3";

    private static final List<String> CHATROOM_MEMBERS_1 = Arrays.asList(MEMBERS_ID_1, MEMBERS_ID_2);

    private static final List<String> CHATROOM_MEMBERS_2 = Arrays.asList(MEMBERS_ID_1, MEMBERS_ID_2, MEMBERS_ID_3);

    private static final String CHATROOM_TITLE = "title";

    private static final ChatroomRequest REQUEST_1 = ChatroomRequest.builder()
            .members(CHATROOM_MEMBERS_1)
            .name(CHATROOM_TITLE)
            .build();

    private static final ChatroomRequest REQUEST_2 = ChatroomRequest.builder()
            .members(CHATROOM_MEMBERS_2)
            .name(CHATROOM_TITLE)
            .build();

    @Mock
    private RequestValidator validator;

    @InjectMocks
    private ChatroomRequestMapper requestMapper;

    @After
    public void tearDown() {
        verifyNoMoreInteractions(validator);
    }

    @Test
    public void testGivenChatroomRequestWith2MembersAndChatroomIdByCallingToChatroomReturnChatroom() {
        when(validator.validate(REQUEST_1)).thenReturn(REQUEST_1);

        Chatroom chatroom = requestMapper.toChatroom(REQUEST_1, CHATROOM_ID);

        assertThat(chatroom).isNotNull();
        assertThat(chatroom.getId()).isEqualTo(CHATROOM_ID);
        assertThat(chatroom.getType()).isEqualTo(ChatroomType.PRIVATE);
        assertThat(chatroom.getMembers().size()).isEqualTo(CHATROOM_MEMBERS_1.size());

        verify(validator).validate(REQUEST_1);
    }

    @Test
    public void testGivenChatroomRequestWith3MembersAndChatroomIdByCallingToChatroomReturnChatroom() {
        when(validator.validate(REQUEST_2)).thenReturn(REQUEST_2);

        Chatroom chatroom = requestMapper.toChatroom(REQUEST_2, CHATROOM_ID);

        assertThat(chatroom).isNotNull();
        assertThat(chatroom.getId()).isEqualTo(CHATROOM_ID);
        assertThat(chatroom.getType()).isEqualTo(ChatroomType.GROUP);
        assertThat(chatroom.getMembers().size()).isEqualTo(CHATROOM_MEMBERS_2.size());

        verify(validator).validate(REQUEST_2);
    }


}
