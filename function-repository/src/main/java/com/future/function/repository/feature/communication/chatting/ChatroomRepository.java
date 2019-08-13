package com.future.function.repository.feature.communication.chatting;

import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatroomRepository extends MongoRepository<Chatroom, String> {

  Page<Chatroom> findAllByTypeAndMembersOrderByUpdatedAtDesc(ChatroomType type, User member, Pageable pageable);

  Page<Chatroom> findAllByTitleContainingIgnoreCaseAndMembersOrderByUpdatedAtDesc(String titleKeyword, User member, Pageable pageable);

  Optional<Chatroom> findByType(String type);

  List<Chatroom> findAllByMembersContaining(List<User> members);
}
