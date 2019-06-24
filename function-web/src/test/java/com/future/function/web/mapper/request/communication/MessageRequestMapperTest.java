package com.future.function.web.mapper.request.communication;

import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.MessageRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Author: priagung.satyagama
 * Created At: 1:52 PM 6/10/2019
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageRequestMapperTest {

    private static final String MESSAGE = "message";

    private static final MessageRequest MESSAGE_REQUEST = MessageRequest.builder()
            .message(MESSAGE)
            .build();

    private static final String USER_ID = "userId";

    private static final String CHATROOM_ID = "chatroomId";

    @Mock
    private RequestValidator validator;

    @InjectMocks
    private MessageRequestMapper mapper;

    @Before
    public void setUp() {
        when(validator.validate(MESSAGE_REQUEST)).thenReturn(MESSAGE_REQUEST);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(validator);
    }

    @Test
    public void testGivenMessageRequestByCallingToMessageReturnMessageObject() {
        Message message = mapper.toMessage(MESSAGE_REQUEST, USER_ID, CHATROOM_ID);

        assertThat(message).isNotNull();
        assertThat(message.getText()).isEqualTo(MESSAGE);
        assertThat(message.getSender().getId()).isEqualTo(USER_ID);
        assertThat(message.getChatroom().getId()).isEqualTo(CHATROOM_ID);

        verify(validator).validate(MESSAGE_REQUEST);

    }

}
