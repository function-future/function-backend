package com.future.function.service.impl.feature.communication;

import com.future.function.model.entity.feature.communication.MessageStatus;
import com.future.function.service.api.feature.communication.MessageStatusService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: PriagungSatyagama
 * Created At: 19:27 04/06/2019
 */
@Service
public class MessageStatusServiceImpl implements MessageStatusService {
  @Override
  public List<MessageStatus> getUnseenMessageStatus(String chatroomId, String userId) {
    return null;
  }

  @Override
  public List<MessageStatus> getUnseenMessageStatusBeforeTimestamp(String chatroomId, String userId, Long timestamp) {
    return null;
  }

  @Override
  public boolean getSeenStatus(String chatroomId, String userId) {
    return false;
  }

  @Override
  public MessageStatus createMessageStatus(MessageStatus messageStatus) {
    return null;
  }

  @Override
  public MessageStatus updateMessageStatus(MessageStatus messageStatus) {
    return null;
  }
}
