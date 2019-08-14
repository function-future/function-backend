package com.future.function.web.mapper.response.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LogMessage;
import com.future.function.model.entity.feature.communication.logging.LoggingRoom;
import com.future.function.model.entity.feature.communication.logging.Topic;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.logging.LogMessageWebResponse;
import com.future.function.web.model.response.feature.communication.logging.LoggingRoomWebResponse;
import com.future.function.web.model.response.feature.communication.logging.MemberResponse;
import com.future.function.web.model.response.feature.communication.logging.TopicWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggingRoomResponseMapper {

  public static PagingResponse<LoggingRoomWebResponse> toPagingLoggingRoomResponse(
    Page<LoggingRoom> data,
    String urlPrefix
  ){
    return ResponseHelper.toPagingResponse(
      HttpStatus.OK, toLoggingRoomResponseList(data, urlPrefix), PageHelper.toPaging(data)
    );
  }

  public static List<LoggingRoomWebResponse> toLoggingRoomResponseList(Page<LoggingRoom> data,
                                                                       String urlPrefix){
    return data.getContent()
            .stream()
            .map((LoggingRoom loggingRoom) -> toLoggingRoomResponse(loggingRoom, urlPrefix))
            .collect(Collectors.toList());
  }

  public static LoggingRoomWebResponse toLoggingRoomResponse(LoggingRoom loggingRoom,
                                                             String urlPrefix){
    return LoggingRoomWebResponse.builder()
            .id(loggingRoom.getId())
            .title(loggingRoom.getTitle())
            .description(loggingRoom.getDescription())
            .members(toListMemberResponse(loggingRoom.getMembers(), urlPrefix))
            .build();
  }

  private static List<MemberResponse> toListMemberResponse(List<User> members, String urlPrefix) {
    return members.stream()
            .map(member -> toMemberResponse(member, urlPrefix))
            .collect(Collectors.toList());
  }

  private static MemberResponse toMemberResponse(User member, String urlPrefix) {
    return MemberResponse.builder()
            .id(member.getId())
            .name(member.getName())
            .avatar(getThumnailUrl(member, urlPrefix))
            .batchName(member.getBatch().getName())
            .role(member.getRole().toString())
            .university(member.getUniversity())
            .build();
  }

  public static DataResponse<LoggingRoomWebResponse> toDataResponseLoggingRoomResponse(LoggingRoom data, String urlPrefix) {
    return ResponseHelper.toDataResponse(HttpStatus.OK, toLoggingRoomResponse(data, urlPrefix));
  }

  public static PagingResponse<TopicWebResponse> toPagingTopicResponse(Page<Topic> data) {

    return ResponseHelper.toPagingResponse(
      HttpStatus.OK,
      toTopicResponseList(data),
      PageHelper.toPaging(data)
    );
  }

  private static List<TopicWebResponse> toTopicResponseList(Page<Topic> data) {
    return data.getContent()
            .stream()
            .map(LoggingRoomResponseMapper::toTopicResponse)
            .collect(Collectors.toList());
  }

  private static TopicWebResponse toTopicResponse(Topic topic) {
    return TopicWebResponse.builder()
            .id(topic.getId())
            .title(topic.getTitle())
            .build();
  }

  public static DataResponse<TopicWebResponse> toDataResponseTopicResponse(Topic topic) {
    return ResponseHelper.toDataResponse(HttpStatus.OK, toTopicResponse(topic));
  }

  public static PagingResponse<LogMessageWebResponse> toPagingLogMessageResponse(Page<LogMessage> data, String urlPrefix) {
    return ResponseHelper.toPagingResponse(
      HttpStatus.OK,
      toLogMessageResponseList(data, urlPrefix),
      PageHelper.toPaging(data)
    );
  }

  private static List<LogMessageWebResponse> toLogMessageResponseList(Page<LogMessage> data, String urlPrefix) {
    return data.getContent()
            .stream()
            .map((logMessage) -> toLogMessageResponse(logMessage, urlPrefix))
            .collect(Collectors.toList());
  }

  private static LogMessageWebResponse toLogMessageResponse(LogMessage logMessage, String urlPrefix) {
    return LogMessageWebResponse.builder()
            .id(logMessage.getId())
            .createdAt(logMessage.getCreatedAt())
            .senderAvatar(getThumnailUrl(logMessage.getSender(), urlPrefix))
            .senderName(logMessage.getSender().getName())
            .text(logMessage.getText())
            .build();
  }

  private static String getThumnailUrl(User user, String urlPrefix) {
    return Optional.ofNullable(user)
      .map(User::getPictureV2)
      .map(FileV2::getThumbnailUrl)
      .map(urlPrefix::concat)
      .orElse(null);
  }
}
